package structures.ratings;

import structures.data.HashMap;
import structures.data.interfaces.Map;

import java.time.LocalDateTime;

/**
 * Holds a single rating value paired with the timestamp at which the user
 * recorded it
 */
public class Rating {
    // rating
    float rating;
    // When the user submitted this rating
    LocalDateTime timestamp;

    /**
     * Builds a rating with its initial value and timestamp.
     *
     * @param rating    the star rating (between 0 and 5)
     * @param timestamp when the rating was created
     */
    public Rating(float rating, LocalDateTime timestamp) {
        this.rating = rating;
        this.timestamp = timestamp;
    }

    /**
     * @return the star rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * Replaces the star rating in place.
     *
     * @param rating the new rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * @return the timestamp at which this rating was recorded
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Replaces the timestamp in place.
     *
     * @param timestamp the new timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
