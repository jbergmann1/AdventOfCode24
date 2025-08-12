package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day18 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "testInput.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            final int memorySize = 7;
            String line;
            int currentTime = 12;
            final Character[][] memoryMap = new Character[memorySize][memorySize];
            final int[][][] shortestPath = new int[memorySize][memorySize][2];
            var bytePositions = new ArrayList<Tuple<Integer>>();
            var helper = new Helper<Character>();
            while ((line = reader.readLine()) != null) {
                var coordinates = line.split(",");
                bytePositions.add(new Tuple<>(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
            }
            for (Character[] row : memoryMap) {
                Arrays.fill(row, '.');
            }
            for (int i = 0; i < currentTime; i++) {
                var position = bytePositions.get(i);
                memoryMap[position.x()][position.y()] = '#';
            }
            for (int row = 0; row < memorySize; row++) {
                for (int col = 0; col < memorySize; col++) {
                    int distanceToExit = euclideanDistance(new Tuple<>(memorySize-1, memorySize-1), new Tuple<>(row, col));
                    shortestPath[row][col][0] = Integer.MAX_VALUE;
                    shortestPath[row][col][1] = distanceToExit;
                }
            }
            helper.printArrayNative(memoryMap);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public int euclideanDistance(Tuple<Integer> a, Tuple<Integer> b) {
        double sum = 0;
        for (int i = 0; i < a.values().size(); i++) {
            sum += Math.pow(b.values().get(i) - a.values().get(i), 2);
        }
        return Math.toIntExact(Math.round(Math.sqrt(sum)));
    }

    public int calculateShortestPath(Character[][] memoryMap, int[][][] shortestPath) {
        Queue<Tuple<Integer>> queue = new LinkedList<>();
        HashSet<Tuple<Integer>> visited = new HashSet<>();
        int row = 0;
        int col = 0;
        queue.add(new Tuple<>(row, col));
        shortestPath[row][col][0] = 0;

        while (!queue.isEmpty()) {
            int score = shortestPath[row][col][0] + euclideanDistance(new Tuple<>(memoryMap.length - 1, memoryMap.length - 1), new Tuple<>(row, col));
            var potentialNeighbours = getNeighbours(memoryMap, visited, row, col);
        }

        return 0;
    }

    public List<Tuple<Integer>> getNeighbours(Character[][] memoryMap, HashSet<Tuple<Integer>> visited, int row, int col) {
        List<Tuple<Integer>> neighbours = new ArrayList<>();
        int[] xDirection = {-1, 0, 1, 0};
        int[] yDirection = {0, 1, 0, -1};
        for (int i = 0; i < 4; i++) {
            int newRow = row + xDirection[i];
            int newCol = col + yDirection[i];
            if (newRow < 0 || newRow >= memoryMap.length || newCol < 0 || newCol >= memoryMap.length) continue;
            if (visited.contains(new Tuple<>(newRow, newCol))) continue;
            if (memoryMap[newRow][newCol] == '#') continue;
            neighbours.add(new Tuple<>(newRow, newCol));
        }
        return neighbours;
    }
}
