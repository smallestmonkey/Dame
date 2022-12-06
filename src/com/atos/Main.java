package com.atos;

import com.atos.exceptions.InvalidMoveException;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        String[][] Spielfeld = new String[8][8];

        Scanner scanner = new Scanner(System.in);
        String von;
        String zu;
        String defaultFenString = "1p1p1p1p/p1p1p1p1/1p1p1p1p/8/8/P1P1P1P1/1P1P1P1P/P1P1P1P1 w"; //Small Char is Black

        try {
            Game game = new Game();

            String input;
            String configWahl;
            do {


                System.out.print("Hallo wählen sie eine Startkonfiguration\n" +
                        "(1)Standard\n" +
                        "(2)Eigene\n" +
                        "Eingabe:");

                configWahl = scanner.nextLine();

                if (configWahl.equals("1")) {

                } else if (configWahl.equals("2")) {
                    System.out.print("Bitte gebe deine Startposition in FEN an\n" +
                            "Eingabe(FEN):");
                    String fen = scanner.nextLine();
                    defaultFenString = fen;
                }


                Game.readFEN(defaultFenString);

                System.out.println("Aktueller Spielstand = "+ defaultFenString);

                System.out.print("Gib deinen Zug an:");
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