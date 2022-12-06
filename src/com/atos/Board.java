package com.atos;

public class Board {
    protected int size;

    protected Field[][] fields;

    public Board(int size) {
        this.size = size;
        this.fields = new Field[size][size];
    }
}
