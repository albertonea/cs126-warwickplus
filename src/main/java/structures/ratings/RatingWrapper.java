package structures.ratings;

import structures.data.HashMap;
import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

import java.time.LocalDateTime;

public class RatingWrapper {
    Map<Integer, Rating> ratings = new HashMap<>();
    float total = 0;

    public RatingWrapper(int id, float rating, LocalDateTime timestamp) {
        put(id, rating, timestamp);
    }

    public Rating put(int id, float rating, LocalDateTime timestamp) {
        total += rating;
        return ratings.put(id, new Rating(rating, timestamp));
    }

    public Rating get(int id) {
        return ratings.get(id);
    }

    public void remove(int id) {
        Rating rating = ratings.remove(id);
        total -= rating.getRating();
    }

    public int size() {
        return ratings.size();
    }

    public Set<Rating> ratings() {
        return ratings.valueSet();
    }

    public float averageRating() {
        return total/ratings.size();
    }
}
