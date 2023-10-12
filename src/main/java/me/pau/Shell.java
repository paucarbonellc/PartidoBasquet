package me.pau;

import java.util.Scanner;
import java.util.function.Consumer;

public class Shell {

    private static final Scanner scanner = new Scanner(System.in);

    public static void read(String sentence, Consumer<String> input) {
        input.accept(read(sentence));
    }

    public static String read(String sentence) {
        if (sentence != null && !sentence.isEmpty()) {
            System.out.print("\n" + sentence + ": ");
        }

        return scanner.next();
    }

}
