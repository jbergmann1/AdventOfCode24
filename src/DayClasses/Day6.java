package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class Day6 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "input6.txt";
        Helper<Character> helper = new Helper<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int index = 0;
            Character[][] map = new Character[130][130];
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    map[index][i] = line.charAt(i);
                }
                index++;
            }
            Tupel<Integer> position = new Tupel<>(-1, -1);
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] != '.' && map[i][j] != '#') {
                        position = new Tupel<>(i, j);
                        break;
                    }
                }
            }
            var startPosition = new Tupel<>(position.x(), position.y());
            Character[][] tmpMap = new Character[map.length][];
            char[][] directionMap = new char[map.length][map.length];
            HashMap<Tupel<Integer>, Character> overwrittenDirections = new HashMap<>();
            for (int i = 0; i < map.length; i++) {
                tmpMap[i] = Arrays.copyOf(map[i], map[i].length);
            }
            int cycleCount = 0;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] == '#') continue;
                    map[i][j] = '#';
                    for (char[] chars : directionMap) {
                        Arrays.fill(chars, ' ');
                    }
                    overwrittenDirections.clear();
                    boolean hasCycle = false;
                    boolean samePosition = false;
                    var newPosition = new Tupel<>(-1, -1);
                    while (position != null && !hasCycle) {
                        if (directionMap[position.x()][position.y()] != ' ' && !samePosition) {
                            overwrittenDirections.put(new Tupel<>(position.x(), position.y()), directionMap[position.x()][position.y()]);
                        }
                        hasCycle = checkCycle(map, directionMap, overwrittenDirections, position.x(), position.y());
                        newPosition = doStep(map, directionMap, position.x(), position.y());
                        samePosition = newPosition != null && newPosition.equals(position);
                        position = newPosition;
                    }
                    if (hasCycle) cycleCount++;
                    for (int ind = 0; ind < map.length; ind++) {
                        map[ind] = Arrays.copyOf(tmpMap[ind], tmpMap[ind].length);
                    }
                    position = new Tupel<>(startPosition.x(), startPosition.y());
                }
            }
            while (position != null) {
                position = doStep(map, directionMap, position.x(), position.y());
            }
            helper.printArray(map);
            return "distinct visited positions: "+countPositions(map)+", cycle count: "+cycleCount;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private Tupel<Integer> doStep(Character[][] map, char[][] directionMap, int x, int y) {
        char guard = map[x][y];
        directionMap[x][y] = guard;
        try {
            switch (guard) {
                case '<':
                    if (map[x][y-1] == '#') {
                        map[x][y] = '^';
                        return new Tupel<>(x, y);
                    } else {
                        map[x][y] = 'X';
                        map[x][y-1] = guard;
                        return new Tupel<>(x, y-1);
                    }
                case '>':
                    if (map[x][y+1] == '#') {
                        map[x][y] = 'v';
                        return new Tupel<>(x, y);
                    } else {
                        map[x][y] = 'X';
                        map[x][y+1] = guard;
                        return new Tupel<>(x, y+1);
                    }
                case '^':
                    if (map[x-1][y] == '#') {
                        map[x][y] = '>';
                        return new Tupel<>(x, y);
                    } else {
                        map[x][y] = 'X';
                        map[x-1][y] = guard;
                        return new Tupel<>(x-1, y);
                    }
                case 'v':
                    if (map[x+1][y] == '#') {
                        map[x][y] = '<';
                        return new Tupel<>(x, y);
                    } else {
                        map[x][y] = 'X';
                        map[x+1][y] = guard;
                        return new Tupel<>(x+1, y);
                    }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            map[x][y] = 'X';
        }
        return null;
    }

    private boolean checkCycle(Character[][] map, char[][] directionMap, HashMap<Tupel<Integer>, Character> overwrittenDirections, int x, int y) {
        return map[x][y] == directionMap[x][y] || overwrittenDirections.getOrDefault(new Tupel<>(x, y), '-') == map[x][y];
    }

    private int countPositions(Character[][] map) {
        int count = 0;
        for (Character[] chars : map) {
            for (char aChar : chars) {
                if (aChar == 'X') count++;
            }
        }
        return count;
    }
}