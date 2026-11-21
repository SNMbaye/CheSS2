package com.example.chess2;

public class ChessPiece {

    private final PieceType type;
    private final PieceColor color;
    private boolean hasMoved = false; // wichtig später für Rochade und Bauern-Doppelschritt

    public ChessPiece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    public PieceType getType() { return type; }
    public PieceColor getColor() { return color; }
    public boolean hasMoved() { return hasMoved; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
}