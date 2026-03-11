package interfaces;

public interface IGenre extends Comparable<IGenre>{
    public int getID();
    public String getName();
    public int compareTo(IGenre genre);
    public String toString();
}
