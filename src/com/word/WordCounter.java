package com.word;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class WordCounter {

    final static String WORD_REGEX = "[^a-zA-Z]+";

    /**
     * folderContent is a dictionary which has words as keys and
     * a list of filenames where the word is to be found as values.
     */
    private final HashMap<String, List<String>> folderContent = new HashMap<>();

    /**
     * ranks is a dictionary which has a filename as key and the
     * percentage of word occurrences as values.
     */
    private final Map<String, Integer> ranks = new HashMap<>();

    public WordCounter(final String folderPath) {
        try {
            this.loadFolder(folderPath);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * fileWords returns a Set with all the words found in the
     * specified file.
     *
     * @param filePath Path to said file.
     * @return Set of words found in the file.
     */
    private Set<String> fileWords(final Path filePath) throws IOException {

        Set<String> fileContent;

        try (final Stream<String> lines = Files.lines(filePath, Charset.defaultCharset())) {
            fileContent = lines.flatMap(line -> Arrays.stream(line.split(WORD_REGEX))).collect(toSet());
        }

        return fileContent;
    }

    /**
     * loadFolder walks through a folder and reads all files in the
     * first level of depth. While doing so, it will populate a dictionary
     * which has words as keys and a list of files where those words are to
     * be found.
     *
     * @param folderPath Path to said folder.
     */
    private void loadFolder(final String folderPath) throws IOException {

        try (final Stream<Path> files = Files.walk(Paths.get(folderPath), 1)) {
            files.filter(Files::isRegularFile)
                    .forEach(f -> {
                                try {
                                    var fileName = f.getFileName().toString();
                                    var wordsFromFile = this.fileWords(f.toAbsolutePath());

                                    this.ranks.put(fileName, 0);
                                    wordsFromFile.forEach(w -> {
                                        this.folderContent.putIfAbsent(w, new ArrayList<>());
                                        this.folderContent.get(w).add(fileName);
                                    });
                                } catch (IOException e) {
                                    System.out.println(e);
                                    System.exit(1);
                                }
                            }
                    );
        }
    }

    /**
     * search looks up into the internal words dictionary to check if
     * the given words are found.
     *
     * @param words Array of words to lookup for.
     * @return A dictionary which has filenames as keys and a percentage
     * of word occurrence within each file as values.
     */
    public final Map<String, Integer> search(final String[] words) {
        // reset any previous ranks.
        this.ranks.entrySet().forEach(x -> x.setValue(0));

        if (words.length == 0) {
            return this.ranks;
        }

        Stream.of(words).forEach(w -> {
            this.folderContent.getOrDefault(w, new ArrayList<>()).forEach(f -> {
                var currentOccurrence = this.ranks.get(f);
                this.ranks.put(f, ++currentOccurrence);
            });
        });

        this.ranks.entrySet().forEach(e -> {
            var occurrences = e.getValue();
            e.setValue(occurrences * 100 / words.length);
        });

        return this.ranks;
    }
}