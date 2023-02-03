package ReversiAwesomePlayer;

public class GameSimulator {

    public static int[] getNumberOfPoints (int[][] state) {
        int[] points = {0, 0};
        for (int[] s: state) {
            for (int p: s) {
                if(p != 0) {
                    points[p - 1]++;
                }
            }
        }
        return points;
    }

    public static int[] getNumberOfCorners(int[][] state) {
        int[] points = {0, 0};
        int[][] cornersPos = {{0, 0}, {7, 0}, {0, 7}, {7, 7}};

        for (int[] pos : cornersPos) {
            int cornerVal = state[pos[0]][pos[1]];
            if (cornerVal != 0) {
                points[cornerVal - 1]++;
            }
        }
        return points;
    }

    public static int[][] changeColors(int[][] initState, int row, int col, int turn) {
        int incx, incy;

        int[][] state = new int[initState.length][];
        for(int j = 0; j < initState.length; j++) {
            state[j] = initState[j].clone();
        }

        for (incx = -1; incx < 2; incx++) {
            for (incy = -1; incy < 2; incy++) {
                if ((incx == 0) && (incy == 0))
                    continue;

                checkDirection(state, row, col, incx, incy, turn);
            }
        }

        return state;
    }

    private static int[][] checkDirection(int[][] state, int row, int col, int incx, int incy, int turn) {
        int[] sequence = new int[7];
        int seqLen;
        int i, r, c;

        state[row][col] = turn + 1;

        seqLen = 0;
        for (i = 1; i < 8; i++) {
            r = row+incy*i;
            c = col+incx*i;

            if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
                break;

            sequence[seqLen] = state[r][c];
            seqLen++;
        }

        int count = 0;
        for (i = 0; i < seqLen; i++) {
            if (turn == 0) {
                if (sequence[i] == 2)
                    count ++;
                else {
                    if ((sequence[i] == 1) && (count > 0))
                        count = 20;
                    break;
                }
            }
            else {
                if (sequence[i] == 1)
                    count ++;
                else {
                    if ((sequence[i] == 2) && (count > 0))
                        count = 20;
                    break;
                }
            }
        }

        if (count > 10) {
            if (turn == 0) {
                i = 1;
                r = row+incy*i;
                c = col+incx*i;
                while (state[r][c] == 2) {
                    state[r][c] = 1;
                    i++;
                    r = row+incy*i;
                    c = col+incx*i;
                }
            }
            else {
                i = 1;
                r = row+incy*i;
                c = col+incx*i;
                while (state[r][c] == 1) {
                    state[r][c] = 2;
                    i++;
                    r = row+incy*i;
                    c = col+incx*i;
                }
            }
        }

        return state;
    }

    public static void printState(int[][] state) {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                System.out.print(state[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[] getStabilityScores(int[][] state) {
        StabilityCalculator calculator = new StabilityCalculator();
        int[] stabilityScores = new int[] {0, 0};
        for (int i = 0, stateLength = state.length; i < stateLength; i++) {
            int[] r = state[i];
            for (int j = 0, rLength = r.length; j < rLength; j++) {
                int val = r[j];
                if(val == 0) {
                    continue;
                }

                if (calculator.canBeTaken(state, i, j)) {
                    stabilityScores[val - 1]--;
                }
                else if (calculator.canNeverBeTaken(state, i, j)) {
                    stabilityScores[val - 1]++;
                }
            }
        }
        return  stabilityScores;
    }


}
