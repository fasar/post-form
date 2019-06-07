package fr.fasar.postform.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.fasar.postform.PostFormConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DbController {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    PostFormConfiguration conf;

    @GetMapping("/db")
    public ArrayNode db() throws IOException {
        List<Path> collect = Files.list(conf.getBase().toPath())
                .filter(e -> e.toString().endsWith(".json"))
                .sorted()
                .collect(Collectors.toList());
        if (collect.size() > 0) {
            Path last = collect.get(collect.size() - 1);
            List<String> strings = Files.readAllLines(last);
            ArrayNode arrayNode = mapper.createArrayNode();
            strings.stream().forEach(e -> {
                JsonNode jsonNode = null;
                try {
                    jsonNode = mapper.readTree(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                arrayNode.add(jsonNode);
            });
            return arrayNode;
        }
        return mapper.createArrayNode();
    }

}
