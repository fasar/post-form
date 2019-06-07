package fr.fasar.postform;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@ConfigurationProperties("conf")
public class PostFormConfiguration {

    private File base;
    private File filesBase;
    private String prefix;
    private String suffix;

    public File getFilesBase() {
        return filesBase;
    }

    public void setFilesBase(File filesBase) throws IOException {
        this.filesBase = filesBase;
        if(!filesBase.exists()) {
            FileUtils.forceMkdir(filesBase);
        }
    }

    public File getBase() {
        return base;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setBase(File base) throws IOException {
        this.base = base;
        if(!base.exists()) {
            FileUtils.forceMkdir(base);
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
