package ReversiAwesomePlayer;

public class StabilityCalculator {

    public boolean canNeverBeTaken(int[][] state, int i, int j) {
        int count;
        int val = state[i][j];
        for (int incx = -1; incx < 2; incx++) {
            for (int incy = -1; incy < 2; incy++) {
                if ((incx == 0) && (incy == 0))
                    continue;

                int r = i + incx;
                int c = j + incy;

            }
        }
        return false;
    }

    public boolean canBeTaken(int[][] state, int i, int j) {
        return false;
    }
}
