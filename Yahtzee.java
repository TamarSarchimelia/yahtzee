
/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		scores = new Integer[nPlayers][TOTAL];
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
		winGame();
	}
	// here I wrote a code where in the end of the game wrotes who won the game
	private void winGame() {
		String name = playerNames[0];
		int score = scores[0][16] + scores[0][7] + scores[0][8];;
		for(int i=1; i<nPlayers; i++) {
			if((scores[i][16] + scores[i][7] + scores[i][8])>score) {
				name = playerNames[i];
				score = scores[i][16] + scores[i][7] + scores[i][8];;
			}else if((scores[i][16] + scores[i][7] + scores[i][8])>score) {
				name = name+ " and " + playerNames[i];
			}
		}
		display.printMessage("The game win: " + name + " with a score: " + score);
	}
	// here is body of the game
	private void playGame() {
		/* You fill this in */
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			for (int j = 0; j < nPlayers; j++) {
				roll(j);
				choice(j);
				upperScore(j);
				lowerScore(j);
				if ((i + 1) == N_SCORING_CATEGORIES) {
					int n = scores[j][16] + scores[j][7] + scores[j][8];
					display.updateScorecard(17, j + 1, n);
				}
			}
		}
	}
	// here is body of the roll dices
	private void roll(int j) {
		rollOne(j);
		rollOther(j);

	}
	// this code is for first roll dices
	private void rollOne(int j) {
		display.printMessage(playerNames[j] + "'s turn! Click ''Roll Dice'' button to roll the dice.");
		for (int i = 0; i < N_DICE; i++) {
			dices[i] = rgen.nextInt(1, 6);

		}
		display.waitForPlayerToClickRoll(j + 1);
		display.displayDice(dices);
	}
	//its for second and third roll dices
	private void rollOther(int j) {
		for (int i = 0; i < 2; i++) {
			display.printMessage("Select the dice you wish to re-roll and click ''Roll Again''.");
			display.waitForPlayerToSelectDice();
			for (int k = 0; k < N_DICE; k++) {
				if (display.isDieSelected(k)) {
					dices[k] = rgen.nextInt(1, 6);
				}
			}
			display.displayDice(dices);
		}

	}
	// here is a code that whaits when a person choose a category
	private void choice(int j) {
		display.printMessage("Select a category for this roll.");
		int a = display.waitForPlayerToSelectCategory();
		while (scores[j][a] != null) {
			display.printMessage("This category is selected, select other");
			a = display.waitForPlayerToSelectCategory();
		}
		int k = score(a);
		scores[j][a] = k;
		display.updateScorecard(a, j + 1, k);
	}
	// here is a body of code that calsulates score
	private int score(int a) {
		int n = 0;
		int k = 0;
		if (a < 7) {
			for (int i = 0; i < N_DICE; i++) {
				if (dices[i] == a) {
					n = n + a;
				}
			}
		} else if (a == 9 || a == 10) {
			if (a == 9) {
				k = 3;
			} else {
				k = 4;
			}
			if (Ndice(k)) {
				for (int i = 0; i < N_DICE; i++) {
					n = n + dices[i];
				}
			}
		} else if (a == 11) {
			if (fullHouse())
				n = 25;
		} else if (a == 12) {
			if (smallStrit())
				n = 30;
		} else if (a == 13) {
			if (bigStright())
				n = 40;
		} else if (a == 14) {
			if (Ndice(N_DICE))
				n = 50;
		} else if (a == 15) {
			n = chance();
		}
		return n;
	}
	// its calculate chances score
	private int chance() {
		int n = dices[0];
		for (int i = 1; i < N_DICE; i++) {
			n = n + dices[i];
		}
		return n;
	}
	// this code says its a  bigstright or not
	private boolean bigStright() {
		int ones = 0;
		int twos = 0;
		int three = 0;
		int fours = 0;
		int fives = 0;
		int sixes = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dices[i] == 1) {
				ones++;
			} else if (dices[i] == 2) {
				twos++;
			} else if (dices[i] == 3) {
				three++;
			} else if (dices[i] == 4) {
				fours++;
			} else if (dices[i] == 5) {
				fives++;
			} else {
				sixes++;
			}
		}
		if (twos != 0 && three != 0 && fours != 0 && fives != 0 && (ones != 0 || sixes != 0)) {
			return true;
		} else {
			return false;
		}
	}
	// this code says its a small strit or not
	private boolean smallStrit() {
		int ones = 0;
		int twos = 0;
		int three = 0;
		int fours = 0;
		int fives = 0;
		int sixes = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dices[i] == 1) {
				ones++;
			} else if (dices[i] == 2) {
				twos++;
			} else if (dices[i] == 3) {
				three++;
			} else if (dices[i] == 4) {
				fours++;
			} else if (dices[i] == 5) {
				fives++;
			} else {
				sixes++;
			}

		}
		if (ones > 0 && twos > 0 && three > 0 && fours > 0) {
			return true;
		} else if (twos > 0 && three > 0 && fours > 0 && fives > 0) {
			return true;
		} else if (three > 0 && fours > 0 && fives > 0 && sixes > 0) {
			return true;
		} else {
			return false;
		}
	}
	// this code says its fullhouse or not
	private boolean fullHouse() {
		int a = dices[0];
		int b = 0;
		int k = 1;
		for (int i = 1; i <N_DICE; i++) {
			if (dices[i] == a) {
				k++;
			} else if (b == 0) {
				b = dices[i];
			} else if (dices[i] != b) {
				return false;
			}

		}
		if (k == 2 || k == 3) {
			return true;
		} else {
			return false;
		}
	}
	// this code says here is a number dices or not
	private boolean Ndice(int a) {
		int one = dices[0];
		int two = 0;
		int three = 0;
		int k = 1;
		int n = 0;

		for (int i = 1; i < N_DICE; i++) {
			if (dices[i] == one) {
				k++;
			} else if (two == 0) {
				two = dices[i];
				n++;
			} else if (two == dices[i]) {
				n++;
			} else if (three == 0) {
				three = dices[i];
			} else if (three != dices[i])
				return false;
		}

		int other = 5 - (k + n);
		if (k >= a || n >= a || other >= a) {
			return true;
		} else {
			return false;
		}

	}
	// this code calsulates upperscore
	private void upperScore(int j) {
		int n = 0;
		boolean t = true;
		for (int i = 1; i < 7; i++) {
			if (scores[j][i] == null) {
				t = false;
				break;
			} else {
				n = n + scores[j][i];
			}
		}
		if (t) {
			scores[j][7] = n;
			display.updateScorecard(7, j + 1, n);
			if (n > 62) {
				scores[j][8] = 35;
				display.updateScorecard(8, j + 1, 35);
			} else {
				scores[j][8] = 0;
				display.updateScorecard(8, j + 1, 0);
			}
		}
	}
	// this code calsulate lowerscore
	private void lowerScore(int j) {
		int n = 0;
		boolean t = true;
		for (int i = 9; i < 16; i++) {
			if (scores[j][i] == null) {
				t = false;
				break;
			} else {
				n = n + scores[j][i];
			}
		}
		if (t) {
			scores[j][16] = n;
			display.updateScorecard(16, j + 1, n);
		}
	}

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	// here is some code that I wrote
	private Integer[][] scores;
	private int[] dices = new int[N_DICE];

}
