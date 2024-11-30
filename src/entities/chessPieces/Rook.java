package entities.chessPieces;

import entities.ChessPiece;
import entities.Tile;

public class Rook extends ChessPiece {
    public Rook(String color, Tile position) {
        super(color, position);
    }

    @Override
    protected boolean canMoveToPosition(int x, int y) {
        return x == 0 || y == 0;
    }
}
