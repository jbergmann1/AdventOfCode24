package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day12 implements Day {
    @Override
    public void execute() {
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
            List<List<Tupel<Integer>>> regions = getRegions(map);
            int price = 0;
            int priceNew = 0;
            for (List<Tupel<Integer>> region : regions) {
                price += calculatePerimeter(map, region) * region.size();
                priceNew += calculateSides(map, region) * region.size();
            }
            System.out.println("Price for part one: "+price);
            System.out.println("Price for part two: "+priceNew);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bfs(char[][] map, boolean[][] visited, int startRow, int startCol, List<Tupel<Integer>> region, char charToFind) {
        Queue<Tupel<Integer>> queue = new LinkedList<>();
        queue.add(new Tupel<>(startRow, startCol));
        visited[startRow][startCol] = true;
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, 1, -1};

        while (!queue.isEmpty()) {
            Tupel<Integer> current = queue.poll();
            int row = current.x();
            int col = current.y();
            region.add(new Tupel<>(row, col));

            for (int i = 0; i < 4; i++) {
                int newRow = row + dr[i];
                int newCol = col + dc[i];
                if (newRow >= 0 && newRow < map.length && newCol >= 0 && newCol < map[0].length &&
                        !visited[newRow][newCol] && map[newRow][newCol] == charToFind) {
                    queue.add(new Tupel<>(newRow, newCol));
                    visited[newRow][newCol] = true;
                }
            }
        }
    }

    private List<List<Tupel<Integer>>> getRegions(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<List<Tupel<Integer>>> regions = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!visited[row][col]) {
                    List<Tupel<Integer>> region = new ArrayList<>();
                    bfs(map, visited, row, col, region, map[row][col]);
                    if (!region.isEmpty()) {
                        regions.add(region);
                    }
                }
            }
        }
        return regions;
    }

    private int calculatePerimeter(char[][] map, List<Tupel<Integer>> region) {
        int perimeter = 0;

        for (Tupel<Integer> coordinates : region) {
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

    private int calculateSides(char[][] map, List<Tupel<Integer>> region) {
        int sides = 0;
        Set<Tupel<Integer>> visited = new HashSet<>();

        for (Tupel<Integer> coordinates : region) {
            visited.add(coordinates);
            int row = coordinates.x();
            int col = coordinates.y();
            if (isLeftOuterBorder(map, row, col) && (!isLeftOuterBorder(map, row+1, col) || !visited.contains(new Tupel<>(row + 1, col))) &&
                    (!isLeftOuterBorder(map, row-1, col) || !visited.contains(new Tupel<>(row - 1, col)))) {
                sides++;
            }
            if (isRightOuterBorder(map, row, col) && (!isRightOuterBorder(map, row+1, col) || !visited.contains(new Tupel<>(row + 1, col))) &&
                    (!isRightOuterBorder(map, row-1, col) || !visited.contains(new Tupel<>(row - 1, col)))) {
                sides++;
            }
            if (isTopOuterBorder(map, row, col) && (!isTopOuterBorder(map, row, col+1) || !visited.contains(new Tupel<>(row, col + 1))) &&
                    (!isTopOuterBorder(map, row, col-1) || !visited.contains(new Tupel<>(row, col - 1)))) {
                sides++;
            }
            if (isBottomOuterBorder(map, row, col) && (!isBottomOuterBorder(map, row, col+1) || !visited.contains(new Tupel<>(row, col + 1))) &&
                    (!isBottomOuterBorder(map, row, col-1) || !visited.contains(new Tupel<>(row, col - 1)))) {
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
