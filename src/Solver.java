import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private MinPQ<SearchNode> searchNodeMinPQ;
    private MinPQ<SearchNode> searchNodeTwinMinPQ;
    private int moves;
    private Stack solutionStack;
    private boolean isSolvable;

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        solutionStack = new Stack();
        searchNodeMinPQ = new MinPQ<>();
        searchNodeTwinMinPQ = new MinPQ<>();
        searchNodeMinPQ.insert(new SearchNode(initial, 0, null));
        searchNodeTwinMinPQ.insert(new SearchNode(initial.twin(), 0, null));

        while (true) {
            SearchNode currentSearchNode = searchNodeMinPQ.delMin();
            if (currentSearchNode.board.isGoal()) {
                isSolvable = true;
                moves = currentSearchNode.move;
                while (currentSearchNode != null) {
                    solutionStack.push(currentSearchNode.board);
                    currentSearchNode = currentSearchNode.predecessor;
                }
                break;
            }
            for (Board board : currentSearchNode.board.neighbors()) {
                if (currentSearchNode.predecessor == null || !board.equals(currentSearchNode.predecessor.board)) {
                    searchNodeMinPQ.insert(new SearchNode(board, currentSearchNode.move + 1, currentSearchNode));
                }
            }
            SearchNode currentSearchNodeTwin = searchNodeTwinMinPQ.delMin();
            if (currentSearchNodeTwin.board.isGoal()) {
                isSolvable = false;
                moves = -1;
                solutionStack = null;
                break;
            }
            for (Board board : currentSearchNodeTwin.board.neighbors()) {
                if (currentSearchNodeTwin.predecessor == null || !board.equals(currentSearchNodeTwin.predecessor.board)) {
                    searchNodeTwinMinPQ.insert(new SearchNode(board, currentSearchNodeTwin.move + 1, currentSearchNodeTwin));
                }
            }

        }
    }         // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return isSolvable;
    }          // is the initial board solvable?

    public int moves() {
        return moves;

    }             // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        return solutionStack;
    }    // sequence of boards in a shortest solution; null if unsolvable


    private class SearchNode implements Comparable<SearchNode> {

        private Board board;
        private int move;
        private SearchNode predecessor;
        private int priority;

        SearchNode(Board board, int move, SearchNode predecessor) {
            this.board = board;
            this.move = move;
            this.predecessor = predecessor;
            this.priority = board.manhattan() + move;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }
}
