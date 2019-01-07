import java.util.Arrays;

import edu.princeton.cs.algs4.In;

public class Board {
	private static final String SPACE = " ";
	private static final String NEWLINE = "\n";
	private final int[][] blocks;
	private final int n;

	/*
	 * construct a board from an n-by-n array of blocks (where blocks[i][j] = block
	 * in row i, column j)
	 */
	public Board(int[][] blocks) {
		this.n = blocks.length;
		this.blocks = Arrays.copyOf(blocks, this.n);
	}

	/***
	 * board dimension n
	 * 
	 * @return
	 */
	public int dimension() {
		return n;
	}

	/**
	 * number of blocks out of place
	 * 
	 * @return number of blocks out of place
	 */
	public int hamming() {
		int counter = 1;
		int outOfPlace = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (blocks[i][j] != counter++ && blocks[i][j] > 0) {
					outOfPlace++;
				}
			}
		}
		return outOfPlace;
	}

	private Cell findEmptyBlock() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (blocks[i][j] == 0) {
					new Cell(i, j);
				}
			}
		}
		return null;
	}

	/**
	 * sum of Manhattan distances between blocks and goal
	 * 
	 * @return
	 */
	public int manhattan() {
		return -1;
	}

	/***
	 * is this board the goal board?
	 * 
	 * @return
	 */
	public boolean isGoal() {
		return false;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		return null;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			return false;
		}
		Board other = (Board) obj;
		if (n != other.n) {
			return false;
		}
		if (!Arrays.deepEquals(blocks, other.blocks)) {
			return false;
		}
		return true;
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		return null;
	}

	private static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	/**
	 * string representation of this board (in the output format specified below)
	 */
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		int prefix = 1;
		if (n >= 10) {
			prefix = 2;
		} else if (n >= 100) {
			prefix = 3;
		}
		for (int[] rows : blocks) {
			for (int col : rows) {
				builder.append(padLeft(col + "", prefix));
				builder.append(SPACE);
			}
			builder.append(NEWLINE);
		}
		return builder.toString();
	}

	private static class Cell {
		public final int i;
		public final int j;

		public Cell(int i, int j) {
			super();
			this.i = i;
			this.j = j;
		}
	}

	public static void main(String[] args) {
		// read in the board specified in the filename
		In in = new In("./8puzzle/puzzle00.txt");
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				tiles[i][j] = in.readInt();
			}
		}

		// solve the slider puzzle
		Board initial = new Board(tiles);
		System.out.println(initial);
		System.out.println(initial.hamming());
	}

}