package chaofan;
import java.util.*;
import java.io.*;

public class StableMatching {
	
	static int n;
	static String[][] boyPref;
	static String[][] girlPref;
	static int[][] girlPrefInverse;
	static String[] girlPartner;
	static int[] boyNextProp;
	
	public static void main(String[] args) throws IOException {
		int N = 11;
		for (int k = 4; k <= N; k++) {
			System.out.println(k + ": ");
			long start = System.currentTimeMillis();
			Scanner sc = new Scanner(new File("/Users/Chaofan/Downloads/" + (int) Math.pow(2, k) + ".txt"));
			n = Integer.parseInt(sc.nextLine());
			boyPref = new String[n][n];
			girlPref = new String[n][n];
			for (int i = 0; i < n; i++) 
				boyPref[i] = sc.nextLine().split(" ");
			for (int i = 0; i < n; i++)
				girlPref[i] = sc.nextLine().split(" ");
			sc.close();
			
			girlPrefInverse = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++)
					girlPrefInverse[i][Integer.parseInt(girlPref[i][j])-1] = j;
			}
			
			Stack<String> freeBoy = new Stack<>();
			for (int i = n; i >= 1; i--)
				freeBoy.push(String.valueOf(i));
			girlPartner = new String[n];
			boyNextProp = new int[n];
			
			int[] boyH = new int[n];
			int[] girlH = new int[n];
			
			while (!freeBoy.isEmpty() && boyNextProp[boyIndex(freeBoy.peek())] < n) {
				String boy = freeBoy.peek();
				int nextGirl = Integer.parseInt(boyPref[boyIndex(boy)][boyNextProp[boyIndex(boy)]]) - 1;
				if (girlPartner[nextGirl] == null) {
					girlPartner[nextGirl] = boy;
					girlH[nextGirl] = boyRank(nextGirl, boy);
					freeBoy.pop();
					boyH[boyIndex(boy)] = boyNextProp[boyIndex(boy)] + 1;
				} else if (girlPartner[nextGirl] != null && !rejectBoy(boy, nextGirl)) {
					 freeBoy.pop();
					 freeBoy.push(girlPartner[nextGirl]);
					 girlPartner[nextGirl] = boy;
					 girlH[nextGirl] = boyRank(nextGirl, boy);
					 boyH[boyIndex(boy)] = boyNextProp[boyIndex(boy)] + 1;
				}
				boyNextProp[boyIndex(boy)]++;
			}
			
			int[] boyPartner = new int[n];
			int[] girlP = new int[n];
			for (int i = 0; i < n; i++) {
				boyPartner[Integer.parseInt(girlPartner[i])-1] = i + 1;
				girlP[i] = Integer.parseInt(girlPartner[i]);
			}
			
			int boySum = 0, girlSum = 0;
			for (int i = 0; i < n; i++) {
				boySum += boyH[i];
				girlSum += girlH[i];
			}
			double boyMean = boySum / (double) n;
			double girlMean = girlSum / (double) n;
			
			BufferedWriter br = new BufferedWriter(new FileWriter("/Users/Chaofan/Downloads/output.txt", true));
			br.write((int) (Math.log(n) / Math.log(2)) + " ");
			br.write(n + " ");
			br.write(boyMean + " ");
			br.write(girlMean + " ");
			br.newLine();
			br.close();
			
			long end = System.currentTimeMillis();
			System.out.println("Time elapsed: " + (end - start));
			
		}
	}
	
	private static int boyRank(int girlIndex, String boy) {
		for (int i = 0; i < n; i++) {
			if (girlPref[girlIndex][i].equals(boy))
				return i + 1;
		}
		return -1;
	}
	
	private static int boyIndex(String boy) {
		return Integer.parseInt(boy) - 1;
	}
	
	private static boolean rejectBoy(String boy, int girlIndex) {
		if (girlPrefInverse[girlIndex][Integer.parseInt(girlPartner[girlIndex])-1] < 
				girlPrefInverse[girlIndex][Integer.parseInt(boy)-1])
			return true;
		return false;
	}
}
