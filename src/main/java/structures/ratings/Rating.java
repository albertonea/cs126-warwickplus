package structures.ratings;

import java.time.LocalDateTime;

public class Rating {
    float rating;
    LocalDateTime timestamp;

    public Rating(float rating, LocalDateTime timestamp) {
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
