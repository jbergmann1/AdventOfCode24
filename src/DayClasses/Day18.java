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
        String filePath = Day.filePath + "input18.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            final int memorySize = 71;
            String line;
            int currentTime = 1024;
            final Character[][] memoryMap = new Character[memorySize][memorySize];
            var bytePositions = new ArrayList<Tuple<Integer>>();
            while ((line = reader.readLine()) != null) {
                var coordinates = line.split(",");
                bytePositions.add(new Tuple<>(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
            }
            for (Character[] row : memoryMap) {
                Arrays.fill(row, '.');
            }

            StringBuilder output = new StringBuilder();
            for (int i = 0; i < bytePositions.size(); i++) {
                var position = bytePositions.get(i);
                memoryMap[position.x()][position.y()] = '#';
                int shortestPath = calculateShortestPath(memoryMap);
                if (i == currentTime - 1) {
                    output.append("Minimal number of steps necessary: ").append(shortestPath);
                }
                if (shortestPath == -1) {
                    output.append("\nFirst path blocking byte: ").append(position);
                    break;
                }
            }
            return output.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private int calculateShortestPath(Character[][] memoryMap) {
        Queue<Node18> queue = new PriorityQueue<>();
        Set<Node18> visited = new HashSet<>();
        int row = 0;
        int col = 0;
        int exitIndex = memoryMap.length - 1;
        queue.add(new Node18(row, col, 0, exitIndex));

        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (visited.contains(node)) continue;
            row = node.x;
            col = node.y;
            visited.add(node);
            if (node.h == 0) return node.g;
            var potentialNeighbours = getNeighbours(memoryMap, visited, row, col);
            for (var potentialNeighbour : potentialNeighbours) {
                int x = potentialNeighbour.x();
                int y = potentialNeighbour.y();
                var neighbourNode = new Node18(x, y, node.g + 1, exitIndex);
                queue.add(neighbourNode);
            }
        }
        return -1;
    }

    private List<Tuple<Integer>> getNeighbours(Character[][] memoryMap, Set<Node18> visited, int row, int col) {
        List<Tuple<Integer>> neighbours = new ArrayList<>();
        int[] xDirection = {-1, 0, 1, 0};
        int[] yDirection = {0, 1, 0, -1};
        for (int i = 0; i < 4; i++) {
            int newRow = row + xDirection[i];
            int newCol = col + yDirection[i];
            if (newRow < 0 || newRow >= memoryMap.length || newCol < 0 || newCol >= memoryMap.length) continue;
            if (visited.contains(new Node18(newRow, newCol, 0, 0))) continue;
            if (memoryMap[newRow][newCol] == '#') continue;
            neighbours.add(new Tuple<>(newRow, newCol));
        }
        return neighbours;
    }
}
