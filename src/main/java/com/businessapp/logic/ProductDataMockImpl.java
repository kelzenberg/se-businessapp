package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Product;

import java.util.Collection;
import java.util.HashMap;


/**
 * Implementation of Product data.
 */
class ProductDataMockImpl implements ProductDataIntf {

    private final HashMap<String, Product> _data;    // HashMap as data container
    private final ProductDataIntf DS;                // Data Source/Data Store Intf
    private Component parent;                        // parent component

    /**
     * Constructor.
     */
    ProductDataMockImpl() {
        this._data = new HashMap<String, Product>();
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
        if (name.equals("Katalog")) {
            // Product list 1
            Product hitchhiker = DS.newProduct("The Hitchhiker's Guide to the Galaxy","Buena Vista Pictures");
            //DS.newProduct("Moritz Baumann").addContact("moritz@gmx.de");
            hitchhiker.addNote("");
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public Product findProductById(String id) {
        return _data.get(id);
    }

    @Override
    public Collection<Product> findAllProducts() {
        return _data.values();
    }

    @Override
    public Product newProduct(String title, String publisher) {
        Product p = new Product(null, title, publisher);
        _data.put(p.getId(), p);
        //save( "created: ", c );
        return p;
    }

    @Override
    public void updateProduct(Product p) {
        String msg = "updated: ";
        if (p != null) {
            Product p2 = _data.get(p.getId());
            if (p != p2) {
                if (p2 != null) {
                    _data.remove(p2.getId());
                }
                msg = "created: ";
                _data.put(p.getId(), p);
            }
            //save( msg, c );
            System.err.println(msg + p.getId());
        }
    }

    @Override
    public void deleteProducts(Collection<String> ids) {
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
