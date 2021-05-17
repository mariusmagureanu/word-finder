package com.word;

import java.io.Console;
import java.util.*;

public class Main {

    private static final int DISPLAY_LIMIT = 10;
    private static final Console console = System.console();

    private static void print(final Map<String, Integer> input) {

        if (input.values().isEmpty() || Collections.max(input.values()) == 0) {
            console.writer().println("$ no matches found\n");
            return;
        }

        final LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        input.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        var idx = 1;
        for (Map.Entry<String, Integer> x : reverseSortedMap.entrySet()) {
            console.writer().printf("%2d. %-30s: %d%%\n", idx, x.getKey(), x.getValue());
            if (idx == DISPLAY_LIMIT) {
                break;
            }
            idx++;
        }
        console.writer().println();
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            console.writer().println("Not enough provided arguments\n");
            console.writer().println("Usage:");
            console.writer().println("      java com.word.Main /some/path/to/a/folder\n");
            System.exit(1);
        }

        final WordCounter wc = new WordCounter(args[0]);

        console.writer().printf("%d files read in directory: %s\n\n", wc.totalReadFiles(), args[0]);

        while (true) {
            var line = console.readLine("search $ ");
            if (line.strip().length() == 0) {
                continue;
            }

            var words = line.trim().split(WordCounter.WORD_REGEX);
            if (words.length == 1 && words[0].equalsIgnoreCase("quit")) {
                break;
            }

            var results = wc.search(words);
            print(results);
        }
    }
}
