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
        Path filePath = Paths.get(Day.filePath + "input20.txt");
        int size = 141;
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
        List<Tuple<Integer>> cheats = new ArrayList<>();
        int[] dirX = {0, 1, 0, -1};
        int[] dirY = {1, 0, -1, 0};

        for (Tuple<Integer> currentPosition : path) {
            var cheatStart = new Tuple<Integer>(currentPosition.x(), currentPosition.y());
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
                        var cheatEnd = new Tuple<Integer>(cheatEndX, cheatEndY);
                        if (!path.contains(new Tuple<>(cheatEndX, cheatEndY))) {
                            continue;
                        }
                        if (path.indexOf(new Tuple<>(cheatEndX, cheatEndY)) > path.indexOf(currentPosition) + 2) {
                            racetrack[cheatX][cheatY] = 'c';
                            List<Tuple<Integer>> cheatPathStart = path.subList(0, path.indexOf(cheatStart));
                            var cheatPathEnd = path.subList(path.indexOf(cheatEnd), path.size() - 1);
                            if (path.size() - (cheatPathStart.size() + cheatPathEnd.size() + 2) >= 100) cheats.add(currentPosition);
                        }
                    }
                }
            }
        }
        return cheats;
    }

    private List<Tuple<Integer>> getPath(Character[][] racetrack, Tuple<Integer> startPosition) {
        List<Tuple<Integer>> path = new ArrayList<>(Collections.singleton(startPosition));
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