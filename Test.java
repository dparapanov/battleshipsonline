import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
    private File file;

    public Test(File file) {
        this.file = file;
    }

    public Map<Character, Integer> processPart(long start, long size, int numOfThread)
            throws Exception {
        System.out.println("the thread with numner is: " + numOfThread + "and the start is: " + start);
        Map<Character, Integer> freqTable = new HashMap<>();

        try (BufferedInputStream b = new BufferedInputStream(new FileInputStream(file))) {
            int buffSize = 1024 * 1024;
            int numOfChunks = (int) (size / buffSize);
            b.skip(start);
            byte[] buff = new byte[buffSize];
            for (int i = 0; i < numOfChunks; i++) {
                int readBytes = b.read(buff, 0, buffSize);
                for (int j = 0; j < readBytes; j++) {
                    char ch = (char) (buff[j] & 0xFF);
                    if (freqTable.containsKey(ch)) {
                        freqTable.put(ch, freqTable.get(ch) + 1);
                    } else {
                        freqTable.put(ch, 1);
                    }
                }
            }

            int remainingBytes = (int)(size % buffSize);
            int readBytes = b.read(buff, 0, remainingBytes);
            for (int j = 0; j < readBytes; j++) {
                char ch = (char) (buff[j] & 0xFF);
                if (freqTable.containsKey(ch)) {
                    freqTable.put(ch, freqTable.get(ch) + 1);
                } else {
                    freqTable.put(ch, 1);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return freqTable;
    }

    // Creates a task that will process the given portion of the file,
    // when executed.
    public Callable<Map<Character, Integer>> processPartTask(final long start, final long end, int numOfThread) {
        return new Callable<Map<Character, Integer>>() {
            public Map<Character, Integer> call()
                    throws Exception {
                return processPart(start, end, numOfThread);
            }
        };
    }

    // Tests the computation into chunks of the given size,
    // creates appropriate tasks and runs them using a 
    // given number of threads.
    public void processAll(int noOfThreads)
            throws Exception {
        int partLength = (int) (file.length() / noOfThreads);
        List<Callable<Map<Character, Integer>>> tasks = new ArrayList<>(noOfThreads);

        for (int i = 0; i < noOfThreads; i++) {
            if (i == noOfThreads - 1) {
                tasks.add(processPartTask(i * partLength, partLength + file.length() % noOfThreads, i));
                break;
            }
            tasks.add(processPartTask(i * partLength, partLength, i));
        }

        ExecutorService es = Executors.newFixedThreadPool(noOfThreads);

        Instant start = Instant.now();

        List<Future<Map<Character, Integer>>> results = es.invokeAll(tasks);

        Instant end = Instant.now();
        Duration time = Duration.between(start, end);

        es.shutdown();
        // use the results for something
        Map<Character, Integer> freqTable = new HashMap<>();

        for (Future<Map<Character, Integer>> result : results) {
            for (Map.Entry<Character, Integer> ent : result.get().entrySet()) {
                Character ch = ent.getKey();
                if (freqTable.containsKey(ch)) {
                    freqTable.put(ch, freqTable.get(ch) + ent.getValue());
                } else {
                    freqTable.put(ch, ent.getValue());
                }
            }
        }

        for (Map.Entry<Character, Integer> ent : freqTable.entrySet()) {
            System.out.println(ent.getKey() + " - > " + ent.getValue());
        }

        System.out.println("The time for processing file was: " + time.toMillis());
    }

    public static void main(String argv[])
            throws Exception {

        Test s = new Test(new File(argv[1])); // itcont.txt, testing.txt, testing2.txt, testing3.txt
        //Test s = new Test(new File("f.txt"));
        s.processAll(Integer.parseInt(argv[0]));
    }
}