package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;

import dev.blunch.blunch.domain.Valoration;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Created by pere on 5/3/16.
 */
public class ValorationRepository extends FirebaseRepository<Valoration> {

    public ValorationRepository(Context context) {
        super(context);
    }

    @Override
    protected Valoration convert(DataSnapshot data) {
        Valoration valoration = new Valoration();
        valoration.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("points".equals(d.getKey())) {
                valoration.setPoints(d.getValue(Double.class));
            }else if ("comment".equals(d.getKey())) {
                valoration.setComment(d.getValue(String.class));
            }else if ("menu".equals(d.getKey())) {
                valoration.setMenu(d.getValue(String.class));
            }else if ("host".equals(d.getKey())) {
                valoration.setHost(d.getValue(String.class));
            }else if ("guest".equals(d.getKey())) {
                valoration.setGuest(d.getValue(String.class));
            }
        }
        return valoration;

    }

    @Override
    public String getObjectReference() {
        return "valoration";
    }

}
