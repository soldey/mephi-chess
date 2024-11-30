package entities.chessPieces;

import entities.ChessPiece;
import entities.Tile;

public class King extends ChessPiece {
    public King(String color, Tile position) {
        super(color, position);
    }

    @Override
    protected boolean canMoveToPosition(int x, int y) {
        return Math.abs(x) < 2 && Math.abs(y) < 2;
    }
}
