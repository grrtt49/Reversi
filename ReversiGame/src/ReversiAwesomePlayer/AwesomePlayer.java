package ReversiAwesomePlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

public class AwesomePlayer extends Player {

    long totalTime = 0;
    int times = 0;

    AwesomePlayer(int _me, String host) {
        super(_me, host);
    }

    @Override
    protected int move(int[] validMoves, int numValidMoves) {
        int[] minimax = minimax(state, 8, 8, turn, me == 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println("Worst score: " + minimax[0]);
        System.out.println();
        System.out.println("Total time: " + ((double)totalTime / 1000000000));
        System.out.println("Avg time: " + (((double)totalTime / 1000000000) / times));
        System.out.println();
        return minimax[1];
    }

    public int getHeuristic(int[][] state) {
        long startTime = System.nanoTime();
        int pointsWeight = 1;
        int cornersWeight = 10;
        int movesWeight = 2;

        MoveValidator validator1 = new MoveValidator(0);
        validator1.getNumValidMoves(4, state);

        MoveValidator validator2 = new MoveValidator(1);
        validator2.getNumValidMoves(4, state);

        int[] points = GameSimulator.getNumberOfPoints(state);
        int[] corners = GameSimulator.getNumberOfCorners(state);
        int[] moves = new int[] {validator1.numValidMoves, validator2.numValidMoves};

        int heuristic1 = movesWeight * moves[0] + pointsWeight * points[0] + cornersWeight * corners[0];
        int heuristic2 = movesWeight * moves[1] + pointsWeight * points[1] + cornersWeight * corners[1];
        long endTime = System.nanoTime();

        totalTime += (endTime - startTime);
        times++;
        return heuristic1 - heuristic2;
    }

    public int getOrderingHeuristic(int[][] state) {
        int pointsWeight = 1;
        int cornersWeight = 10;

        int[] points = GameSimulator.getNumberOfPoints(state);
        int[] corners = GameSimulator.getNumberOfCorners(state);

        int heuristic1 = pointsWeight * points[0] + cornersWeight * corners[0];
        int heuristic2 = pointsWeight * points[1] + cornersWeight * corners[1];

        return heuristic1 - heuristic2;
    }

    public int getEndHeuristic(int[][] state) {
        int[] points = GameSimulator.getNumberOfPoints(state);
        System.out.println(Arrays.toString(points));

        return points[0] - points[1];
    }

    public int[] minimax(int[][] node, int startDepth, int depth, int turn, boolean maximizingPlayer, int alpha, int beta) {
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

        int value;
        int bestMove = 0;
        PriorityQueue<SortableState> bestMoves;

        if(maximizingPlayer) {
            bestMoves = new PriorityQueue<>(Collections.reverseOrder());
        }
        else {
            bestMoves = new PriorityQueue<>();
        }

        for (int i = 0; i < numValidMoves; i++) {
            int move = validMoves[i];
            int moveRow = move / 8;
            int moveCol = move % 8;

            int[][] newState = GameSimulator.changeColors(node, moveRow, moveCol, turn - 1);
            int newStateHeuristic = getOrderingHeuristic(newState);
            SortableState sortableState = new SortableState(newState, newStateHeuristic, i);

            bestMoves.add(sortableState);
        }

        SortableState checkState = bestMoves.poll();

        if(maximizingPlayer) {
            value = Integer.MIN_VALUE;
            while (checkState != null) {
                int[][] newState = checkState.state;

                int moveValue = minimax(newState, startDepth, depth - 1, (turn % 2) + 1, false, alpha, beta)[0];
                if(moveValue > value || (moveValue == value && Math.random() < 0.5)) {
                    value = moveValue;
                    bestMove = checkState.stateID;
                }

                if(value > beta) {
                    break;
                }

                alpha = Math.max(alpha, value);

                checkState = bestMoves.poll();
            }
        }
        else {
            value = Integer.MAX_VALUE;
            while (checkState != null) {
                int[][] newState = checkState.state;

                int moveValue = minimax(newState, startDepth,depth - 1, (turn % 2) + 1, true, alpha, beta)[0];
                if(moveValue < value || (moveValue == value && Math.random() < 0.5)) {
                    value = moveValue;
                    bestMove = checkState.stateID;
                }

                if (value < alpha) {
                    break;
                }

                beta = Math.min(beta, value);

                checkState = bestMoves.poll();
            }
        }

        return new int[]{value, bestMove};
    }

    public static void main(String[] args) {
        new AwesomePlayer(Integer.parseInt(args[1]), args[0]);
    }
}
