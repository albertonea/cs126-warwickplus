package structures.data.interfaces;

/**
 * An interface for a generic list.
 */
public interface List<E> extends Collection<E> {
    // Returns the element stored at position index.
    public E get(int index);
    
    // Returns the index of element in the list, returns -1 if element was not found.
    public int indexOf(E element);
    
    // Sets position index of the list to element.
    public E set(int index, E element);

    public List<E> clone();
}
