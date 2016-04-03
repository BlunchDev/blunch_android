package dev.blunch.blunch.domain.dishes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Collaborative Dish subclass
 * @author albert
 */
public class CollaborativeDish extends Dish {

    private boolean suggested;
    private Map<String, Object> collaborativeMenus  = new LinkedHashMap<>();

    public CollaborativeDish() {
        super();
    }

    public CollaborativeDish(String name, boolean suggested, Set<String> collaborativeMenusKeys) {
        super(name);
        this.suggested = suggested;
        for (String key : collaborativeMenusKeys) {
            collaborativeMenus.put(key, true);
        }
    }

    public boolean isSuggested() {
        return suggested;
    }

    public Map<String, Object> getCollaborativeMenus() {
        return collaborativeMenus;
    }

    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }

    public void setCollaborativeMenus(Set<String> collaborativeMenusKeys) {
        for (String key : collaborativeMenusKeys) {
            collaborativeMenus.put(key, true);
        }
    }

    public void setCollaborativeMenus(Map<String, Object> collaborativeMenus) {
        this.collaborativeMenus = collaborativeMenus;
    }

    @Override
    public String toString() {
        return "[id : " + getId() + "] [name : " + getName() + "] [suggested : " + this.suggested + "]" +
                "\n[collaborativeDishes keys: " + collaborativeMenus.keySet().toString();
    }

}
