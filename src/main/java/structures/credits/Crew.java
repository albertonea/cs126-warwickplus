package structures.credits;

import stores.Person;
import structures.data.HashSet;
import structures.data.interfaces.Set;

/**
 * Crew class bundles the underlying {@link Person} with the
 * set of films they have appeared in
 */
public class Crew {
    // The underlying person record (name, profile picture, etc)
    private Person person;
    // Every film this crew member has worked on
    private Set<Integer> films = new HashSet<>();

    /**
     * Builds a Crew from the first credit observed for this person.
     *
     * @param person the underlying person record
     * @param film   the first film id to associate
     */
    public Crew(Person person, int film) {
        this.person = person;
        addFilm(film);
    }

    /**
     * @return the underlying person record
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @return the ids of every film the crew member has worked on
     */
    public int[] getFilms() {
        return films.toIntArray(Integer::intValue);
    }

    /**
     * Records a film this crew member contributed to
     *
     * @param id the film id
     */
    public void addFilm(int id) {
        films.add(id);
    }

    /**
     * Removes a film association
     *
     * @param id the film id to remove
     */
    public void removeFilm(int id) {
        films.remove(id);
    }

    /**
     * @return whether the crew member has any remaining films
     */
    public boolean filmsIsEmpty() {
        return films.isEmpty();
    }
}
