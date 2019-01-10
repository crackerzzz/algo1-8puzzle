import java.util.ArrayList;
import java.util.List;
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
		do {
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
					System.out.println(s);
					pq.insert(s);
				});
		} while (true);
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

		public SearchNode(Board current) {
			this(null, current, 0);
		}

		public SearchNode(Board predecessor, Board current, int moves) {
			this.predecessor = predecessor;
			this.current = current;
			this.moves = moves;
		}

		@Override
		public int compareTo(SearchNode other) {
			return (this.current.manhattan() + this.moves) - (other.current.manhattan() + other.moves);
		}

		@Override
		public String toString() {
			return current.toString();
		}
	}

	/**
	 * solve a slider puzzle (given below)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String filename = "./8puzzle/puzzle02.txt";
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
