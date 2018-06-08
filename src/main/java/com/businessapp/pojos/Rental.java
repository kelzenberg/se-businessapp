package com.businessapp.pojos;

import com.businessapp.logic.IDGen;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Rental is an entity that represents a person (or a business)
 * to which a business activity can be associated.
 */
public class Rental implements EntityIntf {

    private static final long serialVersionUID = 1L;

    private static IDGen IDG = new IDGen("tt", IDGen.IDTYPE.NUM, 6);

    // Rental states.
    public enum RentalStatus {
        ACTIVE, SUSPENDED, TERMINATED
    }


    /*
     * Properties.
     */
    private String id = null;

    private LocalDateTime reservedOn = null;

    private LocalDateTime pickedUpOn = null;

    private LocalDateTime rentedUntil = null;

    private LocalDateTime returnedOn = null;

    private Boolean isPaid = null;

    private String customerId = null;

    private String productId = null;

    private HashMap<String, Rental> rentalRefs = new HashMap<String, Rental>();

    private RentalStatus status = RentalStatus.ACTIVE;


    /**
     * Private default constructor (required by JSON deserialization).
     */
    @SuppressWarnings("unused")
    private Rental() {
    }

    /**
     * Public constructor.
     *
     * @param id    if rental id is null, an id is generated for the new rental object.
     * @param reservedOn if reservedOn is null, local date time (time of creation) will be used
     * @param customerId customerId of customer<>product association
     * @param productId productId of customer<>product association
     */
    public Rental(String id, LocalDateTime reservedOn, String customerId, String productId) {
        this.id = id == null ? IDG.nextId() : id;
        this.reservedOn = reservedOn == null ? LocalDateTime.now() : reservedOn;
        this.customerId = customerId;
        this.productId = productId;
    }


    /**
     * Public getter/setter methods.
     */
    @Override
    public String getId() {
        return id;
    }

    public LocalDateTime getReservedOn() {
        return reservedOn;
    }

    public LocalDateTime getPickedUpOn() {
        return pickedUpOn;
    }

    public LocalDateTime getRentedUntil() {
        return rentedUntil;
    }

    public LocalDateTime getReturnedOn() {
        return returnedOn;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public HashMap<String, Rental> getRentalRefs() {
        return rentalRefs;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public Boolean isOverdue() {
        return (returnedOn == null || returnedOn.isAfter(rentedUntil)) && LocalDateTime.now().isAfter(rentedUntil);
    }

    public Rental setPickedUpOn(LocalDateTime pickedUpOn) {
        this.pickedUpOn = pickedUpOn;
        // when pickup happened before creation of rental object (e.g. rental got added later)
        if (pickedUpOn.isBefore(reservedOn)){
            reservedOn = pickedUpOn;
        }
        return this;
    }

    public Rental setRentedUntil(LocalDateTime rentedUntil) {
        this.rentedUntil = rentedUntil;
        return this;
    }

    public Rental setReturnedOn(LocalDateTime returnedOn) {
        this.returnedOn = returnedOn;
        return this;
    }

    public Rental setPaid(Boolean paid) {
        isPaid = paid;
        return this;
    }

    public HashMap<String, Rental> addRentalRef(Rental rentalRef) {
        if (rentalRef != null) {
            rentalRefs.put(rentalRef.getId(),rentalRef);
            return rentalRefs;
        }
        return null;
    }

    public Rental setStatus(RentalStatus status) {
        this.status = status;
        return this;
    }
}
