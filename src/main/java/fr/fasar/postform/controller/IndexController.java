package fr.fasar.postform.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/try")
    public String index() {
        return "forms.html";
    }
}
