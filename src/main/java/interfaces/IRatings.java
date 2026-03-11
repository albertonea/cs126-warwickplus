package interfaces;

import java.time.LocalDateTime;

public interface IRatings {
    public boolean add(int userID, int movieID, float rating, LocalDateTime timestamp);

    public boolean remove(int userID, int movieID);

    public boolean set(int userID, int movieID, float rating, LocalDateTime timestamp);

    public float[] getMovieRatings(int movieID);
    public float[] getUserRatings(int userID);
    public float getMovieAverageRating(int movieID);
    public float getUserAverageRating(int userID);

    public int[] getMostRatedMovies(int numResults);
    public int[] getMostRatedUsers(int numResults);
    public int getNumRatings(int movieID);

    public int[] getTopAverageRatedMovies(int numResults);

    public int size();
}
