package dev.blunch.blunch.domain.dishes;

/**
 * Collaborative Dish subclass
 * @author albert
 */
public class CollaborativeDish extends Dish {

    private boolean suggested;

    public CollaborativeDish() {

    }

    public CollaborativeDish(String name, boolean suggested) {
        super(name);
        this.suggested = suggested;
    }

    public boolean isSuggested() {
        return suggested;
    }

    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }
}
