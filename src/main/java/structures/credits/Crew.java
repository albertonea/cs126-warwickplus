package structures.credits;

import stores.Person;
import structures.data.HashSet;
import structures.data.interfaces.Set;

public class Crew {
    private Person person;
    private Set<Integer> films = new HashSet<>();

    public Crew(Person person, int film) {
        this.person = person;
        addFilm(film);
    }

    public Person getPerson() {
        return person;
    }

    public int[] getFilms() {
        return films.toIntArray(Integer::intValue);
    }

    public void addFilm(int id) {
        films.add(id);
    }

    public boolean removeFilm(int id) {
        return films.remove(id);
    }

    public boolean filmsIsEmpty() {
        return films.isEmpty();
    }
}
