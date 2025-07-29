package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Day1 implements Day {
    @Override
    public void execute() {
        String filePath = Day.filePath + "input1.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int correctReportCount = 0;
            while ((line = reader.readLine()) != null) {
                LinkedList<Integer> report = new LinkedList<Integer>();
                StringBuilder num = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ' ') {
                        report.add(Integer.parseInt(num.toString()));
                        num = new StringBuilder();
                        continue;
                    }
                    num.append(line.charAt(i));
                }
                report.add(Integer.parseInt(num.toString()));

                if (Correct(report)) {
                    correctReportCount++;
                } else {
                    for (int i = 0; i < report.size(); i++) {
                        LinkedList<Integer> newReport = new LinkedList<>(report);
                        newReport.remove(i);
                        if (Correct(newReport)) {
                            correctReportCount++;
                            break;
                        }
                    }
                }
            }
            System.out.println("Count of correct reports: "+correctReportCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean Correct(LinkedList<Integer> report) {
        boolean correctLevel = true;
        boolean incr = report.get(1) > report.get(0);
        int min, max, current, next;

        for (int i = 0; i < report.size() - 1; i++) {
            current = report.get(i);
            next = report.get(i + 1);

            min = incr ? current + 1 : current - 1;
            max = incr ? current + 3 : current - 3;
            if (incr && (next > max || next < min) || !incr && (next < max || next > min)) {
                correctLevel = false;
            }
        }
        return correctLevel;
    }
}
