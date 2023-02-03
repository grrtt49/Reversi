package ReversiAwesomePlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

public class AwesomePlayer extends Player {

    long totalTime = 0;
    int times = 0;

    AwesomePlayer(int _me, String host) {
        super(_me, host);
    }

    @Override
    protected int move(int[] validMoves, int numValidMoves) {
        System.out.println("Turn: " + round);
        if(round == 0) {
            return 0;
        }
        if(round == 2) {
            return 1;
        }
        MinimaxScore minimax = minimax(state, 8, 8, turn, me == 1, Float.MIN_VALUE, Float.MAX_VALUE);
        System.out.println("Got score: " + minimax.score);
        System.out.println();
        System.out.println("Total time: " + ((double)totalTime / 1000000000));
        System.out.println("Avg time: " + (((double)totalTime / 1000000000) / times));
        System.out.println();
        return minimax.move;
    }

    public float getScoreFromArr(int[] arr) {
        return 100 * ((float)(arr[0] - arr[1]) / (arr[0] + arr[1]));
    }

    public float getParityHeuristic(int[][] state) {
        int[] points =  GameSimulator.getNumberOfPoints(state);
        if(points[0] + points[1] == 0) {
            return 0;
        }
        return getScoreFromArr(points);
    }

    public float getCornersHeuristic(int[][] state) {
        int[] corners = GameSimulator.getNumberOfCorners(state);
        if(corners[0] + corners[1] == 0) {
            return 0;
        }
        return getScoreFromArr(corners);
    }

    public float getStabilityHeuristic(int[][] state) {
        int[] stabilityScores = GameSimulator.getStabilityScores(state);
        if(stabilityScores[0] + stabilityScores[1] == 0) {
            return 0;
        }
        return getScoreFromArr(stabilityScores);
    }

    public float getMobilityHeuristic(int[][] state) {
        MoveValidator validator = new MoveValidator(0);
        validator.getNumValidMoves(4, state);
        int[] moves = new int[] {validator.numValidMoves, validator.numValidMovesOther};
        return 100 * ((float)(moves[0] - moves[1]) / (moves[0] + moves[1]));
    }

    public float getHeuristic(int[][] state) {
        long startTime = System.nanoTime();

        float parityWeight = 1;
        float cornersWeight = 10;
        float movesWeight = 2;

        float parity = getParityHeuristic(state);
        float corners = getCornersHeuristic(state);

        long endTime = System.nanoTime();
        totalTime += (endTime - startTime);
        times++;
        return parityWeight * parity + cornersWeight * corners;
    }

    public float getOrderingHeuristic(int[][] state) {
        int pointsWeight = 1;
        int cornersWeight = 10;

        int[] points = GameSimulator.getNumberOfPoints(state);
        int[] corners = GameSimulator.getNumberOfCorners(state);

        int heuristic1 = pointsWeight * points[0] + cornersWeight * corners[0];
        int heuristic2 = pointsWeight * points[1] + cornersWeight * corners[1];

        return heuristic1 - heuristic2;
    }

    public float getEndHeuristic(int[][] state) {
        int[] points = GameSimulator.getNumberOfPoints(state);
        System.out.println(Arrays.toString(points));

        return (points[0] - points[1]) * 100;
    }

    public MinimaxScore minimax(int[][] node, int startDepth, int depth, int turn, boolean maximizingPlayer, float alpha, float beta) {
        MoveValidator moveValidator = new MoveValidator(turn);
        moveValidator.getValidMoves(round + (startDepth - depth), node);

        int[] validMoves = moveValidator.validMoves;
        int numValidMoves = moveValidator.numValidMoves;

        if(numValidMoves <= 0) {
            return new MinimaxScore(getEndHeuristic(node), -1);
        }

        if(depth == 0) {
            return new MinimaxScore(getHeuristic(node), -1);
        }

        float value;
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
            float newStateHeuristic = getOrderingHeuristic(newState);
            SortableState sortableState = new SortableState(newState, newStateHeuristic, i);

            bestMoves.add(sortableState);
        }

        SortableState checkState = bestMoves.poll();

        if(maximizingPlayer) {
            value = Integer.MIN_VALUE;
            while (checkState != null) {
                int[][] newState = checkState.state;

                MinimaxScore minimaxScore = minimax(newState, startDepth, depth - 1, (turn % 2) + 1, false, alpha, beta);
                float moveValue = minimaxScore.score;
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

                MinimaxScore minimaxScore = minimax(newState, startDepth,depth - 1, (turn % 2) + 1, true, alpha, beta);
                float moveValue = minimaxScore.score;
                if(moveValue < value) { //(moveValue == value && Math.random() < 0.5)
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

        return new MinimaxScore(value, bestMove);
    }

    public static void main(String[] args) {
        new AwesomePlayer(Integer.parseInt(args[1]), args[0]);
    }
}
