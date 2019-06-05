package fr.fasar.postform;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fasar.postform.controller.RequestEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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

    @Autowired
    public RecorderService(
            ObjectMapper mapper
    ) {
        this.mapper = mapper;
        Instant now = Instant.now();
        long forTomorrow = msBeforeTomorrowUTC(now);
        setOutputFile();
        executor.scheduleAtFixedRate(this::setOutputFile, forTomorrow, 24*3600*1000L,TimeUnit.MILLISECONDS);
    }

    protected static long msBeforeTomorrowUTC(Instant now) {
        Instant tomorrow = now.plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        return tomorrow.toEpochMilli() - now.toEpochMilli();
    }

    protected static String dayOfYear() {
        Instant now = Instant.now();
        ZonedDateTime day = now.atZone(ZoneOffset.UTC);
        return String.format("%04d%02d%02d", day.get(ChronoField.YEAR), day.get(ChronoField.MONTH_OF_YEAR), day.get(ChronoField.DAY_OF_MONTH));
    }

    private void setOutputFile() {
        String year = dayOfYear();
        output = new File("out-"+year+".json");
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

    public CompletableFuture<Void> record(RequestEntity requestEntity) throws IOException {
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
}
