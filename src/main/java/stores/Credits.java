package stores;

import interfaces.ICredits;
import structures.credits.Cast;
import structures.credits.Crew;
import structures.credits.FilmCredits;
import structures.data.*;
import structures.data.interfaces.List;

import java.util.ArrayList;
import java.util.Comparator;

public class Credits implements ICredits{
    Stores stores;

    // Film-centric: pre-sorted for O(1) retrieval
    private final HashMap<Integer, FilmCredits> filmCredits = new HashMap<>();

    // Cast lookup by ID
    private final HashMap<Integer, Cast> castMap = new HashMap<>();

    // Crew lookup by ID
    private final HashMap<Integer, Crew> crewMap = new HashMap<>();


    // N-gram string search indexes, keyed by person ID (not Person object)
    // so stale entries after remove() are safely filtered via the person maps
    private final StringSearchIndex<Integer> castSearchIndex = new StringSearchIndex<>();
    private final StringSearchIndex<Integer> crewSearchIndex = new StringSearchIndex<>();

    /**
     * The constructor for the Credits data store. This is where you should
     * initialise your data structures.
     * 
     * @param stores An object storing all the different key stores, 
     *               including itself
     */
    public Credits (Stores stores) {
        this.stores = stores;
        // TODO Add initialisation of data structure here
    }

    /**
     * Adds data about the people who worked on a given film. The movie ID should be
     * unique
     * 
     * @param cast An array of all cast members that starred in the given film
     * @param crew An array of all crew members that worked on a given film
     * @param id   The (unique) movie ID
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(CastCredit[] cast, CrewCredit[] crew, int id) {
        if (filmCredits.containsKey(id)) return false;

        // --- Cast: sort by order, store ---
        CastCredit[] sortedCast = cast.clone();
        MergeSort.sort(sortedCast, Comparator.comparingInt(CastCredit::getOrder));

        CrewCredit[] sortedCrew = crew.clone();
        MergeSort.sort(sortedCrew, Comparator.comparingInt(CrewCredit::getID));

        filmCredits.put(id, new FilmCredits(sortedCast, sortedCrew));

        for (CastCredit cc : sortedCast) {
            int castID = cc.getID();

            if (!castMap.containsKey(castID)) {
                castMap.put(castID, new Cast(new Person(castID, cc.getName(), cc.getProfilePath()), id, cc.getOrder()));
                castSearchIndex.add(castID, cc.getName());
            } else {
                Cast castMember = castMap.get(castID);
                castMember.incrementCredit();
                castMember.addFilm(id, cc.getOrder());
            }
        }

        for (CrewCredit cc : crew) {
            int crewID = cc.getID();

            if (!crewMap.containsKey(crewID)) {
                crewMap.put(crewID, new Crew(new Person(crewID, cc.getName(), cc.getProfilePath()), id));
                crewSearchIndex.add(crewID, cc.getName());
            } else {
                Crew crewMember = crewMap.get(crewID);
                crewMember.addFilm(id);
            }
        }

        return true;
    }

    /**
     * Remove a given films data from the data structure
     * 
     * @param id The movie ID
     * @return TRUE if the data was removed, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        FilmCredits credits = filmCredits.remove(id);
        if (credits == null) return false;

        // --- Cast cleanup ---
        for (CastCredit cc : credits.getCast()) {
            int castID = cc.getID();
            Cast castMember = castMap.get(castID);

            castMember.removeFilm(id);
            if (castMember.filmsIsEmpty()) {
                castMap.remove(castID);
                castSearchIndex.remove(castID);
            } else {
                castMember.decrementCredit();
            }
        }

        // --- Crew cleanup ---
        for (CrewCredit cc : credits.getCrew()) {
            int crewID = cc.getID();

            Crew crewMember = crewMap.get(crewID);
            crewMember.removeFilm(id);
            if (crewMember.filmsIsEmpty()) {
                crewMap.remove(crewID);
                crewSearchIndex.remove(crewID);
            }
        }

        return true;
    }

    /**
     * Gets all the cast members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CastCredit objects, one for each member of cast that is 
     *         in the given film. The cast members should be in "order" order. If
     *         there is no cast members attached to a film, or the film cannot be 
     *         found in Credits, then return an empty array
     */
    @Override
    public CastCredit[] getFilmCast(int filmID) {
        FilmCredits credits = filmCredits.get(filmID);
        return (credits != null) ? credits.getCast() : new CastCredit[0];
    }

    /**
     * Gets all the crew members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CrewCredit objects, one for each member of crew that is
     *         in the given film. The crew members should be in "id" order (not "elementID"). If there 
     *         is no crew members attached to a film, or the film cannot be found in Credits, 
     *         then return an empty array
     */
    @Override
    public CrewCredit[] getFilmCrew(int filmID) {
        FilmCredits credits = filmCredits.get(filmID);
        return (credits != null) ? credits.getCrew() : new CrewCredit[0];
    }

    /**
     * Gets the number of cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of cast member that worked on a given film. If the film
     *         cannot be found in Credits, then return -1
     */
    @Override
    public int sizeOfCast(int filmID) {
        FilmCredits credits = filmCredits.get(filmID);
        return (credits != null) ? credits.getCast().length : -1;
    }

    /**
     * Gets the number of crew that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of crew member that worked on a given film. If the film
     *         cannot be found in Credits, then return -1
     */
    @Override
    public int sizeOfCrew(int filmID) {
        FilmCredits credits = filmCredits.get(filmID);
        return (credits != null) ? credits.getCrew().length : -1;
    }

