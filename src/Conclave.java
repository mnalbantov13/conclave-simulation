import java.util.*;

public class Conclave {

    private static boolean positionOccupied(List<Cardinal> cardinals, int x, int y) {
        for (Cardinal c : cardinals) {
            if (c.getX() == x && c.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<Cardinal> allCardinals = new ArrayList<>();

        for (int i = 0; i < 135; i++) {
            int x, y;
            Random rand = new Random();
            do {
                x = rand.nextInt(50);
                y = rand.nextInt(50);
            } while (positionOccupied(allCardinals, x, y));
            //Cardinal c = new Cardinal("Cardinal" + i, x, y, "Cardinal" + i, allCardinals);
            Cardinal c = new Cardinal("Cardinal" + i, i, x, y, new Random().nextInt(10),"Cardinal" + i, allCardinals);

            allCardinals.add(c);
        }
        for (Cardinal c : allCardinals) {
            c.start();
        }
        new Thread(() -> {
            while (true) {
                printBoard(allCardinals, 50, 50);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    public static void printBoard(List<Cardinal> cardinals, int width, int height) {
        String[][] board = new String[height][width];

        // Fill board with empty spaces
        for (int i = 0; i < height; i++) {
            Arrays.fill(board[i], ".");
        }

        for (int i = 0; i < cardinals.size(); i++) {
            Cardinal c = cardinals.get(i);
            int x = c.getX();
            int y = c.getY();

            if (x >= 0 && x < width && y >= 0 && y < height) {
                // If multiple on same cell, use '*'
                if (!Objects.equals(board[y][x], ".")) {
                    board[y][x] = "*";
                } else {
                    board[y][x] = String.valueOf(i);
                }
            }
        }

        // Print board
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.printf("%3s", board[i][j]);
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------");

    }



}