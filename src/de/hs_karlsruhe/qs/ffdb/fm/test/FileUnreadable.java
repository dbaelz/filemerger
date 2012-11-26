package de.hs_karlsruhe.qs.ffdb.fm.test;

import java.io.File;

import org.junit.Test;

import de.hs_karlsruhe.qs.ffdb.fm.Merge;

/**
 * @author Daniel Baelz and Fabian Freimueller
 *
 */
public class FileUnreadable {
	@Test
	public void TestFileUnreadable() {
		String files[]= { "res/test/test_long.txt",  "res/test/test_long_long.txt", "res/test/test_unreadable.txt"};
		File mergeFile1 = new File(files[0]);
		File mergeFile2 = new File(files[1]);
		File mergeFile3 = new File(files[2]);
		Merge m = new Merge(mergeFile1, mergeFile2, mergeFile3);
		m.mergeFiles();
		m.getMergedList();
	}
}
