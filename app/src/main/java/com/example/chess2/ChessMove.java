package com.example.chess2;

public class ChessMove {

    public final int fromRow;
    public final int fromCol;
    public final int toRow;
    public final int toCol;

    public ChessMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }
}