package fr.fasar.postform.controller;

import fr.fasar.postform.RecorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController {
    @Autowired
    RecorderService recorderService;

    @RequestMapping("/post")
    public String post(HttpServletRequest request) {
        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String contentType = request.getContentType();
        String characterEncoding = request.getCharacterEncoding();


        Cookie[] cookies = request.getCookies();
        request.getHeaderNames();
        request.getInputStream()

        recorderService.
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
