package com.businessapp.persistence.dto;

import com.businessapp.pojos.Product;
import com.businessapp.pojos.LogEntry;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;


/**
 * Private JSON DTO (Data Access Object) class that is associated with Product Pojo.
 * 
 */
class ProductJSON extends Product implements JSONIntf {
	private static final long serialVersionUID = 1L;

	/**
	 * Required by JSON de-serialization.
	 */
	private ProductJSON() {
		super(null, null, null);
		this.getNotes().clear();
	}


	/**
	 * Public copy constructor to create JSON DTO from original POJO.
	 * @param p copied Product object.
	 */
	public ProductJSON(Product p ) {
		super( p.getId(), p.getTitle(), p.getPublisher() );
		this.getNotes().clear();
		for( LogEntry le : p.getNotes() ) {
			this.getNotes().add( le );
		}
		this.setStatus( p.getStatus() );
	}

	/**
	 * Public method to create original POJO from JSON DTO.
	 * @return Product POJO.
	 */
	@JsonIgnore
	public Product getProduct() {
		Product p = new Product( this.getId(), this.getTitle(), this.getPublisher() );
		p.getNotes().clear();
		for( LogEntry le : this.getNotes() ) {
			p.getNotes().add( le );
		}
		p.setStatus( this.getStatus() );
		return p;
	}


	/**
	 * Custom Json-Serializer for 'notes' property.
	 * Maps notes as LogEntry array to String list.
	 * @return notes as String list.
	 */
	@JsonGetter("notes")
	public List<String> getNotesAsStringList() {
		List<String>res = new ArrayList<String>();
		for( LogEntry n : getNotes() ) {
			res.add( n.toString() );
		}
		return res;
	}

	/**
	 * Custom Json-de-Serializer for 'notes' property.
	 * Maps notes from String list to LogEntry array.
	 * @param notesAsStr notes as String list.
	 * @return self reference. 
	 */
	@JsonSetter("notes")
	public Product setNotesAsStringList( String[] notesAsStr ) {
		for( String noteAsStr : notesAsStr ) {
			LogEntry note = new LogEntry( noteAsStr );
			getNotes().add( note );
		}
		return this;
	}

}
