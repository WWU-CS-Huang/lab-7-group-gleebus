/*
 * Tyler Eads & Fabien Gardes-Picado
 * 2025-06-03
 * Lab 7
 */
package lab7;

public class Components {
    private HashTable<Character, Integer> map = new HashTable<Character, Integer>();
    private Heap<JTree, Integer> heap = new Heap<JTree, Integer>();

    public static void main(String[] args) {
        Components c = new Components();
        c.countFrequencies("aaabbc");
        c.map.dump();
    }

    // count frequencies. we add all the characters and their corresponding 
    // frequencies into a hash map which will then be added into a heap 
    // precondition: input is not null
    public void countFrequencies(String input) {
        if (input.length() == 0) {
            return;
        }

        char first = input.charAt(0);
        
        if (map.containsKey(first)) {
            map.put(first, map.get(first)+1);
        } else {
            map.put(first, 1);
        }

        countFrequencies(input.substring(1));
    }
}
