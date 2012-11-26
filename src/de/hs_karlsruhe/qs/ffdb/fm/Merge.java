package de.hs_karlsruhe.qs.ffdb.fm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Baelz and Fabian Freimueller
 * Three-Way-Merger with LCS
 */
public class Merge {
	private File mergeFile1;
	private File mergeFile2; 
	private File mergeFile3;
	
	private List<String> hashList1;
	private List<String> hashList2;
	private List<String> hashList3;
		
	private List<String> lcs1;
	private List<String> lcs2;
	private List<String> lcsAll;
	
	private List<String> mergedFile = null;
	private Boolean mergeFailed = false;
	
	public Merge(File mergeFile1, File mergeFile2, File mergeFile3) {
		this.mergeFile1 = mergeFile1;
		this.mergeFile2 = mergeFile2;
		this.mergeFile3 = mergeFile3;
	}
	
	/**
	 * Check if the files mergeable and creates an ArrayList with the merged lines.
	 * @return true if files mergeable, otherwise false
	 */
	public Boolean mergeFiles() {
		mergedFile = null;
		
		hashList1 = makeHash(mergeFile1);
		hashList2 = makeHash(mergeFile2);
		hashList3 = makeHash(mergeFile3);
		
		// Check if files to merge are equal
		if (hashList2.equals(hashList3)) {
			mergedFile = makeStringList(mergeFile2);
		} else {
			lcs1 = lcs(hashList1, hashList2);
			lcs2 = lcs(hashList1, hashList3);
			lcsAll = lcs(lcs1, lcs2);
			
			mergedFile = merge();
		}
		
		if (mergeFailed) {
			return false;
		}
		return true;	
	}
	
	/**
	 * Returns the merged ArrayList.
	 * @return mergedFile as ArrayList
	 */
	public List<String> getMergedList() {
		return mergedFile;
	}
	
	/**
	 * Creates the SHA-1 hashed ArrayList from a given file. 
	 * @param file File to hash
	 * @return SHA-1 hashed ArrayList
	 */
	private List<String> makeHash(File file) {
		List<String> list = new ArrayList<String>();
		
		try {
			BufferedReader inputFile = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = inputFile.readLine()) != null) {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				list.add(getHexString(md.digest(line.getBytes("UTF-8"))));
			}
			inputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Creates a hex string from a given byte array
	 * @param b byte array
	 * @return Hex encoded string
	 * @throws Exception Throws exception in case of converting problem
	 */
	private String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	/**
	 * Calculate the 'Longest Common Subsequences' from two given files.
	 * @param list1 First list for calculation
	 * @param list2 Second list for calculation
	 * @return The list with the LCS as entries
	 */
	private List<String> lcs(List<String> list1, List<String> list2) {
		// Matrix for LCS
		int[][] lengths = new int[list1.size()+1][list2.size()+1];
		
		// Fill matrix with length values
		for (int i = list1.size() - 1; i >= 0; i--) {
			for (int j = list2.size() - 1; j >= 0; j--) {
				if (list1.get(i).equals(list2.get(j))) {
					lengths[i][j] = 1 + lengths[i+1][j+1];
				} else {
					lengths[i][j] = Math.max(lengths[i+1][j], lengths[i][j+1]);
				}
			}
		}
		
		// LCS result
		List<String> lcsList = new ArrayList<String>();
		int i = 0;
		int j = 0;
		
		// Create subsequence list
		while (i < list1.size() && j < list2.size()) {
			if(list1.get(i).equals(list2.get(j))) {
				lcsList.add(list1.get(i));
				i++;
				j++;
			} else if (lengths[i+1][j] >= lengths[i][j+1]) {
				i++;
			} else {
				j++;
			}
		}
		return lcsList;
	}
	
	/**
	 * Merges the files as ArrayList to new ArrayList.
	 * @return ArrayList with the merges lines as entries
	 */
	private List<String> merge() {
		int sizeList2 = hashList2.size();
		int sizeList3 = hashList3.size();
		int sizeLCS1 = lcs1.size();
		int sizeLCS2 = lcs2.size();
		int sizeLCSAll = lcsAll.size();
		
		List<String> stringList2 = makeStringList(mergeFile2);
		List<String> stringList3 = makeStringList(mergeFile3);		
		
		List<String> mergeList = new ArrayList<String>();		
		
		int countLCS1, countLCS2, countLCSAll, countList2, countList3;
		countLCS1 = countLCS2 = countLCSAll = countList2 = countList3 = 0;
		
		while ((countList2 < sizeList2) || (countList3 < sizeList3)) {
			// Check for end of lists
			if ((countList2 < sizeList2) && (countList3 < sizeList3)) {
				// Both lines are equal, then add line and increment counters
				if (hashList2.get(countList2).equals(hashList3.get(countList3))) {
					mergeList.add(stringList2.get(countList2));
					countLCS1++;
					countLCS2++;
					countLCSAll++;
					countList2++;
					countList3++;
				} else if ((countLCSAll < sizeLCSAll) && (lcsAll.get(countLCSAll).equals(hashList2.get(countList2)))) {
					// Line is in both LCS and file2 => Add line from file3  
					mergeList.add(stringList3.get(countList3));
					countList3++;
				} else if ((countLCSAll < sizeLCSAll) && (lcsAll.get(countLCSAll).equals(hashList3.get(countList3)))) {
					// Line is in both LCS and file3 => Add line from file2
					mergeList.add(stringList2.get(countList2));
					countList2++;
				} else if ((countLCS1 < sizeLCS1) && lcs1.get(countLCS1).equals(hashList2.get(countList2))) {
					// Line is in LCS1 and file2 => file3 changed line, add line from file3
					mergeList.add(stringList3.get(countList3));
					countList2++;
					countList3++;
					countLCS1++;
				} else if ((countLCS2 < sizeLCS2) && lcs2.get(countLCS2).equals(hashList3.get(countList3))) {
					// Line is in LCS2 and file3 => file2 changed line, add line from file2
					mergeList.add(stringList2.get(countList2));
					countList2++;
					countList3++;
					countLCS2++;
				} else {
					// Merge error, lines not mergeable => Add error message
					mergeList.add("Merge Error: File2: " + stringList2.get(countList2) + " and File3: "
							+ stringList3.get(countList3));
					mergeFailed = true;
					countList2++;
					countList3++;
				}
			} else if ((sizeList2 - countList2) > (sizeList3 - countList3)) {
				// file2 line size higher than file3 line size => Add file2 line
				mergeList.add(stringList2.get(countList2));
				countList2++;
			} else if ((sizeList3 - countList3) > (sizeList2 - countList2)) {
				// file3 line size higher than file2 line size => Add file3 line
				mergeList.add(stringList3.get(countList3));
				countList3++;
			}
		}
		return mergeList;
	}
	
	/**
	 * Creates a ArrayList with the line entries from a given file.
	 * @param file The file to convert into ArrayList
	 * @return ArrayList with the file input as line entries
	 */
	private List<String> makeStringList(File file) {
		List<String> list = new ArrayList<String>();
		
		try {
			BufferedReader inputFile = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = inputFile.readLine()) != null) {
				list.add(line);
			}
			inputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
