/*
 * Tyler Eads & Fabien Gardes-Picado
 * 2025-06-03
 * Lab 7
 */
package lab7;
import java.util.HashMap;

public class Components {
    private HashMap<Character, Integer> map = new HashMap<Character, Integer>();
    private Heap<JTree, Integer> heap = new Heap<JTree, Integer>();
    private JTree huffTree;

    public static void main(String[] args) {
        Components c = new Components();
        c.countFrequencies("aaabbc");
        c.setupTree();
        String wubba = c.decode("011");
        System.out.println(wubba);
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
    
    // task 2: building the coding tree
    public void setupTree() {
        //setup the heap
        for (Character c: map.keySet()) {
            JTree wungus = new JTree(c, map.get(c));
            heap.add(wungus, map.get(c));
        }

        //setup the coding tree
        while (heap.size() > 1) {
            JTree low1 = heap.poll();
            JTree low2 = heap.poll();
            JTree newGuy = new JTree(low1.root, low2.root);
            heap.add(newGuy, newGuy.root.frequency);
        }

        huffTree = heap.poll();
    }

    public String decode(String input) {
        return huffTree.decode(input);
    }

    public String encode(String input) {
        
    }
}
