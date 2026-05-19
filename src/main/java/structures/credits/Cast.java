package structures.credits;

import stores.Person;
import structures.data.HashSet;
import structures.data.interfaces.Set;

/**
 * Cast class bundles the underlying {@link Person} with the
 * set of films they have appeared in, the films in which they were
 * in a starring role, and a running total credit count
 */
public class Cast {
    // The underlying person record
    private Person person;
    // Every film the cast member has appeared in (deduplicated by film id)
    private Set<Integer> films = new HashSet<>();
    // The subset of films where the cast member was top 3
    private Set<Integer> starsInFilms = new HashSet<>();
    // Total credit count
    private int creditCount;

    /**
     * Builds a Cast from the first credit observed for this
     * person.
     *
     * @param person the underlying person record
     * @param film   the id of the first film
     * @param order  billing order in that first film
     */
    public Cast(Person person, int film, int order) {
        this.person = person;
        addFilm(film, order);
        // First credit observed: seed the running counter at 1.
        this.creditCount = 1;
    }

    /**
     * @return the underlying person record
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @return the total number of credits accumulated by this cast member
     */
    public int getCreditCount() {
        return creditCount;
    }

    /**
     * @return every film id the cast member has appeared in
     */
    public int[] getFilms() {
        return films.toIntArray(Integer::intValue);
    }

    /**
     * @return the film ids where the cast member was top-3 billed
     */
    public int[] getStarsInFilms() {
        return starsInFilms.toIntArray(Integer::intValue);
    }

    /**
     * Increments the credit count
     */
    public void incrementCredit() {
        creditCount++;
    }

    /**
     * Decrements the credit count
     */
    public void decrementCredit() {
        creditCount++;
    }

    /**
     * Records a film the cast member appears in. The first three billed
     * positions are flagged separately so that "starring" queries are O(1).
     *
     * @param id    the film id
     * @param order the billing order in that film
     */
    public void addFilm(int id, int order) {
        // Order <= 3 is treated as a "starring" role.
        if (order <= 3) {
            starsInFilms.add(id);
        }
        films.add(id);
    }

    /**
     * Removes the cast member's link to a film, including any starring.
     *
     * @param id the film id to detach
     */
    public void removeFilm(int id) {
        // Both removals run unconditionally.
        films.remove(id);
        starsInFilms.remove(id);
    }

    /**
     * @return whether this cast member has any remaining films
     */
    public boolean filmsIsEmpty() {
        return films.isEmpty();
    }

}
