package structures.credits;

import stores.CastCredit;
import stores.CrewCredit;

public class FilmCredits {
    private final CastCredit[] cast;
    private final CrewCredit[] crew;

    public FilmCredits(CastCredit[] cast, CrewCredit[] crew) {
        this.cast = cast;
        this.crew = crew;
    }

    public CastCredit[] getCast() {
        return cast;
    }

    public CrewCredit[] getCrew() {
        return crew;
    }
}
