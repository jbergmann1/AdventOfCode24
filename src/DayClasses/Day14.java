package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day14 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "input14.txt";
        final int width = 101;
        final int height = 103;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            List<Tupel<Integer>> positions = new ArrayList<>();
            List<Tupel<Integer>> velocities = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] position = line.substring(0, line.indexOf(' ')).substring(2).split(",");
                String[] velocity = line.substring(line.indexOf('v')).substring(2).split(",");
                positions.add(new Tupel<>(Integer.parseInt(position[0]), Integer.parseInt(position[1])));
                velocities.add(new Tupel<>(Integer.parseInt(velocity[0]), Integer.parseInt(velocity[1])));
            }
            int[][] floor = new int[height][width];
            for (Tupel<Integer> position : positions) {
                floor[position.y()][position.x()]++;
            }
            Tupel<Integer> tree = new Tupel<>(0, 0);
            for (int i = 0; i < 9999; i++) {
                int size = getBiggestRegion(floor);
                if (size > tree.x()) tree = new Tupel<>(size, i);
                for (int j = 0; j < positions.size(); j++) {
                    positions.set(j, moveRobot(floor, positions.get(j), velocities.get(j)));
                }
            }
            return Integer.toString(calculateSafetyFactor(floor)) + "\n" + tree;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private Tupel<Integer> moveRobot(int[][] floor, Tupel<Integer> position, Tupel<Integer> velocity) {
        int newX = position.x() + velocity.x();
        int newY = position.y() + velocity.y();
        if (newX >= floor[0].length) newX -= floor[0].length;
        if (newX < 0) newX += floor[0].length;
        if (newY >= floor.length) newY -= floor.length;
        if (newY < 0) newY += floor.length;
        floor[position.y()][position.x()]--;
        floor[newY][newX]++;
        return new Tupel<>(newX, newY);
    }

    private int calculateSafetyFactor(int[][] floor) {
        int halfRows = floor.length / 2;
        int halfCols = floor[0].length / 2;
        int[][] quadrant1 = new int[halfRows][halfCols];
        int[][] quadrant2 = new int[halfRows][halfCols];
        int[][] quadrant3 = new int[halfRows][halfCols];
        int[][] quadrant4 = new int[halfRows][halfCols];
        for (int i = 0; i < quadrant1.length; i++) {
            System.arraycopy(floor[i], 0, quadrant1[i], 0, halfCols);
            System.arraycopy(floor[i], halfCols + 1, quadrant2[i], 0, halfCols);
            System.arraycopy(floor[i + halfRows + 1], 0, quadrant3[i], 0, halfCols);
            System.arraycopy(floor[i + halfRows + 1], halfCols + 1, quadrant4[i], 0, halfCols);
        }
        int[] robotCounts = new int[4];
        for (int i = 0; i < quadrant1.length; i++) {
            for (int j = 0; j < quadrant1[i].length; j++) {
                robotCounts[0] += quadrant1[i][j];
                robotCounts[1] += quadrant2[i][j];
                robotCounts[2] += quadrant3[i][j];
                robotCounts[3] += quadrant4[i][j];
            }
        }
        return robotCounts[0] * robotCounts[1] * robotCounts[2] * robotCounts[3];
    }

    private void bfs(int[][] floor, boolean[][] visited, int startRow, int startCol, List<Tupel<Integer>> region) {
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
                if (newRow >= 0 && newRow < floor.length && newCol >= 0 && newCol < floor[0].length &&
                        !visited[newRow][newCol] && floor[newRow][newCol] != 0) {
                    queue.add(new Tupel<>(newRow, newCol));
                    visited[newRow][newCol] = true;
                }
            }
        }
    }

    private int getBiggestRegion(int[][] floor) {
        int rows = floor.length;
        int cols = floor[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<List<Tupel<Integer>>> regions = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!visited[row][col]) {
                    List<Tupel<Integer>> region = new ArrayList<>();
                    bfs(floor, visited, row, col, region);
                    if (!region.isEmpty()) {
                        regions.add(region);
                    }
                }
            }
        }
        int size = 0;
        for (List<Tupel<Integer>> region : regions) {
            if (region.size() > size) size = region.size();
        }
        return size;
    }
}
