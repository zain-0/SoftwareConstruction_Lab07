import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Test class for FileFinder using JUnit 4
 * Tests various scenarios including:
 * - Finding files in root directory
 * - Finding files in subdirectories
 * - Handling missing files
 * - Error cases and edge conditions
 */
public class FileFinderTestRecursive {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private FileFinderRecursive finder;
    private File rootDir;

    @Before
    public void setUp() throws IOException {
        finder = new FileFinderRecursive();
        rootDir = tempFolder.getRoot();
        
        // Create a test directory structure
        createTestDirectoryStructure();
    }

    /**
     * Creates a test directory structure with various files and subdirectories
     */
    private void createTestDirectoryStructure() throws IOException {
        // Create files in root directory
        tempFolder.newFile("test1.txt");
        tempFolder.newFile("test2.doc");
        
        // Create subdirectory structure
        File subDir1 = tempFolder.newFolder("subdir1");
        File subDir2 = tempFolder.newFolder("subdir2");
        File subSubDir = new File(subDir1, "subsubdir");
        subSubDir.mkdir();
        
        // Create files in subdirectories
        new File(subDir1, "test1.txt").createNewFile();
        new File(subDir2, "test3.txt").createNewFile();
        new File(subSubDir, "test1.txt").createNewFile();
    }

    @Test
    public void testFindFileInRootDirectory() {
        List<String> results = finder.findFile(rootDir.getAbsolutePath(), "test2.doc");
        
        assertEquals("Should find exactly one file", 1, results.size());
        assertTrue("File path should contain test2.doc", 
            results.get(0).endsWith("test2.doc"));
    }

    @Test
    public void testFindMultipleFiles() {
        List<String> results = finder.findFile(rootDir.getAbsolutePath(), "test1.txt");
        
        assertEquals("Should find exactly three files", 3, results.size());
        assertTrue("All paths should end with test1.txt", 
            results.stream().allMatch(path -> path.endsWith("test1.txt")));
    }

    @Test
    public void testFileNotFound() {
        List<String> results = finder.findFile(rootDir.getAbsolutePath(), "nonexistent.txt");
        
        assertTrue("Result should be empty for non-existent file", results.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDirectoryPath() {
        finder.findFile(null, "test.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDirectoryPath() {
        finder.findFile("", "test.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFileName() {
        finder.findFile(rootDir.getAbsolutePath(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFileName() {
        finder.findFile(rootDir.getAbsolutePath(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonExistentDirectory() {
        finder.findFile("/this/directory/does/not/exist", "test.txt");
    }

    @Test
    public void testFindFileInDeepDirectory() throws IOException {
        // Create a deeply nested directory structure
        File deepDir = tempFolder.newFolder("deep1", "deep2", "deep3");
        File targetFile = new File(deepDir, "deeptest.txt");
        targetFile.createNewFile();

        List<String> results = finder.findFile(rootDir.getAbsolutePath(), "deeptest.txt");
        
        assertEquals("Should find exactly one file", 1, results.size());
        assertTrue("File path should contain the deep directory structure",
            results.get(0).contains("deep1" + File.separator + "deep2" + File.separator + "deep3"));
    }

    @Test
    public void testSearchWithSpecialCharacters() throws IOException {
        // Create a file with special characters in name
        File specialFile = new File(rootDir, "test$#@!.txt");
        specialFile.createNewFile();

        List<String> results = finder.findFile(rootDir.getAbsolutePath(), "test$#@!.txt");
        
        assertEquals("Should find exactly one file", 1, results.size());
        assertTrue("Should find file with special characters",
            results.get(0).endsWith("test$#@!.txt"));
    }

    @Test
    public void testCaseSensitivity() throws IOException {
        // Create files with different cases
        File lowerCase = new File(rootDir, "testcase.txt");
        File upperCase = new File(rootDir, "TESTCASE.TXT");
        lowerCase.createNewFile();
        upperCase.createNewFile();

        List<String> resultsLower = finder.findFile(rootDir.getAbsolutePath(), "testcase.txt");
        List<String> resultsUpper = finder.findFile(rootDir.getAbsolutePath(), "TESTCASE.TXT");
        
        assertEquals("Should find exactly one lowercase file", 1, resultsLower.size());
        assertEquals("Should find exactly one uppercase file", 1, resultsUpper.size());
        assertFalse("Should find different files", 
            resultsLower.get(0).equals(resultsUpper.get(0)));
    }

    @Test
    public void testEmptyDirectory() throws IOException {
        File emptyDir = tempFolder.newFolder("empty");
        
        List<String> results = finder.findFile(emptyDir.getAbsolutePath(), "test.txt");
        
        assertTrue("Should return empty list for empty directory", results.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchInFile() throws IOException {
        // Create a file and try to search within it
        File testFile = tempFolder.newFile("notADirectory.txt");
        finder.findFile(testFile.getAbsolutePath(), "test.txt");
    }

    @Test
    public void testDirectoryWithNoReadPermission() throws IOException {
        // Create a directory with no read permission
        File restrictedDir = tempFolder.newFolder("restricted");
        File testFile = new File(restrictedDir, "test.txt");
        testFile.createNewFile();
        restrictedDir.setReadable(false);

        List<String> results = finder.findFile(rootDir.getAbsolutePath(), "test.txt");
        // Should not throw exception, should just skip unreadable directory
        assertNotNull("Should return non-null result even with permission error", results);
    }

    @After
    public void tearDown() {
        // Restore permissions for cleanup
        if (rootDir != null && rootDir.exists()) {
            makeReadable(rootDir);
        }
    }

    private void makeReadable(File file) {
        file.setReadable(true);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    makeReadable(child);
                }
            }
        }
    }
}