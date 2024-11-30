package entities;

import console.Logging;
import entities.chessPieces.*;

import static entities.ChessPiece.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ChessBoard {
    private final Logging consoleUI = new Logging();
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private final ChessPiece kingWhite;
    private final ChessPiece kingBlack;
    private final ChessPiece[] capturedByWhite = new ChessPiece[16];
    private int sizeCapturedByWhite = 0;
    private final ChessPiece[] capturedByBlack = new ChessPiece[16];
    private int sizeCapturedByBlack = 0;
    private String nowPlayer;

    {
        kingWhite = new King(WHITE, Tile.of("E1"));
        kingBlack = new King(BLACK, Tile.of("E8"));
        add(new Rook(WHITE, Tile.of("A1")));
        add(new Horse(WHITE, Tile.of("B1")));
        add(new Bishop(WHITE, Tile.of("C1")));
        add(new Queen(WHITE, Tile.of("D1")));
        add(kingWhite);
        add(new Bishop(WHITE, Tile.of("F1")));
        add(new Horse(WHITE, Tile.of("G1")));
        add(new Rook(WHITE, Tile.of("H1")));
        add(new Pawn(WHITE, Tile.of("A2")));
        add(new Pawn(WHITE, Tile.of("B2")));
        add(new Pawn(WHITE, Tile.of("C2")));
        add(new Pawn(WHITE, Tile.of("D2")));
        add(new Pawn(WHITE, Tile.of("E2")));
        add(new Pawn(WHITE, Tile.of("F2")));
        add(new Pawn(WHITE, Tile.of("G2")));
        add(new Pawn(WHITE, Tile.of("H2")));

        add(new Rook(BLACK, Tile.of("A8")));
        add(new Horse(BLACK, Tile.of("B8")));
        add(new Bishop(BLACK, Tile.of("C8")));
        add(new Queen(BLACK, Tile.of("D8")));
        add(kingBlack);
        add(new Bishop(BLACK, Tile.of("F8")));
        add(new Horse(BLACK, Tile.of("G8")));
        add(new Rook(BLACK, Tile.of("H8")));
        add(new Pawn(BLACK, Tile.of("A7")));
        add(new Pawn(BLACK, Tile.of("B7")));
        add(new Pawn(BLACK, Tile.of("C7")));
        add(new Pawn(BLACK, Tile.of("D7")));
        add(new Pawn(BLACK, Tile.of("E7")));
        add(new Pawn(BLACK, Tile.of("F7")));
        add(new Pawn(BLACK, Tile.of("G7")));
        add(new Pawn(BLACK, Tile.of("H7")));
    }

    public ChessBoard(String nowPlayer) {
        this.nowPlayer = nowPlayer;
    }

    public String nowPlayerColor() {
        return this.nowPlayer;
    }

    private void add(ChessPiece figure) {
        board[figure.getPos().y()][figure.getPos().x()] = figure;
    }

    private ChessPiece get(Tile position) {
        return board[position.y()][position.x()];
    }

    private ChessPiece move(ChessPiece figure, Tile dest) {
        ChessPiece captured = remove(dest);
        figure = remove(figure);
        figure.setPos(dest);
        add(figure);
        capture(captured);
        return captured;
    }

    private ChessPiece remove(ChessPiece figure) {
        return remove(figure.getPos());
    }

    private ChessPiece remove(Tile position) {
        ChessPiece removed = get(position);
        if (nonNull(removed)) {
            board[position.y()][position.x()] = null;
        }
        return removed;
    }

    private void capture(ChessPiece captured) {
        if (isNull(captured)) {
            return;
        }
        if (WHITE.equals(captured.getColor())) {
            capturedByBlack[sizeCapturedByBlack++] = captured;
        } else {
            capturedByWhite[sizeCapturedByWhite++] = captured;
        }
    }

    private void endTurn() {
        nowPlayer = WHITE.equals(nowPlayer) ? BLACK : WHITE;
    }

    public boolean moveToPosition(Tile start, Tile end) {
        ChessPiece figure = get(start);
        if (isNull(figure)) {
            throw new RuntimeException("На клетке " + start + " нет фигуры");
        }
        if (!nowPlayer.equals(figure.getColor())) {
            throw new RuntimeException("Нельзя двигать фигуру противника");
        }
        if (!canMoveToPosition(figure, end)) {
            throw new RuntimeException("Нельзя двигать " + figure.getName() + " " + figure.getPos() + " на " + end);
        }

        boolean isPassant = isPassantCapture(figure);
        ChessPiece captured = move(figure, end);
        if (isNull(captured) && isPassant) {
            captured = remove(consoleUI.getLastDestTile());
            capture(captured);
        }

        if (isUnderAttack(WHITE.equals(figure.getColor()) ? kingWhite : kingBlack)) {
            move(figure, start);
            if (nonNull(captured)) {
                add(captured);
            }
            throw new RuntimeException("Нельзя оставлять короля под шахом");
        }

        consoleUI.add(figure.getName(), start, end, nonNull(captured) ? captured.getName() : null);
        endTurn();
        return isUnderAttack(WHITE.equals(figure.getColor()) ? kingBlack : kingWhite);
    }

    private boolean canMoveToPosition(ChessPiece figure, Tile dest) {
        Tile[] steps = figure.steps(dest);
        if (isNull(steps)) {
            return false;
        }
        ChessPiece figureOnLastTile = get(steps[steps.length - 1]);
        for (int i = 0; i < steps.length - 1; i++) {
            if (nonNull(get(steps[i]))) {
                return false;
            }
        }
        if (figure.getSymbol().equals("P")) {
            if (nonNull(figureOnLastTile)) {
                return figureOnLastTile.getPos().x() != figure.getPos().x()
                        && !figure.getColor().equals(figureOnLastTile.getColor());
            }
            return figure.getPos().x() == dest.x() || isPassantCapture(figure);
        }
        return isNull(figureOnLastTile) || !figure.getColor().equals(figureOnLastTile.getColor());
    }

    private boolean isPassantCapture(ChessPiece pawn) {
        if (!pawn.getSymbol().equals("P")) {
            return false;
        }
        Tile previousTile = consoleUI.getLastDestTile();
        ChessPiece enemyPawn = nonNull(previousTile) ? get(previousTile) : null;
        return nonNull(enemyPawn)
                && enemyPawn.getSymbol().equals("P")
                && !nowPlayer.equals(enemyPawn.getColor())
                && enemyPawn.getPos().y() == pawn.getPos().y()
                && Math.abs(enemyPawn.getPos().x() - pawn.getPos().x()) == 1;
    }

    public boolean castling(Tile Tile) {
        ChessPiece rook = get(Tile);
        ChessPiece king = WHITE.equals(nowPlayer) ? kingWhite : kingBlack;
        if (isNull(rook)
                || !king.getColor().equals(rook.getColor())
                || !king.check()
                || !rook.check()) {
            throw new RuntimeException("Рокировка с ладьей " + Tile + " невозможна");
        }
        if (isUnderAttack(king)) {
            throw new RuntimeException("Рокировка под шахом невозможна");
        }

        int directionX = rook.getPos().x() > king.getPos().x() ? 1 : -1;

        Tile sourceRook = rook.getPos();
        Tile destRook = new Tile(king.getPos().x() + directionX, rook.getPos().y());
        if (!canMoveToPosition(rook, destRook) || nonNull(get(destRook))) {
            throw new RuntimeException("Рокировка через фигуры невозможна");
        }

        Tile sourceKing = king.getPos();
        Tile midKing = new Tile(king.getPos().x() + directionX, king.getPos().y());
        Tile destKing = new Tile(king.getPos().x() + 2 * directionX, king.getPos().y());
        String enemyColor = WHITE.equals(king.getColor()) ? BLACK : WHITE;
        if (isUnderAttack(midKing, enemyColor) || isUnderAttack(destKing, enemyColor)) {
            throw new RuntimeException("Рокировка короля через клетки под атакой невозможна");
        }

        move(king, destKing);
        move(rook, destRook);

        consoleUI.add(king.getName(), sourceKing, destKing, null);
        consoleUI.add(rook.getName(), sourceRook, destRook, null);
        endTurn();
        return isUnderAttack(WHITE.equals(king.getColor()) ? kingBlack : kingWhite);
    }

    private boolean isUnderAttack(ChessPiece checkedFigure) {
        String enemyColor = WHITE.equals(checkedFigure.getColor()) ? BLACK : WHITE;
        return isUnderAttack(checkedFigure.getPos(), enemyColor);
    }

    private boolean isUnderAttack(Tile Tile, String enemyColor) {
        for (ChessPiece[] figures : board) {
            for (ChessPiece figure : figures) {
                if (nonNull(figure)
                        && figure.getColor().equals(enemyColor)
                        && !figure.getPos().equals(Tile)
                        && canMoveToPosition(figure, Tile)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printBoard() {
        System.out.println();
        System.out.println(sizeCapturedByBlack + " фигур взято черными");
        for (int i = 0; i < sizeCapturedByBlack; i++) {
            System.out.print(capturedByBlack[i] + " ");
        }
        System.out.println("\n\tA\tB\tC\tD\tE\tF\tG\tH\t");
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + "\t");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print(".." + "\t");
                } else {
                    System.out.print(board[i][j] + "\t");
                }
            }
            System.out.print(8 - i + "\t");
            System.out.println();
            System.out.println();
        }
        System.out.println("\tA\tB\tC\tD\tE\tF\tG\tH");
        System.out.println(sizeCapturedByWhite + " фигур взято белыми");
        for (int i = 0; i < sizeCapturedByWhite; i++) {
            System.out.print(capturedByWhite[i] + " ");
        }
        System.out.println();
        System.out.println();
    }
}
