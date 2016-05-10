package dev.blunch.blunch.services;

import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.InverseMenuComparator;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.MenuComparator;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.domain.Valoration;
import dev.blunch.blunch.utils.Preferences;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Created by jmotger on 26/04/16.
 */
@SuppressWarnings("all")
public class MenuService extends Service<CollaborativeMenu> {

    private Repository<PaymentMenu> paymentMenuRepository;
    private Repository<User> userRepository;
    private Repository<Valoration> valorationRepository;
    int loadNeed = 1;
    int loaded = 0;


    public MenuService(Repository<CollaborativeMenu> repository, Repository<PaymentMenu> paymentMenuRepository, Repository<Valoration> valorationRepository,Repository<User> userRepository) {
        super(repository);
        this.paymentMenuRepository = paymentMenuRepository;
        this.valorationRepository = valorationRepository;
        this.userRepository = userRepository;
    }

    public List<CollaborativeMenu> getCollaborativeMenus() {
        return this.repository.all();
    }

    public List<PaymentMenu> getPaymentMenus(){
            List<PaymentMenu> a = this.paymentMenuRepository.all();
        return a;
    }

    public List<User> getUsers() { return this.userRepository.all(); }

    public User findUserByEmail(String email) {
        return userRepository.get(email);
    }

    public User createNewUser(User user) {
        return userRepository.update(user);
    }

    public List<Menu> getMenus() {
        List<Menu> menus = new ArrayList<>();
        menus.addAll(repository.all());
        menus.addAll(paymentMenuRepository.all());
        return menus;
    }

    public List<Menu> getMyMenu() {
        List<Menu> menuList = new ArrayList<>();
        User user = findUserByEmail(Preferences.getCurrentUserEmail());
        Set<String> myMenus = user.getMyMenus().keySet();
        for (String k : myMenus) menuList.add(getMenu(k));
        return menuList;
    }

