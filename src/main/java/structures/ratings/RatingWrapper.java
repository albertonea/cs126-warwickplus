package structures.ratings;

import structures.data.HashMap;
import structures.data.interfaces.List;
import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

import java.time.LocalDateTime;

/**
 * Aggregates the ratings stored against a single entity (either a user or a
 * film). The wrapper indexes ratings by the id of the "other side" of the
 * relationship. A running total of the stored ratings is maintained so
 * that {@link #averageRating()} is O(1) instead of O(n).
 */
public class RatingWrapper {
    // Inner map of "other-side id" -> Rating
    private Map<Integer, Rating> ratings = new HashMap<>(2);
    // Running sum of all stored ratings; division by size gives the average
    private double total = 0;

    /**
     * Builds a wrapper seeded with a single rating
     *
     * @param id        the id on the other side of the relationship
     * @param rating    the rating
     */
    public RatingWrapper(int id, Rating rating) {
        put(id, rating);
    }

    /**
     * Records a rating, replacing any existing rating for the same id and
     * keeping the running total in sync
     *
     * @param id the id on the other side of the relationship
     * @param rating the rating to store
     * @return the {@link Rating} that was displaced, or {@code null} when
     *         this is the first rating for {@code id}
     */
    public Rating put(int id, Rating rating) {
        Rating oldRating = ratings.put(id, rating);
        // If an existing rating was replaced, subtract its contribution
        if (oldRating != null) {
            total -= oldRating.getRating();
        }
        total += rating.getRating();
        return oldRating;
    }

    /**
     * @param id the id to look up
     * @return the rating stored under {@code id}, or {@code null} if absent
     */
    public Rating get(int id) {
        return ratings.get(id);
    }

    /**
     * Removes the rating for the supplied id and keeps the running total
     * consistent
     *
     * @param id the id whose rating should be discarded
     */
    public void remove(int id) {
        Rating rating = ratings.remove(id);
        if (rating != null) {
            total -= rating.getRating();
        }
    }

    /**
     * @return the number of ratings held in the wrapper
     */
    public int size() {
        return ratings.size();
    }

    /**
     * @return a snapshot of every {@link Rating} held by the wrapper
     */
    public List<Rating> ratings() {
        return ratings.valueList();
    }

    /**
     * @return the arithmetic mean of every stored rating, or 0.0f when no
     *         ratings have been recorded yet
     */
    public float averageRating() {
        // Guard against division by zero on empty ratings
        return ratings.size() == 0 ? 0.0f : (float) total / ratings.size();
    }
}
