package entities.chessPieces;

import entities.ChessPiece;
import entities.Tile;

public class Queen extends ChessPiece {
    public Queen(String color, Tile position) {
        super(color, position);
    }

    @Override
    public boolean canMoveToPosition(int x, int y) {
        return x == 0 || y == 0 || Math.abs(x) == Math.abs(y);
    }
}
