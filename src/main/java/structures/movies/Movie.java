package structures.movies;

import stores.Company;
import stores.Genre;
import structures.data.LinkedList;
import structures.data.interfaces.List;

import java.time.LocalDate;

public class Movie {
    private int id;
    private String title, originalTitle, overview, tagline, status;
    private String homepage, poster, originalLanguage, imdbID;
    private Genre[] genres;
    private LocalDate release;
    private long budget, revenue;
    private String[] languages;
    private double runtime, voteAverage, popularity;
    private int voteCount;
    private boolean adult, video;
    private List<Company> productionCompanies = new LinkedList<>();
    private List<String> productionCountries = new LinkedList<>();
    private int collectionId = -1;

    public Movie(String title, String originalTitle, String overview,
            String tagline, String status, Genre[] genres, LocalDate release, long budget,
            long revenue, String[] languages, String originalLanguage, double runtime,
            String homepage, boolean adult, boolean video, String poster) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.tagline = tagline;
        this.status = status;
        this.genres = genres;
        this.release = release;
        this.budget = budget;
        this.revenue = revenue;
        this.languages = languages;
        this.originalLanguage = originalLanguage;
        this.runtime = runtime;
        this.homepage = homepage;
        this.adult = adult;
        this.video = video;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getTagline() {
        return tagline;
    }

    public String getStatus() {
        return status;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getPoster() {
        return poster;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getImdbID() {
        return imdbID;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public LocalDate getRelease() {
        return release;
    }

    public long getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public String[] getLanguages() {
        return languages;
    }

    public double getRuntime() {
        return runtime;
    }

    public double getPopularity() {
        return popularity;
    }

    public boolean setVote(double voteAverage, int voteCount) {
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        return true;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isAdult() {
        return adult;
    }

    public boolean isVideo() {
        return video;
    }

    public boolean addProductionCompany(Company productionCompany) {
        this.productionCompanies.add(productionCompany);
        return true;
    }

    public List<Company> getProductionCompanies() {
        return productionCompanies;
    }

    public boolean addProductionCountry(String productionCountry) {
        this.productionCountries.add(productionCountry);
        return true;
    }

    public List<String> getProductionCountries() {
        return productionCountries;
    }

    public boolean setIMDB(String imdbId) {
        this.imdbID = imdbId;
        return true;
    }

    public boolean setPopularity(double popularity) {
        this.popularity = popularity;
        return true;
    }

    public void setCollectionId(int id) {
        this.collectionId = id;
    }

    public int getCollectionId() {
        return collectionId;
    }
}
