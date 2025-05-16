import java.util.*;


public class Main {	
	public static void search(HashMap<Integer, String> map, int key) {
		// START
        // ... Your code goes here ...
        if (map.containsKey(key)) {
            System.out.println(map.get(key));
        } else {
            System.out.println("Not found.");
        }

        // FINISH
		
	}
    public static void sortByName(HashMap<Integer, String> map) {
        // START
        // ... Your code goes here ...
        List<Map.Entry<Integer, String>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Comparator.comparing(Map.Entry::getValue)); // sort by name (value)

        for (Map.Entry<Integer, String> entry : entries) {
            System.out.println(entry.getKey() + "|" + entry.getValue());
        }
        // FINISH
    	
    	
    }

    public static void main(String[] args) {
        HashMap<Integer, String> testCase = new HashMap<Integer, String>();
        testCase.put(100024, "John Doe");
        testCase.put(100001, "Bob Smith");
        testCase.put(100048, "Alice Brown");

        // Print the result
        System.out.println("Search with student number 100024:");
        search(testCase, 100024);
        System.out.println("Search with student number 100000:");
        search(testCase, 100000);
        System.out.println("Sort students by their names:");
        sortByName(testCase);
    }
}