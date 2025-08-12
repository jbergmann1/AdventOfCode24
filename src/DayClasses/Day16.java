package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day16 implements Day {
    public static int[][][] costs;

    @Override
    public String execute() {
        String filePath = Day.filePath + "input16.txt";
        final int size = 141;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int index = 0;
            Tupel<Integer> startPosition = new Tupel<>(0, 0);
            Tupel<Integer> endPosition = new Tupel<>(size - 1, size - 1);
            char[][] map = new char[size][size];
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    map[index][i] = line.charAt(i);
                    if (map[index][i] == 'S') startPosition = new Tupel<>(index, i);
                    if (map[index][i] == 'E') endPosition = new Tupel<>(index, i);
                }
                index++;
            }
            int[] dRow = {-1, 0, 1, 0};
            int[] dCol = {0, 1, 0, -1};
            return minCostPath(map, startPosition, endPosition, 1, dRow, dCol) + "\n" + optimalPathTiles(map, endPosition, dRow, dCol).size();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private Set<Tupel<Integer>> optimalPathTiles(char[][] map, Tupel<Integer> endPosition, int[] dRow, int[] dCol) {
        Set<Tupel<Integer>> tiles = new HashSet<>();

        Queue<State16> toVisit = new LinkedList<>();
        for (int direction = 0; direction < 4; direction++) {
            toVisit.add(new State16(endPosition.x(), endPosition.y(), direction, costs[endPosition.x()][endPosition.y()][direction]));
        }

        while (!toVisit.isEmpty()) {
            State16 current = toVisit.poll();
            int row = current.row;
            int col = current.col;
            int direction = Math.abs(current.direction);
            int currentCost = current.cost;
            tiles.add(new Tupel<>(row, col));

            int newRow = row - dRow[direction];
            int newCol = col - dCol[direction];
            if (isValid(newRow, newCol, map)) {
                if (currentCost - 1 == costs[newRow][newCol][direction]) {
                    costs[newRow][newCol][direction] = -1;
                    toVisit.add(new State16(newRow, newCol, direction, currentCost - 1));
                }
            }

            for (int newDirection = 0; newDirection < 4; newDirection++) {
                if (newDirection != direction) {
                    int rotationCost = (Math.abs(newDirection - direction) == 2) ? 2000 : 1000;
                    if (currentCost - rotationCost == costs[row][col][newDirection]) {
                        costs[row][col][newDirection] = -1;
                        toVisit.add(new State16(row, col, newDirection, currentCost - rotationCost));
                    }
                }
            }
        }

        return tiles;
    }

    private int minCostPath(char[][] map, Tupel<Integer> startPosition, Tupel<Integer> endPosition, int startDirection, int[] dRow, int[] dCol) {
        costs = new int[map.length][map[0].length][4];

        for (int[][] layer : costs) {
            for (int[] row : layer) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }

        Queue<State16> toVisit = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost));
        for (int direction = 0; direction < 4; direction++) {
            int initialCost = (direction == startDirection) ? 0 : 1000;
            costs[startPosition.x()][startPosition.y()][direction] = initialCost;
            toVisit.add(new State16(startPosition.x(), startPosition.y(), direction, initialCost));
        }

        while (!toVisit.isEmpty()) {
            State16 current = toVisit.poll();
            int row = current.row;
            int col = current.col;
            int direction = current.direction;
            int currentCost = current.cost;

            if (row == endPosition.x() && col == endPosition.y()) {
                return currentCost;
            }

            int newRow = row + dRow[direction];
            int newCol = col + dCol[direction];
            if (isValid(newRow, newCol, map)) {
                if (currentCost + 1 < costs[newRow][newCol][direction]) {
                    costs[newRow][newCol][direction] = currentCost + 1;
                    toVisit.add(new State16(newRow, newCol, direction, currentCost + 1));
                }
            }

            for (int newDirection = 0; newDirection < 4; newDirection++) {
                if (newDirection != direction) {
                    int rotationCost = (Math.abs(newDirection - direction) == 2) ? 2000 : 1000;
                    if (currentCost + rotationCost < costs[row][col][newDirection]) {
                        costs[row][col][newDirection] = currentCost + rotationCost;
                        toVisit.add(new State16(row, col, newDirection, currentCost + rotationCost));
                    }
                }
            }
        }

        return -1;
    }

    private boolean isValid(int row, int col, char[][] map) {
        return row >= 0 && row < map.length && col >= 0 && col < map[0].length && map[row][col] != '#';
    }
}
