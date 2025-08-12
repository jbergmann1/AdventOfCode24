package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.BiFunction;

public class Day15 implements Day {
    @Override
    public String execute() {
        String filePath = Day.filePath + "input15.txt";
        final int size = 50;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int index = 0;
            char[][] warehouse = new char[size][size];
            char[][] wideWarehouse = new char[size][size * 2];
            StringBuilder movements = new StringBuilder();
            Tuple<Integer> position = new Tuple<>(0, 0);
            Tuple<Integer> widePosition = new Tuple<>(0, 0);
            while ((line = reader.readLine()) != null) {
                if (index < size) {
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        warehouse[index][i] = c;
                        fillWideWarehouse(wideWarehouse, c, index, i * 2);
                        if (c == '@') {
                            position = new Tuple<>(index, i);
                            widePosition = new Tuple<>(index, i * 2);
                        }
                    }
                    index++;
                } else {
                    movements.append(line);
                }
            }
            for (int i = 0; i < movements.length(); i++) {
                position = doMovement(warehouse, movements.charAt(i), position.x(), position.y());
                widePosition = doMovement(wideWarehouse, movements.charAt(i), widePosition.x(), widePosition.y());
            }
            return Integer.toString(sumGPS(warehouse)) + "\n" + sumGPS(wideWarehouse);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private Tuple<Integer> doMovement(char[][] warehouse, char movement, int row, int col) {
        return switch (movement) {
            case '<' -> movementHelper(warehouse, row, col, (r, c) -> new Tuple<>(r, c - 1));
            case '>' -> movementHelper(warehouse, row, col, (r, c) -> new Tuple<>(r, c + 1));
            case '^' -> movementHelper(warehouse, row, col, (r, c) -> new Tuple<>(r - 1, c));
            case 'v' -> movementHelper(warehouse, row, col, (r, c) -> new Tuple<>(r + 1, c));
            default -> new Tuple<>(row, col);
        };
    }

    private Tuple<Integer> movementHelper(char[][] warehouse, int row, int col, BiFunction<Integer, Integer, Tuple<Integer>> moveDirectional) {
        Queue<Tuple<Integer>> positionsToVisit = new LinkedList<>();
        Stack<Tuple<Integer>> positionsToMove = new Stack<>();
        positionsToVisit.add(new Tuple<>(row, col));
        while (!positionsToVisit.isEmpty()) {
            Tuple<Integer> currentPosition = positionsToVisit.poll();
            Tuple<Integer> nextPosition = moveDirectional.apply(currentPosition.x(), currentPosition.y());
            char currentObject = warehouse[currentPosition.x()][currentPosition.y()];
            BiFunction<Integer, Integer, Tuple<Integer>> moveToCompleteBox;
            switch (currentObject) {
                case '#':
                    return new Tuple<>(row, col);
                case '.':
                    continue;
                case '[':
                    moveToCompleteBox = (r, c) -> new Tuple<>(r, c + 1);
                    break;
                case ']':
                    moveToCompleteBox = (r, c) -> new Tuple<>(r, c - 1);
                    break;
                default:
                    moveToCompleteBox = Tuple::new;
            }
            addNextToVisit(positionsToVisit, positionsToMove, currentPosition, nextPosition, moveToCompleteBox, moveDirectional);
        }
        while (!positionsToMove.isEmpty()) {
            Tuple<Integer> currentPos = positionsToMove.pop();
            Tuple<Integer> newPos = moveDirectional.apply(currentPos.x(), currentPos.y());
            warehouse[newPos.x()][newPos.y()] = warehouse[currentPos.x()][currentPos.y()];
            for (int i = 0; i < positionsToMove.size(); i++) {
                if (moveDirectional.apply(positionsToMove.get(i).x(), positionsToMove.get(i).y()).equals(currentPos)) break;
                if (i == positionsToMove.size() - 1) warehouse[currentPos.x()][currentPos.y()] = '.';
            }
            if (positionsToMove.isEmpty()) warehouse[currentPos.x()][currentPos.y()] = '.';
        }
        return moveDirectional.apply(row, col);
    }

    private void addNextToVisit(Queue<Tuple<Integer>> positionsToVisit, Stack<Tuple<Integer>> positionsToMove, Tuple<Integer> currentPosition, Tuple<Integer> nextPosition, BiFunction<Integer, Integer, Tuple<Integer>> moveToCompleteBox, BiFunction<Integer, Integer, Tuple<Integer>> moveDirectional) {
        Tuple<Integer> secondBoxPart = moveToCompleteBox.apply(currentPosition.x(), currentPosition.y());
        Tuple<Integer> additionalNextPosition = moveDirectional.apply(secondBoxPart.x(), secondBoxPart.y());
        addToDataStructures(currentPosition, nextPosition, currentPosition, positionsToVisit, positionsToMove);
        if (secondBoxPart.equals(currentPosition)) return;
        addToDataStructures(currentPosition, additionalNextPosition, secondBoxPart, positionsToVisit, positionsToMove);
    }

    private void addToDataStructures(Tuple<Integer> currentPosition, Tuple<Integer> nextPosition, Tuple<Integer> positionToMove, Queue<Tuple<Integer>> positionsToVisit, Stack<Tuple<Integer>> positionsToMove) {
        if (!positionsToVisit.contains(nextPosition) && !nextPosition.equals(currentPosition)) {
            positionsToVisit.add(nextPosition);
            positionsToMove.push(positionToMove);
        }
    }

    private int sumGPS(char[][] warehouse) {
        int sum = 0;
        for (int i = 0; i < warehouse.length; i++) {
            for (int j = 0; j < warehouse[i].length; j++) {
                if (warehouse[i][j] == 'O' || warehouse[i][j] == '[') sum += 100 * i + j;
            }
        }
        return sum;
    }

    private void fillWideWarehouse(char[][] warehouse, char c, int row, int col) {
        warehouse[row][col] = c == 'O' ? '[' : c;
        warehouse[row][col+1] = switch (c) {
            case '@' -> '.';
            case 'O' -> ']';
            default -> c;
        };
    }
}
