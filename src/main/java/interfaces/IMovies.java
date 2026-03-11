package interfaces;

import java.time.LocalDate;

import stores.Company;
import stores.Genre;

public interface IMovies {
    public boolean add(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster);
    public boolean remove(int id);

    public int[] getAllIDs();
    public int[] getAllIDsReleasedInRange(LocalDate start, LocalDate end);

    public String getTitle(int id);
    public String getOriginalTitle(int id);
    public String getOverview(int id);
    public String getTagline(int id);
    public String getStatus(int id);
    public Genre[] getGenres(int id);
    public LocalDate getRelease(int id);
    public long getBudget(int id);
    public long getRevenue(int id);
    public String[] getLanguages(int id);
    public String getOriginalLanguage(int id);
    public double getRuntime(int id);
    public String getHomepage(int id);
    public boolean getAdult(int id);
    public boolean getVideo(int id);
    public String getPoster(int id);

    public boolean setVote(int id, double voteAverage, int voteCount);
    public double getVoteAverage(int id);
    public int getVoteCount(int id);

    public boolean addToCollection(int filmID, int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath);
    public int[] getFilmsInCollection(int collectionID);
    public String getCollectionName(int collectionID);
    public String getCollectionPoster(int collectionID);
    public String getCollectionBackdrop(int collectionID);
    public int getCollectionID(int filmID);

    public boolean setIMDB(int filmID, String imdbID);
    public String getIMDB(int filmID);

    public boolean setPopularity(int id, double popularity);
    public double getPopularity(int id);

    public boolean addProductionCompany(int movieId, Company company);
    public boolean addProductionCountry(int movieId, String country);
    public Company[] getProductionCompanies(int movieId);
    public String[] getProductionCountries(int movieId);

    public int[] findFilms(String searchTerm);

    public int size();
}
