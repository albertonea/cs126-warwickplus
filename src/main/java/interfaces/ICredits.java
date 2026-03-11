package interfaces;

import stores.CastCredit;
import stores.CrewCredit;
import stores.Person;

public interface ICredits{
    public boolean add(CastCredit[] cast, CrewCredit[] crew, int filmID);
    public boolean remove(int filmID);
    
    public CastCredit[] getFilmCast(int filmID);
    public CrewCredit[] getFilmCrew(int filmID);

    public Person getCast(int castID);
    public Person getCrew(int crewID);
    public int[] getCastFilms(int castID);
    public int[] getCrewFilms(int crewID);
    public int[] getCastStarsInFilms(int castID);

    public Person[] getMostCastCredits(int numResults);
    public int getNumCastCredits(int castID);

    public int sizeOfCast(int filmID);
    public int sizeOfCrew(int filmID);

    public Person[] getUniqueCast();
    public Person[] getUniqueCrew();

    public Person[] findCast(String searchTerm);
    public Person[] findCrew(String searchTerm);

    public int size();
}