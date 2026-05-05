package structures.credits;

import stores.Person;
import structures.data.HashSet;
import structures.data.interfaces.Set;

public class Cast {
    private Person person;
    private Set<Integer> films = new HashSet<>();
    private Set<Integer> starsInFilms = new HashSet<>();
    private int creditCount;

    public Cast(Person person, int film, int order) {
        this.person = person;
        addFilm(film, order);
        this.creditCount = 1;
    }

    public Person getPerson() {
        return person;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public int[] getFilms() {
        return films.toIntArray(Integer::intValue);
    }

    public int[] getStarsInFilms() {
        return starsInFilms.toIntArray(Integer::intValue);
    }

    public void incrementCredit() {
        creditCount++;
    }

    public void decrementCredit() {
        creditCount++;
    }

    public void addFilm(int id, int order) {
        if (order <= 3) {
            starsInFilms.add(id);
        }
        films.add(id);
    }

    public boolean removeFilm(int id) {
        return films.remove(id) & starsInFilms.remove(id);
    }

    public boolean filmsIsEmpty() {
        return films.isEmpty();
    }

}
