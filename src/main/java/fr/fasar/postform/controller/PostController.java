package fr.fasar.postform.controller;

import fr.fasar.postform.PostFormConfiguration;
import fr.fasar.postform.services.RecorderService;
import fr.fasar.postform.Utils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@RestController
public class PostController {

    @Value("${conf.controller.max-body-length}")
    long maxBodyLength;

    @Autowired
    PostFormConfiguration conf;

    @Autowired
    RecorderService recorderService;

    AtomicLong number = new AtomicLong(0);

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
    @RequestMapping("/{path:.*}")
    public String post(@PathVariable String path, HttpServletRequest request) throws ControllerException, IOException, InterruptedException, ExecutionException, TimeoutException {
        long contentLength = request.getContentLengthLong();
        if (contentLength > maxBodyLength) {
            throw new ControllerException("Max body size is " + maxBodyLength + " bytes. Current body size is " + contentLength);
        }

        String id = ifOfRequest();
        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        String requestURL = request.getRequestURL().toString();
        String characterEncoding = request.getCharacterEncoding();

        Map<String, List<String>> headers = getHeaders(request.getHeaderNames(), request::getHeaders);

        Map<String, String[]> parameterMap = request.getParameterMap();

        ServletInputStream is = request.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        List<PartEntity> parts = getPartEntities(id, request, parameterMap);


        RequestEntity requestEntity = new RequestEntity(
                id, Instant.now(), method, remoteAddr,
                requestURL, characterEncoding,
                parameterMap, headers,
                parts, bytes
        );
        CompletableFuture<Void> record = recorderService.record(requestEntity);
        record.get(10, TimeUnit.SECONDS);
        return "{}";
    }

    private String ifOfRequest() {
        String s = Utils.dayOfYear();
        long id = number.getAndIncrement();
        s = s + id;
        return s;
    }


    private List<PartEntity> getPartEntities(String id, HttpServletRequest request, Map<String, String[]> parameterMap) {
        List<PartEntity> parts = null;

        if (request.getContentType() == null || !request.getContentType().contains("multipart")) {
            return parts;
        }

        try {
            if (request.getParts() != null) {
                parts = new ArrayList<>();
                Set<String> alreadyParsedPart = parameterMap.keySet();
                for (Part part : request.getParts()) {
                    String name = part.getName();
                    if (name == null || alreadyParsedPart.contains(name)) {
                        continue;
                    }
                    String contentType = part.getContentType();
                    Map<String, List<String>> headers = getHeaders(part.getHeaderNames(), part::getHeaders);
                    long size = part.getSize();
                    String fileName = part.getSubmittedFileName();
                    String storedFileName = fileNameOf(id, fileName);
                    File output = new File(conf.getFilesBase(), storedFileName);
                    part.write(output.getAbsolutePath());

                    PartEntity partEntity = new PartEntity(storedFileName, name, contentType, headers, size, storedFileName);
                    parts.add(partEntity);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parts;
    }

    private String fileNameOf(String id, String filename) {
        String name = filename.replaceAll("\\W+", "");
        return id + "-" + name;
    }


    private Map<String, List<String>> getHeaders(Enumeration<String> headerNames, Function<String, Enumeration<String>> headerValues) {
        if (headerNames == null) {
            return new HashMap<>();
        }
        Map<String, List<String>> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValue = headerValues.apply(headerName);
            List<String> values = new ArrayList<>();
            while (headerValue.hasMoreElements()) {
                values.add(headerValue.nextElement());
            }
            headers.put(headerName, values);
        }
        return headers;
    }

    private Map<String, List<String>> getHeaders(Collection<String> headerNames, Function<String, Collection<String>> headerValues) {
        if (headerNames == null) {
            return new HashMap<>();
        }
        Enumeration<String> enumeration = Collections.enumeration(headerNames);
        Function<String, Enumeration<String>> headerValues2 = (name) -> Collections.enumeration(headerValues.apply(name));
        return getHeaders(enumeration, headerValues2);

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
