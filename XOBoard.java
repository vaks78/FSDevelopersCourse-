import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class XOBoard {
	/* --------- globals ------------- */
	private static int GRID_SIZE = 3;
	private static String X = "X";
	private static String O = "O";
	private static String NONE = "None";
	private static String DRAW = "Draw";

	private static char[][] board;
	private static String turn = X; // by default X is first
	private static String status = NONE;
	// results counts number of same values in every row, every column and 2 main diagonals 
	private static int[] results = new int[2 * GRID_SIZE + 2]; 
	private static int putCounter = 0;

	// initialize board
	private static void createBoard(int grid_size) {
		board = new char[grid_size][grid_size];

		for (int i = 0; i < GRID_SIZE; i++)
			for (int j = 0; j < GRID_SIZE; j++)
				board[i][j] = '-';
		
		// initialize array of counters
		for (int i = 0; i < results.length; i++)
			results[i] = 0;
	}

	// check if current cell is unoccupied
	private static boolean isEmpty(int row, int col) {
		return board[row][col] == '-';
	}

	// change player
	private static void changeTurn() {
		if (turn.equals(X))
			turn = O;

		else
			turn = X;
	}

	// update affected row-, column- and diagonals- counters
	private static void updateResults(int row, int col) {
		
		// 1 for X, -1 for O
		int increment = (turn.equals(X)) ? 1 : -1;

		results[row] += increment;
		results[GRID_SIZE + col] += increment;
		
		List<Integer> winnerStrikeCandidatesList = new ArrayList<>(Arrays.asList(results[row], results[GRID_SIZE + col]));
		
		//main upper-left <-> lower-right diagonal
		if (row == col) {
			results[2 * GRID_SIZE] += increment;
			winnerStrikeCandidatesList.add(results[2 * GRID_SIZE]);
		}
		
		// main upper-right <-> lower-left diagonal
		if (GRID_SIZE - 1 - col == row) {
			results[2 * GRID_SIZE + 1] += increment;
			winnerStrikeCandidatesList.add(results[2 * GRID_SIZE + 1]);
		}

		int winnerIndicator = checkWinnerExist(winnerStrikeCandidatesList);
		// X won
		if (winnerIndicator == 1)
			updateStatus(X);
		
		// O won
		if (winnerIndicator == -1)
			updateStatus(O);
	}

	// check if somebody already won
	private static int checkWinnerExist(List<Integer> winnerStrikeCandidatesList) {
		for (int candidate : winnerStrikeCandidatesList)
			
			// only 1-s - indicate X won
			if (candidate == GRID_SIZE)
				return 1;
		
			// only (-1)-s - indicates O won
			else if (candidate == GRID_SIZE * (-1))
				return -1;
		return 0;

	}

	// set game outcome 
	private static void updateStatus(String newStatus) {
		status = newStatus;
	}

	// output decorations
	private static void decorateOutput() {
		for (int i = 0; i < GRID_SIZE; i++)
			System.out.print("|---");
		System.out.println("|");
	}

	
	
	/* --------- API ------------- */
	// print game board
	public static void display() {
		decorateOutput();
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++)
				System.out.print("| " + board[i][j] + " ");
			System.out.println("|");
			decorateOutput();
		}

	}

	// return current game status
	public static String status() {
		return status;
	}

	// make move
	public static void put(int row, int col) {
		
		// check if the cell is empty
		if (!isEmpty(row, col))
			System.out.println("The cell is occupied. Unable to insert new value.");
		
		else {
			
			// put new value
			board[row][col] = turn.charAt(0);
			
			// update affected row, column and diagonals
			updateResults(row, col);
			
			// pass move to opponent
			changeTurn();
			
			// increment occupied cells' counter
			putCounter++;
			
			//all cells occupied => announce DRAW 
			if (putCounter == GRID_SIZE * GRID_SIZE)
				updateStatus(DRAW);
		}

	}

	
	
	/* --------- Driver method ------------- */
	public static void main(String[] args) {
		createBoard(GRID_SIZE);
		display();
		Scanner in = new Scanner(System.in);
		
		// while the game is not over
		while (status().equals(NONE)) {
			
			// ask current to enter coordinates of his move (pair of integers from 1 to GRID_SIZE) 
			System.out.println ("Please enter " + turn + "-player turn coordinates from 1 to " + GRID_SIZE +
					" seprated by blank space");
			
			// make move with entered coordinates
			put(in.nextInt()-1, in.nextInt()-1);
			
			// display a board after the move
			System.out.println("Current board status: ");
			display();
			
			// display current game status
			System.out.println("Current game winner is: " + status());
		}
		in.close();
	}

}
