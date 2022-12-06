package com.atos;

import com.atos.exceptions.InvalidMoveException;

public class Game {
    protected Board board;

    protected Player[] players = new Player[2];

    protected Player currentPlayer;

    public Game() throws InvalidMoveException {
        this.board = new Board(8);

        this.players[0] = new Player(CheckersColor.WHITE);
        this.players[1] = new Player(CheckersColor.BLACK);

        this.currentPlayer = this.players[0];
    }
}
