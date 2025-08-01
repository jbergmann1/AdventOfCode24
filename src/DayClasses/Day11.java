package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 implements Day {
    public static Set<Long> memSet = new HashSet<>();
    public static Map<Long, Long> mem = new HashMap<>();
    public static Map<Long, List<Long>> changeStoneMem = new HashMap<>();

    @Override
    public void execute() {
        String filePath = Day.filePath + "input11.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            List<Long> stoneRow = new LinkedList<>();
            for (String stone : line.split(" ")) {
                stoneRow.add(Long.parseLong(stone));
            }
            for (Long stone : stoneRow) {
                memSet.add(stone);
                if (mem.containsKey(stone)) mem.put(stone, mem.get(stone) + 1);
                else mem.put(stone, (long) 1);
            }
            long result;
            for (int i = 0; i < 75; i++) {
                result = 0;
                for (Long stone : memSet) {
                    result += mem.get(stone);
                }
                System.out.println("blink "+i+": "+result);
                blinkPerformant();
            }
            result = 0;
            for (Long stone : memSet) {
                result += mem.get(stone);
            }
            System.out.println("final blink: "+result);
            /*for (int i = 0; i < stoneRow.size(); i++) {
                Collections.sort(stoneRow);
                System.out.println("blink "+i+": "+stoneRow.size());
                blink(stoneRow, stoneRow.size());
            }
            Collections.sort(stoneRow);
            System.out.println("final blink: "+stoneRow.size());*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Long> changeStone(Long stone) {
        List<Long> stones = new LinkedList<>();
        String stoneString = Long.toString(stone);
        if (stone == 0) {
            stones.add((long)1);
        } else if (stoneString.length() % 2 == 0) {
            stones.add(Long.parseLong(stoneString.substring(stoneString.length()/2)));
            stones.add(Long.parseLong(stoneString.substring(0, stoneString.length()/2)));
        } else {
            stones.add(stone * 2024);
        }
        changeStoneMem.put(stone, stones);
        return stones;
    }

    //naive implementation, very bad performance
    private void blink(List<Long> stoneRow, int size) {
        for (int index = size; index > 0; index--) {
            Long stone = stoneRow.removeFirst();
            List<Long> changedStones = changeStone(stone);
            stoneRow.addAll(changedStones);
        }
    }

    private void blinkPerformant() {
        Set<Long> stones = new HashSet<>(); //all distinct stone values after the blink
        Set<Long> removedStones = new HashSet<>(); //stones, that are no longer present after the blink
        Map<Long, Long> stonesMap = new HashMap<>(); //stone values --> stone count
        Map<Long, Long> stonesToSubtract = new HashMap<>(); //stone values --> stone count
        for (Long stone : memSet) {
            List<Long> changedStones = new LinkedList<>();
            if (changeStoneMem.containsKey(stone)) {
                changedStones.addAll(changeStoneMem.get(stone));
            } else {
                changedStones.addAll(changeStone(stone));
            }
            removedStones.add(stone);
            for (Long changedStone : changedStones) {
                if (!mem.containsKey(changedStone)) {
                    stonesMap.put(changedStone, stonesMap.getOrDefault(changedStone, (long) 0) + mem.get(stone));
                    stones.add(changedStone);
                }
                else {
                    stonesMap.put(changedStone, (stonesMap.getOrDefault(changedStone, (long) 0)) + mem.get(changedStone) + mem.get(stone));
                    stones.add(changedStone);
                    if (stonesToSubtract.containsKey(changedStone)) {
                        stonesToSubtract.put(changedStone, stonesToSubtract.get(changedStone) + mem.get(changedStone));
                    } else {
                        stonesToSubtract.put(changedStone, mem.get(changedStone));
                    }
                }
            }
            if (stonesMap.containsKey(stone)) {
                long stoneRemoveCount = stonesMap.get(stone) - stonesToSubtract.get(stone);
                mem.remove(stone);
                if (stoneRemoveCount <= 0) {
                    stones.remove(stone);
                    stonesMap.remove(stone);
                } else {
                    stonesMap.put(stone, stoneRemoveCount);
                }
            } else {
                mem.remove(stone);
            }
        }
        mem.putAll(stonesMap);
        memSet.removeAll(removedStones);
        memSet.addAll(stones);
    }
}
