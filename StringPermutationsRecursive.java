import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A utility class that generates all possible permutations of a given string.
 * Time Complexity: O(n!), where n is the length of the input string
 * Space Complexity: O(n!) to store all permutations
 */
public class StringPermutationsRecursive {
    
    /**
     * Generates all permutations of the input string.
     * 
     * @param input The string for which to generate permutations
     * @return List of all possible permutations
     * @throws IllegalArgumentException if input is null
     */
    public List<String> generatePermutations(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        // Use Set to handle strings with duplicate characters
        Set<String> permutations = new HashSet<>();
        
        // Handle base cases
        if (input.isEmpty()) {
            permutations.add("");
            return new ArrayList<>(permutations);
        }
        
        if (input.length() == 1) {
            permutations.add(input);
            return new ArrayList<>(permutations);
        }
        
        // Start recursive permutation generation
        generatePermutationsHelper("", input, permutations);
        
        return new ArrayList<>(permutations);
    }
    
    /**
     * Recursive helper method to generate permutations.
     * 
     * @param prefix Current built permutation prefix
     * @param remaining Remaining characters to be permuted
     * @param result Set to store all permutations
     */
    private void generatePermutationsHelper(String prefix, String remaining, Set<String> result) {
        int n = remaining.length();
        
        // Base case: when no characters are left to permute
        if (n == 0) {
            result.add(prefix);
            return;
        }
        
        // Try each character as the next character in the permutation
        for (int i = 0; i < n; i++) {
            generatePermutationsHelper(
                prefix + remaining.charAt(i),
                remaining.substring(0, i) + remaining.substring(i + 1, n),
                result
            );
        }
    }
    
    /**
     * Alternative implementation using character swapping.
     * This method modifies the character array in place.
     * 
     * @param input The string for which to generate permutations
     * @return List of all possible permutations
     * @throws IllegalArgumentException if input is null
     */
    public List<String> generatePermutationsWithSwap(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        Set<String> permutations = new HashSet<>();
        generatePermutationsWithSwapHelper(input.toCharArray(), 0, permutations);
        return new ArrayList<>(permutations);
    }
    
    /**
     * Recursive helper method for swap-based permutation generation.
     * 
     * @param chars Character array being permuted
     * @param start Starting index for current permutation
     * @param result Set to store all permutations
     */
    private void generatePermutationsWithSwapHelper(char[] chars, int start, Set<String> result) {
        if (start == chars.length) {
            result.add(new String(chars));
            return;
        }
        
        for (int i = start; i < chars.length; i++) {
            // Swap characters
            swap(chars, start, i);
            // Recursively generate permutations for remaining characters
            generatePermutationsWithSwapHelper(chars, start + 1, result);
            // Backtrack by swapping back
            swap(chars, start, i);
        }
    }
    
    /**
     * Helper method to swap characters in an array.
     */
    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }
    
    /**
     * Performance analysis method to compare different implementations.
     * 
     * @param input Test string
     * @return PerformanceResult containing timing information
     */
    public PerformanceResult analyzePerformance(String input) {
        long startTime, endTime;
        
        // Test first implementation
        startTime = System.nanoTime();
        List<String> result1 = generatePermutations(input);
        endTime = System.nanoTime();
        long time1 = endTime - startTime;
        
        // Test swap-based implementation
        startTime = System.nanoTime();
        List<String> result2 = generatePermutationsWithSwap(input);
        endTime = System.nanoTime();
        long time2 = endTime - startTime;
        
        return new PerformanceResult(
            result1.size(),
            time1 / 1_000_000.0, // Convert to milliseconds
            time2 / 1_000_000.0
        );
    }
    
    /**
     * Inner class to hold performance analysis results.
     */
    public static class PerformanceResult {
        public final int permutationCount;
        public final double standardTime;
        public final double swapBasedTime;
        
        public PerformanceResult(int count, double time1, double time2) {
            this.permutationCount = count;
            this.standardTime = time1;
            this.swapBasedTime = time2;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Generated %d permutations\nStandard implementation: %.2f ms\nSwap-based implementation: %.2f ms",
                permutationCount, standardTime, swapBasedTime
            );
        }
    }
}