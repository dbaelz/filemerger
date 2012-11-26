package de.hs_karlsruhe.qs.ffdb.fm.test;

import org.junit.Test;

import de.hs_karlsruhe.qs.ffdb.fm.Main;

/**
 * @author Daniel Baelz and Fabian Freimueller
 *
 */
public class FileErros {
	
	@Test
	public void TestWithFilesError() {
		String files[]= { "res/test/V.txt",  "res/test/V1.txt", "res/test/V2.txt"};
		Main.main(files);
	}
	
	@Test
	public void TestWithFiles() {
		String files[]= { "res/test/V.txt",  "res/test/V1.txt", "res/test/V1.txt"};
		Main m = new Main();
		Main.main(files);
	}
	
	@Test
	public void TestFilesNotFound() {
		String files[]= { "", "", ""};
		Main.main(files);
	}
	
	@Test
	public void TestToManyFiles() {
		String files[]= { "", "", "", ""};
		Main.main(files);
	}
}
