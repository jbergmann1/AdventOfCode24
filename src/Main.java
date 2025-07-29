import DayClasses.Day;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Please enter a number between 1 and 25 to execute the corresponding task or enter 0 to terminate the program.");
                String input = scanner.nextLine();
                if (input.equals("0")) break;
                String className = "DayClasses.Day" + input;
                Class<?> clazz = Class.forName(className);
                Day currentDay = (Day) clazz.getDeclaredConstructor().newInstance();
                currentDay.execute();
                input = "";
            } catch (Exception e) {
                System.out.println("Please enter a number between 1 and 25");
            }
        }
        scanner.close();
    }
}