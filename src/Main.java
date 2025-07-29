import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter a number between 1 and 25 to execute the corresponding task.");
            String input = scanner.nextLine();
            scanner.close();
            String className = "Day" + input;
            Class<?> clazz = Class.forName(className);
            Day currentDay = (Day) clazz.getDeclaredConstructor().newInstance();
            currentDay.execute();
        } catch (Exception e) {
            System.out.println("Please enter a number between 1 and 25");
        }
    }
}