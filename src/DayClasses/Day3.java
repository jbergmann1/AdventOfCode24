package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "input3.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            long sum = 0;
            boolean doMul = true;
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    Boolean checkDo = checkDo(line.substring(i));
                    if (checkDo != null) doMul = checkDo;
                    if(line.startsWith("mul(", i)) {
                        int mulIndex = checkMul(line.substring(i));
                        if (mulIndex != -1 && doMul) {
                            sum += mul(line.substring(i, i + mulIndex));
                            i = i + mulIndex;
                        }
                    }
                }
            }
            return Long.toString(sum);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private int checkMul(String instruction) {
        String pattern = "^mul\\(\\d{1,3},\\d{1,3}\\)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(instruction);
        if (matcher.find()) return matcher.end()-1;
        return -1;
    }

    private Boolean checkDo(String instruction) {
        if (instruction.startsWith("don't()")) return false;
        else if (instruction.startsWith("do()")) return true;
        return null;
    }

    private int mul(String instruction) {
        StringBuilder xString = new StringBuilder();
        StringBuilder yString = new StringBuilder();
        for (int i = 4; i <= 6; i++) {
            if (Character.isDigit(instruction.charAt(i))) xString.append(instruction.charAt(i));
            else break;
        }
        if (xString.length() == 1) yString.append(instruction.charAt(6));
        for (int i = 7; i < instruction.length() ; i++) {
            if (Character.isDigit(instruction.charAt(i))) yString.append(instruction.charAt(i));
        }
        return Integer.parseInt(xString.toString()) * Integer.parseInt(yString.toString());
    }
}
