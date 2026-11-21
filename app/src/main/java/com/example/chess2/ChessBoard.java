package com.example.chess2;

public class ChessBoard {

    private final ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        setupInitialPosition();
    }

    public void setupInitialPosition() {
        // Alles leeren
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                squares[r][c] = null;
            }
        }

        PieceType[] backRank = {
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
                PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
        };

        for (int c = 0; c < 8; c++) {
            squares[0][c] = new ChessPiece(backRank[c], PieceColor.BLACK);
            squares[1][c] = new ChessPiece(PieceType.PAWN, PieceColor.BLACK);
            squares[6][c] = new ChessPiece(PieceType.PAWN, PieceColor.WHITE);
            squares[7][c] = new ChessPiece(backRank[c], PieceColor.WHITE);
        }
    }

    public boolean isOnBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public ChessPiece getPiece(int row, int col) {
        return squares[row][col];
    }

    public void setPiece(int row, int col, ChessPiece piece) {
        squares[row][col] = piece;
    }

    // Führt einen Zug aus, ohne Regeln zu prüfen (die Prüfung kommt in ChessGame)
    public void movePiece(ChessMove move) {
        ChessPiece piece = squares[move.fromRow][move.fromCol];
        squares[move.toRow][move.toCol] = piece;
        squares[move.fromRow][move.fromCol] = null;
        if (piece != null) {
            piece.setHasMoved(true);
        }
    }
}