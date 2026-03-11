package structures;

import java.util.ArrayList;
import java.util.List;

public class MovieCollection {
    String name, posterPath, backdropPath;
    List<Integer> filmIDs = new ArrayList<>();

    public MovieCollection(String name, String posterPath, String backdropPath) {
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
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
}