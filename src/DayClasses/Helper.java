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
}
