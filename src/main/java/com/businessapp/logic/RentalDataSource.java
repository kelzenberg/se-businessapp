package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Rental;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

public class RentalDataSource implements RentalDataIntf {

    private final GenericEntityContainer<Rental> rentals;
    private PersistenceProviderIntf persistenceProvider = null;
    private Component parent;

    /**
     * Factory method that returns a CatalogItem data source. * @return new instance of data source.
     */
    public static RentalDataIntf getController(String name, PersistenceProviderIntf persistenceProvider) {
        RentalDataIntf rds = new RentalDataSource(name);
        rds.inject(persistenceProvider);
        return rds;
    }

    /**
     * Private constructor.
     */
    private RentalDataSource(String name) {
        this.rentals = new GenericEntityContainer<Rental>(name, Rental.class);
    }

    @Override
    public void inject(ControllerIntf dep) {
        if (dep instanceof PersistenceProviderIntf) {
            this.persistenceProvider = (PersistenceProviderIntf) dep;
        }
    }

    @Override
    public void inject(Component parent) {
        this.parent = parent;
    }

    @Override
    public void start() {
        if (persistenceProvider != null) {
            try {
                /*
                 * Attempt to load container from persistent storage.
                 */
                persistenceProvider.loadInto(rentals.getId(), entity -> {
                    this.rentals.store((Rental) entity);
                    return true;
                });
            } catch (IOException e) {
                RentalDataIntf mockDS = new RentalDataMockImpl();
                Component parent = new Component( rentals.getId(), null, null ); mockDS.inject( parent );
                mockDS.start();
                for( Rental mockRental : mockDS.findAllRentals() ) {
                    rentals.update( mockRental );
                }
                persistenceProvider.save(rentals, rentals.getId() );
            }
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public Rental findRentalById(String id) {
        return rentals.findById(id);
    }

    @Override
    public Collection<Rental> findAllRentals() {
        return rentals.findAll();
    }

    @Override
    public Rental newRental(LocalDateTime reservedOn, String customerId, String productId) {
        Rental r = new Rental(null, reservedOn, customerId, productId);
        rentals.update(r);
        if (persistenceProvider != null) {
            persistenceProvider.save(rentals, rentals.getId());
        }
        return r;
    }

    @Override
    public void updateRental(Rental r) {
        rentals.update(r);
        if (persistenceProvider != null) {
            persistenceProvider.save(rentals, rentals.getId());
        }
    }

    @Override
    public void deleteRentals(Collection<String> ids) {
        rentals.delete(ids);
        if (persistenceProvider != null) {
            persistenceProvider.save(rentals, rentals.getId());
        }
    }
}