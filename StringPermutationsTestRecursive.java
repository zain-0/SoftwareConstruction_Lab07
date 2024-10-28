import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class StringPermutationsTestRecursive {
    private StringPermutationsRecursive permutations;
    
    @Before
    public void setUp() {
        permutations = new StringPermutationsRecursive();
    }
    
    @Test
    public void testEmptyString() {
        List<String> result = permutations.generatePermutations("");
        assertEquals(1, result.size());
        assertEquals("", result.get(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullString() {
        permutations.generatePermutations(null);
    }
    
    @Test
    public void testSingleCharacter() {
        List<String> result = permutations.generatePermutations("a");
        assertEquals(1, result.size());
        assertEquals("a", result.get(0));
    }
    
    @Test
    public void testTwoCharacters() {
        List<String> result = permutations.generatePermutations("ab");
        assertEquals(2, result.size());
        assertTrue(result.contains("ab"));
        assertTrue(result.contains("ba"));
    }
    
    @Test
    public void testThreeCharacters() {
        List<String> result = permutations.generatePermutations("abc");
        assertEquals(6, result.size());
        String[] expected = {"abc", "acb", "bac", "bca", "cab", "cba"};
        for (String perm : expected) {
            assertTrue(result.contains(perm));
        }
    }
    
    @Test
    public void testDuplicateCharacters() {
        List<String> result = permutations.generatePermutations("aaa");
        assertEquals(1, result.size());
        assertEquals("aaa", result.get(0));
    }
    
    @Test
    public void testSomeDuplicates() {
        List<String> result = permutations.generatePermutations("aba");
        assertEquals(3, result.size());
        assertTrue(result.contains("aba"));
        assertTrue(result.contains("aab"));
        assertTrue(result.contains("baa"));
    }
    
    @Test
    public void testBothImplementationsMatchingResults() {
        String input = "abcd";
        List<String> result1 = permutations.generatePermutations(input);
        List<String> result2 = permutations.generatePermutationsWithSwap(input);
        
        assertEquals(result1.size(), result2.size());
        Set<String> set1 = new HashSet<>(result1);
        Set<String> set2 = new HashSet<>(result2);
        assertEquals(set1, set2);
    }
    
    @Test
    public void testPerformanceAnalysis() {
        String input = "abcd";
        StringPermutationsRecursive.PerformanceResult result = permutations.analyzePerformance(input);
        
        assertEquals(24, result.permutationCount); // 4! = 24
        assertTrue(result.standardTime >= 0);
        assertTrue(result.swapBasedTime >= 0);
    }
    
    @Test
    public void testLargeInput() {
        String input = "abcdefgh"; // 8! = 40320 permutations
        List<String> result = permutations.generatePermutations(input);
        assertEquals(40320, result.size());
    }
}