    public List<CollaborativeMenu> getCollaborativeMenusOrderedByDate() {
        List<CollaborativeMenu> menus = repository.all();
        List<CollaborativeMenu> result = new ArrayList<>();
        for (CollaborativeMenu menu : menus) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) > 0) {
                result.add(menu);
            }
        }
        Collections.sort(result, new MenuComparator());
        return result;
    }

    public List<PaymentMenu> getPaymentMenusOrderedByDate() {
        List<PaymentMenu> menus = paymentMenuRepository.all();
        List<PaymentMenu> result = new ArrayList<>();
        for (PaymentMenu menu : menus) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) > 0) {
                result.add(menu);
            }
        }
        Collections.sort(result, new MenuComparator());
        return result;
    }

    public List<Menu> getMenusOrderedByDate() {
        List<Menu> menus = getMenus();
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) > 0) {
                result.add(menu);
            }
        }
        Collections.sort(result, new MenuComparator());
        return result;
    }

    public List<Menu> getMyMenusOrderedByDate() {
        List<Menu> menus = getMyMenu();
        Collections.sort(menus, new InverseMenuComparator());
        return menus;
    }

    public List<Menu> getMyOutMenusOrderedByDate() {
        List<Menu> menus = getMyMenu();
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) < 0) {
                result.add(menu);
            }
        }
        Collections.sort(result, new InverseMenuComparator());
        return result;
    }

    public List<Menu> getMyCurrentMenusOrderedByDate() {
        List<Menu> menus = getMyMenu();
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) > 0) {
                result.add(menu);
            }
        }
        Collections.sort(result, new InverseMenuComparator());
        return result;
    }

    public List<Menu> getPMenusOrderedByDate() {
        List<Menu> menus = getCollaboratedMenusOf(Preferences.getCurrentUserEmail());
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) > 0) {
                result.add(menu);
            }
        }
        Collections.sort(result, new InverseMenuComparator());
        return result;
    }

    public List<Menu> getPCollaborativeMenusOrderedByDate() {
        List<Menu> menusC = getCollaboratedMenusOf(Preferences.getCurrentUserEmail());
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menusC) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) > 0 && (menu instanceof CollaborativeMenu)) {
                result.add(menu);
            }
        }
        Collections.sort(result, new InverseMenuComparator());
        return result;
    }

    public List<Menu> getPPaymentMenusOrderedByDate() {
        List<Menu> menusC = getCollaboratedMenusOf(Preferences.getCurrentUserEmail());
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menusC) {
            if (menu.getDateEnd().compareTo(Calendar.getInstance().getTime()) > 0 && (menu instanceof PaymentMenu)) {
                result.add(menu);
            }
        }
        Collections.sort(result, new InverseMenuComparator());
        return result;
    }



    @Override
    public void setOnChangedListener(final Repository.OnChangedListener listener) {
        repository.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                triggerListener(listener, type);
            }
        });
        if (paymentMenuRepository != null) {
            loadNeed += 1;
            paymentMenuRepository.setOnChangedListener(new Repository.OnChangedListener() {
                @Override
                public void onChanged(EventType type) {
                    triggerListener(listener, type);
                }
            });
        }
        if (userRepository != null) {
            loadNeed += 1;
            userRepository.setOnChangedListener(new Repository.OnChangedListener() {
                @Override
                public void onChanged(EventType type) {
                    triggerListener(listener, type);
                }
            });
        }
    }

    private void triggerListener(Repository.OnChangedListener listener, Repository.OnChangedListener.EventType type) {
        if (type == Repository.OnChangedListener.EventType.Full) {
            loaded += 1;
        }
        if (loaded == loadNeed) {
            listener.onChanged(Repository.OnChangedListener.EventType.Full);
        } else {
            if (type == Repository.OnChangedListener.EventType.Full){
                type = Repository.OnChangedListener.EventType.Added;
            }
            listener.onChanged(type);
        }
    }

    public List<Menu> getNonValuedCollaboratedMenusOf(String currentUser) {
        List<Menu> menuList = new ArrayList<>();

        User user = findUserByEmail(currentUser);
        Set<String> collaboratedMenus = user.getParticipatedMenus().keySet();

        for (String k : collaboratedMenus) {
            if (!isValuedBy(k, currentUser) &&
                    getMenu(k).getDateEnd().compareTo(Calendar.getInstance().getTime()) < 0) menuList.add(getMenu(k));
        }

        Collections.sort(menuList, new MenuComparator());

        return menuList;
    }

    public List<Menu> getValuedCollaboratedMenusOf(String currentUser) {
        List<Menu> menuList = new ArrayList<>();

        User user = findUserByEmail(currentUser);
        Set<String> collaboratedMenus = user.getParticipatedMenus().keySet();

        for (String k : collaboratedMenus) {
            if (isValuedBy(k, currentUser) &&
                    getMenu(k).getDateEnd().compareTo(Calendar.getInstance().getTime()) < 0) menuList.add(getMenu(k));
        }

        Collections.sort(menuList, new MenuComparator());

        return menuList;
    }


    public List<Menu> getCollaboratedMenusOf(String currentUser) {
        List<Menu> menuList = new ArrayList<>();
        User user = findUserByEmail(currentUser);
        Set<String> collaboratedMenus = user.getParticipatedMenus().keySet();
        for (String k : collaboratedMenus) menuList.add(getMenu(k));
        Collections.sort(menuList, new MenuComparator());
        return menuList;
    }

    public List<Menu> getCaducatedCollaboratedMenusOf(String currentUser) {
        List<Menu> menuList = new ArrayList<>();
        User user = findUserByEmail(currentUser);
        Set<String> collaboratedMenus = user.getParticipatedMenus().keySet();
        for (String k : collaboratedMenus) {
            if (getMenu(k).getDateEnd().compareTo(Calendar.getInstance().getTime()) < 0) menuList.add(getMenu(k));
        }
        Collections.sort(menuList, new MenuComparator());
        return menuList;
    }

    public boolean isValuedBy(String menuId, String user) {
        for (Valoration v : valorationRepository.all()) {
            if (v.getMenu().equals(menuId) && v.getGuest().equals(user)) return true;
        }
        return false;
    }

    public Valoration getValoration(String menuId, String user) {
        for (Valoration v : valorationRepository.all()) {
            if (v.getMenu().equals(menuId) && v.getGuest().equals(user)) return valorationRepository.get(v.getId());
        }
        return null;
    }

    public Valoration value(String menu, double points, String comment, String host, String guest){
        Valoration valoration = new Valoration();
        valoration.setMenu(menu);
        valoration.setPoints(points);
        valoration.setComment(comment);
        valoration.setGuest(guest);
        valoration.setHost(host);

        valoration = valorationRepository.insert(valoration);

        User hostUser = findUserByEmail(host);
        if (hostUser != null) {
            Double percent = Double.valueOf(hostUser.getValorationNumber()) / Double.valueOf(hostUser.getValorationNumber() + 1);
            hostUser.setValorationNumber(hostUser.getValorationNumber() + 1);
            hostUser.setValorationAverage(hostUser.getValorationAverage() * percent +
                    points * (1 - percent));
            userRepository.update(hostUser);
        }

        return valoration;
    }

    public Menu getMenu(String menuId) {
        
        Menu m = paymentMenuRepository.get(menuId);
        if(m == null){
            m = repository.get(menuId);
        }
        
        return m;
    }
}
