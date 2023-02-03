package ReversiAwesomePlayer;

public class MoveValidator {
    int numValidMoves = 0;
    int numValidMovesOther = 0;
    int[] validMoves = new int[64];
    int me;

    MoveValidator(int me) {
        this.me = me;
    }

    public void getNumValidMoves(int round, int[][] state) {
        int i, j;

        numValidMoves = 0;
        numValidMovesOther = 0;
        if (round < 4) {
            if (state[3][3] == 0) {
                numValidMoves ++;
                numValidMovesOther ++;
            }
            if (state[3][4] == 0) {
                numValidMoves ++;
                numValidMovesOther ++;
            }
            if (state[4][3] == 0) {
                numValidMoves ++;
                numValidMovesOther ++;
            }
            if (state[4][4] == 0) {
                numValidMoves ++;
                numValidMovesOther ++;
            }
        }
        else {
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 0) {
                        if (couldBe(state, i, j, 1)) {
                            numValidMoves ++;
                        }

                        if (couldBe(state, i, j, 2)) {
                            numValidMovesOther ++;
                        }
                    }
                }
            }
        }
    }

    public void getValidMoves(int round, int[][] state) {
        int i, j;

        numValidMoves = 0;
        if (round < 4) {
            if (state[3][3] == 0) {
                validMoves[numValidMoves] = 3*8 + 3;
                numValidMoves ++;
            }
            if (state[3][4] == 0) {
                validMoves[numValidMoves] = 3*8 + 4;
                numValidMoves ++;
            }
            if (state[4][3] == 0) {
                validMoves[numValidMoves] = 4*8 + 3;
                numValidMoves ++;
            }
            if (state[4][4] == 0) {
                validMoves[numValidMoves] = 4*8 + 4;
                numValidMoves ++;
            }
        }
        else {
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 0) {
                        if (couldBe(state, i, j, me)) {
                            validMoves[numValidMoves] = i*8 + j;
                            numValidMoves ++;
                        }
                    }
                }
            }
        }
    }

    private boolean couldBe(int[][] state, int row, int col, int turn) {
        int incx, incy;

        for (incx = -1; incx < 2; incx++) {
            for (incy = -1; incy < 2; incy++) {
                if ((incx == 0) && (incy == 0))
                    continue;

                if (checkDirection(state, row, col, incx, incy, turn))
                    return true;
            }
        }

        return false;
    }

    private boolean checkDirection(int[][] state, int row, int col, int incx, int incy, int turn) {
        int[] sequence = new int[7];
        int seqLen;
        int i, r, c;

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
            if (turn == 1) {
                if (sequence[i] == 2)
                    count ++;
                else {
                    if ((sequence[i] == 1) && (count > 0))
                        return true;
                    break;
                }
            }
            else {
                if (sequence[i] == 1)
                    count ++;
                else {
                    if ((sequence[i] == 2) && (count > 0))
                        return true;
                    break;
                }
            }
        }

        return false;
    }
}
