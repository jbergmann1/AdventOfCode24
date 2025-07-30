package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day8 implements Day {
    @Override
    public void execute() {
        String filePath = Day.filePath + "input8.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int rows = 0;
            char[][] map = new char[50][50];
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    map[rows][i] = line.charAt(i);
                }
                rows++;
            }
            Map<Character, Integer> antennaCount = new HashMap<>();
            int[][][] antennas = new int[123][][];
            for (char[] row : map) {
                for (char col : row) {
                    if (col == '.') continue;
                    if (antennaCount.containsKey(col)) {
                        antennaCount.put(col, antennaCount.get(col) + 1);
                    } else {
                        antennaCount.put(col, 1);
                    }
                }
            }
            for (int i = 0; i < antennas.length; i++) {
                if (!antennaCount.containsKey((char) i)) continue;
                antennas[i] = new int[antennaCount.get((char) i)][2];
            }
            for (int i = 0; i < antennas.length; i++) {
                if (antennas[i] == null) continue;
                int j = 0;
                for (int k = 0; k < map.length; k++) {
                    for (int l = 0; l < map[k].length; l++) {
                        if (map[k][l] == (char) i) {
                            antennas[i][j][0] = k;
                            antennas[i][j][1] = l;
                            j++;
                        }
                    }
                }
            }
            Set<Tupel<Integer>> positionsA = new HashSet<>();
            Set<Tupel<Integer>> positionsB = new HashSet<>();
            for (int[][] antenna : antennas) {
                if (antenna == null) continue;
                for (int j = 0; j < antenna.length; j++) {
                    for (int k = j + 1; k < antenna.length; k++) {
                        int[] jAntenna = antenna[j];
                        int[] kAntenna = antenna[k];
                        double antennaDistance = euclideanDistance(jAntenna[0], jAntenna[1], kAntenna[0], kAntenna[1]);
                        for (int l = 0; l < map.length; l++) {
                            for (int m = 0; m < map[l].length; m++) {
                                double jDistance = euclideanDistance(l, m, jAntenna[0], jAntenna[1]);
                                double kDistance = euclideanDistance(l, m, kAntenna[0], kAntenna[1]);
                                double jGradient = gradient(l, m, jAntenna[0], jAntenna[1]);
                                double kGradient = gradient(l, m, kAntenna[0], kAntenna[1]);
                                if (jDistance == antennaDistance && kDistance == antennaDistance * 2 || jDistance == antennaDistance * 2 && kDistance == antennaDistance) {
                                    positionsA.add(new Tupel<>(l, m));
                                }
                                if (jGradient == kGradient || jDistance == 0 || kDistance == 0) {
                                    positionsB.add(new Tupel<>(l, m));
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(positionsA.size());
            System.out.println(positionsB.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double euclideanDistance(int row1, int col1, int row2, int col2) {
        return Math.sqrt(Math.pow((row1 - row2), 2) + Math.pow((col1 - col2), 2));
    }

    private double gradient(int row1, int col1, int row2, int col2) {
        return (double) (col2 - col1) / (row2 - row1);
    }
}
