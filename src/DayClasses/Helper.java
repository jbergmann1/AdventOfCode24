package DayClasses;

public class Helper<T> {

    public void printArray(T[][] array) {
        printColumnHeaders(array[0].length);
        for (int row = 0; row < array.length; row++) {
            printRow(array[row], row);
        }
        System.out.println();
    }

    private void printColumnHeaders(int columnCount) {
        System.out.print("    ");
        for (int col = 0; col < columnCount; col++) {
            System.out.print(formatIndex(col));
        }
        System.out.println();
    }

    private void printRow(T[] rowArray, int rowIndex) {
        System.out.print(formatIndex(rowIndex));
        for (T item : rowArray) {
            System.out.print(item + "   ");
        }
        System.out.println();
    }

    private String formatIndex(int index) {
        int digits = Integer.toString(index).length();
        String whitespace = switch (digits) {
            case 1 -> "   ";
            case 2 -> "  ";
            case 3 -> " ";
            default -> "";
        };
        return index + whitespace;
    }

    public void printArrayNative(T[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[j][i] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
