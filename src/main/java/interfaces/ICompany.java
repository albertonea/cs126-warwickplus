package interfaces;

public interface ICompany extends Comparable<ICompany> {
    public int getID();
    public String getName();
    public int compareTo(ICompany company);
    public String toString();
}
