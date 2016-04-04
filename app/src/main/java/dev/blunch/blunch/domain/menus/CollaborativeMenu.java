package dev.blunch.blunch.domain.menus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Collaborative Menu Class
 * @author albert
 */
public class CollaborativeMenu extends Menu {

    private Map<String, Object> collaborativeDishes = new LinkedHashMap<>();

    public CollaborativeMenu() {
        super();
    }

    public CollaborativeMenu(String name, String description, String address, Set<String> dishesKeys) {
        super(name, description, address);
        for (String key : dishesKeys) {
            collaborativeDishes.put(key, true);
        }
    }

    public Map<String, Object> getCollaborativeDishes() {
        return collaborativeDishes;
    }

    public void setCollaborativeDishes(Map<String, Object> collaborativeDishes) {
        this.collaborativeDishes = collaborativeDishes;
    }

    public void setCollaborativeDishes(Set<String> collaborativeDishesKeys) {
        for (String key : collaborativeDishesKeys) {
            collaborativeDishes.put(key, true);
        }
    }

    @Override
    public String toString() {
        return  "[id : " + getId() + "] [name : " + getName() + "] [description : " + getDescription() + "] " +
                "[address : " + getAddress() + "]\n[collaborativeDishes keys: " + collaborativeDishes.keySet().toString();
    }
}
