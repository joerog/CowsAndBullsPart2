import java.util.ArrayList;
import java.util.Scanner;

public class JRCowsAndBullsPart2 {

	public static void main(String[] args) {
		ArrayList<Integer> secretNum = new ArrayList<Integer>(4);
		ArrayList<ArrayList<Integer>> possibleNums = new ArrayList<ArrayList<Integer>>(); //ArrayList of array lists initially containing the numbers 1-9
		possibleNums = initialisePossible(possibleNums);
		Boolean solved = false;
		
		//get input
		String input;
		System.out.println("Please enter a secret number for me to guess:");
		do {
			Scanner scanner = new Scanner(System.in);
			input = scanner.nextLine();
		}while(!valid(input));
		for (int i = 0; i < 4; i++) {
			secretNum.add(i, Character.getNumericValue(input.charAt(i)));
		}
		int correctlyPositioned = 0;
		int turn = 1;
		ArrayList<Integer> guess = new ArrayList<Integer>(4);
		//loop while secret number hasn't been found
		while(!solved) {
			
			//Generate a guess
			guess.clear();
			while (guess.size() != 4) {
				for (int i = 0; i < 4; i++) {
					int temp = 0;
					int c = 0; // used to time out in case of a deadlock, where the only possible values are already taken
					if (possibleNums.get(i).size() == 1) {
						guess.add(i, possibleNums.get(i).get(0));
					} else {
						int value = 0;
						do {
							c++;
							temp = (int) (Math.random() * (possibleNums.get(i).size()));
							value = possibleNums.get(i).get(temp);
						} while (guess.contains(Integer.valueOf(value)) && c <= 10);
						if (c > 10) {
							guess.clear();
						} else {
							guess.add(value);
						}
					}
				}
			}
			System.out.print("Turn "+ turn++ +": My guess is: ");
			print(guess);
			System.out.print("\n");
			
			//Calculate cows and bulls from guess
			int cows = 0;
			int bulls = 0;
			for (int i = 0; i < 4; i++) {
				if (secretNum.contains(guess.get(i))) {
					if (secretNum.get(i) == guess.get(i)) {
						bulls++;
					} else {
						cows++;
					}
				}
			}
			System.out.println("Bulls = " + bulls + ", Cows = " + cows);
			if (bulls == 4) {solved = true;}
			
			//Process cows and bulls
			if (bulls <= correctlyPositioned) {
				//if there are no cows and the bulls found were all known then all values apart from bulls do not appear and can be discounted entirely
				if (cows == 0) {
					for (int i = 0; i < 4; i++) {
						for (int j = 0; j < 4; j++) {
							if (possibleNums.get(j).contains(guess.get(i)) && possibleNums.get(j).size() > 1) {
								possibleNums.get(j).remove(Integer.valueOf(guess.get(i)));
							}
						}
					}
				} else {
					//if the bulls found are all known then we know all other values are incorrectly positioned so can be removed
					for (int i = 0; i < 4; i++) {
						if (possibleNums.get(i).contains(guess.get(i)) && possibleNums.get(i).size() > 1) {
							possibleNums.get(i).remove(Integer.valueOf(guess.get(i)));
						}
					}
				}
			}
			//if there is only 1 possibility left for a value then it is always going to be a bull and the value can be removed from all the other numbers as it cannot appear again 
			for (int i = 0; i < 4; i++) {
				if (possibleNums.get(i).size() == 1) {
					correctlyPositioned++;
					int value = possibleNums.get(i).get(0);
					for (int j = 0; j < 4; j++) {
						if (possibleNums.get(j).contains(value) && j != i) {
							possibleNums.get(j).remove(Integer.valueOf(value));
						}
					}
				}
			}
			//If the sum of cows and bulls is 4 then we have found the four values that make up the number, so all others can be discounted
			if (cows + bulls == 4) {
				for (int i = 0; i < 4; i++) {
					for (int j = 1; j < 10; j++) {
						if (!guess.contains(j) && possibleNums.get(i).contains(j)) {
							possibleNums.get(i).remove(Integer.valueOf(j));
						}
					}
				}
			}
		}
		System.out.print("\nGame Over: Your number is: ");
		print(guess);
		System.out.println(", found in " + (turn - 1) + " guesses.");
	}
	
	//function to print the guess arrayList
	static void print(ArrayList<Integer> num) {
		for (int i = 0; i < 4; i++) {
			System.out.print(num.get(i));
		}
	}
	
	//function to check the validity of input
	private static boolean valid(String input) {
		boolean valid = true;
		if (input.length() != 4) {valid = false;}
		try {
			int val = Integer.parseInt(input);
		}catch (NumberFormatException e) {
		    valid = false;
		}
		for (int i = 0; i < 4 && valid; i++)
		{
			char c = input.charAt(i);
			if (c == '0') {
				valid = false;
				break;
			}
			for (int j = 0; j < 4; j++) {
				if (input.charAt(j) == c && j != i) {
					valid = false;
				}
			}
		}
		if (!valid) {
			System.out.println("Error: invalid input. Please try again:");
		}
		return valid;
	}
	
	//function to initialise the lists of possible numbers
	static ArrayList<ArrayList<Integer>> initialisePossible(ArrayList<ArrayList<Integer>> possibleNums) {
		for (int i = 0; i < 4; i++) {
			possibleNums.add(new ArrayList<Integer>(8));
			for (int j = 1; j < 10; j++) {
				possibleNums.get(i).add(j);
			}
		}
		return possibleNums;
	}
}
