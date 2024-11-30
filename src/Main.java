import entities.ChessBoard;
import entities.Tile;

import java.util.Scanner;

import static entities.ChessPiece.WHITE;

public class Main {

    public static void main(String[] args) {
        String help = """
                'help' - список команд
                'exit' - завершить игру
                'replay' - перезапустить игру
                'castling H1' - рокировка с ладьей на H1
                'A1 B2' - передвижение фигуры с позиции A1 на B2
                """;
        System.out.println(help);

        ChessBoard board = new ChessBoard(WHITE);
        Scanner scanner = new Scanner(System.in);
        boolean isRun = true;
        boolean isCheck;
        while (isRun) {
            isCheck = false;
            board.printBoard();
            System.out.print("Ход " + (board.nowPlayerColor().equals(WHITE) ? "белых" : "черных") + ": ");
            String command = scanner.nextLine();
            try {
                String[] commands = command.split(" ");
                switch (commands[0]) {
                    case "help" -> System.out.println(help);
                    case "exit" -> isRun = false;
                    case "replay" -> board = new ChessBoard(WHITE);
                    case "castling" -> isCheck = board.castling(Tile.of(commands[1].toUpperCase()));
                    default -> {
                        if (commands.length == 2) {
                            isCheck = board.moveToPosition(Tile.of(commands[0].toUpperCase()), Tile.of(commands[1].toUpperCase()));
                        } else {
                            throw new IllegalArgumentException("Неверная команда");
                        }
                    }
                }
                if (isCheck) {
                    System.out.println("Шах!");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
