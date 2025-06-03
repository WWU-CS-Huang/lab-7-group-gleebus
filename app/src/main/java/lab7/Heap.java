package lab7;
/*
 * Author: Fabien Gardes-Picado
 * Date: 2025-05-23
 * Purpose: Assignment 3
 */
import java.util.NoSuchElementException;

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type P. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public final class Heap<V, P extends Comparable<P>> {

    // Read and understand the class invariants given in the
    // following comment:

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually
     * accomplished using its get method. In the complete tree,
     * c[0] is the root; c[2i+1] is the left child of c[i] and c[2i+2] is the
     * right child of i.  If c[i] is not the root, then c[(i-1)/2] (using
     * integer division) is the parent of c[i].
     *
     * Class Invariants:
     *
     *   The tree is complete:
     *     1. `c[0..c.size()-1]` are non-null
     *
     *   The tree satisfies the heap property:
     *     2. if `c[i]` has a parent, then `c[i]`'s parent's priority
     *        is smaller than `c[i]`'s priority
     *
     *   In Phase 3, the following class invariant also must be maintained:
     *     3. The tree cannot contain duplicate *values*; note that dupliate
     *        *priorities* are still allowed.
     *     4. map contains one entry for each element of the heap, so
     *        map.size() == c.size()
     *     5. For each value v in the heap, its map entry contains in the
     *        the index of v in c. Thus: map.get(c[i]) = i.
     */
    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p*/
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        public String toString() {
            return value.toString();
        }
    }

    /** Add v with priority p to the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. Precondition: p is not null.
     *  In Phase 3 only:
     *  @throws IllegalArgumentException if v is already in the heap.*/
    public void add(V v, P p) throws IllegalArgumentException {
        // Write this whole method. Note that bubbleUp is not implemented,
        // so calling it will have no effect. The first tests of add, using
        // test100Add, ensure that this method maintains the class invariant in
        // cases where no bubbling up is needed.
        // When done, this should pass test100Add.
        //
        if (map.containsKey(v)) {
            throw new IllegalArgumentException();
        }
        Entry newguy = new Entry(v, p);
        c.append(newguy);

        map.put(v, size()-1);
        bubbleUp(size()-1);

        // Update this method to maintain class invariants 3-5.

    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return c.size();
    }

    /** Swap c[h] and c[k].
     *  precondition: h and k are >= 0 and < c.size() */
    protected void swap(int h, int k) {
        // When bubbling values up and down (later on), two values,
        // c[h] and c[k], will have to be swapped. In order to always get this right,
        // write this helper method to perform the swap.
        // When done, this should pass test110Swap.
        //
        //System.out.println("swapping " + c.get(h) + " at " + h + " and " + c.get(k) + " at " + k);
        
        //  make new entries for the new swaps, and replace the old ones
        Entry newH = new Entry(c.get(h).value, c.get(h).priority);
        Entry newK = new Entry(c.get(k).value, c.get(k).priority);

        c.put(h, newK);
        c.put(k, newH);
        // Change this method to additionally maintain class
        // invariants 3-5 by updating the map field.
        map.put(newK.value, h);
        map.put(newH.value, k);
    }

    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    protected void bubbleUp(int k) {
        // As you know, this method should be called within add in order
        // to bubble a value up to its proper place, based on its priority.
        // When done, this should pass test115Add_BubbleUp
        int kPar = (k-1)/2;
        // while node k's priority is smaller than it's parent's, swap up
        while (c.get(k).priority.compareTo(c.get(kPar).priority) < 0)  {
            swap(k, kPar);
            k = kPar;
            kPar = (k-1)/2;
        }
    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  @throws NoSuchElementException if the heap is empty. */
    public V peek() throws NoSuchElementException {
        // Do peek. This is an easy one.
        //         test120Peek will not find errors if this is correct.

        if (size() == 0) {
            throw new NoSuchElementException();
        } else {
            return c.get(0).value;
        }
        
        //throw new UnsupportedOperationException();
    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws NoSuchElementException if the heap is empty. */
    public V poll() throws NoSuchElementException {
        // Do poll (1.5) and bubbleDown (1.6) together. When they
        // are written correctly, testing procedures
        // test130Poll_BubbleDown_NoDups and test140testDuplicatePriorities
        // should pass. The second of these checks that entries with equal
        // priority are not swapped.

        boolean bub = true;
        // first off, throw exception if the heap is empty
        if (size() == 0) {
            throw new NoSuchElementException();
        } else if (size() == 1) { // also if it's a 1 entry heap, just pop the last thing (because it's also the first thing)
            V poppedValue = c.pop().value;
            map.remove(poppedValue);
            return poppedValue;
        } else if (size() == 2) { // if it's a 2 entry heap, bubbling is not neccesary as after we pop there will only be 1 entry
            bub = false;

        }

        //  otherwise it's business time. we need to store the element's value, replace the head with the last value, and then bubble down
        V storedValue = c.get(0).value;
        // replace the head and remove the last value
        map.remove(c.get(0).value);
        Entry lastEntry = c.pop();
        c.put(0, lastEntry);

        // bubble down 
        if (bub) {bubbleDown(0);}
        
        map.put(c.get(0).value, 0);
        

        return storedValue;
        // Update poll() to maintain class invariants 3-5.

    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    protected void bubbleDown(int k) {
        // Do poll (1.5) and bubbleDown together.  We also suggest
        //         implementing and using smallerChild, though you don't
        //         have to. left: c[2i+1] right: c[2i+2]

        // if k has no children
        if (!(hasChildren(k))) {
            //system.out.println("node " + c.get(k) + " attempted to bubble down but has no children");
            return;
        }
        int smol = smallerChild(k);
        //system.out.println("bubble down: node " + c.get(k) + " and its child "+ c.get(smol));
        P smolPrio = c.get(smol).priority;

        // while the parent's priority is greater than the smaller child's, swap down
        //while (c.get(k).priority.compareTo(smolPrio) > 0) {
        //    swap(k, smol);
        //    k = smol;
        //    smol = smallerChild(k);
        //}

        // if the parent's priority is greater than the smaller childs, swap down and recurse
        if (c.get(k).priority.compareTo(smolPrio) >= 0) {
            swap(k, smol);
            bubbleDown(smol);
        }
    }

    /** Return true if the value v is in the heap, false otherwise.
     *  The average case runtime is O(1).  */
    public boolean contains(V v) {
        // Use map to check whether the value is in the heap.
        if (map.containsKey(v)) {
            return true;
        } else {
            return false;
        }
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        // Implement this method to change the priority of node in
        // the heap.
        
        // if the value is not in the heap
        if (!(contains(v))) {
            throw new IllegalArgumentException();
        }

        // get the position and the old priority
        int position = map.get(v);
        P oldPrio = c.get(position).priority;

        // change the entry the list and the map
        Entry replacement = new Entry(v, p);
        c.put(position, replacement);
        map.put(v, position);

        // do the corresponding bubble
        if (p.compareTo(oldPrio) > 0) { // if the priority got bigger, bubble down
            bubbleDown(position);
        } else if (p.compareTo(oldPrio) < 0) { // if the priority got smaller, bubble up
            bubbleUp(position);
        } // if the priority is the same, don't bubble
    }

    /* Check if the node c[k] has any existing children, for the purpose
     * of then calling smallerChild() with its precondition of an existing child*/
    private boolean hasChildren(int k) {
        int leftChild = (k*2)+1;
        int rightChild = (k*2)+2;
        boolean leftExist = true;
        boolean rightExist = true;

        //check if the left child exists
        try {
            c.get(leftChild);
        } catch (ArrayIndexOutOfBoundsException e) {
            leftExist = false;
        }
        
        //check if the right child exists
        try {
            c.get(rightChild);
        } catch (ArrayIndexOutOfBoundsException e) {
            rightExist = false;
        } 

        return (leftExist || rightExist);
    }  

    // Recommended helper method spec:
    /* Return the index of the child of k with smaller priority.
     * if only one child exists, return that child's index
     * Precondition: at least one child exists.*/
    private int smallerChild(int k) {

        int leftChild = (k*2)+1;
        int rightChild = (k*2)+2;
        int smallestChild;

        //system.out.println(c.get(k).value);

        // attempt to get the priority of the left child. if you get an exception, that means 
        // the left child doesn't exist, so the smallest must be the right child
        try {
            P lPrio = c.get(leftChild).priority;
        } catch (ArrayIndexOutOfBoundsException e) {
            //system.out.println("roingus");
            return rightChild;
        }

        // same thing but with right child
        try {
            P rPrio = c.get(rightChild).priority;
        } catch (ArrayIndexOutOfBoundsException e) {
            //system.out.println("loingus");
            return leftChild;
        }

        // if the left child is smaller than the right child, it's the smallest
        if (c.get(leftChild).priority.compareTo(c.get(rightChild).priority) < 0) {
            smallestChild = leftChild;
        } else {  //otherwise, if they're equal or right is smaller, right is smallest
            smallestChild = rightChild;
        }
    
        return smallestChild;
    }

}
