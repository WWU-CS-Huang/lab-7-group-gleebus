/* Fabien Gardes-Picado
 * 2025-05-22
 * Assignment 3*/

package lab7;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/** An ArrayList-like dynamic array class that allocates
* new memory when needed */
public class AList<T> {

  protected int size; // number of elements in the AList
  protected T[] a; // the backing array storage

  public int size() {
    return size;
  }

  protected int getCap() {
    return a.length;
  }

  /** Creates an AList with a default capacity of 8 */
  public AList() {
    a = createArray(8);
    size = 0;
  }

  /** Creates an AList with the given capacity */
  public AList(int capacity) {
    a = createArray(capacity);
    size = 0;
  }

  /* Grows a to double its current capacity if newSize exceeds a's capacity. Does
  * nothing if newSize <= a.length.  Grow the array by allocating a new array
  * and copying the old array's contents into the new one. This does *not*
  * change the AList's size. */
  protected void growIfNeeded(int newSize) {
    //see how many times we need to double
    int targetdouble = 2;
    while (a.length*targetdouble < newSize) {
      targetdouble = targetdouble*2;
    }

    // make a new temp array with the (multiple) doubled size
    T[] b = createArray(a.length*targetdouble);
    // copy everything from the old array into the temp array
    for (int i=0; i < size; i++) {
      b[i] = a[i];
    }

    // set the temp array as the real array, and its size
    a = b;
    size = newSize;
  }

  /** Resizes the AList.
  *  this *does* modify the size, and may modify the capacity if newsize
  *  exceeds capacity. */
  public void resize(int newsize) {
    //System.out.println("new: " + newsize + " old: " + size + " current cap: " + a.length);

    // grow if too big
    if (newsize > a.length) {
      growIfNeeded(newsize);
    }
    // resize
    size = newsize;
  }

  /** Gets element i from AList.
  * @throws ArrayIndexOutOfBoundsException if 0 <= i < size does not hold */
  public T get(int i) {
    if ((i >= 0) && (i < size)) {
      return a[i];
    } else {
      throw new ArrayIndexOutOfBoundsException();
    }
  }

  /** Sets the ith element of the list to value.
  * @throws ArrayIndexOutOfBoundsException if 0 <= i < size does not hold */
  public void put(int i, T value) {
    if ((i >= 0) && (i < size)) {
      a[i] = value;
    } else {
      throw new ArrayIndexOutOfBoundsException();
    }
  }

  /** Appends value at the end of the AList, increasing size by 1.
  * Grows the array if needed to fit the appended value */
  public void append(T value) {
    // TODO 4a
    // increase the size by 1, doubling capacity if needed
    if (size+1 > a.length) {
      growIfNeeded(size+1);
    } else {
      size++;
    }
    a[size-1] = value;
  }

  /** Removes and returns the value at the end of the AList.
  *  this *does* modify size and cannot modify capacity.
  *  @throws NoSuchElementException if size == 0*/
  public T pop() {
    // TODO 4b
    if (size == 0) {
      throw new NoSuchElementException();
    }
    T val = a[size-1];
    a[size-1] = null;
    size--;

    return val;
    //throw new UnsupportedOperationException(); // delete this once implemented!
  }

  /*  Create and return a T[] of size n.
  *  This is necessary because generics and arrays don't play well together.*/
  @SuppressWarnings("unchecked")
  protected T[] createArray(int size) {
    return (T[]) new Object[size];
  }

}
