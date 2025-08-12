package DayClasses;

public class Helper<T> {
    public void printArray(T[][] array) {
        for (T[] arr : array) {
            for (T element : arr) {
                System.out.print(element + "  ");
            }
            System.out.println();
        }
        System.out.println();
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
