package com.atos;

import com.atos.exceptions.InvalidFenStringException;
import com.atos.exceptions.InvalidMoveException;
import com.atos.exceptions.InvalidPlayerException;
import com.atos.exceptions.NoMoveException;

import java.util.ArrayList;
import java.util.Observable;

public class Game extends Observable {


    public boolean ismoved;
    protected int d1x, d1y, d2x, d2y;
    protected Board board;

    protected Player[] players = new Player[2];

    protected Player currentPlayer;

    protected ArrayList<Move> moves = new ArrayList<>();

    protected Field selection;

    protected ArrayList<Field> optionsSelection = new ArrayList<>();


    public Game() throws InvalidFenStringException {
        this.board = new Board(8);

        this.players[0] = new Player(CheckersColor.WHITE);
        this.players[1] = new Player(CheckersColor.BLACK);

        this.currentPlayer = this.players[0];
    }

    public Game(String fenString) throws InvalidMoveException, InvalidFenStringException {
        this();

        parseFenString(fenString);
    }

    protected void parseFenString(String fenString) throws InvalidFenStringException {
        String[] fenParts = fenString.split(" ");

        if (fenParts.length != 2) {
            throw new InvalidFenStringException();
        }

        if (!fenParts[1].matches("^([wb])$")) {
            throw new InvalidFenStringException();
        }

        this.currentPlayer = fenParts[1].equals("w") ? this.players[0] : this.players[1];

        this.board.parseFenString(fenParts[0]);
    }

    public void advance(String move) throws InvalidMoveException, InvalidPlayerException, NoMoveException {
        String[] fields = move.split("-");

        Field from = this.board.getField(fields[0]);
        Field to = this.board.getField(fields[1]);

        boolean isMoveDiagonal = Math.abs(from.getX() - to.getX()) == Math.abs(from.getY() - to.getY());
        if (from.isEmpty() || !to.isEmpty() || !isMoveDiagonal) {
            throw new InvalidMoveException();
        }

        if (from.getGamePiece().getColor() != this.currentPlayer.getColor()) {
            throw new InvalidPlayerException();
        }

        if (from.equals(to)) {
            throw new NoMoveException();
        }

        Move m = new Move(this.currentPlayer, from, to);
        this.moves.add(m);

        to.setGamePiece(from.getGamePiece());
        from.setGamePiece(null);
        // TODO check if game piece is captured

        // switch player
        this.currentPlayer = this.currentPlayer.getColor() == this.players[0].getColor() ? this.players[1] : this.players[0];
    }

    public void advance(Field from, Field to) throws InvalidMoveException, InvalidPlayerException, NoMoveException{

        boolean isMoveDiagonal = Math.abs(from.getX() - to.getX()) == Math.abs(from.getY() - to.getY());

        boolean isMoveAllowed = true;
        boolean twoPiecesbeside = true;

        if (from.isEmpty() || !to.isEmpty() || !isMoveDiagonal) {

            throw new InvalidMoveException();

        }

        if (from.getGamePiece().getColor() != this.currentPlayer.getColor()) {
            throw new InvalidMoveException();
        }

        if (from.equals(to)) {
            throw new NoMoveException();
        }

        int xFrom = from.getX();
        int yFrom = from.getY();

        int xTo = to.getX();
        int yTo = to.getY();

        ArrayList<GamePiece> ueberflogenePieces = new ArrayList<>();
        int ueberflogeneLeereFields = 0;

        GamePiece RelevantPieceForColorCheck = null;
        GamePiece PieceFrom = from.getGamePiece();

        boolean zugNachLinksUnten = xTo - xFrom < 0 && yTo - yFrom > 0;
        boolean zugNachRechtsUnten = xTo - xFrom > 0 && yTo - yFrom > 0;
        boolean zugNachRechtsOben = xTo - xFrom > 0 && yTo - yFrom < 0;
        boolean zugNachLinksOben = xTo - xFrom < 0 && yTo - yFrom < 0;

        for (int i = 1 ; i <= Math.abs(yFrom-yTo); i++) {


            if (zugNachLinksOben) {
                //Zug nach Links oben
                RelevantPieceForColorCheck = board.getField(from.getX() - i, from.getY() - i).getGamePiece();

            } else if (zugNachRechtsOben) {
                //Zug nach Rechts oben
                RelevantPieceForColorCheck = board.getField(from.getX() + i, from.getY() - i).getGamePiece();


            } else if (zugNachRechtsUnten) {
                //Zug nach Rechts unten
                RelevantPieceForColorCheck = board.getField(from.getX() + i, from.getY() + i).getGamePiece();


            } else if (zugNachLinksUnten) {
                //Zug nach Links unten
                RelevantPieceForColorCheck = board.getField(from.getX() - i, from.getY() + i).getGamePiece();

            }

            if (getCurrentPlayer().getColor() == CheckersColor.WHITE) {
                if (zugNachRechtsUnten || zugNachLinksUnten) {
                    isMoveAllowed = false;
                }
            }
            if ( getCurrentPlayer().getColor() == CheckersColor.BLACK) {
                if (zugNachLinksOben || zugNachRechtsOben) {
                    isMoveAllowed = false;
                }
            }


            if (RelevantPieceForColorCheck != null && RelevantPieceForColorCheck.getColor() != PieceFrom.getColor()) {
                ueberflogenePieces.add(RelevantPieceForColorCheck);
                ueberflogeneLeereFields = 0;
            }
            if (RelevantPieceForColorCheck == null) {
                ueberflogenePieces.clear();
                ueberflogeneLeereFields += 1;
            }

            if (ueberflogenePieces.toArray().length > 1) {
                isMoveAllowed = false;
            }

            if (ueberflogeneLeereFields > 1) {
                isMoveAllowed = false;
            }

            if (RelevantPieceForColorCheck != null && RelevantPieceForColorCheck.getColor() == PieceFrom.getColor()) {
                isMoveAllowed = false;
            }
        }

        if (isMoveAllowed) {

            Move m = new Move(this.currentPlayer,from, to);

            this.moves.add(m);

            killGamePiece(this.getSelection(), to);

            to.setGamePiece(from.getGamePiece());
            from.setGamePiece(null);

            // TODO check if game piece is captured
            ismoved = true;

            // switch player
            this.currentPlayer = this.currentPlayer.getColor() == this.players[0].getColor() ? this.players[1] : this.players[0];




            setSelection(null);

        } else {
            throw new InvalidMoveException();

        }

    }



