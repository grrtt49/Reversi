package ReversiAwesomePlayer;

public abstract class Player extends SocketPlayer {

    Player(int _me, String host) {
        super(_me, host);

        while (true) {
            System.out.println("Read");
            readMessage();

            if (turn == me) {
                System.out.println("Move");
                MoveValidator moveValidator = new MoveValidator(me);

                moveValidator.getValidMoves(round, state);
                int[] validMoves = moveValidator.validMoves;
                int numValidMoves = moveValidator.numValidMoves;

                int myMove = move(validMoves, numValidMoves);

                sendMove(validMoves[myMove]);
            }
        }
    }

    protected abstract int move(int[] validMoves, int numValidMoves);
}
