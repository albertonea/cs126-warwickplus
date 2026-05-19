package structures.movies;

import stores.Company;
import stores.Genre;
import structures.data.LinkedList;
import structures.data.interfaces.List;

import java.time.LocalDate;

/**
 * The aggregate record for a single film. All of the metadata-like fields
 * pulled in from the CSV ({@code title}, {@code overview}, {@code genres}
 * etc.) live on this object and are filled in either by the constructor or
 * by deferred setters that the Warwick+ loader calls after the initial
 * {@code add}.
 *
 * <p>The class is a plain holder: business logic (search, ranking, range
 * lookups) lives in the {@code stores.Movies} class, while this type simply
 * owns the per-film data.
 */
public class Movie {
    // Data for a movie
    private String title, originalTitle, overview, tagline, status, homepage, poster, originalLanguage, imdbID;
    private Genre[] genres;
    private LocalDate release;
    private long budget, revenue;
    private String[] languages;
    private double runtime, voteAverage, popularity;
    private int voteCount;
    private boolean adult, video;
    private List<Company> productionCompanies = new LinkedList<>();
    private List<String> productionCountries = new LinkedList<>();
    // -1 indicates the film does not belong to any collection.
    private int collectionId = -1;

    /**
     * Builds a movie from the supplied fields
     *
     * @param title the localised English title
     * @param originalTitle the title in the film's original language
     * @param overview long-form synopsis
     * @param tagline the poster tagline (may be empty)
     * @param status production status (e.g. "Released")
     * @param genres the genres the film belongs to
     * @param release the release date, or {@code null} if unknown
     * @param budget production budget in US dollars
     * @param revenue gross revenue in US dollars
     * @param languages ISO-639 codes for the spoken languages
     * @param originalLanguage ISO-639 code for the original language
     * @param runtime length in minutes
     * @param homepage official homepage URL
     * @param adult whether the film is classed as adult
     * @param video whether the film is direct-to-video
     * @param poster poster URL fragment
     */
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

    /**
     * @return the English-language title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the original-language title
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * @return the long-form film synopsis
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @return the marketing tagline, or an empty string if there isn't one
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * @return the production status field (e.g. "Released", "Post Production")
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the homepage URL for the film
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @return the unique URL fragment for the poster image
     */
    public String getPoster() {
        return poster;
    }

    /**
     * @return the ISO-639 code of the film's original language
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * @return the IMDb identifier, or {@code null} if it has not been supplied yet
     */
    public String getImdbID() {
        return imdbID;
    }

    /**
     * @return the genres array
     */
    public Genre[] getGenres() {
        return genres;
    }

    /**
     * @return the release date, or {@code null} if unknown
     */
    public LocalDate getRelease() {
        return release;
    }

    /**
     * @return the budget in US dollars (0 when unknown)
     */
    public long getBudget() {
        return budget;
    }

    /**
     * @return the revenue in US dollars (0 when unknown)
     */
    public long getRevenue() {
        return revenue;
    }

    /**
     * @return the ISO-639 codes of every language the film is available in
     */
    public String[] getLanguages() {
        return languages;
    }

    /**
     * @return the runtime in minutes (0 when unknown)
     */
    public double getRuntime() {
        return runtime;
    }

    /**
     * @return the popularity score (0 if not yet set)
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     * Records the IMDb vote statistics for the film
     *
     * @param voteAverage the average IMDb score
     * @param voteCount the number of IMDb votes that produced the average
     * @return always {@code true}; the return is kept for API symmetry
     */
    public boolean setVote(double voteAverage, int voteCount) {
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        return true;
    }

    /**
     * @return the average IMDb rating
     */
    public double getVoteAverage() {
        return voteAverage;
    }

    /**
     * @return the number of IMDb votes that contributed to the average
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     * @return whether the film is classed as adult content
     */
    public boolean isAdult() {
        return adult;
    }

    /**
     * @return whether the film was released direct-to-video
     */
    public boolean isVideo() {
        return video;
    }

    /**
     * Records another production company involved in the film
     *
     * @param productionCompany the company to associate with the film
     * @return always {@code true}
     */
    public boolean addProductionCompany(Company productionCompany) {
        this.productionCompanies.add(productionCompany);
        return true;
    }

    /**
     * @return the running list of associated production companies
     */
    public List<Company> getProductionCompanies() {
        return productionCompanies;
    }

    /**
     * Records another ISO-3166 country code under which the film was
     * produced
     *
     * @param productionCountry the ISO-3166 2-letter country code
     * @return always {@code true}
     */
    public boolean addProductionCountry(String productionCountry) {
        this.productionCountries.add(productionCountry);
        return true;
    }

    /**
     * @return the running list of production country codes
     */
    public List<String> getProductionCountries() {
        return productionCountries;
    }

    /**
     * Stores the IMDb identifier
     *
     * @param imdbId the IMDb id fragment
     * @return always {@code true}
     */
    public boolean setIMDB(String imdbId) {
        this.imdbID = imdbId;
        return true;
    }

    /**
     * Stores the popularity score
     *
     * @param popularity the popularity score
     * @return always {@code true}
     */
    public boolean setPopularity(double popularity) {
        this.popularity = popularity;
        return true;
    }

    /**
     * Records that this film is a member of the given collection
     *
     * @param id the collection identifier
     */
    public void setCollectionId(int id) {
        this.collectionId = id;
    }

    /**
     * @return the collection id, or {@code -1} if the film is unaffiliated
     */
    public int getCollectionId() {
        return collectionId;
    }
}
