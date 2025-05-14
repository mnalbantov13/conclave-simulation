import java.util.List;
import java.util.Random;


public class Cardinal extends Thread{
    private String name;
    public int num;
    public int gridx = 50;
    public int gridy = 50;
    private int influenceRating;
    private String startVote;
    private int threshold;
    public int x = new Random().nextInt(gridx);
    public int y = new Random().nextInt(gridy);
    private List<Cardinal> allCardinals;

    public Cardinal(String name, int index, int influenceRating, String startVote, List<Cardinal> allCardinals) {
        this.name = name;
        this.num = index;
        this.influenceRating = influenceRating;
        this.startVote = startVote;
        this.allCardinals = allCardinals;
    }

    public void moveRandomly() {
        int dir = new Random().nextInt(4);
        switch (dir){
                case 0:
                    if (x < gridx - 1) {
                        x++;
                    }
                   // System.out.println(name + " moved right to (" + x + ", " + y + ")");
                    break;
                case 1:
                    if (x > 0) {
                        x--;
                    }
                    //System.out.println(name + " moved left to (" + x + ", " + y + ")");
                    break;
                case 2:
                    if (y < gridy - 1) {
                        y++;
                    }
                   // System.out.println(name + " moved up to (" + x + ", " + y + ")");
                    break;
                case 3:
                    if (y > 0) {
                        y--;
                    }
                    //System.out.println(name + " moved down to (" + x + ", " + y + ")");
                    break;
            }
    }

    public void printPosition() {
        System.out.println(name + " is at (" + x + ", " + y + ")");
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void checkConversation(){
        for (Cardinal other : allCardinals) {
            if (other!=this && other.x == this.x && other.y == this.y){
                System.out.println(name + " is having a conversation with " + other.name + " at (" + x + ", " + y + ")");
                try {
                    Thread.sleep(1000); // Simulate conversation time
                } catch (InterruptedException e) {
                    System.out.println(name + " was interrupted.");
                    break;
                }
            }
        }
    }
    public void run() {
        while (true) {
            checkConversation();
            moveRandomly();
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted.");
                break;
            }
        }
    }
}
