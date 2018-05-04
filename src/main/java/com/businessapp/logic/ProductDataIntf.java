package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Product;

import java.util.Collection;

/**
 * Public interface to Product data.
 *
 */
public interface ProductDataIntf extends ControllerIntf {

	/**
	 * Factory method that returns a Product data source.
	 * @return new instance of Product data source.
	 */
	public static ProductDataIntf getController() {
		return new ProductDataMockImpl();
	}

	/**
	 * Public access methods to Product data.
	 */
	Product findProductById(String id);

	public Collection<Product> findAllProducts();

	public Product newProduct(String title, String publisher);

	public void updateProduct(Product prod);

	public void deleteProducts(Collection<String> ids);

}
