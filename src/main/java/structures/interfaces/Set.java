package structures.interfaces;

import structures.LinkedList;

public interface Set<E> {
    
    // Adds element to the list when it does not already exist.
    // Returns true on success and false otherwise.
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

    // Returns the array representation of the set
    public E[] toArray();

    public LinkedList<E> toLinkedList();
}