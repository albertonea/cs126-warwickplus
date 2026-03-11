package structures.interfaces;

import structures.LinkedList;

import java.util.Iterator;

/**
 * An interface for a generic list.
 */
public interface List<E> extends Iterable<E> {

    // Adds element to the list, returns true on success and false otherwise.
    public boolean add(E element);
        
    // Clears (empties) the list.
    public void clear();
    
    // Returns true when element is in the list, false otherwise.
    public boolean contains(E element);
    
    // Returns true when the list contains no elements.
    public boolean isEmpty();
    
    // Removes an element form the list. 
    // Returns true on success, false if the element was not found.
    public boolean remove(E element);
    
    // Returns the number of elements stored in the list.
    public int size();
    
    // Returns the element stored at position index.
    public E get(int index);
    
    // Returns the index of element in the list, returns -1 if element was not found.
    public int indexOf(E element);
    
    // Sets position index of the list to element.
    public E set(int index, E element);

    public E[] toArray();

    public LinkedList<E> copy();

    public Iterator<E> iterator();
}
