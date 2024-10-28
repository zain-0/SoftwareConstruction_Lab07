import java.util.List;
import java.util.Scanner;

/**
 * Command Line Interface for the String Permutations generator.
 * Provides an interactive way to generate and analyze string permutations.
 */
public class StringPermutationsInteractive {
    private static final StringPermutationsRecursive permutations = new StringPermutationsRecursive();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
           // Interactive mode
           runInteractiveMode();

    }

    /**
     * Runs the interactive mode with a menu-driven interface
     */
    private static void runInteractiveMode() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        generatePermutationsInteractive();
                        break;
                    case "2":
                        System.out.println("Thank you for using String Permutations Generator!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Prints the main menu options
     */
    private static void printMenu() {
        System.out.println("\n=== String Permutations Generator ===");
        System.out.println("1. Generate Permutations");
        System.out.println("2. Exit");
        System.out.print("Enter your choice (1-2): ");
    }

    /**
     * Handles interactive permutation generation
     */
    private static void generatePermutationsInteractive() {
        System.out.print("Enter a string to generate permutations: ");
        String input = scanner.nextLine().trim();
        
        if (input.length() > 8) {
            System.out.print("Warning: This will generate " + factorial(input.length()) + 
                           " permutations. Continue? (y/n): ");
            if (!scanner.nextLine().trim().toLowerCase().startsWith("y")) {
                return;
            }
        }
        
        System.out.print("Show all permutations? (y/n): ");
        boolean showAll = scanner.nextLine().trim().toLowerCase().startsWith("y");
        
        generateAndPrint(input, showAll);
    }

    /**
     * Generates and prints permutations with optional detail level
     */
    private static void generateAndPrint(String input, boolean showAll) {
        System.out.println("\nGenerating permutations for: \"" + input + "\"");
        
        long startTime = System.currentTimeMillis();
        List<String> results = permutations.generatePermutations(input);
        long endTime = System.currentTimeMillis();
        
        System.out.println("Generated " + results.size() + " permutations");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        
        if (showAll || results.size() <= 20) {
            System.out.println("\nPermutations:");
            for (int i = 0; i < results.size(); i++) {
                System.out.printf("%4d: %s%n", i + 1, results.get(i));
            }
        } else {
            System.out.println("\nFirst 10 permutations:");
            for (int i = 0; i < 10; i++) {
                System.out.printf("%4d: %s%n", i + 1, results.get(i));
            }
            System.out.println("... and " + (results.size() - 10) + " more");
        }
    }


    /**
     * Calculate factorial for warning messages
     */
    private static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}