package interfaces;
import java.lang.Comparable;

// Information about the role a cast member played in a film, and the cast member
public interface ICastCredit extends Comparable<ICastCredit> {
    public int getElementID();

    public String getCharacter();

    public String getCreditID();

    public int getID();

    public String getName();

    public int getOrder();
    public void setOrder(int order);

    public String getProfilePath();
    public void setProfilePath(String profilePath);

    public int compareTo(ICastCredit o);

    public String toString();

}
