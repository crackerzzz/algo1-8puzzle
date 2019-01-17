import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private SearchNode last;

	/**
	 * find a solution to the initial board (using the A* algorithm)
	 * 
	 * @param initial
	 */
	public Solver(Board initial) {
		if (initial == null) {
			throw new IllegalArgumentException();
		}
		final MinPQ<SearchNode> pq = new MinPQ<>();
		pq.insert(new SearchNode(initial));

		final MinPQ<SearchNode> twinPq = new MinPQ<>();
		twinPq.insert(new SearchNode(initial.twin()));

		do {
			if (((last = solve(pq)) != null) || solve(twinPq) != null) {
				break;
			}
		} while (true);
	}

	private SearchNode solve(final MinPQ<SearchNode> pq) {
		final SearchNode min = pq.delMin();
		if (min.board.isGoal()) {
			return min;
		}
		for (Board b : min.board.neighbors()) {
			if (min.predecessor == null || !min.predecessor.board.equals(b)) {
				pq.insert(new SearchNode(min, b));
			}
		}
		return null;
	}

	/***
	 * is the initial board solvable?
	 * 
	 * @return
	 */
	public boolean isSolvable() {
		return last != null;
	}

	/**
	 * min number of moves to solve initial board; -1 if unsolvable
	 * 
	 * @return
	 */
	public int moves() {
		return last != null ? last.moves : -1;
	}

	/**
	 * sequence of boards in a shortest solution; null if unsolvable
	 * 
	 * @return
	 */
	public Iterable<Board> solution() {
		if (last == null)
			return null;

		SearchNode temp = last;
		final Stack<Board> stack = new Stack<>();
		do {
			stack.push(temp.board);
			temp = temp.predecessor;
		} while (temp != null);
		return stack;
	}

	private static class SearchNode implements Comparable<SearchNode> {
		private SearchNode predecessor;
		private Board board;
		private int moves;
		private int manhattan;
		private int priority;

		public SearchNode(Board board) {
			this(null, board);
		}

		public SearchNode(SearchNode predecessor, Board board) {
			this.predecessor = predecessor;
			this.board = board;
			if (predecessor != null) {
				this.moves = predecessor.moves + 1;
			} else {
				this.moves = 0;
			}
			this.manhattan = board.manhattan();
			this.priority = this.manhattan + this.moves;
		}

		@Override
		public int compareTo(SearchNode other) {
			return this.priority - other.priority;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("\npriority	= " + priority);
			builder.append("\nmoves		= " + moves);
			builder.append("\nmanhattan	= " + manhattan + "\n");
			builder.append(board.toString());
			return builder.toString();
		}
	}

	/**
	 * solve a slider puzzle (given below)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int start = 0;
		int end = 80;
		for (int x = start; x <= end; x++) {
			String filename = String.format("./8puzzle/puzzle4x4-%02d.txt", x);
			System.out.println("File: " + filename);
			// read in the board specified in the filename
			In in = new In(filename);
			int n = in.readInt();
			int[][] tiles = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					tiles[i][j] = in.readInt();
				}
			}

			// solve the slider puzzle
			Board initial = new Board(tiles);
			Solver solver = new Solver(initial);

			// print solution to standard output
			if (!solver.isSolvable())
				StdOut.println("No solution possible");
			else {
				StdOut.println("Minimum number of moves = " + solver.moves());
//				for (Board board : solver.solution())
//					StdOut.println(board);
			}
		}
	}
}
