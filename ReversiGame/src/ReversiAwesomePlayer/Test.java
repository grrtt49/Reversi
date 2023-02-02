package ReversiAwesomePlayer;

public class Test {
    public static void main(String[] args) {
        int[][] state = new int[8][8];
        GameSimulator.printState(state);
        int[][] newState = GameSimulator.changeColors(state, 3, 3, 0);
        GameSimulator.printState(newState);
    }
}
