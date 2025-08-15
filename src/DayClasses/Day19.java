package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day19 implements Day {
    private final static Map<Map.Entry<String, String>, Boolean> mem = new HashMap<>();
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
            return "Repeatable patterns: " + repeatablePatternsCount;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public boolean repeatPattern(String pattern, List<String> towels) {
        return patternHelper(pattern, towels, 0, 0);
    }

    private boolean patternHelper(String pattern, List<String> towels, int index, int recursionDepth) {
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
            if (patternHelper(pattern, towels, index + extension.length(), recursionDepth + 1)) {
                mem.put(new AbstractMap.SimpleEntry<>(remainingPattern, extension), true);
                return true;
            }
            mem.put(new AbstractMap.SimpleEntry<>(remainingPattern, extension), false);
        }
        return false;
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