    public String toFenString() {
        return this.board.toFenString() + " " + this.currentPlayer.toFenChar();
    }

    @Override
    public String toString() {
        return this.toFenString();
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean hasSelection() {
        return selection != null;
    }

    public Field getSelection() {
        return selection;
    }

    public ArrayList<Field> getOptionsSelection() { return optionsSelection; }

    public void setOptionsSelection() {

        boolean goDown = false;

        int x = this.selection.getX();
        int y = this.selection.getY();

        System.out.println(this.selection.getColor());
        for (int i = 1; i < 3; i++) {

            if (currentPlayer.getColor() == CheckersColor.WHITE && selection.getGamePiece().getColor() == CheckersColor.WHITE) {

                goDown = false;

                d1x = x-i;
                d1y = y-i;

                d2x = x+i;
                d2y = y-i;

            }
            else if (currentPlayer.getColor() == CheckersColor.BLACK && selection.getGamePiece().getColor() == CheckersColor.BLACK) {

                goDown = true;

                d1x = x-i;
                d1y = y+i;

                d2x = x+i;
                d2y = y+i;

            }
            if (this.optionsSelection.toArray().length > 1) {
                break;
            }
            if (d1x < 8 && d1x >= 0 && d1y >=0 && d1y < 8) {
                if (this.board.getField(d1x, d1y).getGamePiece()  == null) {

                    this.optionsSelection.add(this.board.getField(d1x, d1y));
                }
            }
            if (d2x < 8 && d2x >= 0 && d2y >=0 && d2y < 8) {
                if (this.board.getField(d2x, d2y).getGamePiece() == null) {

                    this.optionsSelection.add(this.board.getField(d2x, d2y));
                }
            }
        }
    }

    public void setSelection(Field selection) {
        optionsSelection.clear();

        this.selection = selection;
        if ( selection != null) {

            this.setOptionsSelection();

        }

        setChanged();
        notifyObservers();
    }

    public void killGamePiece ( Field from, Field to) throws InvalidMoveException {
        int xFrom = from.getX();
        int yFrom = from.getY();

        int xTo = to.getX();
        int yTo = to.getY();



        boolean gradeGeschmissen = false;
        GamePiece IteratedGamePieceToCheck = null;
        Field FieldinQuestion = null;
        Field folgendesFieldinQuestion = null;
        GamePiece SelectedGamePieceFrom = this.getSelection().getGamePiece();
        GamePiece GamePieceTo = to.getGamePiece();

        for (int i = 0; i <= Math.abs(yTo-yFrom); i++) {


            if (xTo - xFrom < 0 && yTo - yFrom < 0) {
                //Zug nach Links oben
                FieldinQuestion = this.getBoard().getField(xFrom - i, yFrom - i);


                IteratedGamePieceToCheck = this.getBoard().getField(xFrom - i, yFrom - i).getGamePiece();


            } else if (xTo - xFrom > 0 && yTo - yFrom < 0) {
                //Zug nach Rechts oben
                FieldinQuestion = this.getBoard().getField(xFrom + i, yFrom - i);


                IteratedGamePieceToCheck = this.getBoard().getField(xFrom + i, yFrom - i).getGamePiece();


            } else if (xTo - xFrom > 0 && yTo - yFrom > 0) {
                //Zug nach Rechts unten
                FieldinQuestion = this.getBoard().getField(xFrom + i, yFrom + i);


                IteratedGamePieceToCheck = this.getBoard().getField(xFrom + i, yFrom + i).getGamePiece();


            } else if (xTo - xFrom < 0 && yTo - yFrom > 0) {
                //Zug nach Links unten
                FieldinQuestion = this.getBoard().getField(xFrom - i, yFrom + i);


                IteratedGamePieceToCheck = this.getBoard().getField(xFrom - i, yFrom + i).getGamePiece();


            }


            if (!FieldinQuestion.isEmpty() && IteratedGamePieceToCheck != SelectedGamePieceFrom && IteratedGamePieceToCheck != GamePieceTo ) {


                    FieldinQuestion.setGamePiece(null);
                    System.out.println("GESCHMISSEN!!");
                    gradeGeschmissen = true;



            } else {
                gradeGeschmissen = false;
            }
        }
    }
}
