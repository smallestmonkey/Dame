package com.atos;

import java.text.NumberFormat;
import java.util.Arrays;

public class Board {
    protected int size;

    protected int[][] fields;

    public Board(int size) {
        this.size = size;
        this.fields = new int[size][size];

    }

    public String getFields() {
        return Arrays.toString(fields[0]) +"\n"+
                Arrays.toString(fields[1]) +"\n"+
                Arrays.toString(fields[2])+"\n"+
                Arrays.toString(fields[3])+"\n"+
                Arrays.toString(fields[4])+"\n"+
                Arrays.toString(fields[5])+"\n"+
                Arrays.toString(fields[6])+"\n"+
                Arrays.toString(fields[7]);
    }

    public void setFields(int Zeile, int Spalte, int wert) {

        this.fields[Zeile][Spalte] = 1;
    }



}
