package com.example.chess2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ChessBoardView extends View {

    private final Paint lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final ChessBoard board = new ChessBoard();
    private int selectedRow = -1; // -1 bedeutet: nichts ausgewählt
    private int selectedCol = -1;

    public ChessBoardView(Context context) {
        super(context);
        init();
    }

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        lightPaint.setColor(Color.parseColor("#EEEED2"));
        darkPaint.setColor(Color.parseColor("#769656"));
        selectPaint.setColor(Color.parseColor("#99F6E05E")); // halbtransparentes Gelb

        fillPaint.setTextAlign(Paint.Align.CENTER);
        outlinePaint.setTextAlign(Paint.Align.CENTER);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(3f);
        outlinePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cell = getWidth() / 8f;

        fillPaint.setTextSize(cell * 0.8f);
        outlinePaint.setTextSize(cell * 0.8f);
        // Hilfswert, damit der Text senkrecht in der Mitte des Feldes sitzt
        float textOffset = (fillPaint.ascent() + fillPaint.descent()) / 2f;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Paint paint = ((row + col) % 2 == 0) ? lightPaint : darkPaint;
                canvas.drawRect(col * cell, row * cell,
                        (col + 1) * cell, (row + 1) * cell, paint);

                if (row == selectedRow && col == selectedCol) {
                    canvas.drawRect(col * cell, row * cell,
                            (col + 1) * cell, (row + 1) * cell, selectPaint);
                }

                ChessPiece piece = board.getPiece(row, col);
                if (piece != null) {
                    float x = col * cell + cell / 2f;
                    float y = row * cell + cell / 2f - textOffset;
                    String symbol = symbolFor(piece.getType());

                    fillPaint.setColor(piece.getColor() == PieceColor.WHITE
                            ? Color.WHITE : Color.BLACK);
                    canvas.drawText(symbol, x, y, fillPaint);
                    // Schwarzer Rand, damit weiße Figuren auf hellen Feldern sichtbar bleiben
                    canvas.drawText(symbol, x, y, outlinePaint);
                }
            }
        }
    }

    // Wir nutzen für beide Farben die ausgefüllten Symbole und färben sie selbst ein.
    // \uFE0E erzwingt die Textdarstellung (sonst wird der Bauer auf manchen Handys ein Emoji).
    private String symbolFor(PieceType type) {
        switch (type) {
            case KING:   return "\u265A\uFE0E";
            case QUEEN:  return "\u265B\uFE0E";
            case ROOK:   return "\u265C\uFE0E";
            case BISHOP: return "\u265D\uFE0E";
            case KNIGHT: return "\u265E\uFE0E";
            default:     return "\u265F\uFE0E"; // PAWN
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true; // wir wollen auch das Loslassen mitbekommen
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float cell = getWidth() / 8f;
            int col = (int) (event.getX() / cell);
            int row = (int) (event.getY() / cell);
            if (board.isOnBoard(row, col)) {
                onSquareTapped(row, col);
            }
            performClick();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void onSquareTapped(int row, int col) {
        ChessPiece tapped = board.getPiece(row, col);

        if (selectedRow == -1) {
            // Noch nichts ausgewählt: nur Felder mit einer Figur auswählen
            if (tapped != null) {
                selectedRow = row;
                selectedCol = col;
            }
        } else {
            ChessPiece selected = board.getPiece(selectedRow, selectedCol);
            if (row == selectedRow && col == selectedCol) {
                // Dasselbe Feld nochmal: Auswahl aufheben
                selectedRow = -1;
                selectedCol = -1;
            } else if (tapped != null && tapped.getColor() == selected.getColor()) {
                // Andere eigene Figur: Auswahl wechseln
                selectedRow = row;
                selectedCol = col;
            } else {
                // Ziel ist leer oder gegnerisch: ziehen (noch OHNE Regelprüfung)
                board.movePiece(new ChessMove(selectedRow, selectedCol, row, col));
                selectedRow = -1;
                selectedCol = -1;
            }
        }
        invalidate(); // zeichnet die View neu
    }
}