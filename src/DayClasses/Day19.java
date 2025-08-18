package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day19 implements Day {
    private final static Map<String, Long> solutionCounts = new HashMap<>();
    @Override
    public String execute() {
        Path filePath = Paths.get(Day.filePath + "input19.txt");
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            String[] towels = line.split(", ");
            List<String> patterns = new ArrayList<>();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                patterns.add(line);
            }

            int repeatablePatternsCount = 0;
            long solutionCount = 0;
            for (String pattern : patterns) {
                long perPatternCount = getSolutionCount(pattern, towels);
                if (perPatternCount > 0) repeatablePatternsCount++;
                solutionCount += perPatternCount;
            }
            return "Repeatable patterns: " + repeatablePatternsCount + "\nTotal solutions: " + solutionCount;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private long getSolutionCount(String pattern, String[] towels) {
        if (pattern.isEmpty()) {
            return 1;
        }
        if (solutionCounts.containsKey(pattern)) {
            return solutionCounts.get(pattern);
        }
        List<String> extensions = getExtensions(towels, pattern);
        long solutionCounter = 0;

        for (String extension : extensions) {
            solutionCounter += getSolutionCount(pattern.substring(extension.length()), towels);
        }
        solutionCounts.put(pattern, solutionCounter);
        return solutionCounter;
    }

    private List<String> getExtensions(String[] towels, String remainingPattern) {
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
