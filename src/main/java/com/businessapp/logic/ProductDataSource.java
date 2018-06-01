package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Product;

import java.io.IOException;
import java.util.Collection;

public class ProductDataSource implements ProductDataIntf {

    private final GenericEntityContainer<Product> products;
    private PersistenceProviderIntf persistenceProvider = null;
    private Component parent;

    /**
     * Factory method that returns a CatalogItem data source. * @return new instance of data source.
     */
    public static ProductDataIntf getController(String name, PersistenceProviderIntf persistenceProvider) {
        ProductDataIntf pds = new ProductDataSource(name);
        pds.inject(persistenceProvider);
        return pds;
    }

    /**
     * Private constructor.
     */
    private ProductDataSource(String name) {
        this.products = new GenericEntityContainer<Product>(name, Product.class);
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
                persistenceProvider.loadInto(products.getId(), entity -> {
                    this.products.store((Product) entity);
                    return true;
                });
            } catch (IOException e) {
                ProductDataIntf mockDS = new ProductDataMockImpl();
                Component parent = new Component( products.getId(), null, null ); mockDS.inject( parent );
                mockDS.start();
                for( Product mockProduct : mockDS.findAllProducts() ) {
                    products.update( mockProduct );
                }
                persistenceProvider.save( products, products.getId() );
            }
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public Product findProductById(String id) {
        return products.findById(id);
    }

    @Override
    public Collection<Product> findAllProducts() {
        return products.findAll();
    }

    @Override
    public Product newProduct(String title, String publisher) {
        Product p = new Product(null, title, publisher);
        products.update(p);
        if (persistenceProvider != null) {
            persistenceProvider.save(products, products.getId());
        }
        return p;
    }

    @Override
    public void updateProduct(Product p) {
        products.update(p);
        if (persistenceProvider != null) {
            persistenceProvider.save(products, products.getId());
        }
    }

    @Override
    public void deleteProducts(Collection<String> ids) {
        products.delete(ids);
        if (persistenceProvider != null) {
            persistenceProvider.save(products, products.getId());
        }
    }
}