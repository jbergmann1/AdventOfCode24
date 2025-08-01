package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day17 implements Day {
    public static long A, B, C;
    public static int pointer;

    @Override
    public void execute() {
        String filePath = Day.filePath + "input17.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            List<Integer> program = new ArrayList<>();
            String programString;
            for (int i = 0; i < 5; i++) {
                line = reader.readLine();
                int lastSpaceIndex = line.lastIndexOf(" ") + 1;
                switch (i) {
                    case 0 -> A = Integer.parseInt(line.substring(lastSpaceIndex));
                    case 1 -> B = Integer.parseInt(line.substring(lastSpaceIndex));
                    case 2 -> C = Integer.parseInt(line.substring(lastSpaceIndex));
                    case 4 -> {
                        programString = line.substring(lastSpaceIndex);
                        for (String instruction : programString.split(",")) {
                            program.add(Integer.parseInt(instruction));
                        }
                    }
                }
            }
            String result = runProgram(program);
            System.out.println("output of initial program: " + result);
            System.out.println("correct value for A, so the program outputs a copy of itself: " + reverseProgram(program));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String runProgram(List<Integer> program) {
        StringBuilder output = new StringBuilder();
        int instruction, operand;

        while (pointer < program.size() - 1) {
            instruction = program.get(pointer);
            operand = program.get(pointer + 1);
            boolean hasJumped = false;
            switch (instruction) {
                case 0:
                    adv(operand);
                    break;
                case 1:
                    bxl(operand);
                    break;
                case 2:
                    bst(operand);
                    break;
                case 3:
                    hasJumped = jnz(operand);
                    break;
                case 4:
                    bxc(operand);
                    break;
                case 5:
                    output.append(out(operand)).append(",");
                    break;
                case 6:
                    bdv(operand);
                    break;
                case 7:
                    cdv(operand);
                    break;
            }
            if (!hasJumped) pointer += 2;
        }

        return !output.isEmpty() ? output.substring(0, output.length() - 1) : output.toString();
    }

    private void adv(int operand) { A >>= getCombo(operand); } //opcode 0

    private void bxl(int operand) { B ^= operand; } //opcode 1

    private void bst(int operand) { B = getCombo(operand) % 8; } //opcode 2

    private boolean jnz(int operand) { //opcode 3
        boolean jumped = A != 0;
        if (jumped) pointer = operand;
        return jumped;
    }

    private void bxc(int operand) { // opcode 4
        B ^= C;
    } //opcode 4

    private long out(int operand) { // opcode 5
        return getCombo(operand) % 8;
    } //opcode 5

    private void bdv(int operand) { B = A >> getCombo(operand); } //opcode 6

    private void cdv(int operand) { C = A >> getCombo(operand); } //opcode 7

    private long getCombo(int operand) {
        return switch (operand) {
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            case 7 -> -1;
            default -> operand;
        };
    }

    private List<Integer> convertToInteger(String input) {
        if (input == null || input.isEmpty()) return new ArrayList<>();
        String[] split = input.split(",");
        List<Integer> result = new ArrayList<>();
        for (String s : split) {
            result.add(Integer.parseInt(s));
        }
        return result;
    }

    private long reverseProgram(List<Integer> program) {
        int iterationCount = 0;
        long currentA = 0;
        List<Integer> aExtensions = new ArrayList<>(); //try 0 - 7
        aExtensions.add(0);
        while (iterationCount < program.size()) {
            if (iterationCount < 0) return iterationCount; //tried all combinations, no valid solution
            A = (currentA << 3) + aExtensions.getLast(); //add extension to bit shifted A
            B = 0;
            C = 0;
            pointer = 0;
            var currentResult = convertToInteger(runProgram(program));
            if (currentResult.isEmpty() || !currentResult.equals(program.subList(program.size() - iterationCount - 1, program.size()))) { //current solution already wrong
                if (aExtensions.getLast() == 7) { //dead end, go one step back and increment extension
                    aExtensions.removeLast();
                    aExtensions.set(aExtensions.size() - 1, aExtensions.getLast() + 1);
                    currentA >>= 3;
                    iterationCount--;
                    continue;
                }
                aExtensions.set(aExtensions.size() - 1, aExtensions.getLast() + 1); //increment extension
                continue;
            }
            //successful iteration
            currentA = (currentA << 3) + aExtensions.getLast();
            aExtensions.add(0);
            iterationCount++;
        }
        return currentA;
    }
}
