package fr.fasar.postform.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fasar.postform.PostFormConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class DbServices {
    @Autowired
    PostFormConfiguration conf;

    @Autowired
    private RecorderService recorderService;



    private ConcurrentHashMap<Path, Integer> lineFiles = new ConcurrentHashMap<>();
    private AtomicInteger nbLines = new AtomicInteger(0);


    /**
     * Return the list of files in asc order.
     * <p>
     * The older first
     *
     * @return
     * @throws IOException
     */
    public List<Path> listFiles() throws IOException {
        List<Path> collect = Files.list(conf.getBase().toPath())
                .filter(e -> e.toString().endsWith(".json"))
                .sorted()
                .collect(Collectors.toList());
        return collect;
    }

    public synchronized int nbLines() throws IOException {
        List<Path> paths = listFiles();
        Path currentOutput = recorderService.getCurrentOutput();
        for (Path path : paths) {
            if (!path.equals(currentOutput) && !lineFiles.containsKey(path)) {
                List<String> strings = Files.readAllLines(path);
                lineFiles.put(path, strings.size());
                nbLines.addAndGet(strings.size());
            }
        }
        int nbCurLine = 0;
        if (currentOutput != null) {
            nbCurLine = Files.readAllLines(currentOutput).size();
        }

        return nbLines.get() + nbCurLine;
    }

    /**
     * Get a max of pageSize lines and skip the pageNum * pageSize first lines
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<String> lines(int pageSize, int pageNum) throws IOException {
        Path currentOutput = recorderService.getCurrentOutput();
        List<String> curLines = Files.readAllLines(currentOutput);
        Collections.reverse(curLines);
        return curLines;
        //TODO: Implement this function
    }

}
