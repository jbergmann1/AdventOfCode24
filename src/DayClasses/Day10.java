package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

public class Day10 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "input10.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int[][] map = new int[55][55];
            int rows = 0;
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    map[rows][i] = Integer.parseInt(line.substring(i, i + 1));
                }
                rows++;
            }
            int result = 0;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] != 0) continue;
                    //System.out.println("i: " + i + ", j: " + j + ", score: " + result + ", value: " + map[i][j]);
                    result += calculateScore(map, i, j, 0, -1, new HashSet<>());
                }
            }
            System.out.println(result);
            result = 0;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] != 0) continue;
                    //System.out.println("i: " + i + ", j: " + j + ", score: " + result + ", value: " + map[i][j]);
                    result += calculateRating(map, i, j, 0, -1);
                }
            }
            return Integer.toString(result);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private int calculateScore(int[][] map, int row, int col, int currentScore, int lastHeight, HashSet<Tuple<Integer>> visited) {
        if (row < 0 || col < 0 || row == map.length || col == map[row].length || map[row][col] != lastHeight + 1 || visited.contains(new Tuple<>(row, col))) return 0;
        int height = map[row][col];
        visited.add(new Tuple<>(row, col));
        if (height == 9) return 1;
        int newScore = currentScore;
        newScore += calculateScore(map, row + 1, col, currentScore, height, visited);
        newScore += calculateScore(map, row - 1, col, currentScore, height, visited);
        newScore += calculateScore(map, row, col + 1, currentScore, height, visited);
        newScore += calculateScore(map, row, col - 1, currentScore, height, visited);
        if (newScore == currentScore) visited.remove(new Tuple<>(row, col));
        return newScore;
    }

    private int calculateRating(int[][] map, int row, int col, int currentRating, int lastHeight) {
        if (row < 0 || col < 0 || row == map.length || col == map[row].length || map[row][col] != lastHeight + 1) return 0;
        int height = map[row][col];
        if (height == 9) return 1;
        int newRating = currentRating;
        newRating += calculateRating(map, row + 1, col, currentRating, height);
        newRating += calculateRating(map, row - 1, col, currentRating, height);
        newRating += calculateRating(map, row, col + 1, currentRating, height);
        newRating += calculateRating(map, row, col - 1, currentRating, height);
        return newRating;
    }
}
