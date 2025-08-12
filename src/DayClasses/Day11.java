package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 implements Day {
    public static Map<Long, Long> mem = new HashMap<>();
    public static Map<Long, List<Long>> changeStoneMem = new HashMap<>();

    @Override
    public String execute() {
        String filePath = Day.filePath + "input11.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            List<Long> stoneRow = new LinkedList<>();
            for (String stone : line.split(" ")) {
                stoneRow.add(Long.parseLong(stone));
            }
            for (Long stone : stoneRow) {
                mem.put(stone, mem.getOrDefault(stone, 0L) + 1);
            }
            System.out.println("Please enter a number n > 0 to calculate the amount of stones after n blinks.");
            Scanner scanner = new Scanner(System.in);
            try {
                String input = scanner.nextLine();
                long n = Long.parseLong(input);
                for (int i = 0; i < n; i++) {
                    blinkPerformant(new HashSet<>(mem.keySet()));
                }
                return "blink " + n + ": " + getResult() + " stones.";
            } catch (NoSuchElementException | NumberFormatException e) {
                System.out.println("Invalid input.");
            }
            //scanner.close(); //infinite loop
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private List<Long> changeStone(Long stone) {
        List<Long> stones = new LinkedList<>();
        String stoneString = Long.toString(stone);
        if (stone == 0) {
            stones.add(1L);
        } else if (stoneString.length() % 2 == 0) {
            stones.add(Long.parseLong(stoneString.substring(stoneString.length() / 2)));
            stones.add(Long.parseLong(stoneString.substring(0, stoneString.length() / 2)));
        } else {
            stones.add(stone * 2024);
        }
        changeStoneMem.put(stone, stones);
        return stones;
    }

    //naive implementation, very bad performance
    /* private void blink(List<Long> stoneRow, int size) {
        for (int index = size; index > 0; index--) {
            Long stone = stoneRow.removeFirst();
            List<Long> changedStones = changeStone(stone);
            stoneRow.addAll(changedStones);
        }
    } */

    private void blinkPerformant(Set<Long> memSet) {
        Map<Long, Long> stonesMap = new HashMap<>(); //temporary map for changes in mem
        Map<Long, Long> stonesToSubtract = new HashMap<>(); //temporary map for memorizing unnecessary stones
        for (Long stone : memSet) {
            List<Long> changedStones = new LinkedList<>(changeStoneMem.getOrDefault(stone, changeStone(stone))); //one or two new stones for current stone
            for (Long changedStone : changedStones) {
                stonesMap.put(changedStone, (stonesMap.getOrDefault(changedStone, 0L) + mem.getOrDefault(changedStone, 0L) + mem.get(stone))); //increase value of changed stone by existing values + new value (stone)
                stonesToSubtract.merge(changedStone, mem.getOrDefault(changedStone, 0L), Long::sum); //unnecessary: existing value in mem since it will be transformed to a new value, too
            }
            if (stonesMap.containsKey(stone)) {
                stonesMap.merge(stone, stonesMap.get(stone) - stonesToSubtract.get(stone), (_, newValue) -> newValue <= 0 ? null : newValue); //adjust value in stonesMap based on unnecessary stones or remove, if 0
            }
            mem.remove(stone); //remove current stone after calculations are finished
        }
        mem.putAll(stonesMap); //adjust mem for next iteration
    }

    private Long getResult() {
        long result = 0;
        for (Long stone : mem.keySet()) {
            result += mem.get(stone);
        }
        return result;
    }
}
