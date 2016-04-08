package dev.blunch.blunch.services;

import dev.blunch.blunch.domain.PaymentMenu;
import dev.blunch.blunch.utils.Repository;
import dev.blunch.blunch.utils.Service;

/**
 * Payment Menu Service Class
 * @author albert on 7/04/16.
 */
public class PaymentMenuService extends Service<PaymentMenu> {

    public PaymentMenuService(Repository<PaymentMenu> repository) {
        super(repository);
    }

}