package com.atos.components;

import com.atos.Field;
import com.atos.Game;
import com.atos.GamePiece;
import com.atos.exceptions.InvalidMoveException;
import com.atos.exceptions.InvalidPlayerException;
import com.atos.exceptions.NoMoveException;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Observable;
import java.util.Observer;

public class FieldStack extends StackPane implements Observer {

    int xFrom,yFrom,xTo,yTo;
    private Rectangle rectangle;
    private Text text;
    private Color color;
    private Game game;
    private Field field;

    public FieldStack(Game game, Field field, GridPane pane, Color color) {
        super();

        this.game = game;
        this.field = field;
        this.color = color;

        text = new Text();
        rectangle = new Rectangle();

        rectangle.setFill(color);

        game.addObserver(this);

        rectangle.widthProperty().bind(pane.widthProperty().divide(8));
        rectangle.heightProperty().bind(pane.heightProperty().divide(8));

        setOnMouseClicked(event -> {
            // Hier muss alles programmiert werden was passieren soll wenn auf eine Figur geklickt wird.

            System.out.println(game.getBoard().toFenString());

            if (field.isEmpty()) {

                System.out.println(game.getSelection().getX()+ " " + game.getSelection().getY());

                Field to = field;


                try {

                    game.advance(game.getSelection(), to);

                } catch (InvalidMoveException e){
                    throw new RuntimeException(e);
                } catch (InvalidPlayerException e) {
                    throw new RuntimeException(e);
                } catch (NoMoveException e) {
                    throw new RuntimeException(e);
                }

            }

            boolean shouldSelectField = !game.hasSelection() && !field.isEmpty();
            boolean shouldChangeSelectedField = game.hasSelection() && !game.getSelection().equals(field) && !field.isEmpty() && field.getGamePiece().getColor() == game.getCurrentPlayer().getColor();

            if (shouldSelectField) {
                game.setSelection(field);

            } else if (shouldChangeSelectedField) {
                game.setSelection(field);

            }
        });

        if (!field.isEmpty()) {
            text = new Text(String.valueOf(field.getGamePiece().getUnicode()));
            System.out.println(text);

        }

        text.setScaleX(3);
        text.setScaleY(3);

        getChildren().addAll(rectangle, text);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (game.hasSelection()) {
            if (field.equals(game.getSelection())) {
                rectangle.setFill(Color.FUCHSIA);
            }
            else {
                rectangle.setFill(color);
            }
            for (int i = 0; i<game.getOptionsSelection().toArray().length; i++) {
                if (field.equals(game.getOptionsSelection().get(i))){
                    rectangle.setFill(Color.ORANGE);
                }
            }

            text.setText(field.isEmpty() ? "" : String.valueOf(field.getGamePiece().getUnicode()));

            if (game.ismoved == true) {
                if (!field.equals(game.getSelection())) {
                    game.ismoved = false;
                    rectangle.setFill(Color.LIGHTGRAY);
                }
                for (int i = 0; i < game.getOptionsSelection().toArray().length; i++) {
                    if (field.equals(game.getOptionsSelection().get(i))) {
                        rectangle.setFill(Color.LIGHTGRAY);
                    }
                }
            }


        }
    }
}
