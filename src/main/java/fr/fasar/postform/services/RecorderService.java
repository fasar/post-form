package fr.fasar.postform.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fasar.postform.PostFormConfiguration;
import fr.fasar.postform.Utils;
import fr.fasar.postform.controller.RequestEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RecorderService {

    public static final Charset UTF_8 = Charset.forName("UTF8");
    private File output = null;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ObjectMapper mapper;
    private PostFormConfiguration conf;

    @Autowired
    public RecorderService(
            ObjectMapper mapper,
            PostFormConfiguration conf
    ) {
        this.mapper = mapper;
        this.conf = conf;
        Instant now = Instant.now();
        long forTomorrow = Utils.msBeforeTomorrowUTC(now);
        setOutputFile();
        executor.scheduleAtFixedRate(this::setOutputFile, forTomorrow, 24*3600*1000L,TimeUnit.MILLISECONDS);
    }


    private void setOutputFile() {
        String year = Utils.dayOfYear();
        output = new File(conf.getBase(),"out-"+year+".json");
    }

    public CompletableFuture<Void> record(String line) {
        CompletableFuture future = new CompletableFuture();
        executor.execute(() -> {
            try {
                writeToFile(line);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    private void writeToFile(String lines) throws IOException {
        FileUtils.write(output, lines, UTF_8, true);
    }

    public <T>  CompletableFuture<Void> record(T requestEntity) throws IOException {
        CompletableFuture future = new CompletableFuture();
        executor.execute(() -> {
            try {
                String s = mapper.writeValueAsString(requestEntity);
                writeToFile(s + "\n");
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public Path getCurrentOutput() {
        if (output != null) {
            return output.toPath();
        }
        return null;
    }
}
