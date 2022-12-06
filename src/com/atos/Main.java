package com.atos;

import com.atos.exceptions.InvalidMoveException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String von;
        String zu;
        String defaultFenString = "1p1p1p1p/p1p1p1p1/1p1p1p1p/8/8/P1P1P1P1/1P1P1P1P/P1P1P1P1 w";

        try {
            Game game = new Game();

            String input;
            do {
                System.out.print("Zugfolge:");
                input = scanner.nextLine();
                von = input.substring(0,2);
                zu = input.substring(3,5);
                System.out.println(von);
                System.out.println(zu);

            } while (!input.equals(""));


        } catch (InvalidMoveException e) {
            System.exit(e.getCode());
        }
    }
}