package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day19 implements Day {
    private final static Map<Map.Entry<String, String>, Boolean> mem = new HashMap<>();
    private final static Map<Map.Entry<Integer, String>, Integer> solutionCount = new HashMap<>();
    @Override
    public String execute() {
        String filePath = Day.filePath + "input19.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            List<String> towels = Arrays.asList(line.split(", "));
            List<String> patterns = new ArrayList<>();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                patterns.add(line);
            }

            int repeatablePatternsCount = 0;
            for (String pattern : patterns) {
                if (repeatPattern(pattern, towels)) repeatablePatternsCount++;
            }
            long solutionCounter = 0;
            for (String pattern : patterns) {
                System.out.println(pattern);
                if (getSolutionCount(pattern, towels)) {
                    for (var entry : solutionCount.keySet()) {
                        if (entry.getKey() == 0) {
                            solutionCounter += solutionCount.get(entry);
                        }
                    }
                }
            }
            return "Repeatable patterns: " + repeatablePatternsCount + "\nTotal solutions: " + solutionCounter;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public boolean repeatPattern(String pattern, List<String> towels) {
        return patternHelper(pattern, towels, 0);
    }

    private boolean patternHelper(String pattern, List<String> towels, int index) {
        if (index == pattern.length()) {
            return true;
        }
        String remainingPattern = pattern.substring(index);
        List<String> extensions = getExtensions(towels, remainingPattern);

        for (String extension : extensions) {
            Boolean memExtension = mem.get(new AbstractMap.SimpleEntry<>(remainingPattern, extension));

            if (memExtension != null) {
                if (memExtension) {
                    return true;
                }
                continue;
            }
            if (patternHelper(pattern, towels, index + extension.length())) {
                mem.put(new AbstractMap.SimpleEntry<>(remainingPattern, extension), true);
                return true;
            }
            mem.put(new AbstractMap.SimpleEntry<>(remainingPattern, extension), false);
        }
        return false;
    }

    private boolean getSolutionCount(String pattern, List<String> towels) {
        solutionCount.clear();
        return solutionCountHelper(pattern, towels, 0);
    }

    private boolean solutionCountHelper(String pattern, List<String> towels, int index) {
        if (index == pattern.length()) {
            return true;
        }
        String remainingPattern = pattern.substring(index);
        List<String> extensions = getExtensions(towels, pattern.substring(index));
        boolean foundSolution = false;

        for (String extension : extensions) {
            int memExtension = solutionCount.getOrDefault(new AbstractMap.SimpleEntry<>(index, extension), -1);
            if (memExtension > 0) {
                foundSolution = true;
                continue;
            } else if (memExtension == 0) {
                continue;
            }
            //596594064985
            //640028712201
            if (solutionCountHelper(pattern, towels, index + extension.length())) {
                int currentCount = solutionCount.getOrDefault(new AbstractMap.SimpleEntry<>(index, extension), 0);
                int followingCount = 0;
                for (var entry : solutionCount.keySet()) {
                    if (entry.getKey() == index + extension.length()) {
                        followingCount += solutionCount.get(entry);
                    }
                }
                solutionCount.put(new AbstractMap.SimpleEntry<>(index, extension), Math.max(currentCount + followingCount, 1));
                foundSolution = true;
            } else {
                solutionCount.put(new AbstractMap.SimpleEntry<>(index, extension), 0);
            }
        }
        return foundSolution;
    }

    private List<String> getExtensions(List<String> towels, String remainingPattern) {
        List<String> extensions = new ArrayList<>();

        for (String towel : towels) {
            if (!remainingPattern.startsWith(towel)) {
                continue;
            }
            extensions.add(towel);
        }
        return extensions;
    }
}
