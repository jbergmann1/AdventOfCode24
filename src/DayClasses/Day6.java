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
    public void execute() {
        String filePath = Day.filePath + "input6.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int index = 0;
            char[][] map = new char[130][130];
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    map[index][i] = line.charAt(i);
                }
                index++;
            }
            int[] position = new int[2];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] != '.' && map[i][j] != '#') {
                        position[0] = i;
                        position[1] = j;
                        break;
                    }
                }
            }
            int[] startPosition = Arrays.copyOfRange(position, 0, 2);
            char[][] tmpMap = new char[map.length][];
            char[][] directionMap = new char[map.length][map.length];
            HashMap<Tupel<Integer>, Character> overwrittenDirections = new HashMap<>();
            for (int i = 0; i < map.length; i++) {
                tmpMap[i] = Arrays.copyOf(map[i], map[i].length);
            }
            int cycleCount = 0;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (i == 30 && j == 60) {
                        System.out.println();
                    }
                    if (map[i][j] == '#') continue;
                    map[i][j] = '#';
                    for (char[] chars : directionMap) {
                        Arrays.fill(chars, ' ');
                    }
                    overwrittenDirections.clear();
                    boolean hasCycle = false;
                    while (position != null && !hasCycle) {
                        hasCycle = checkCycle(map, directionMap, overwrittenDirections, position[0], position[1]);
                        position = doStep(map, directionMap, overwrittenDirections, position[0], position[1]);
                    }
                    if (hasCycle) cycleCount++;
                    for (int ind = 0; ind < map.length; ind++) {
                        map[ind] = Arrays.copyOf(tmpMap[ind], tmpMap[ind].length);
                    }
                    position = Arrays.copyOfRange(startPosition, 0, 2);
                }
            }
            while (position != null) {
                position = doStep(map, directionMap, overwrittenDirections, position[0], position[1]);
            }
            for (char[] chars : map) {
                System.out.println();
                for (char aChar : chars) {
                    System.out.print(aChar + " ");
                }
            }
            System.out.println();
            System.out.println();
            System.out.println("distinct visited positions: "+countPositions(map)+", cycle count: "+cycleCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] doStep(char[][] map, char[][] directionMap, HashMap<Tupel<Integer>, Character> overwrittenDirections, int x, int y) {
        char guard = map[x][y];
        if (directionMap[x][y] != ' ') {
            overwrittenDirections.put(new Tupel<>(x, y), directionMap[x][y]);
        }
        directionMap[x][y] = guard;
        try {
            switch (guard) {
                case '<':
                    if (map[x][y-1] == '#') {
                        map[x][y] = '^';
                        return new int[]{x, y};
                    } else {
                        map[x][y] = 'X';
                        map[x][y-1] = guard;
                        return new int[]{x, y-1};
                    }
                case '>':
                    if (map[x][y+1] == '#') {
                        map[x][y] = 'v';
                        return new int[]{x, y};
                    } else {
                        map[x][y] = 'X';
                        map[x][y+1] = guard;
                        return new int[]{x, y+1};
                    }
                case '^':
                    if (map[x-1][y] == '#') {
                        map[x][y] = '>';
                        return new int[]{x, y};
                    } else {
                        map[x][y] = 'X';
                        map[x-1][y] = guard;
                        return new int[]{x-1, y};
                    }
                case 'v':
                    if (map[x+1][y] == '#') {
                        map[x][y] = '<';
                        return new int[]{x, y};
                    } else {
                        map[x][y] = 'X';
                        map[x+1][y] = guard;
                        return new int[]{x+1, y};
                    }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            map[x][y] = 'X';
        }
        return null;
    }

    private boolean checkCycle(char[][] map, char[][] directionMap, HashMap<Tupel<Integer>, Character> overwrittenDirections, int x, int y) {
        return map[x][y] == directionMap[x][y] || overwrittenDirections.getOrDefault(new Tupel<>(x, y), '-') == directionMap[x][y];
    }

    private int countPositions(char[][]map) {
        int count = 0;
        for (char[] chars : map) {
            for (char aChar : chars) {
                if (aChar == 'X') count++;
            }
        }
        return count;
    }
}