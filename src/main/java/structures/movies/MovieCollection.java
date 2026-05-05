package structures.movies;

import java.util.ArrayList;
import java.util.List;

public class MovieCollection {
    String name, posterPath, backdropPath;
    List<Integer> filmIDs = new ArrayList<>();

    public MovieCollection(String name, String posterPath, String backdropPath, int filmId) {
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.filmIDs.add(filmId);
    }

    public boolean addFilm(int filmID) {
        filmIDs.add(filmID);
        return true;
    }

    public int[] getFilms() {
        return filmIDs.stream().mapToInt(Integer::intValue).toArray();
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void removeFilm(int id) {
        filmIDs.remove(id);
    }
}