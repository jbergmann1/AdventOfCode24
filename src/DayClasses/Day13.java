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
    public void execute() {
        String filePath = Day.filePath + "input13.txt";
        final long conversionError = 10000000000000L;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int index = 0;
            List<Tupel<Long>> a = new ArrayList<>();
            List<Tupel<Long>> b = new ArrayList<>();
            List<Tupel<Long>> prize = new ArrayList<>();
            String[] subS;
            while ((line = reader.readLine()) != null) {
                int lineType = index % 4;
                switch (lineType) {
                    case 0:
                        subS = line.split("\\+");
                        a.add(new Tupel<>(Long.parseLong(subS[1].substring(0, subS[1].indexOf(","))), Long.parseLong(subS[2])));
                        break;
                    case 1:
                        subS = line.split("\\+");
                        b.add(new Tupel<>(Long.parseLong(subS[1].substring(0, subS[1].indexOf(","))), Long.parseLong(subS[2])));
                        break;
                    case 2:
                        subS = line.split("=");
                        prize.add(new Tupel<>(Long.parseLong(subS[1].substring(0, subS[1].indexOf(",")))+conversionError, Long.parseLong(subS[2])+conversionError));
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
            System.out.println(cost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long calculateMinCost(Tupel<Long> a, Tupel<Long> b, Tupel<Long> prize) {
        long det1 = calculateDeterminant(prize, b);
        long det2 = calculateDeterminant(a, prize);
        long det3 = calculateDeterminant(a, b);
        long costA = det1 % det3 == 0 ? det1 / det3 : 0;
        long costB = det2 % det3 == 0 ? det2 / det3 : 0;
        if (costA == 0 || costB == 0) return 0;
        return costA * 3 + costB;
    }

    private long calculateDeterminant(Tupel<Long> col1, Tupel<Long> col2) {
        return col1.x() * col2.y() - col1.y() * col2.x();
    }
}
