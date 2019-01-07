import java.util.Arrays;

public class Board {
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
		return -1;
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

	// does this board equal y?
	public boolean equals(Object y) {
		return true;
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		return null;
	}

	// string representation of this board (in the output format specified below)
	public String toString() {
		return "";
	}

	public static void main(String[] args) {
	}
}