package structures;

public class ListElement<E> {
    private final E value;
    private ListElement<E> next;
    private ListElement<E> prev;
    
    public ListElement(E value) {
        this.value = value;
    }
    
    public E getValue() {
        return this.value;
    }
    
    public ListElement<E> getNext() {
        return this.next;
    }
    
    public ListElement<E> getPrev() {
        return this.prev;
    }
    
    public void setNext(ListElement<E> e) {
        this.next = e;
    }
    
    public void setPrev(ListElement<E> e) {
        this.prev = e;
    }

}
