package ReversiAwesomePlayer;

public class SortableState implements Comparable<SortableState> {
    int[][] state;
    Integer heuristic;
    int stateID;

    SortableState(int[][] state, int heuristic, int stateID) {
        this.state = state;
        this.heuristic = heuristic;
        this.stateID = stateID;
    }

    @Override
    public int compareTo(SortableState o) {
        return heuristic.compareTo(o.heuristic);
    }
}
