package fr.fasar.postform.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fasar.postform.services.DbServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private DbServices dbServices;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/try")
    public String index() {
        return "forms.html";
    }

    @GetMapping("/")
    public ModelAndView hello() throws IOException {
        ModelAndView res = new ModelAndView("index.html");
        List<Path> paths = dbServices.listFiles();
        int nbLines = dbServices.nbLines();
        List<String> lines = dbServices.lines(0, 0);
        List<JsonNode> collect = lines.stream().map(l -> {
            try {
                return mapper.readTree(l);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        res.addObject("paths", paths);
        res.addObject("nbLines", nbLines);
        res.addObject("lines", collect);
        return res;
    }
}
