package stores;

import structures.*;

import interfaces.ICredits;
import structures.data.*;
import structures.data.interfaces.List;
import structures.data.interfaces.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Credits implements ICredits{
    Stores stores;

    // Film-centric: pre-sorted for O(1) retrieval
    private final HashMap<Integer, CastCredit[]> filmCastMap;
    private final HashMap<Integer, CrewCredit[]> filmCrewMap;

    // Person lookups by ID
    private final HashMap<Integer, Person> castPersonMap;
    private final HashMap<Integer, Person> crewPersonMap;

    // Reverse lookups: person -> films they appeared in
    private final HashMap<Integer, ArrayList<Integer>> castFilmsMap;
    private final HashMap<Integer, ArrayList<Integer>> crewFilmsMap;

    // Top-3 billing films per cast member
    private final HashMap<Integer, ArrayList<Integer>> castStarsFilmsMap;

    // Total credit count per cast member (multiple roles = multiple credits)
    private final HashMap<Integer, Integer> castCreditCount;

    // N-gram string search indexes, keyed by person ID (not Person object)
    // so stale entries after remove() are safely filtered via the person maps
    private final StringSearchIndex<Integer> castSearchIndex;
    private final StringSearchIndex<Integer> crewSearchIndex;

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
        filmCastMap       = new HashMap<>();
        filmCrewMap       = new HashMap<>();
        castPersonMap     = new HashMap<>();
        crewPersonMap     = new HashMap<>();
        castFilmsMap      = new HashMap<>();
        crewFilmsMap      = new HashMap<>();
        castStarsFilmsMap = new HashMap<>();
        castCreditCount   = new HashMap<>();
        castSearchIndex   = new StringSearchIndex<>();
        crewSearchIndex   = new StringSearchIndex<>();
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
        if (filmCastMap.containsKey(id)) return false;

        // --- Cast: sort by order, store ---
        CastCredit[] sortedCast = cast.clone();
        MergeSort.sort(sortedCast, Comparator.comparingInt(CastCredit::getOrder));
        filmCastMap.put(id, sortedCast);

        HashMap<Integer, Integer> castMinOrder = new HashMap<>();
        for (CastCredit cc : cast) {
            int castID = cc.getID();

            if (!castPersonMap.containsKey(castID)) {
                castPersonMap.put(castID, new Person(castID, cc.getName(), cc.getProfilePath()));
                castSearchIndex.add(castID, cc.getName());
            }

            // merge(castID, 1, Integer::sum)
            Integer count = castCreditCount.get(castID);
            castCreditCount.put(castID, count == null ? 1 : count + 1);

            // merge(castID, cc.getOrder(), Math::min)
            Integer existingMin = castMinOrder.get(castID);
            if (existingMin == null || cc.getOrder() < existingMin) {
                castMinOrder.put(castID, cc.getOrder());
            }
        }

        for (Map.Entry<Integer, Integer> entry : castMinOrder.entrySet()) {
            int castID = entry.getKey();

            // computeIfAbsent(castID, k -> new ArrayList<>()).add(id)
            ArrayList<Integer> films = castFilmsMap.get(castID);
            if (films == null) {
                films = new ArrayList<>();
                castFilmsMap.put(castID, films);
            }
            films.add(id);

            if (entry.getValue() <= 3) {
                ArrayList<Integer> starFilms = castStarsFilmsMap.get(castID);
                if (starFilms == null) {
                    starFilms = new ArrayList<>();
                    castStarsFilmsMap.put(castID, starFilms);
                }
                starFilms.add(id);
            }
        }

        // --- Crew: sort by id, store ---
        CrewCredit[] sortedCrew = crew.clone();
        MergeSort.sort(sortedCrew, Comparator.comparingInt(CrewCredit::getID));
        filmCrewMap.put(id, sortedCrew);

        HashSet<Integer> seenCrew = new HashSet<>();
        for (CrewCredit cc : crew) {
            int crewID = cc.getID();

            if (!crewPersonMap.containsKey(crewID)) {
                crewPersonMap.put(crewID, new Person(crewID, cc.getName(), cc.getProfilePath()));
                crewSearchIndex.add(crewID, cc.getName());
            }

            if (seenCrew.add(crewID)) {
                // computeIfAbsent(crewID, k -> new ArrayList<>()).add(id)
                ArrayList<Integer> films = crewFilmsMap.get(crewID);
                if (films == null) {
                    films = new ArrayList<>();
                    crewFilmsMap.put(crewID, films);
                }
                films.add(id);
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
        CastCredit[] cast = filmCastMap.remove(id);
        if (cast == null) return false;
        CrewCredit[] crew = filmCrewMap.remove(id);

        // --- Cast cleanup ---
        // Decrement credit count once per CastCredit entry (covers multiple roles)
        for (CastCredit cc : cast) {
            int castID = cc.getID();
            Integer count = castCreditCount.get(castID);
            if (count != null) {
                castCreditCount.put(castID, count - 1);
            }
        }

        HashSet<Integer> processedCast = new HashSet<>();
        for (CastCredit cc : cast) {
            int castID = cc.getID();
            if (!processedCast.add(castID)) continue;

            ArrayList<Integer> films = castFilmsMap.get(castID);
            if (films != null) {
                films.remove(Integer.valueOf(id));
                if (films.isEmpty()) {
                    castFilmsMap.remove(castID);
                    castPersonMap.remove(castID);
                    castCreditCount.remove(castID);
                    castSearchIndex.remove(castID);
                }
            }

            ArrayList<Integer> starFilms = castStarsFilmsMap.get(castID);
            if (starFilms != null) {
                starFilms.remove(Integer.valueOf(id));
                if (starFilms.isEmpty()) castStarsFilmsMap.remove(castID);
            }
        }

        // --- Crew cleanup ---
        if (crew != null) {
            HashSet<Integer> processedCrew = new HashSet<>();
            for (CrewCredit cc : crew) {
                int crewID = cc.getID();
                if (!processedCrew.add(crewID)) continue;

                ArrayList<Integer> films = crewFilmsMap.get(crewID);
                if (films != null) {
                    films.remove(Integer.valueOf(id));
                    if (films.isEmpty()) {
                        crewFilmsMap.remove(crewID);
                        crewPersonMap.remove(crewID);
                        crewSearchIndex.remove(crewID);
                    }
                }
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
        CastCredit[] cast = filmCastMap.get(filmID);
        return (cast != null) ? cast : new CastCredit[0];    }

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
        CrewCredit[] crew = filmCrewMap.get(filmID);
        return (crew != null) ? crew : new CrewCredit[0];    }

    /**
     * Gets the number of cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of cast member that worked on a given film. If the film
     *         cannot be found in Credits, then return -1
     */
    @Override
    public int sizeOfCast(int filmID) {
        CastCredit[] cast = filmCastMap.get(filmID);
        return (cast != null) ? cast.length : -1;
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
        CrewCredit[] crew = filmCrewMap.get(filmID);
        return (crew != null) ? crew.length : -1;
    }

    /**
     * Gets a list of all unique cast members present in the data structure
     * 
     * @return An array of all unique cast members as Person objects. If there are 
     *         no cast members, then return an empty array
     */
    @Override
    public Person[] getUniqueCast() {
        if (castPersonMap.size() == 0) return new Person[0];
        return Arrays.stream(castPersonMap.valueSet().toArray())
                .map(o -> (Person) o)
                .toArray(Person[]::new);
    }

    /**
     * Gets a list of all unique crew members present in the data structure
     * 
     * @return An array of all unique crew members as Person objects. If there are
     *         no crew members, then return an empty array
     */
    @Override
    public Person[] getUniqueCrew() {
        if (crewPersonMap.size() == 0) return new Person[0];
        return Arrays.stream(crewPersonMap.valueSet().toArray())
                .map(o -> (Person) o)
                .toArray(Person[]::new);
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
            Person p = castPersonMap.get(castID);
            if (p != null) result[i++] = p;
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
            Person p = crewPersonMap.get(crewID);
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
        return castPersonMap.get(castID);
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
        return crewPersonMap.get(crewID);
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
        ArrayList<Integer> films = castFilmsMap.get(castID);
        if (films == null || films.isEmpty()) return new int[0];
        int[] result = new int[films.size()];
        for (int i = 0; i < films.size(); i++) result[i] = films.get(i);
        return result;
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
        ArrayList<Integer> films = crewFilmsMap.get(crewID);
        if (films == null || films.isEmpty()) return new int[0];
        int[] result = new int[films.size()];
        for (int i = 0; i < films.size(); i++) result[i] = films.get(i);
        return result;
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
        ArrayList<Integer> films = castStarsFilmsMap.get(castID);
        if (films == null || films.isEmpty()) return new int[0];
        int[] result = new int[films.size()];
        for (int i = 0; i < films.size(); i++) result[i] = films.get(i);
        return result;
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
        if (castCreditCount.size() == 0) return new Person[0];
        int limit = Math.min(numResults, castCreditCount.size());

        PriorityQueue<Map.Entry<Integer, Integer>> minHeap =
                new PriorityQueue<>(limit, Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<Integer, Integer> entry : castCreditCount.entrySet()) {
            if (minHeap.size() < limit) {
                minHeap.add(entry);
            } else if (entry.getValue() > minHeap.peek().getValue()) {
                minHeap.remove();
                minHeap.add(entry);
            }
        }

        Person[] result = new Person[minHeap.size()];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = castPersonMap.get(minHeap.remove().getKey());
        }
        return result;
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
        Integer count = castCreditCount.get(castID);
        return count != null ? count : -1;
    }

    /**
     * Gets the number of films stored in this data structure
     * 
     * @return The number of films in the data structure
     */
    @Override
    public int size() {
        return filmCastMap.size();
    }
}
