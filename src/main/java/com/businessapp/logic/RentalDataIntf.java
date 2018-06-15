package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Rental;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Public interface to Rental data.
 *
 */
public interface RentalDataIntf extends ControllerIntf {

	/**
	 * Factory method that returns a Rental data source.
	 * @return new instance of Rental data source.
	 */
	public static RentalDataIntf getController() {
		return new RentalDataMockImpl();
	}

	/**
	 * Public access methods to Rental data.
	 */
	Rental findRentalById(String id); // READ

	public Collection<Rental> findAllRentals(); // READ

	public Rental newRental(LocalDateTime reservedOn, String customerId, String productId); // CREATE

	public void updateRental(Rental r); // UPDATE

	public void deleteRentals(Collection<String> ids); // DELETE

}
