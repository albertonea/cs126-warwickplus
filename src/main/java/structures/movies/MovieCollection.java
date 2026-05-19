package structures.movies;

import structures.data.LinkedList;
import structures.data.interfaces.List;

/**
 * Represents a "collection" and the films that belong to it.
 *
 * <p>The {@code Movies} store mirrors collection membership on each
 * {@code Movie} so that lookups in either direction are O(1).
 */
public class MovieCollection {
    // Metadata for the collection
    String name, posterPath, backdropPath;
    // IDs of every film currently in the collection
    List<Integer> filmIDs = new LinkedList<>();

    /**
     * Creates a collection with seeded with a film
     *
     * @param name         the collection name
     * @param posterPath   poster URL fragment
     * @param backdropPath backdrop URL fragment
     * @param filmId       the id of the first member film
     */
    public MovieCollection(String name, String posterPath, String backdropPath, int filmId) {
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.filmIDs.add(filmId);
    }

    /**
     * Adds another film to the collection
     *
     * @param filmID the id of the film joining the collection
     */
    public void addFilm(int filmID) {
        filmIDs.add(filmID);
    }

    /**
     * @return every member film id as a primitive int array
     */
    public int[] getFilms() {
        // Map the boxed list down to a primitive array in one pass
        return filmIDs.toIntArray(Integer::intValue);
    }

    /**
     * @return the display name of the collection
     */
    public String getName() {
        return name;
    }

    /**
     * @return the poster URL fragment for the collection
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * @return the backdrop URL fragment for the collection
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Removes the given film from the collection
     *
     * @param id the film id to remove
     */
    public void removeFilm(int id) {
        filmIDs.remove(id);
    }
}
