import java.util.ArrayList;
import java.util.Random;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Generates a file (with user-specified name) containing an instance of the stable matching problem,
 * for some user-specified n >= 1. The preferences lists in this file are random permutations of [n].
 *
 * Instructions:
 * 1. Make sure java is installed.
 * 2. Go into terminal and change to the directory containing the generator file.
 * 3. Compile: javac PreferencesGenerator.java
 * 4. Run: java PreferencesGenerator [n>=1] [outputFileName] (you can also do this in Eclipse by setting the Run Configurations;
 * 	  see http://stackoverflow.com/questions/19646719/eclipse-command-line-arguments or http://www.cs.colostate.edu/helpdocs/eclipseCommLineArgs.html
 * 	  for instructions)
 */
public class PreferencesGenerator {

	// a fixed random number generator (rng) for generating random indices
	private static final Random rng = new Random();

	/**
	 * Given a number (n) and filename through commandline arguments, generates a random stable
	 * matching problem instance of the specified size and writes to a file of the given filename.
	 */
	public static void main(String[] args) {
		// ensure given exactly given two arguments
		if (args.length == 2) {
			int n = 0;
			try {
				n = Integer.parseInt(args[0]); // parse user-given problem size
				if (n >= 1) { // make sure it is a valid problem size
					String fileName = args[1]; // take user-given output filename
					ArrayList<int[]> boyPreferenceList = generatePreferenceList(n); // generate random preference list for boys
					ArrayList<int[]> girlPreferenceList = generatePreferenceList(n); // generate random preference list for girls
					outputBoyGirlPreferenceLists(boyPreferenceList, girlPreferenceList, fileName); // write it out!
				} else {
					usage();
				}
			} catch (NumberFormatException nfe) {
				usage();
			}
		} else {
			usage();
		}
	}

	/**
	 * Generates a list of length n populate with the integers 1 through n
	 */
	private static int[] generateFirstN(int n) {
		int[] firstN = new int[n];
		for (int i = 0; i < n; i++) {
			firstN[i] = i + 1;
		}
		return firstN;
	}

	/**
	 * Generates a random permutation of the given integer array. Does so in linear time; see Fisher-Yates algorithm.
	 */
	private static void shuffle(int[] arr) {
		for (int i = arr.length - 1; i > 0; i--) {
			int randIndex = rng.nextInt(i + 1);

			// do a swap if the randomly generated index is not i itself
			if (randIndex != i) {
				int buf = arr[randIndex];
				arr[randIndex] = arr[i];
				arr[i] = buf;
			}
		}
	}

	/**
	 * Returns a random permutation of [1,...,n]
	 */
	static int[] generateRandomPreferences(int n) {
		int[] permutation = generateFirstN(n);
		shuffle(permutation);
		return permutation;
	}

	/**
	 * Generates a random list of preferences for a given n by generating a random
	 * permutation of {1,...,n} (resp. boys or girls) for each {1,...,n} (resp. girls or boys)
	 */
	static ArrayList<int[]> generatePreferenceList(int n) {
		ArrayList<int[]> preferenceList = new ArrayList<int[]>(n);
		for (int i = 0; i < n; i++) {
			preferenceList.add(generateRandomPreferences(n));
		}
		return preferenceList;
	}

	/**
	 * Writes out a given instance of the stable matching problem (given by a list of preferences for boys
	 * and another for girls) to a file of the specified name. The format is as follows:
	 *
	 * n
	 * g_11 ... g_1j ... g_1n
	 * ...
	 * g_i1 ... g_ij ... g_in
	 * ...
	 * g_n1 ... g_nj ... g_nn
	 * b_11 ... b_1j ... b_1n
	 * ...
	 * b_i1 ... b_ij ... b_in
	 * ...
	 * b_n1 ... b_nj ... b_nn
	 *
	 * where g_ij is in {1,...,n} for all i,j. The first line gives the size n of this problem instance.
	 * Each the following n lines is a preference list of girls for a boy (line i corresponds to boy i). Similarly,
	 * each of the (then) following (last) n lines is a preference list for boys for a girl (line i corresponds
	 * to girl i).
	 */
	static void outputBoyGirlPreferenceLists(ArrayList<int[]> boyPreferenceList,
											 ArrayList<int[]> girlPreferenceList,
											 String fileName) {
		if (boyPreferenceList != null && girlPreferenceList != null &&
				!boyPreferenceList.isEmpty() && !girlPreferenceList.isEmpty() &&
				boyPreferenceList.size() == girlPreferenceList.size()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
				writer.write(String.valueOf(boyPreferenceList.size()));
				writer.newLine();
				for (int[] preferences : boyPreferenceList) {
					writeSinglePreferenceList(writer, preferences);
				}
				for (int[] preferences : girlPreferenceList) {
					writeSinglePreferenceList(writer, preferences);
				}
				writer.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * Writes a single preference list to a single line
	 */
	private static void writeSinglePreferenceList(BufferedWriter writer, int[] preferences) throws IOException {
		if (preferences != null && preferences.length > 0) {
			writer.write(String.valueOf(preferences[0]));
			for (int i = 1; i < preferences.length; i++) {
				writer.write(' ');
				writer.write(String.valueOf(preferences[i]));
			}
			writer.newLine();
		}
	}

	/**
	 * Prints the command for how to run this generator
	 */
	private static void usage() {
		System.out.println("Usage: \"java PreferencesGenerator [n >= 1] [outputFileName]\"");
	}
}