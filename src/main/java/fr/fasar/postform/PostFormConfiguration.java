package fr.fasar.postform;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties("conf")
public class PostFormConfiguration {

    private File base;
    private String prefix;
    private String suffix;

}
