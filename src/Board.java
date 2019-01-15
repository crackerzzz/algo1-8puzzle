import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		this.blocks = deepCopy(blocks);
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

	/**
	 * sum of Manhattan distances between blocks and goal
	 * 
	 * @return
	 */
	public int manhattan() {
		int distance = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				// skip empty block i.e. value with 0
				if (blocks[i][j] == 0) {
					continue;
				}

				// calculate actual element that should be in the cell.
				// + 1 is to make array value start at 1 rather than 0.
				final int goal = i * n + j + 1;

				// if not same, determine what cell that element should be in and calculate the
				// distance. The correct row and column can be determined by reverse of above
				// goal calculation.
				if (blocks[i][j] != goal) {
					final int k = blocks[i][j] - 1;
					final int x = k / n;
					final int y = k % n;
					final int t = Math.abs(x - i) + Math.abs(y - j);
//					System.out.println(String.format(
//							"Goal for [%d, %d] = %d, Found %d (%d) which should be at [%d, %d]. Manhattan distance: %d",
//							i, j, goal, blocks[i][j], k, x, y, t));
					distance += t;
				}
			}
		}
		return distance;
	}

	/***
	 * is this board the goal board?
	 * 
	 * @return true if goal board has been reached.
	 */
	public boolean isGoal() {
		// just check the last block to see if it is the goal, i.e should contain 0.
		if (blocks[n - 1][n - 1] != 0) {
			return false;
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// skip the last cell as this has already been covered by earlier check. Also
				// the calculation doesn't work for last cell.
				if (i == n - 1 && j == n - 1) {
					continue;
				}
				int goal = i * n + j + 1;
				if (blocks[i][j] != goal) {
					return false;
				}
			}
		}
		return true;
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
		final int[] emptyCell = findEmptyBlock(blocks);
		final int i = emptyCell[0];
		final int j = emptyCell[1];

		final List<List<Integer>> neighbouringCells = buildNeighbouringCells(i, j, this.n);
		final List<Board> neighbourBoards = new ArrayList<>(neighbouringCells.size());

		for (final List<Integer> n : neighbouringCells) {
			final int x = n.get(0);
			final int y = n.get(1);

			final int[][] copy = deepCopy(blocks);
			swap(copy, i, j, x, y);
			neighbourBoards.add(new Board(copy));
		}

		return neighbourBoards;
	}

	private static void swap(int[][] array, int x1, int y1, int x2, int y2) {
		int temp = array[x1][y1];
		array[x1][y1] = array[x2][y2];
		array[x2][y2] = temp;
	}

	private static List<List<Integer>> buildNeighbouringCells(int i, int j, int size) {
		final List<List<Integer>> list = new ArrayList<>();

		int[][] neighbours = new int[][] { { i - 1, j }, // above
				{ i, j + 1 }, // right
				{ i + 1, j }, // below
				{ i, j - 1 }// left
		};

		for (int[] n : neighbours) {
			int x = n[0];
			int y = n[1];
			// skip out of boundary cases
			if (x < 0 || x >= size || y < 0 || y >= size) {
				continue;
			}
			final List<Integer> inner = new ArrayList<>();
			list.add(inner);
			inner.add(x);
			inner.add(y);
		}

		return list;
	}

	public static int[][] deepCopy(int[][] original) {
		if (original == null) {
			return null;
		}

		final int[][] result = new int[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}

	private static int[] findEmptyBlock(int[][] original) {
		final int n = original.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (original[i][j] == 0) {
					return new int[] { i, j };
				}
			}
		}
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
		// if n is more than 9 means there will values more than 100, so three spaces
		// needs to be reserved
		if (n > 9) {
			prefix = 3;
		} else if (n > 3) {
			// if n is more than 3 means there will values more than 10, so two spaces
			// needs to be reserved
			prefix = 2;
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

	public static void main(String[] args) {
		// read in the board specified in the filename
		In in = new In("./8puzzle/puzzle-my.txt");
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				tiles[i][j] = in.readInt();
			}
		}

//		// solve the slider puzzle
		Board initial = new Board(tiles);
		System.out.println(initial);
//		System.out.println("hamming: " + initial.hamming());
		System.out.println("manhattan: " + initial.manhattan());
		System.out.println("isGoal: " + initial.isGoal());
		System.out.println("Neighbours: ");
		for (Board b : initial.neighbors()) {
			System.out.println(b);
		}

	}

}