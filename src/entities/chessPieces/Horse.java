package entities.chessPieces;

import entities.ChessPiece;
import entities.Tile;

import static java.util.Objects.isNull;

public class Horse extends ChessPiece {
    public Horse(String color, Tile position) {
        super(color, position);
    }

    @Override
    public Tile[] steps(Tile dest) {
        if (isNull(dest) || dest.equals(getPos())) {
            return null;
        }
        int x = Math.abs(dest.x() - getPos().x());
        int y = Math.abs(dest.y() - getPos().y());
        return canMoveToPosition(x, y) ? new Tile[]{dest} : null;
    }

    @Override
    protected boolean canMoveToPosition(int x, int y) {
        return x == 2 && y == 1 || x == 1 && y == 2;
    }
}
