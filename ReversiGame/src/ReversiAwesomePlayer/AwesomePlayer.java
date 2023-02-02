package ReversiAwesomePlayer;

public class AwesomePlayer extends Player {

    int bestMove;

    AwesomePlayer(int _me, String host) {
        super(_me, host);
    }

    @Override
    protected int move(int[] validMoves, int numValidMoves) {
        int worstScore = minimax(state, 5, turn, me == 1);
        System.out.println("Worst score: " + worstScore);

        return bestMove;
    }

    public int getHeuristic(int[][] state) {
        int pointsWeight = 1;
        int cornersWeight = 20;

        int[] points = GameSimulator.getNumberOfPoints(state);
        int[] corners = GameSimulator.getNumberOfCorners(state);

        int heuristic1 = pointsWeight * points[0] + cornersWeight * corners[0];
        int heuristic2 = pointsWeight * points[1] + cornersWeight * corners[1];

        return heuristic1 - heuristic2;
    }

    public int getEndHeuristic(int[][] state) {
        int[] points = GameSimulator.getNumberOfPoints(state);

        return points[0] - points[1];
    }

    public int minimax(int[][] node, int depth, int turn, boolean maximizingPlayer) {
        MoveValidator moveValidator = new MoveValidator(turn);
        moveValidator.getValidMoves(round + depth, node);

        int[] validMoves = moveValidator.validMoves;
        int numValidMoves = moveValidator.numValidMoves;

        if(depth == 0) {
            return getHeuristic(node);
        }

        if(numValidMoves <= 0) {
            return getEndHeuristic(node);
        }

        int value = 0;
        int bestMove = 0;

        for (int i = 0; i < numValidMoves; i++) {
            int move = validMoves[i];
            int moveRow = move / 8;
            int moveCol = move % 8;

            int[][] newState = GameSimulator.changeColors(state, moveRow, moveCol, turn);

            if(maximizingPlayer) {
                int moveValue = minimax(newState, depth - 1, (turn % 2) + 1, false);
                if(i == 0 || moveValue > value) {
                    value = moveValue;
                    bestMove = i;
                }
            }
            else {
                int moveValue = minimax(newState, depth - 1, (turn % 2) + 1, true);
                if(i == 0 || moveValue < value) {
                    value = moveValue;
                    bestMove = i;
                }
            }
        }

        this.bestMove = bestMove;
        return value;
    }

    public static void main(String[] args) {
        new AwesomePlayer(Integer.parseInt(args[1]), args[0]);
    }
}
