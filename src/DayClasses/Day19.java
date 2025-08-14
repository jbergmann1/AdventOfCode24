package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day19 implements Day {
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

            int repeatablePatternCount = 0;
            for (String pattern : patterns) {
                System.out.println(pattern);
                if (repeatPattern(pattern, towels)) repeatablePatternCount++;
            }
            return "Repeatable patterns: " + repeatablePatternCount;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public boolean repeatPattern(String pattern, List<String> towels) {
        if (pattern.equals("wwgubrruggbgrwbggwbrubwggbwuuuwbbrwgbgbbbrbwruwwwwurw")) {
            System.out.println("");
        }
        return patternHelper(pattern, towels, 0);
    }

    private boolean patternHelper(String pattern, List<String> towels, int index) {
        String remainingPattern = pattern.substring(index);
        if (index == pattern.length() - 1) return true;
        if (index >= pattern.length() - 1) return false;

        List<String> extensions = getExtensions(towels, remainingPattern);
        for (String extension : extensions) {
            if (patternHelper(pattern, towels, index + extension.length())) {
                return true;
            }
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
