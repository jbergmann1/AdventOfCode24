package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Day7 implements Day {
    @Override
    public void execute() {
        String filePath = Day.filePath + "input7.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            ArrayList<ArrayList<Long>> equations = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                ArrayList<Long> equation = new ArrayList<>();
                String[] split = line.split(":");
                equation.add(Long.parseLong(split[0]));
                split = split[1].split(" ");
                for (String s : split) {
                    if (!Objects.equals(s, "")) equation.add(Long.parseLong(s));
                }
                equations.add(equation);
            }
            System.out.println(calculate(equations));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long calculate(ArrayList<ArrayList<Long>> equations) {
        long result = 0;
        for (ArrayList<Long> equation : equations) {
            if (calcStep(equation, 1, 0)) result += equation.getFirst();
        }
        return result;
    }

    private boolean calcStep(ArrayList<Long> equation, int step, long currentSum) {
        if (step == equation.size()) {
            return currentSum == equation.getFirst();
        }
        long nextNumber = equation.get(step);
        if (calcStep(equation, step + 1, currentSum + nextNumber)) return true;
        if (currentSum != 0 && calcStep(equation, step + 1, currentSum * nextNumber)) return true;
        if (calcStep(equation, step + 1, concatenate(currentSum, nextNumber))) return true;
        return false;
    }

    private long concatenate(long a, long b) {
        return Long.parseLong(Long.toString(a) + b);
    }
}
