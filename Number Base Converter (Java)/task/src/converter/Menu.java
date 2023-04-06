package converter;

import java.util.Objects;
import java.util.Scanner;

public class Menu {
    static Scanner scanner = new Scanner(System.in);
    static boolean exit = false;
    static Converter converter;

    public static boolean isExit() {
        return exit;
    }

    Menu() {
        boolean back = false;
        chooseConversionPrompt();
        while (!back && !exit) {
            back = enterNumberPrompt();
        }
    }

    static void chooseConversionPrompt() {
        System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
        String input = scanner.nextLine();

        if (Objects.equals(input.trim(), "/exit")) {
            exit = true;
        } else if (input.matches("[0-9]{1,2} [0-9]{1,2}")){
            int source = Integer.parseInt(input.split(" ")[0]);
            int target = Integer.parseInt(input.split(" ")[1]);
            converter = new Converter(source, target);
        } else {
            System.out.println("Invalid argument");
        }
    }

    static boolean enterNumberPrompt() {
        System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ", converter.getSource(), converter.getTarget());
        String input = scanner.nextLine();

        if (Objects.equals(input.trim(), "/back")) {
            System.out.println();
            return true;
        } else if (input.matches("\\d*\\w*\\.?\\d*\\w*")) {
            if (input.contains(".")) {
                converter.setInteger(input.split("\\.")[0]);
                converter.setFraction(input.split("\\.")[1]);
            } else {
                converter.setInteger(input);
            }

            System.out.println("Conversion result: " + converter.convert());
            System.out.println();
            return false;

        } else {
            System.out.println("Invalid argument");
            return false;
        }
    }

}
