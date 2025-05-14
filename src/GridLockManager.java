import java.util.concurrent.locks.ReentrantLock;

public class GridLockManager {
    private final int rows;
    private final int cols;
    private final ReentrantLock[][] locks;

    public GridLockManager(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        locks = new ReentrantLock[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                locks[i][j] = new ReentrantLock();
            }
        }
    }

    public ReentrantLock getLock(int x, int y) {
        return locks[x][y];
    }
}