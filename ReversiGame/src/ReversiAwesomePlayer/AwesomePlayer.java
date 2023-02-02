package ReversiAwesomePlayer;

import java.util.Arrays;

public class AwesomePlayer extends Player {

    int bestMove;

    AwesomePlayer(int _me, String host) {
        super(_me, host);
    }

    @Override
    protected int move(int[] validMoves, int numValidMoves) {
        int[] minimax = minimax(state, 5, 5, turn, me == 1);
        System.out.println("Worst score: " + minimax[0]);

        return minimax[1];
    }

    public int getHeuristic(int[][] state) {
        int pointsWeight = 1;
        int cornersWeight = 10;
        int movesWeight = 2;

        MoveValidator validator1 = new MoveValidator(0);
        validator1.getValidMoves(4, state);

        MoveValidator validator2 = new MoveValidator(1);
        validator2.getValidMoves(4, state);

        int[] points = GameSimulator.getNumberOfPoints(state);
        int[] corners = GameSimulator.getNumberOfCorners(state);
        int[] moves = new int[] {validator1.numValidMoves, validator2.numValidMoves};

        int heuristic1 = movesWeight * moves[0] + pointsWeight * points[0] + cornersWeight * corners[0];
        int heuristic2 = movesWeight * moves[1] + pointsWeight * points[1] + cornersWeight * corners[1];

        return heuristic1 - heuristic2;
    }

    public int getEndHeuristic(int[][] state) {
        int[] points = GameSimulator.getNumberOfPoints(state);
        System.out.println(Arrays.toString(points));

        return points[0] - points[1];
    }

    public int[] minimax(int[][] node, int startDepth, int depth, int turn, boolean maximizingPlayer) {
        MoveValidator moveValidator = new MoveValidator(turn);
        moveValidator.getValidMoves(round + (startDepth - depth), node);

        int[] validMoves = moveValidator.validMoves;
        int numValidMoves = moveValidator.numValidMoves;

        if(numValidMoves <= 0) {
            return new int[]{getEndHeuristic(node), -1};
        }

        if(depth == 0) {
            return new int[]{getHeuristic(node), -1};
        }

        int value = 0;
        int bestMove = 0;

        for (int i = 0; i < numValidMoves; i++) {
            int move = validMoves[i];
            int moveRow = move / 8;
            int moveCol = move % 8;

            int[][] newState = GameSimulator.changeColors(node, moveRow, moveCol, turn - 1);

            if(maximizingPlayer) {
                int moveValue = minimax(newState, startDepth, depth - 1, (turn % 2) + 1, false)[0];
                if(i == 0 || moveValue > value || (moveValue == value && Math.random() < 0.5)) {
                    value = moveValue;
                    bestMove = i;
                }
            }
            else {
                int moveValue = minimax(newState, startDepth,depth - 1, (turn % 2) + 1, true)[0];
                if(i == 0 || moveValue < value || (moveValue == value && Math.random() < 0.5)) {
                    value = moveValue;
                    bestMove = i;
                }
            }
        }

        return new int[]{value, bestMove};
    }

    public static void main(String[] args) {
        new AwesomePlayer(Integer.parseInt(args[1]), args[0]);
    }
}
