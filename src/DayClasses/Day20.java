package DayClasses;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day20 implements Day {
    @Override
    public String execute() {
        Path filePath = Paths.get(Day.filePath + "testInput.txt");
        int size = 15;
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            Character[][] racetrack = new Character[size][size];
            int index = 0;
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    racetrack[index][i] = line.charAt(i);
                }
                index++;
            }
            Helper<Character> helper = new Helper<>();
            var startPosition = new Tuple<Integer>();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (racetrack[i][j] == 'S') {
                        startPosition = new Tuple<>(i, j);
                    }
                }
            }
            var path = getPath(racetrack, startPosition);
            var cheats = getCheats(racetrack, path);
            helper.printArray(racetrack);
            System.out.println(cheats.size());
            System.out.println(cheats);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private List<Tuple<Integer>> getCheats(Character[][] racetrack, List<Tuple<Integer>> path) {
        List<Tuple<Integer>> cheatEnds = new ArrayList<>();
        int[] dirX = {0, 1, 0, -1};
        int[] dirY = {1, 0, -1, 0};

        for (Tuple<Integer> currentPosition : path) {
            var k = path.indexOf(currentPosition);
            //cheatEnds.addAll(getCheatsForPosition(racetrack, path, currentPosition, dirX, dirY));
            int previousSize = cheatEnds.size();
            for (var neighbour : getNeighbours(racetrack, currentPosition, new Tuple<>(), '#')){
                //getCheatEnds(racetrack, path, neighbour, new HashSet<>(), cheatEnds);
            }
            var newCheatEnds = cheatEnds.subList(previousSize, cheatEnds.size());
            var invalidCheats = new HashSet<Tuple<Integer>>();
            for (var cheatEnd : newCheatEnds) {
                if (!(path.indexOf(cheatEnd) > path.indexOf(currentPosition))) {
                    invalidCheats.add(cheatEnd);
                }
            }
            cheatEnds.removeAll(invalidCheats);
        }
        return cheatEnds;
    }

    private boolean getCheatEnds(Character[][] racetrack, List<Tuple<Integer>> path, Tuple<Integer> currentPosition, Set<Tuple<Integer>> visited, List<Tuple<Integer>> cheatEnds) {
        if (path.contains(currentPosition)) {
            cheatEnds.add(currentPosition);
            return true;
        }
        for (var neighbour : getNeighbours(racetrack, currentPosition, new Tuple<>(), '#', '.', 'E')) {
            if (visited.contains(neighbour)) {
                continue;
            }
            visited.add(neighbour);
            if (getCheatEnds(racetrack, path, neighbour, visited, cheatEnds)) {
                return true;
            }
            visited.remove(neighbour);
        }
        return false;
    }

    private List<Tuple<Integer>> getCheatsForPosition(Character[][] racetrack, List<Tuple<Integer>> path, Tuple<Integer> currentPosition, int[] dirX, int[] dirY) {
        List<Tuple<Integer>> cheats = new ArrayList<>();
        var cheatStart = new Tuple<>(currentPosition.x(), currentPosition.y());
        for (int i = 0; i < 4; i++) {
            int cheatX = currentPosition.x() + dirX[i];
            int cheatY = currentPosition.y() + dirY[i];
            if (cheatX < 0 || cheatY < 0 || cheatX >= racetrack.length || cheatY >= racetrack.length) {
                continue;
            }
            if (racetrack[cheatX][cheatY] == '#') {
                for (int j = 0; j < 4; j++) {
                    int cheatEndX = cheatX + dirX[j];
                    int cheatEndY = cheatY + dirY[j];
                    var cheatEnd = new Tuple<>(cheatEndX, cheatEndY);
                    if (!path.contains(new Tuple<>(cheatEndX, cheatEndY))) {
                        continue;
                    }
                    if (path.indexOf(new Tuple<>(cheatEndX, cheatEndY)) > path.indexOf(currentPosition) + 2) {
                        racetrack[cheatX][cheatY] = 'c';
                        var cheatPathStart = path.subList(0, path.indexOf(cheatStart));
                        var cheatPathEnd = path.subList(path.indexOf(cheatEnd), path.size() - 1);
                        if (path.size() - (cheatPathStart.size() + cheatPathEnd.size() + 2) >= 10) cheats.add(currentPosition);
                    }
                }
            }
        }
        return cheats;
    }

    private List<Tuple<Integer>> getPath(Character[][] racetrack, Tuple<Integer> startPosition) {
        List<Tuple<Integer>> path = new ArrayList<>();
        path.add(startPosition);
        Tuple<Integer> currentPosition = startPosition;
        Tuple<Integer> previousPosition = new Tuple<>();

        while (true) {
            var neighbours = getNeighbours(racetrack, currentPosition, previousPosition, '.', 'E');
            if (neighbours.isEmpty()) {
                break;
            }
            path.addAll(neighbours);
            previousPosition = currentPosition;
            currentPosition = path.getLast();
        }
        return path;
    }

    private List<Tuple<Integer>> getNeighbours(Character[][] racetrack, Tuple<Integer> currentPosition, Tuple<Integer> previousPosition, char... acceptableValues) {
        List<Tuple<Integer>> neighbours = new ArrayList<>();
        int[] dirX = {0, 1, 0, -1};
        int[] dirY = {1, 0, -1, 0};

        for (int i = 0; i < 4; i++) {
            int x = currentPosition.x() + dirX[i];
            int y = currentPosition.y() + dirY[i];
            if (x < 0 || y < 0 || x >= racetrack.length || y >= racetrack.length) {
                continue;
            }
            for (var value : acceptableValues) {
                if (racetrack[x][y] == value) {
                    neighbours.add(new Tuple<>(x, y));
                    break;
                }
            }
        }
        neighbours.remove(previousPosition);
        return neighbours;
    }
}