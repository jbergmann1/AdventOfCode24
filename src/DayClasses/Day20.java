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
            helper.printArray(racetrack);
            var path = getPath(racetrack, new Tuple<>(3, 1));
            var cheats = getCheats(racetrack, path);
            System.out.println(cheats.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private List<Tuple<Integer>> getCheats(Character[][] racetrack, List<Tuple<Integer>> path) {
        List<Tuple<Integer>> cheats = new ArrayList<>();
        int[] dirX = {0, 1, 0, -1};
        int[] dirY = {1, 0, -1, 0};

        for (Tuple<Integer> currentPosition : path) {
            for (int i = 0; i < 4; i++) {
                int cheatStartX = currentPosition.x() + dirX[i];
                int cheatStartY = currentPosition.y() + dirY[i];
                if (cheatStartX < 0 || cheatStartY < 0 || cheatStartX >= racetrack.length || cheatStartY >= racetrack.length) {
                    continue;
                }
                if (racetrack[cheatStartX][cheatStartY] == '#') {
                    for (int j = 0; j < 4; j++) {
                        int cheatEndX = cheatStartX + dirX[j];
                        int cheatEndY = cheatStartY + dirY[j];
                        if (!path.contains(new Tuple<>(cheatEndX, cheatEndY))) {
                            continue;
                        }
                        if (path.indexOf(new Tuple<>(cheatEndX, cheatEndY)) > path.indexOf(currentPosition)) {
                            cheats.add(currentPosition);
                        }
                    }
                }
            }
        }
        return cheats;
    }

    private List<Tuple<Integer>> getPath(Character[][] racetrack, Tuple<Integer> startPosition) {
        List<Tuple<Integer>> path = new ArrayList<>();
        Tuple<Integer> currentPosition = startPosition;
        int[] dirX = {0, 1, 0, -1};
        int[] dirY = {1, 0, -1, 0};

        while (racetrack[currentPosition.x()][currentPosition.y()] != 'E') {
            int x = 0;
            int y = 0;
            for (int i = 0; i < 4; i++) {
                x = currentPosition.x() + dirX[i];
                y = currentPosition.y() + dirY[i];
                if (x < 0 || y < 0 || y >= racetrack.length || x >= racetrack.length) {
                    continue;
                }
                if ((racetrack[x][y] == '.' || racetrack[x][y] == 'E') && !path.contains(new Tuple<>(x, y))){
                    break;
                }
            }
            currentPosition = new Tuple<>(x, y);
            path.add(currentPosition);
        }
        return path;
    }
}