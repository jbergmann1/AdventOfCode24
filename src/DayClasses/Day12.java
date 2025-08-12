package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day12 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "input12.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int rows = 0;
            char[][] map = new char[140][140];
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    map[rows][i] = line.charAt(i);
                }
                rows++;
            }
            List<List<Tuple<Integer>>> regions = getRegions(map);
            int price = 0;
            int priceNew = 0;
            for (List<Tuple<Integer>> region : regions) {
                price += calculatePerimeter(map, region) * region.size();
                priceNew += calculateSides(map, region) * region.size();
            }
            return "Price for part one: " + price + "\nPrice for part two: " + priceNew;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private void bfs(char[][] map, boolean[][] visited, int startRow, int startCol, List<Tuple<Integer>> region, char charToFind) {
        Queue<Tuple<Integer>> queue = new LinkedList<>();
        queue.add(new Tuple<>(startRow, startCol));
        visited[startRow][startCol] = true;
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, 1, -1};

        while (!queue.isEmpty()) {
            Tuple<Integer> current = queue.poll();
            int row = current.x();
            int col = current.y();
            region.add(new Tuple<>(row, col));

            for (int i = 0; i < 4; i++) {
                int newRow = row + dr[i];
                int newCol = col + dc[i];
                if (newRow >= 0 && newRow < map.length && newCol >= 0 && newCol < map[0].length &&
                        !visited[newRow][newCol] && map[newRow][newCol] == charToFind) {
                    queue.add(new Tuple<>(newRow, newCol));
                    visited[newRow][newCol] = true;
                }
            }
        }
    }

    private List<List<Tuple<Integer>>> getRegions(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<List<Tuple<Integer>>> regions = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!visited[row][col]) {
                    List<Tuple<Integer>> region = new ArrayList<>();
                    bfs(map, visited, row, col, region, map[row][col]);
                    if (!region.isEmpty()) {
                        regions.add(region);
                    }
                }
            }
        }
        return regions;
    }

    private int calculatePerimeter(char[][] map, List<Tuple<Integer>> region) {
        int perimeter = 0;

        for (Tuple<Integer> coordinates : region) {
            int row = coordinates.x();
            int col = coordinates.y();
            char field = map[row][col];
            if (row == map.length - 1 || map[row+1][col] != field) perimeter++;
            if (row == 0 || map[row-1][col] != field) perimeter++;
            if (col == map[row].length - 1 || map[row][col+1] != field) perimeter++;
            if (col == 0 || map[row][col-1] != field) perimeter++;
        }
        return perimeter;
    }

    private int calculateSides(char[][] map, List<Tuple<Integer>> region) {
        int sides = 0;
        Set<Tuple<Integer>> visited = new HashSet<>();

        for (Tuple<Integer> coordinates : region) {
            visited.add(coordinates);
            int row = coordinates.x();
            int col = coordinates.y();
            if (isLeftOuterBorder(map, row, col) && (!isLeftOuterBorder(map, row+1, col) || !visited.contains(new Tuple<>(row + 1, col))) &&
                    (!isLeftOuterBorder(map, row-1, col) || !visited.contains(new Tuple<>(row - 1, col)))) {
                sides++;
            }
            if (isRightOuterBorder(map, row, col) && (!isRightOuterBorder(map, row+1, col) || !visited.contains(new Tuple<>(row + 1, col))) &&
                    (!isRightOuterBorder(map, row-1, col) || !visited.contains(new Tuple<>(row - 1, col)))) {
                sides++;
            }
            if (isTopOuterBorder(map, row, col) && (!isTopOuterBorder(map, row, col+1) || !visited.contains(new Tuple<>(row, col + 1))) &&
                    (!isTopOuterBorder(map, row, col-1) || !visited.contains(new Tuple<>(row, col - 1)))) {
                sides++;
            }
            if (isBottomOuterBorder(map, row, col) && (!isBottomOuterBorder(map, row, col+1) || !visited.contains(new Tuple<>(row, col + 1))) &&
                    (!isBottomOuterBorder(map, row, col-1) || !visited.contains(new Tuple<>(row, col - 1)))) {
                sides++;
            }
        }
        return sides;
    }

    private boolean isLeftOuterBorder(char[][] map, int row, int col) {
        if (row < 0 || row >= map.length || col < 0 || col >= map[row].length) return false;
        return col == 0 || map[row][col - 1] != map[row][col];
    }

    private boolean isRightOuterBorder(char[][] map, int row, int col) {
        if (row < 0 || row >= map.length || col < 0 || col >= map[row].length) return false;
        return col == map[row].length - 1 || map[row][col + 1] != map[row][col];
    }

    private boolean isTopOuterBorder(char[][] map, int row, int col) {
        if (row < 0 || row >= map.length || col < 0 || col >= map[row].length) return false;
        return row == 0 || map[row - 1][col] != map[row][col];
    }

    private boolean isBottomOuterBorder(char[][] map, int row, int col) {
        if (row < 0 || row >= map.length || col < 0 || col >= map[row].length) return false;
        return row == map.length - 1 || map[row + 1][col] != map[row][col];
    }
}
