package entities.chessPieces;

import entities.ChessPiece;
import entities.Tile;

public class Bishop extends ChessPiece {
    public Bishop(String color, Tile position) {
        super(color, position);
    }

    @Override
    protected boolean canMoveToPosition(int x, int y) {
        return Math.abs(x) == Math.abs(y);
    }
}
