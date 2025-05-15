import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Cardinal extends Thread{
    private Map<String, Integer> influenceForCandidates = new HashMap<>();
    private String name;
    public int num;
    public int gridx = 10;
    public int gridy = 10;
    private int influenceRating;
    private String vote;
    private int threshold = new Random().nextInt(20) + 10;;
    public int x;
    public int y;
    private List<Cardinal> allCardinals;

    public Cardinal(String name, int index, int x, int y, int influenceRating, List<Cardinal> allCardinals) {
        this.name = name;
        this.num = index;
        this.x = x;
        this.y = y;
        this.influenceRating = influenceRating;
        this.vote = name;
        this.allCardinals = allCardinals;
    }

    public synchronized void moveRandomly() {
        int dir = new Random().nextInt(4);
        switch (dir){
                case 0:
                    if (x < gridx - 1) {
                        x++;
                    }
                    break;
                case 1:
                    if (x > 0) {
                        x--;
                    }
                    break;
                case 2:
                    if (y < gridy - 1) {
                        y++;
                    }
                    break;
                case 3:
                    if (y > 0) {
                        y--;
                    }
                    break;
            }
    }

    public void printPosition() {
        System.out.println(name + " is at (" + x + ", " + y + ")");
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public String getVote(){
        return vote;
    }



    public void checkConversation(){
        for (Cardinal other : allCardinals) {
            if (other!=this && other.x == this.x && other.y == this.y){
                synchronized (this){
                    synchronized (other){
                        System.out.println(name + " is having a conversation with " + other.name + " at (" + x + ", " + y + ")");
                        String candidate = other.getVote();

                        int influence = other.influenceRating;
                        int updatedInfluence = influenceForCandidates.getOrDefault(candidate, 0) + influence;
                        influenceForCandidates.put(candidate, updatedInfluence);

                        System.out.println(name + " was influenced by " + other.name + " to vote for " + candidate + "(total influence for " + candidate + ":" + updatedInfluence + ", current vote: " + vote +")");

                        if(updatedInfluence>threshold){
                            vote = candidate;
                            System.out.println(name + " changed his vote to " + vote);
                        }
                    }
                }

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
