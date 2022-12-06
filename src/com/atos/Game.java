package com.atos;

import com.atos.exceptions.InvalidMoveException;

import java.util.Arrays;

public class Game {
    public static Board board;

    protected Player[] players = new Player[2];

    protected Player currentPlayer;

    public Game() throws InvalidMoveException {
        this.board = new Board(8);

        this.players[0] = new Player(CheckersColor.WHITE);
        this.players[1] = new Player(CheckersColor.BLACK);

        this.currentPlayer = this.players[0];
    }

    public static void readFEN(String fen) {

        System.out.println("Start readFEn Methode");



        String PositionInZeile_FarbeAmZug[] = fen.split(" ");
        String FarbeAmZug = PositionInZeile_FarbeAmZug[1];
        String PositionInZeile[] = PositionInZeile_FarbeAmZug[0].split("/");

        for (int i = 0; i < PositionInZeile.length; i++) {

            for (int p = 0; p < PositionInZeile[i].length();  ){

                char fenInfo = PositionInZeile[i].charAt(p);

                if (fenInfo == 'p' || fenInfo == 'P') {
                    Game.board.setFields(i, p, 1);
                    p++;
                } else {

                    int FelderLeer = Character.getNumericValue(fenInfo);
                    p+= FelderLeer;
                }
            }


        }

        System.out.println(Game.board.getFields());


        System.out.println(FarbeAmZug);
        System.out.println(Arrays.toString(PositionInZeile));

        System.out.println("Ende readFEn Methode");



    }
}
