import java.util.*;

public class Conclave {
    public static volatile boolean electionOver = false;

    public static void main(String[] args) {
        List<Cardinal> allCardinals = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            Cardinal c = new Cardinal("Cardinal" + i, i, new Random().nextInt(10), allCardinals);
//            allCardinals.add(c);
//        }
        for (int i = 0; i < 10; i++) {
            int x, y;
            Random rand = new Random();
            do {
                x = rand.nextInt(10);
                y = rand.nextInt(10);
            } while (positionOccupied(allCardinals, x, y));
            Cardinal c = new Cardinal("Cardinal" + i, i, x, y, new Random().nextInt(10), allCardinals);

            allCardinals.add(c);
        }
        for (Cardinal c : allCardinals) {
            c.start();
        }
        Thread printer = new Thread(() -> {
            while (!electionOver) {
                printBoard(allCardinals, 10, 10);
                try { Thread.sleep(150); }
                catch (InterruptedException e) { break; }
            }
        });
        printer.start();

        // Eric - Scheduler thread to manage voting rounds
        new Thread(() -> {
            final int ROUND_MS = 5000; // Here I set the duration of each voting round
            final int GRACE_MS = 200; // This is just a period to allow cardinals to finish their current tasks

            while (!electionOver) {
                // 1) I wait for the round to elapse
                try {
                    Thread.sleep(ROUND_MS);
                } catch (InterruptedException e) {
                    // If main wants to shut everything down early, exit
                    break;
                }

                // 2) Interrupt all cardinals to wake them up
                for (Cardinal c : allCardinals) {
                    c.interrupt();
                }

                // 3) Give them a moment to finish checkConversation()
                try {
                    Thread.sleep(GRACE_MS);
                } catch (InterruptedException ignored) { }

                // 4) Get votes
                Map<String, Integer> voteCounts = new HashMap<>();
                for (Cardinal c : allCardinals) {
                    String v = c.getVote();
                    voteCounts.put(v, voteCounts.getOrDefault(v, 0) + 1);
                }

                // 5) Print the votes
                System.out.println("\n Voting In Progress:");
                voteCounts.forEach((candidate, count) ->
                        System.out.printf("  %s : %d%n", candidate, count)
                );

                // 6) Check for 2/3 majority
                int needed = (int)Math.ceil((2.0 / 3.0) * allCardinals.size());
                for (Map.Entry<String, Integer> entry : voteCounts.entrySet()) {
                    if (entry.getValue() >= needed) {
                        System.out.printf(
                                "%nWHITE SMOKE! %s is elected with %d votes.%n",
                                entry.getKey(), entry.getValue()
                        );

                        // Signal shutdown
                        electionOver = true;
                        allCardinals.forEach(Thread::interrupt);
                        printer.interrupt();

                        return;
                    }
                }
                // No winner --> next round begins
            }
        }).start();

    }

    private static boolean positionOccupied(List<Cardinal> cardinals, int x, int y) {
        for (Cardinal c : cardinals) {
            if (c.getX() == x && c.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public static synchronized void printBoard(List<Cardinal> cardinals, int width, int height) {
        String[][] board = new String[height][width];

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

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.printf("%3s", board[i][j]);
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------");

    }



}
