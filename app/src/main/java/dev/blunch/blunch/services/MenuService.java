package dev.blunch.blunch.services;

import java.util.List;

import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Created by jmotger on 26/04/16.
 */
public class MenuService extends Service<CollaborativeMenu> {

    private final Repository<PaymentMenu> paymentMenuRepository;
    int loadNeed = 1;
    int loaded = 0;


    public MenuService(Repository<CollaborativeMenu> repository, Repository<PaymentMenu> paymentMenuRepository) {
        super(repository);
        this.paymentMenuRepository = paymentMenuRepository;
    }

    public List<CollaborativeMenu> getCollaborativeMenus() {
        return repository.all();
    }

    public List<PaymentMenu> getPaymentMenus() {
        return paymentMenuRepository.all();
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