    /**
     * Gets a list of all unique cast members present in the data structure
     * 
     * @return An array of all unique cast members as Person objects. If there are 
     *         no cast members, then return an empty array
     */
    @Override
    public Person[] getUniqueCast() {
        if (castMap.size() == 0) return new Person[0];
        return castMap.valueSet().toArray(Person.class, Cast::getPerson);
    }

    /**
     * Gets a list of all unique crew members present in the data structure
     * 
     * @return An array of all unique crew members as Person objects. If there are
     *         no crew members, then return an empty array
     */
    @Override
    public Person[] getUniqueCrew() {
        if (crewMap.size() == 0) return new Person[0];
        return crewMap.valueSet().toArray(Person.class, Crew::getPerson);
    }

    /**
     * Get all the cast members that have the given string within their name
     * 
     * @param cast The string that needs to be found
     * @return An array of unique Person objects of all cast members that have the 
     *         requested string in their name. If there are no matches, return an 
     *         empty array
     */
    @Override
    public Person[] findCast(String cast) {
        List<Integer> ids = castSearchIndex.search(cast);
        Person[] result = new Person[ids.size()];
        int i = 0;
        for (int castID : ids) {
            Person p = castMap.get(castID).getPerson();
            if (p != null) {
                result[i++] = p;
            }
        }
        return result;
    }

    /**
     * Get all the crew members that have the given string within their name
     * 
     * @param crew The string that needs to be found
     * @return An array of unique Person objects of all crew members that have the 
     *         requested string in their name. If there are no matches, return an 
     *         empty array
     */
    @Override
    public Person[] findCrew(String crew) {
        List<Integer> ids = crewSearchIndex.search(crew);
        Person[] result = new Person[ids.size()];
        int i = 0;
        for (int crewID : ids) {
            Person p = crewMap.get(crewID).getPerson();
            if (p != null) result[i++] = p;
        }
        return result;
    }

    /**
     * Gets the Person object corresponding to the cast ID
     * 
     * @param castID The cast ID of the person to be found
     * @return The Person object corresponding to the cast ID provided. 
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCast(int castID) {
        Cast cast = castMap.get(castID);
        return cast != null ? cast.getPerson() : null;
    }

    /**
     * Gets the Person object corresponding to the crew ID
     * 
     * @param crewID The crew ID of the person to be found
     * @return The Person object corresponding to the crew ID provided. 
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCrew(int crewID){
        Crew crew = crewMap.get(crewID);
        return crew != null ? crew.getPerson() : null;
    }

    
    /**
     * Get an array of film IDs where the cast member has starred in
     * 
     * @param castID The cast ID of the person
     * @return An array of all the films the member of cast has starred
     *         in. If there are no films attached to the cast member, 
     *         then return an empty array
     */
    @Override
    public int[] getCastFilms(int castID){
        Cast cast = castMap.get(castID);
        return cast != null ? cast.getFilms() : new int[0];
    }

    /**
     * Get an array of film IDs where the crew member has starred in
     * 
     * @param crewID The crew ID of the person
     * @return An array of all the films the member of crew has starred
     *         in. If there are no films attached to the crew member, 
     *         then return an empty array
     */
    @Override
    public int[] getCrewFilms(int crewID) {
        Crew crew = crewMap.get(crewID);
        return crew != null ? crew.getFilms() : new int[0];
    }

    /**
     * Get the films that this cast member stars in (in the top 3 cast
     * members/top 3 billing). This is determined by the order field in
     * the CastCredit class
     * 
     * @param castID The cast ID of the cast member to be searched for
     * @return An array of film IDs where the the cast member stars in.
     *         If there are no films where the cast member has starred in,
     *         or the cast member does not exist, return an empty array
     */
    @Override
    public int[] getCastStarsInFilms(int castID){
        Cast cast = castMap.get(castID);
        return cast != null ? cast.getStarsInFilms() : new int[0];
    }
    
    /**
     * Get Person objects for cast members who have appeared in the most
     * films. If the cast member has multiple roles within the film, then
     * they would get a credit per role played. For example, if a cast
     * member performed as 2 roles in the same film, then this would count
     * as 2 credits. The list should be ordered by the highest to lowest number of credits.
     * 
     * @param numResults The maximum number of elements that should be returned
     * @return An array of Person objects corresponding to the cast members
     *         with the most credits, ordered by the highest number of credits.
     *         If there are less cast members that the number required, then the
     *         list should be the same number of cast members found.
     */
    @Override
    public Person[] getMostCastCredits(int numResults) {
        if (numResults <= 0 || castMap.size() == 0) {
            return new Person[0];
        }

        Cast[] casts = castMap.valueSet().toArray(Cast.class);

        TopK<Cast> topK = new TopK<>(
                Comparator.comparingInt(Cast::getCreditCount).reversed()
        );

        return topK.topK(casts, numResults, Cast::getPerson, Person[]::new);
    }

    /**
     * Get the number of credits for a given cast member. If the cast member has
     * multiple roles within the film, then they would get a credit per role
     * played. For example, if a cast member performed as 2 roles in the same film,
     * then this would count as 2 credits.
     * 
     * @param castID A cast ID representing the cast member to be found
     * @return The number of credits the given cast member has. If the cast member
     *         cannot be found, return -1
     */
    @Override
    public int getNumCastCredits(int castID) {
        var cast = castMap.get(castID);
        if (cast != null) {
            return cast.getCreditCount();
        }

        return -1;
    }

    /**
     * Gets the number of films stored in this data structure
     * 
     * @return The number of films in the data structure
     */
    @Override
    public int size() {
        return filmCredits.size();
    }
}
