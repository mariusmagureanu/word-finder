package com.word;

import java.io.Console;
import java.util.*;

public class Main {

    private static final int DISPLAY_LIMIT = 10;

    private static void print(final Map<String, Integer> input) {

        if (input.values().isEmpty() || Collections.max(input.values()) ==0) {
            System.out.println("$ no matches found\n");
            return;
        }

        final LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        input.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        var idx = 1;
        for (Map.Entry<String, Integer> x : reverseSortedMap.entrySet()) {
            System.out.printf("%2d. %-30s: %d%%\n", idx, x.getKey(), x.getValue());
            if (idx == DISPLAY_LIMIT) {
                break;
            }
            idx++;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Not enough provided arguments\n");
            System.out.println("Usage:");
            System.out.println("      java com.word.Main /some/path/to/a/folder\n");
            System.exit(1);
        }

        final Console console = System.console();
        final WordCounter wc = new WordCounter(args[0]);

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
