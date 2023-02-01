package ReversiAwesomePlayer;

import java.util.Random;

public class RandomPlayer extends Player {

    Random generator;

    RandomPlayer(int _me, String host) {
        super(_me, host);
    }

    @Override
    protected int move(int[] validMoves, int numValidMoves) {
        generator = new Random();
        return generator.nextInt(numValidMoves);
    }

    public static void main(String[] args) {
        new RandomPlayer(Integer.parseInt(args[1]), args[0]);
    }
}
