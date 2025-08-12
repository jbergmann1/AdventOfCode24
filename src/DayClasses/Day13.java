package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day13 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "input13.txt";
        final long conversionError = 10000000000000L;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int index = 0;
            List<Tuple<Long>> a = new ArrayList<>();
            List<Tuple<Long>> b = new ArrayList<>();
            List<Tuple<Long>> prize = new ArrayList<>();
            String[] subS;
            while ((line = reader.readLine()) != null) {
                int lineType = index % 4;
                switch (lineType) {
                    case 0:
                        subS = line.split("\\+");
                        a.add(new Tuple<>(Long.parseLong(subS[1].substring(0, subS[1].indexOf(","))), Long.parseLong(subS[2])));
                        break;
                    case 1:
                        subS = line.split("\\+");
                        b.add(new Tuple<>(Long.parseLong(subS[1].substring(0, subS[1].indexOf(","))), Long.parseLong(subS[2])));
                        break;
                    case 2:
                        subS = line.split("=");
                        prize.add(new Tuple<>(Long.parseLong(subS[1].substring(0, subS[1].indexOf(",")))+conversionError, Long.parseLong(subS[2])+conversionError));
                        break;
                    default:
                        break;
                }
                index++;
            }

            long cost = 0;
            for (int i = 0; i < prize.size(); i++) {
                cost += calculateMinCost(a.get(i), b.get(i), prize.get(i));
            }
            return Long.toString(cost);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private long calculateMinCost(Tuple<Long> a, Tuple<Long> b, Tuple<Long> prize) {
        long det1 = calculateDeterminant(prize, b);
        long det2 = calculateDeterminant(a, prize);
        long det3 = calculateDeterminant(a, b);
        long costA = det1 % det3 == 0 ? det1 / det3 : 0;
        long costB = det2 % det3 == 0 ? det2 / det3 : 0;
        if (costA == 0 || costB == 0) return 0;
        return costA * 3 + costB;
    }

    private long calculateDeterminant(Tuple<Long> col1, Tuple<Long> col2) {
        return col1.x() * col2.y() - col1.y() * col2.x();
    }
}
