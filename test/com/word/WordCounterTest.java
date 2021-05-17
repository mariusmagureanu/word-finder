package com.word;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordCounterTest {

    private static final File dataDir = new File("data");

    @AfterAll
    static void deleteDataFolder() {
        try (final Stream<Path> testFiles = Files.walk(dataDir.toPath())){
            testFiles.map(Path::toFile).forEach(File::delete);
            dataDir.deleteOnExit();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @BeforeAll
    static void createDataFolder() {
        final Map<String, String> testFiles = new HashMap<>();
        testFiles.put("a.txt", "foo !bar? baz");
        testFiles.put("b.txt", "fo .foo. foo.o");
        testFiles.put("c.txt", "   foo     bar      baz    ");
        testFiles.put("d.txt", "foo\n bar\n baz\n");
        testFiles.put("e.txt", "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                "industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. ");
        testFiles.put("f.txt", "");
        testFiles.put("g.txt", "       ");

        dataDir.mkdir();

        testFiles.forEach((key, value) -> {
            try (final FileWriter fileWriter = new FileWriter(Paths.get(dataDir.getPath(), key).toString())){
                fileWriter.write(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testBasicOutput() {
        final WordCounter wc = new WordCounter(dataDir.getPath());
        var out = wc.search(new String[]{"foo", "bar"});

        assertEquals(100, out.get("a.txt"));
        assertEquals(50, out.get("b.txt"));
        assertEquals(100, out.get("d.txt"));
        assertEquals(100, out.get("c.txt"));
        assertEquals(0, out.get("e.txt"));
    }

    @Test
    public void testNoWords() {
        final WordCounter wc = new WordCounter(dataDir.getPath());
        var out = wc.search(new String[]{});

        assertEquals(0, out.get("a.txt"));
        assertEquals(0, out.get("b.txt"));
        assertEquals(0, out.get("d.txt"));
        assertEquals(0, out.get("c.txt"));
        assertEquals(0, out.get("e.txt"));
    }

    @Test
    public void testSameWordMultipleTimes() {
        final WordCounter wc = new WordCounter(dataDir.getPath());
        var out = wc.search(new String[]{"foo", "foo", "foo"});

        assertEquals(100, out.get("a.txt"));
        assertEquals(100, out.get("b.txt"));
        assertEquals(100, out.get("d.txt"));
        assertEquals(100, out.get("c.txt"));
        assertEquals(0, out.get("e.txt"));
    }

    @Test
    public void testCaseSensitiveWords() {
        final WordCounter wc = new WordCounter(dataDir.getPath());
        var out = wc.search(new String[]{"foo", "Foo", "fOO"});

        assertEquals(33, out.get("a.txt"));
        assertEquals(33, out.get("b.txt"));
        assertEquals(33, out.get("d.txt"));
        assertEquals(33, out.get("c.txt"));
        assertEquals(0, out.get("e.txt"));
        assertEquals(0, out.get("f.txt"));
    }
}
