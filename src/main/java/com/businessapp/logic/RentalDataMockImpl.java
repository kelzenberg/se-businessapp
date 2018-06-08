package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Rental;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;


/**
 * Implementation of Rental data.
 */
class RentalDataMockImpl implements RentalDataIntf {

    private final HashMap<String, Rental> _data;    // HashMap as data container
    private final RentalDataIntf DS;                // Data Source/Data Store Intf
    private Component parent;                        // parent component

    /**
     * Constructor.
     */
    RentalDataMockImpl() {
        this._data = new HashMap<String, Rental>();
        this.DS = this;
    }

    /**
     * Dependency injection methods.
     */
    @Override
    public void inject(ControllerIntf dep) {
    }

    @Override
    public void inject(Component parent) {
        this.parent = parent;
    }

    /**
     * Start.
     */
    @Override
    public void start() {

        String name = parent.getName();
        if (name.equals("Ausleihe")) {
            // Rental list 1
            Rental rental1 = DS.newRental(null,"C.GEMJAQ","tt592510");
            Rental rental2 = DS.newRental(null, "C.GEMJAQ","tt191614");
            rental1.addRentalRef(rental2);
            DS.newRental(null, "C.2KTAAA", "tt242787");
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public Rental findRentalById(String id) {
        return _data.get(id);
    }

    @Override
    public Collection<Rental> findAllRentals() {
        return _data.values();
    }

    @Override
    public Rental newRental(LocalDateTime reservedOn, String customerId, String productId) {
        Rental r = new Rental(null, reservedOn, customerId, productId);
        _data.put(r.getId(), r);
        //save( "created: ", c );
        return r;
    }

    @Override
    public void updateRental(Rental r) {
        String msg = "updated: ";
        if (r != null) {
            Rental p2 = _data.get(r.getId());
            if (r != p2) {
                if (p2 != null) {
                    _data.remove(p2.getId());
                }
                msg = "created: ";
                _data.put(r.getId(), r);
            }
            //save( msg, c );
            System.err.println(msg + r.getId());
        }
    }

    @Override
    public void deleteRentals(Collection<String> ids) {
        String showids = "";
        for (String id : ids) {
            _data.remove(id);
            showids += (showids.length() == 0 ? "" : ", ") + id;
        }
        if (ids.size() > 0) {
            //save( "deleted: " + idx, customers );
            System.err.println("deleted: " + showids);
        }
    }

}
