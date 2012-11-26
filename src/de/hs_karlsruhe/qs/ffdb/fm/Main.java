package de.hs_karlsruhe.qs.ffdb.fm;

import java.io.File;
import java.util.List;

/**
 * @author Daniel Baelz and Fabian Freimueller
 * Main class for file merger. 
 */
public class Main {
	private static File mergeFile1;
	private static File mergeFile2; 
	private static File mergeFile3;
	private static Boolean fileError = false;
	
	/**
	 * @param args Input files to merge
	 */
	public static void main(String[] args) {
		
		if (args.length == 3) {
			mergeFile1 = new File(args[0]);
			mergeFile2 = new File(args[1]);
			mergeFile3 = new File(args[2]);
		} else {
			System.out.println("Only three files as arguments allowed");
			return;
		} 

		if (!mergeFile1.exists()) {
			System.out.println("File " + mergeFile1.toString() + " does not exist.");
			fileError = true;
		}
		if (!mergeFile2.exists()) {
			System.out.println("File " + mergeFile2.toString() + " does not exist.");
			fileError = true;
		}	
		if (!mergeFile3.exists()){
			System.out.println("File " + mergeFile3.toString() + " does not exist.");
			fileError = true;
		}
		
		if (fileError) {
			return;
		} else {
			Merge merge = new Merge(mergeFile1, mergeFile2, mergeFile3);
			
			Boolean mergeFailed = merge.mergeFiles();
			List<String> mergedFile = merge.getMergedList();
			for (int i = 0; i < mergedFile.size(); i++) {
				System.out.println(mergedFile.get(i));
			}
			
			if (!mergeFailed) {
				System.out.println("Merge failed, please merge files manually.");
			}
		}

	}

}
