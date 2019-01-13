import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private final MinPQ<SearchNode> pq;
	private int moves;
	private final List<Board> boards = new ArrayList<>();

	/**
	 * find a solution to the initial board (using the A* algorithm)
	 * 
	 * @param initial
	 */
	public Solver(Board initial) {
		if (initial == null) {
			throw new IllegalArgumentException();
		}
		pq = new MinPQ<>();
		pq.insert(new SearchNode(initial));
		int step = 0;
		do {
			System.out.println("\nStep: " + step++);
			printPQ(pq);
			final SearchNode min = pq.delMin();
			boards.add(min.current);
			if (min.current.isGoal()) {
				break;
			}
			final int finalMoves = ++moves;
			StreamSupport.stream(min.current.neighbors()
				.spliterator(), false)
				.filter(b -> min.predecessor == null || (min.predecessor != null && !min.predecessor.equals(b)))
				.map(b -> new SearchNode(min.current, b, finalMoves))
				.forEach(s -> {
					pq.insert(s);
				});
		} while (true);
	}

	private void printPQ(final MinPQ<SearchNode> pq) {		
		pq.forEach(System.out::print);
	}

	/***
	 * is the initial board solvable?
	 * 
	 * @return
	 */
	public boolean isSolvable() {
		return true;
	}

	/**
	 * min number of moves to solve initial board; -1 if unsolvable
	 * 
	 * @return
	 */
	public int moves() {
		return moves;
	}

	/**
	 * sequence of boards in a shortest solution; null if unsolvable
	 * 
	 * @return
	 */
	public Iterable<Board> solution() {
		return boards;
	}

	private static class SearchNode implements Comparable<SearchNode> {
		private Board predecessor;
		private Board current;
		private int moves;
		private int manhattan;
		private int priority;

		public SearchNode(Board current) {
			this(null, current, 0);
		}

		public SearchNode(Board predecessor, Board current, int moves) {
			this.predecessor = predecessor;
			this.current = current;
			this.moves = moves;
			this.manhattan = current.manhattan();
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
			builder.append(current.toString());
			return builder.toString();
		}
	}

	/**
	 * solve a slider puzzle (given below)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int start = 14;
		int end = 14;
		for (int x = start; x <= end; x++) {
			String filename = String.format("./8puzzle/puzzle%02d.txt", x);
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
				for (Board board : solver.solution())
					StdOut.println(board);
			}
		}
	}
}
