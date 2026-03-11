package interfaces;

import java.lang.Comparable;

public interface IKeyword extends Comparable<IKeyword>{

    public int getID();

    public String getName();

    public int compareTo(IKeyword o);

    public String toString();
    
}
