/* Fabien Gardes-Picado
 * 2025-05-25
 * Assignment 3*/

package lab7;

/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        // get our hash value for our target bucket
        int kHash = key.hashCode();
        int targetBucket = Math.floorMod(kHash, getCapacity());

        if (buckets[targetBucket] == null) {
            return null;
        } else {
            // look through the chain of buckets
            Pair bucketToCheck = buckets[targetBucket];
            while (bucketToCheck != null) { 
                if (bucketToCheck.key == key) {
                    return bucketToCheck.value;
                }

                bucketToCheck = bucketToCheck.next;
            }
            return null;
        }

    }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Precondition: val is not null.
     * Runtime: average case O(1); worst case O(size + a.length)*/
    public V put(K key, V val) {
        //   do this together with get. For now, don't worry about growing the
        //   array and rehashing.
        //   Tips:
        //     - Use the key's hashCode method to find which bucket it belongs in.
        //     - It's possible for hashCode to return a negative integer.
        //System.out.println("Putting " + val + " at " + key);
        
        // get our hash value for our target bucket
        int kHash = key.hashCode();
        int bucketPos = Math.floorMod(kHash, getCapacity());

        //System.out.println("Key is " + key + ", kHash is " + kHash + ", buckets.length is " + buckets.length + ", bucketPos is " + bucketPos);

        //System.out.println("Actually putting " + val + " at bucket " + bucketPos);
        // look to see if we're making a new bucket or not
        if (buckets[bucketPos] != null) {
            //System.out.println("non-null bucket");

            // look through all the buckets in the chain. if we find an exact key match, replace the value
            // otherwise, add another bucket chained at the end
            Pair bucketToCheck = buckets[bucketPos];
            Pair endBucket = bucketToCheck;

            // look through every bucket
            while (bucketToCheck != null) {
                // if we find an exact key match: hold on to the old value, and return it after changing
                if (bucketToCheck.key == key) {
                    V retValue = bucketToCheck.value;
                    bucketToCheck.value = val;
                    growIfNeeded();
                    return retValue;
                }
                endBucket = bucketToCheck;
                bucketToCheck = bucketToCheck.next;
            }
            // if we've gotten to the last bucket and haven't found the exact match, we chain

            // get the old value
            V retValue = buckets[bucketPos].value;
            // make a new bucket that will be chained into by the existing bucket
            Pair bucket = new Pair(key, val);
            endBucket.next = bucket;
            size++;
            // return new value
            growIfNeeded();
            return retValue;

        } else { // we need to make a new bucket, and return null
            //System.out.println("null bucket");

            Pair bucket = new Pair(key, val);
            buckets[bucketPos] = bucket;
            size++;
            growIfNeeded();
            return null;
        }
        
    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {

        int kHash = key.hashCode();
        int targetBucket = Math.floorMod(kHash, getCapacity());

        // if the hash bucket is empty, there is no mapping
        if (buckets[targetBucket] == null) {
            return false;
        } else {
            Pair bucketToCheck = buckets[targetBucket];
            // look through every bucket 
            while (bucketToCheck != null) {
                // if we find an exact key match: return true
                if (bucketToCheck.key == key) {
                    return true;
                }
                bucketToCheck = bucketToCheck.next;
            }
            // if we never found an exact key match, return false
            return false;
        }

    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key) {
        int kHash = key.hashCode();
        int targetBucket = Math.floorMod(kHash, getCapacity());

        // if the hash bucket is empty, there is no mapping
        if (buckets[targetBucket] == null) {
            return null;
        } else {
            Pair bucketToCheck = buckets[targetBucket];
            // look through every bucket 
            while (bucketToCheck != null) {
                // if we find an exact key match: remove the mapping and return the old value
                if (bucketToCheck.key == key) {
                    V retValue = bucketToCheck.value;
                    size--;
                    
                    // if this bucket was the first in its hash code, remove it and make its next into the new first
                    if (buckets[targetBucket] == bucketToCheck) {
                        buckets[targetBucket] = bucketToCheck.next;
                        bucketToCheck = null;
                        
                    } else { // otherwise if this bucket was chained to, redo the chain until 1 before and skip it
                        Pair bucketPreSkip = buckets[targetBucket];
                        while (bucketPreSkip.next != bucketToCheck) {
                            bucketPreSkip = bucketPreSkip.next;
                        }
                        bucketPreSkip.next = bucketToCheck.next;
                    }

                    return retValue;
                }
                bucketToCheck = bucketToCheck.next;
            }
            // if we never found an exact key match, return null
            return null;
        }
    }


    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the capacity 
     * and rehash values from the old array to the new array */
    private void growIfNeeded() {
        double loadFactor = (double)getSize() / (double)getCapacity();
        //System.out.println("Load factor is " + loadFactor + " (" + getSize() + "/" + getCapacity() + ")");

        // do nothing if the load factor is less than 0.8
        if (loadFactor < 0.8) {
            return;
        } else {
            // if the load factor is too large, create a new array with doubled capacity
            Pair[] newBuckets = createBucketArray(getCapacity()*2);
            // check every bucket in the old array and put its values into the new array
            for (int i = 0; i < getCapacity(); i++) {
                // while this bucket has anything in it, remove it and rehash it into the new array
                while (buckets[i] != null) {
                    K keyToAdd = buckets[i].key;
                    V valToAdd = remove(keyToAdd);
                    
                    int kHash = keyToAdd.hashCode();
                    int bucketPos = Math.floorMod(kHash, newBuckets.length);

                    // look to see if we're making a new bucket or not
                    if (newBuckets[bucketPos] != null) {
                        //System.out.println("non-null bucket");

                        // look through all the buckets in the chain. we know there's no duplicates so just add it at the end
                        // otherwise, add another bucket chained at the end
                        Pair bucketToCheck = newBuckets[bucketPos];
                        Pair endBucket = bucketToCheck;
                        while (bucketToCheck != null) {
                            endBucket = bucketToCheck;
                            bucketToCheck = bucketToCheck.next;
    
                        }

                        // make a new bucket that will be chained into by the existing bucket
                        Pair bucket = new Pair(keyToAdd, valToAdd);
                        size++;
                        endBucket.next = bucket;

                    } else { // we need to make a new bucket, and return null
                        //System.out.println("null bucket");

                        Pair bucket = new Pair(keyToAdd, valToAdd);
                        size++;
                        newBuckets[bucketPos] = bucket;
                    }
                } 
            }
            this.buckets = newBuckets;
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < getCapacity(); i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
