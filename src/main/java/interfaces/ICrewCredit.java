package interfaces;
import java.lang.Comparable;

// Information about the role a crew member played in the production of a film, 
// and information on the crew member
public interface ICrewCredit extends Comparable<ICrewCredit> {

    public String getElementID();

    public String getDepartment();

    public int getID();

    public String getJob();

    public String getName();

    public String getProfilePath();
    public void setProfilePath(String profilePath);

    public int compareTo(ICrewCredit o);

    public String toString();
}
