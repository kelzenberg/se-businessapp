package com.businessapp.pojos;

import com.businessapp.logic.IDGen;

import java.util.ArrayList;
import java.util.List;


/**
 * Product is an entity that represents a person (or a business)
 * to which a business activity can be associated.
 */
public class Product implements EntityIntf {

    private static final long serialVersionUID = 1L;

    private static IDGen IDG = new IDGen("tt", IDGen.IDTYPE.NUM, 6);

    // Product states.
    public enum ProductStatus {
        ACTIVE, SUSPENDED, TERMINATED
    }


    /*
     * Properties.
     */
    private String id = null;

    private String title = null;

    private String publisher = null;

    private List<LogEntry> notes = new ArrayList<LogEntry>();

    private ProductStatus status = ProductStatus.ACTIVE;


    /**
     * Private default constructor (required by JSON deserialization).
     */
    @SuppressWarnings("unused")
    private Product() {
    }

    /**
     * Public constructor.
     *
     * @param id    if product id is null, an id is generated for the new product object.
     * @param title product.
     */
    public Product(String id, String title, String publisher) {
        this.id = id == null ? IDG.nextId() : id;
        this.title = title;
        this.publisher = publisher;
        this.notes.add(new LogEntry("Product record created."));
    }


    /**
     * Public getter/setter methods.
     */
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public List<String> getNotesAsStringList() {
        List<String> res = new ArrayList<String>();
        for (LogEntry n : notes) {
            res.add(n.toString());
        }
        return res;
    }

    public List<LogEntry> getNotes() {
        return notes;
    }

    public Product setTitle(String title) {
        this.title = title;
        return this;
    }

    public Product setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public Product setStatus(ProductStatus status) {
        this.status = status;
        return this;
    }

    public void addNote(String logStr) {
        // TODO
    }

}
