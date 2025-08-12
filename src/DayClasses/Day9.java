package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day9 implements Day {
    private static int lastIndex = 0;

    @Override
    public String execute() {
        String filePath = Day.filePath + "input9.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String map = reader.readLine();
            List<String> disk = generateDiskFromMap(map);
            List<String> copyDisk = new ArrayList<>(disk);
            int lastNumber = disk.size() - 1;
            while (lastNumber > disk.indexOf(".")) {
                disk = moveFileBlock(disk, lastNumber);
                lastNumber = findLastNumber(disk, lastNumber);
            }
            System.out.println(calculateCheckSum(disk));
            List<TreeSet<Integer>> freeSpace = calculateFreeSpace(copyDisk);
            lastIndex = copyDisk.size() - 1;
            for (int i = 9999; i > 0; i--) {
                copyDisk = moveFile(copyDisk, freeSpace, i);
            }
            return Long.toString(calculateCheckSum(copyDisk));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private List<String> generateDiskFromMap(String map) {
        List<String> disk = new ArrayList<>();
        boolean isFile = true;
        for (int i = 0; i < map.length(); i++) {
            int fileLength = Integer.parseInt(map.substring(i, i + 1));
            for (int j = 0; j < fileLength; j++) {
                if (isFile) {
                    disk.add(String.valueOf(i/2));
                } else {
                    disk.add(".");
                }
            }
            isFile = !isFile;
        }
        return disk;
    }

    private List<String> moveFileBlock(List<String> disk, int lastNumber) {
        int leftMostSpace = disk.indexOf(".");
        int fileBlock = findLastNumber(disk, lastNumber);
        if (leftMostSpace > fileBlock) return disk;
        List<String> changedDisk = new ArrayList<>();
        changedDisk.addAll(disk.subList(0, leftMostSpace));
        changedDisk.add(disk.get(fileBlock));
        changedDisk.addAll(disk.subList(leftMostSpace + 1, fileBlock));
        changedDisk.add(disk.get(leftMostSpace));
        changedDisk.addAll(disk.subList(fileBlock + 1, disk.size()));
        return changedDisk;
    }

    private List<String> moveFile(List<String> disk, List<TreeSet<Integer>> freeSpace, int fileId) {
        int file = findFile(disk, fileId, lastIndex);
        int fileLength = getFileLength(disk, file);
        int leftMostSpace = -1;
        List<Integer[]> spaceMap = new ArrayList<>();
        for (int i = fileLength; i <= freeSpace.size(); i++) {
            if (freeSpace.get(i-1).isEmpty()) continue;
            spaceMap.add(new Integer[]{freeSpace.get(i-1).first(), (i-1)});
        }
        Integer[] smallest = null;
        for (Integer[] spaceMapping : spaceMap) {
            if (smallest == null || spaceMapping[0] < smallest[0]) smallest = spaceMapping;
        }
        if (smallest != null) leftMostSpace = freeSpace.get(smallest[1]).pollFirst();
        if (leftMostSpace == -1 || file == -1 || leftMostSpace > file) {
            return disk;
        }
        if ((smallest[1]+1) > fileLength) {
            freeSpace.get((smallest[1]+1)-fileLength-1).add(leftMostSpace+fileLength);
        }
        List<String> changedDisk = new ArrayList<>();
        changedDisk.addAll(disk.subList(0, leftMostSpace));
        for (int i = 0; i < fileLength; i++) {
            changedDisk.add(disk.get(file));
        }
        changedDisk.addAll(disk.subList(leftMostSpace + fileLength, file - (fileLength - 1)));
        for (int i = 0; i < fileLength; i++) {
            changedDisk.add(".");
        }
        changedDisk.addAll(disk.subList(file + 1, disk.size()));
        lastIndex = file - fileLength;
        return changedDisk;
    }

    private int findLastNumber(List<String> disk, int lastNumber) {
        for (int i = lastNumber; i > 0; i--) {
            if (!Objects.equals(disk.get(i), ".")) {
                return i;
            }
        }
        return -1;
    }

    private int findFile(List<String> disk, int fileId, int lastNumber) {
        for (int i = lastNumber; i > 0; i--) {
            if (Objects.equals(disk.get(i), ".")) continue;
            if (Integer.parseInt(disk.get(i)) == fileId) {
                return i;
            }
        }
        return -1;
    }

    private int getFileLength(List<String> disk, int lastNumber) {
        int fileLength = 1;
        for (int i = lastNumber - 1; i > 0; i--) {
            if (Objects.equals(disk.get(i), disk.get(lastNumber))) {
                fileLength++;
                continue;
            }
            break;
        }
        return fileLength;
    }

    private long calculateCheckSum(List<String> disk) {
        long sum = 0;
        for (int i = 0; i < disk.size(); i++) {
            if (Objects.equals(disk.get(i), ".")) continue;
            sum += i * Long.parseLong(disk.get(i));
        }
        return sum;
    }

    private List<TreeSet<Integer>> calculateFreeSpace(List<String> disk) {
        int spaceLength = 0;
        boolean spaceFound = false;
        List<TreeSet<Integer>> freeSpace = new LinkedList<>();
        for (int i = 0; i < disk.size(); i++) {
            String block = disk.get(i);
            if (block.equals(".") && spaceFound) {
                spaceLength++;
            } else if (block.equals(".")) {
                spaceFound = true;
                spaceLength++;
            } else if (spaceFound) {
                if (freeSpace.size() < spaceLength) {
                    for (int j = freeSpace.size(); j < spaceLength; j++) {
                        freeSpace.add(new TreeSet<>());
                    }
                }
                freeSpace.get(spaceLength-1).add(i-spaceLength);
                spaceLength = 0;
                spaceFound = false;
            }
        }
        return freeSpace;
    }
}
