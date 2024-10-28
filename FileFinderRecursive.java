import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that provides functionality to recursively search for files in directories.
 */
public class FileFinderRecursive {
    private List<String> foundPaths;

    public FileFinderRecursive() {
        this.foundPaths = new ArrayList<>();
    }

    /**
     * Searches for a file with the specified name in the given directory and its subdirectories.
     * 
     * @param directoryPath The starting directory path for the search
     * @param fileName The name of the file to search for
     * @return List of full paths where the file was found
     * @throws IllegalArgumentException if directory path is invalid or file name is empty
     */
    public List<String> findFile(String directoryPath, String fileName) {
        // Input validation
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Directory path cannot be null or empty");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        // Clear previous results
        foundPaths.clear();
        
        // Create File object for the directory
        File directory = new File(directoryPath);
        
        // Verify directory exists and is actually a directory
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist: " + directoryPath);
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + directoryPath);
        }

        // Start recursive search
        searchRecursively(directory, fileName);
        
        return new ArrayList<>(foundPaths);
    }

    /**
     * Recursive helper method to search through directory structure.
     * 
     * @param currentDir The current directory being searched
     * @param fileName The name of the file to search for
     */
    private void searchRecursively(File currentDir, String fileName) {
        File[] files = currentDir.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().equals(fileName)) {
                    // Found a matching file, add its path to the results
                    foundPaths.add(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    // Recursively search subdirectories
                    searchRecursively(file, fileName);
                }
            }
        }
    }

    /**
     * Main method to run the file finder from command line.
     * 
     * @param args Command line arguments: directory path and file name
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java FileFinder <directory_path> <file_name>");
            System.exit(1);
        }

        String directoryPath = args[0];
        String fileName = args[1];
        FileFinderRecursive finder = new FileFinderRecursive();

        try {
            List<String> foundFiles = finder.findFile(directoryPath, fileName);
            
            if (foundFiles.isEmpty()) {
                System.out.println("File '" + fileName + "' not found in directory: " + directoryPath);
            } else {
                System.out.println("File '" + fileName + "' found at the following location(s):");
                for (String path : foundFiles) {
                    System.out.println(path);
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}