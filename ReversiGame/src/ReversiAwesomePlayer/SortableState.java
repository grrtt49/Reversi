package ReversiAwesomePlayer;

public class SortableState implements Comparable<SortableState> {
    int[][] state;
    Float heuristic;
    int stateID;

    SortableState(int[][] state, float heuristic, int stateID) {
        this.state = state;
        this.heuristic = heuristic;
        this.stateID = stateID;
    }

    @Override
    public int compareTo(SortableState o) {
        return heuristic.compareTo(o.heuristic);
    }
}
