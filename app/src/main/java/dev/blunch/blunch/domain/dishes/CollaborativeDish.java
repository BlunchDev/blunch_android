package dev.blunch.blunch.domain.dishes;

import java.util.Map;

/**
 * Collaborative Dish subclass
 * @author albert
 */
public class CollaborativeDish extends Dish {

    private boolean suggested;
    private Map<String, Boolean> collaborativeMenus;

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

    @Override
    public String toString() {
        return "[id : " + getId() + "] [name : " + getName() + "] [suggested : " + this.suggested + "]";
    }

}
