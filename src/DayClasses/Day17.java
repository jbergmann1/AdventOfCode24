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
            String programString = "";
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
            long originalA = A;
            long originalB = B;
            long originalC = C;
            String result = runProgram(program);
            System.out.println("original result: "+result);
            System.out.println("desired result: "+programString);
            List<Long> originalResult = convertToLong(result);
            List<Long> desiredResult = convertToLong(programString);
            long correctA = calculateAValue(originalA, originalResult, desiredResult);
            System.out.println("correct value for A: "+correctA);
            for (int i = (int) originalA; i < originalA + 100; i++) {
                A = i;
                B = originalB;
                C = originalC;
                pointer = 0;
                System.out.println(programString+"; A: "+A+"; B: "+B+"; C: "+C+" --> "+runProgram(program));
            }
            A = correctA;
            B = originalB;
            C = originalC;
            pointer = 0;
            String finalResult = runProgram(program);
            System.out.println("final result: "+finalResult);
            System.out.println(desiredResult.equals(convertToLong(finalResult)));
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

    private void adv(int operand) { // opcode 0
        A /= (int) Math.pow(2, getCombo(operand));
    }

    private void bxl(int operand) { // opcode 1
        B ^= operand;
    }

    private void bst(int operand) { // opcode 2
        B = getCombo(operand) % 8;
    }

    private boolean jnz(int operand) { // opcode 3
        if (A != 0) pointer = operand;
        return A != 0;
    }

    private void bxc(int operand) { // opcode 4
        B ^= C;
    }

    private long out(int operand) { // opcode 5
        return getCombo(operand) % 8;
    }

    private void bdv(int operand) { // opcode 6
        B = A / (int) Math.pow(2, getCombo(operand));
    }

    private void cdv(int operand) { // opcode 7
        C = A / (int) Math.pow(2, getCombo(operand));
    }

    private long getCombo(int operand) {
        return switch (operand) {
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            case 7 -> -1;
            default -> operand;
        };
    }

    private List<Long> convertToLong(String input) {
        String[] split = input.split(",");
        List<Long> result = new ArrayList<>();
        for (String s : split) {
            result.add(Long.parseLong(s));
        }
        return result;
    }

    private long calculateAValue(long originalA, List<Long> originalResult, List<Long> desiredResult) {
        long toAdd = originalA % 8;
        while (originalResult.size() < desiredResult.size()) {
            originalResult.add(0L);
        }
        //System.out.println(originalResult);
        for (int i = 0; i < desiredResult.size(); i++) {
            //long add = (long) (((desiredResult.get(i) == 0 ? 0 : desiredResult.get(i)) - originalResult.get(i) == 0 ? 0 : desiredResult.get(i)) * Math.pow(8, i + 1));
            long add = desiredResult.get(i);
            add = add - originalResult.get(i) == 0 ? 0 : (long) ((desiredResult.get(i)) * Math.pow(8, i + 1));
            toAdd += add;
        }
        return toAdd;
    }
}
