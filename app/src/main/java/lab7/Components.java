/*
 * Tyler Eads & Fabien Gardes-Picado
 * 2025-06-03
 * Lab 7
 */
package lab7;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Components {
    private HashMap<Character, Integer> map = new HashMap<Character, Integer>();
    private Heap<JTree, Integer> heap = new Heap<JTree, Integer>();
    private JTree huffTree;

    public static void main(String[] args) throws FileNotFoundException{
        /*
            Components c = new Components();
            c.countFrequencies("aaabbc");
            c.setupTree();
            String wubba = c.decode("011110");
            System.out.println("decoding for \"011110\" is " + wubba);
            String gungus = c.encode("cab");
            System.out.println("encoding for \"cab\" is " + gungus);

            String badDecode = c.decode("17");
            System.out.println("Output when given a bad bitstring (17) to decode is: " + badDecode);

            String badEncode = c.encode("k");
            System.out.println("Output when given a bad string (k) to encode is: " + badEncode);
        */

        
        Components c = new Components();

        Scanner sc = new Scanner(new File("sample.txt"));

        String input = sc.nextLine();
        //String input = "i love making files it's so fun";

        c.countFrequencies(input);
        c.setupTree();
        String encodedInput = c.encode(input);
        String decodedEncode = c.decode(encodedInput);

        if (input.length() < 100) {
            System.out.println("Input string: " + input);
            System.out.println("Encoded bitstring: " + encodedInput);
            System.out.println("Decoding of the encoded bitstring: " + decodedEncode);
       }
       
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
        huffTree.buildMap();
    }

    public String decode(String input) {
        return huffTree.decode(input);
    }

    public String encode(String input) {
        return huffTree.encode(input);
    }
}
