package fr.fasar.postform.controller;

import fr.fasar.postform.RecorderService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class PostController {

    @Value("${conf.controller.max-body-length}")
    long maxBodyLength;

    @Autowired
    RecorderService recorderService;

    /****
     *
     * Example of posts :
     *
     * Example 1 application/x-www-form-urlencoded :
     * ==============================================
     * POST / HTTP/1.1
     * Host: foo.com
     * Content-Type: application/x-www-form-urlencoded
     * Content-Length: 13
     *
     * say=Hi&to=Mom
     *
     *
     * Example 2 multipart/form-data :
     *==============================================
     * <form action="/test.html" method="post" enctype="multipart/form-data">
     *   <input type="text" name="description" value="du texte">
     *   <input type="file" name="monFichier">
     *   <button type="submit">Envoyer</button>
     * </form>
     *
     * POST /toto HTTP/1.1
     * Content-Length: 68137
     * Content-Type: multipart/form-data; boundary=---------------------------974767299852498929531610575
     * Content-Disposition: form-data; name="description"
     *
     * ---------------------------974767299852498929531610575
     *
     * du texte par ici
     *
     * ---------------------------974767299852498929531610575
     * Content-Disposition: form-data; name="monFichier"; filename="toto.txt"
     * Content-Type: text/plain
     *
     * (contenu du fichier envoyé en ligne toto.txt)
     *
     * ---------------------------974767299852498929531610575
     *
     *
     *
     *
     *
     * Content-Type: text/plain; charset=utf-8
     * Content-Type: text/html; charset=utf-8
     * Content-Type: multipart/form-data; boundary=something
     *
     *
     *
     * @param request
     * @return
     * @throws ControllerException
     * @throws IOException
     */
    @RequestMapping("/post")
    public String post(HttpServletRequest request) throws ControllerException, IOException, InterruptedException, ExecutionException, TimeoutException {
        long contentLength = request.getContentLengthLong();
        if (contentLength > maxBodyLength) {
            throw new ControllerException("Max body size is " + maxBodyLength + " bytes. Current body size is " + contentLength);
        }


        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String contentType = request.getContentType();
        String characterEncoding = request.getCharacterEncoding();
        List<Cookie> cookies = Arrays.asList(request.getCookies());


        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, List<String>> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValue = request.getHeaders(headerName);
            List<String> values = new ArrayList<>();
            while (headerValue.hasMoreElements()) {
                values.add(headerValue.nextElement());
            }
            headers.put(headerName, values);
        }

        Map<String, String[]> parameterMap = request.getParameterMap();

        ServletInputStream is = request.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        RequestEntity requestEntity = new RequestEntity(
                Instant.now(), method, remoteAddr, requestURL, queryString,
                contentType, characterEncoding, cookies, parameterMap, bytes);
        CompletableFuture<Void> record = recorderService.record(requestEntity);
        record.get(10, TimeUnit.SECONDS);
        return "ok";
    }

    public static class Answer {
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

}
