package structures.credits;

import stores.CastCredit;
import stores.CrewCredit;

/**
 * FilmCredits class holding the cast and crew arrays for a single
 * film
 */
public class FilmCredits {
    // Cast pre-sorted by billing order so getFilmCast() can hand it back as-is
    private final CastCredit[] cast;
    // Crew pre-sorted by id so getFilmCrew() can hand it back as-is
    private final CrewCredit[] crew;

    /**
     * Constructs the record from cast and crew arrays
     *
     * @param cast the sorted cast array
     * @param crew the sorted crew array
     */
    public FilmCredits(CastCredit[] cast, CrewCredit[] crew) {
        this.cast = cast;
        this.crew = crew;
    }

    /**
     * @return the cast array
     */
    public CastCredit[] getCast() {
        return cast;
    }

    /**
     * @return the crew array
     */
    public CrewCredit[] getCrew() {
        return crew;
    }
}
