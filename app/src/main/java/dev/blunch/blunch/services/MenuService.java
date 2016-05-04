package dev.blunch.blunch.services;

import android.util.Log;

import com.firebase.client.core.Repo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.MenuComparator;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Created by jmotger on 26/04/16.
 */
public class MenuService extends Service<CollaborativeMenu> {

    private final Repository<PaymentMenu> paymentMenuRepository;
    private final Repository<User> userRepository;
    int loadNeed = 1;
    int loaded = 0;

    public MenuService(Repository<CollaborativeMenu> repository, Repository<PaymentMenu> paymentMenuRepository,
                       Repository<User> userRepository) {
        super(repository);
        this.paymentMenuRepository = paymentMenuRepository;
        this.userRepository = userRepository;
    }

    public List<CollaborativeMenu> getCollaborativeMenus() {
        return repository.all();
    }

    public List<PaymentMenu> getPaymentMenus() {
        return paymentMenuRepository.all();
    }

    public List<User> getUsers() { return userRepository.all(); }

    public User findUserByEmail(String email) {
        return userRepository.get(email);
    }

    public User createNewUser(User user) {
        user.setId(user.getId().split("\\.")[0]);
        return userRepository.update(user);
    }

    public List<Menu> getMenus() {
        List<Menu> menus = new ArrayList<>();
        menus.addAll(repository.all());
        menus.addAll(paymentMenuRepository.all());
        return menus;
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
}
