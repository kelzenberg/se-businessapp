package com.businessapp.fxgui;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.RentalDataIntf;
import com.businessapp.pojos.Rental;
import com.businessapp.pojos.Rental.RentalStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.util.*;

/**
 * FXML Controller class for Rental.fxml
 * 
 */
public class RentalFXMLController implements FXMLControllerIntf {
	private RentalDataIntf DS;

	/**
	 * FXML skeleton defined as:
	 * AnchorPane > GridPane > TableView	- GridPane as resizable container for TableView
	 * AnchorPane > HBox > Button			- buttons in footer area
	 * 
	 * Defined CSS style classes:
	 *   .tableview-rental-column-id
	 *   .tableview-rental-column-name
	 *   .tableview-rental-column-status
	 *   .tableview-rental-column-contacts
	 *   .tableview-rental-column-notes
	 *   .tableview-rental-column-notes-button
	 *   .tableview-rental-hbox
	 */

	@FXML
	private AnchorPane fxRental_AnchorPane;

	@FXML
	private GridPane fxRental_GridPane;

	@FXML
	private TableView<Rental> fxRental_TableView;

	@FXML
	private TableColumn<Rental,String> fxRental_TableCol_ID;


	@FXML
	private HBox fxRental_HBox;	// Bottom area container for buttons, search box, etc.

	/*
	 * TableView model.
	 */
	private final ObservableList<Rental> cellDataObservable = FXCollections.observableArrayList();

	private final String LABEL_ID		= "ID";
	private final String LABEL_PRODUCTID		= "Rental ID";
	private final String LABEL_CUSTOMERID		= "Customer ID";
	private final String LABEL_RESERVEDON	= "Reserved on";
	private final String LABEL_PICKEDUPON= "Picked up on";
	private final String LABEL_RENTEDUNTIL	= "Rented until";
	private final String LABEL_RETURNEDON	= "Returned on";
	private final String LABEL_PAID	= "Payment";
	private final String LABEL_REFS	= "Linked Rentals";
	private final String LABEL_STATUS	= "Status";
	private StringBuilder rentalRefsIds = new StringBuilder();


	@Override
	public void inject( ControllerIntf dep ) {
		this.DS = (RentalDataIntf) dep;
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
		// Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
		fxRental_HBox.prefWidthProperty().bind( ((AnchorPane) fxRental_AnchorPane).widthProperty().subtract( 12 ) );
		fxRental_HBox.prefHeightProperty().bind( ((AnchorPane) fxRental_AnchorPane).heightProperty() );

		fxRental_GridPane.prefWidthProperty().bind( ((AnchorPane) fxRental_AnchorPane).widthProperty().subtract( 16 ) );
		fxRental_GridPane.prefHeightProperty().bind( ((AnchorPane) fxRental_AnchorPane).heightProperty().subtract( 70 ) );

		/*
		 * Bottom area HBox extends from the top across the entire AnchorPane hiding
		 * GridPane/TableView underneath (depending on z-stacking order). This prevents
		 * Mouse events from being propagated to TableView.
		 * 
		 * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
		 * to the underlying GridPane/TableView layer (Mouse event "transparency").
		 */
		fxRental_HBox.setPickOnBounds( false );

		/*
		 * Visualize resizing propagation by colored bounding boxes.
		 */
		//fxRental_GridPane.setStyle( "-fx-border-color: red;" );
		//fxRental_HBox.setStyle( "-fx-border-color: blue;" );

		fxRental_HBox.getStyleClass().add( "tableview-rental-hbox" );


		/*
		 * Construct TableView columns.
		 * 
		 * TableView presents a row/column cell rendering of an ObservableList<Object>
		 * model. Each cell computes a "value" from the associated object property that
		 * defines how the object property is visualized in a TableView.
		 * See also: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
		 * 
		 * TableView columns define how object properties are visualized and cell values
		 * are computed.
		 * 
		 * In the simplest form, cell values are bound to object properties, which are
		 * public getter-names of the object class, and visualized in a cell as text.
		 * 
		 * More complex renderings such as with graphical elements, e.g. buttons in cells,
		 * require overloading of the built-in behavior in:
		 *   - CellValueFactory - used for simple object property binding.
		 *   - CellFactory - overriding methods allows defining complex cell renderings. 
		 * 
		 * Constructing a TableView means defining
		 *   - a ObservableList<Object> model
		 *   - columns with name, css-style and Cell/ValueFactory.
		 * 
		 * Variation 1: Initialize columns defined in FXML.
		 *  - Step 1: associate a .css class with column.
		 *  - Step 2: bind cell value to object property (must have public property getters,
		 *            getId(), getTitle()).
		 */
		fxRental_TableCol_ID.getStyleClass().add( "tableview-rental-column-id" );
		fxRental_TableCol_ID.setText( LABEL_ID );
		fxRental_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "id" ) );

		/*
		 * Variation 2: Programmatically construct TableView columns.
		 */
		TableColumn<Rental,String> tableCol_PRODUCTID = new TableColumn<>( LABEL_PRODUCTID );
		tableCol_PRODUCTID.getStyleClass().add( "tableview-rental-column-title" );
		tableCol_PRODUCTID.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			observable.set( r.getProductId() );
			return observable;
		});

		TableColumn<Rental,String> tableCol_CUSTOMERID = new TableColumn<>( LABEL_CUSTOMERID );
		tableCol_CUSTOMERID.getStyleClass().add( "tableview-rental-column-title" );
		tableCol_CUSTOMERID.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			observable.set( r.getCustomerId() );
			return observable;
		});

		TableColumn<Rental,String> tableCol_RESERVEDON = new TableColumn<>( LABEL_RESERVEDON );
		tableCol_RESERVEDON.getStyleClass().add( "tableview-rental-column-title" );
		tableCol_RESERVEDON.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			observable.set( r.getReservedOn().toString() );
			return observable;
		});

		TableColumn<Rental,String> tableCol_PICKEDUPON = new TableColumn<>( LABEL_PICKEDUPON );
		tableCol_PICKEDUPON.getStyleClass().add( "tableview-rental-column-title" );
		tableCol_PICKEDUPON.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			observable.set( r.getPickedUpOn().toString() );
			return observable;
		});

		TableColumn<Rental,String> tableCol_RENTEDUNTIL = new TableColumn<>( LABEL_RENTEDUNTIL );
		tableCol_RENTEDUNTIL.getStyleClass().add( "tableview-rental-column-title" );
		tableCol_RENTEDUNTIL.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			observable.set( r.getRentedUntil().toString() );
			return observable;
		});

		TableColumn<Rental,String> tableCol_RETURNEDON = new TableColumn<>( LABEL_RETURNEDON );
		tableCol_RETURNEDON.getStyleClass().add( "tableview-rental-column-title" );
		tableCol_RETURNEDON.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			observable.set( r.getReturnedOn().toString() );
			return observable;
		});

		TableColumn<Rental,String> tableCol_PAID = new TableColumn<>( LABEL_PAID );
		tableCol_PAID.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			observable.set( r.getPaid().toString() );
			if (r.getPaid()){
				tableCol_PAID.getStyleClass().add( "tableview-rental-column-paid" );
			} else {
				tableCol_PAID.getStyleClass().add("tableview-rental-column-notpaid");
			}
			return observable;
		});

		TableColumn<Rental,String> tableCol_REFS = new TableColumn<>( LABEL_REFS );
		tableCol_REFS.getStyleClass().add( "tableview-rental-column-title" );
		tableCol_REFS.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Rental r = cellData.getValue();
			HashMap<String, Rental> refs = r.getRentalRefs();
			for (String key : refs.keySet()) {
					rentalRefsIds.append(refs.get(key).getId()).append("\n");
			}
			observable.set( rentalRefsIds.toString().trim() );
			return observable;
		});

		TableColumn<Rental,String> tableCol_STATUS = new TableColumn<>( LABEL_STATUS );
		tableCol_STATUS.getStyleClass().add( "tableview-rental-column-status" );
		tableCol_STATUS.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			// Render status as 3-letter shortcut of Rental state enum.
			Rental r = cellData.getValue();
			observable.set( r.getStatus().name().substring( 0, 3 ) );
			return observable;
		});

		/*
		// TableColumn<Rental,String> tableCol_NOTES = new TableColumn<>( "Notes" );
		TableColumn<Rental,String> tableCol_NOTES = new TableColumn<>( LABEL_NOTES );
		tableCol_NOTES.getStyleClass().add( "tableview-rental-column-notes" );

		tableCol_NOTES.setCellFactory(

			// Complex rendering of Notes column as clickable button with number of notes indicator.
			new Callback<TableColumn<Rental,String>, TableCell<Rental, String>>() {

				@Override
				public TableCell<Rental, String> call( TableColumn<Rental, String> col ) {

					col.setCellValueFactory( cellData -> {
						Rental r = cellData.getValue();
						StringProperty observable = new SimpleStringProperty();
						observable.set( r.getId() );
						return observable;
					});

					TableCell<Rental, String> tc = new TableCell<Rental, String>() {
						final Button btn = new Button();

						@Override public void updateItem( final String item, final boolean empty ) {
							super.updateItem( item, empty );
							int rowIdx = getIndex();
							ObservableList<Rental> rent = fxRental_TableView.getItems();

							if( rowIdx >= 0 && rowIdx < rent.size() ) {
								Rental rental = rent.get( rowIdx );
								setGraphic( null );		// always clear, needed for refresh
								if( rental != null ) {
									btn.getStyleClass().add( "tableview-rental-column-notes-button" );
									List<LogEntry> nL = rental.getNotes();
									btn.setText( "notes: " + nL.size() );
									setGraphic( btn );	// set button as rendering of cell value
	
									//Event updateEvent = new ActionEvent();
									btn.setOnMouseClicked( event -> {
										String n = rental.getTitle();
										String label = ( n==null || n.length()==0 )? rental.getId() : n;
	
										PopupNotes popupNotes = new PopupNotes( label, nL );
	
										popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
											// Notification that List<Note> has been updated.
											// update button label [note: <count>]
											btn.setText( "notes: " + rental.getNotes().size() );
											// -> save node
											DS.updateRental( rental );
										});
	
										popupNotes.show();
									});
								}
							} else {
								//System.out.println( "OutOfBounds rowIdx() ==> " + rowIdx );
								setGraphic( null );		// reset button in other rows
							}
						}
					};
					return tc;
				}
			});
		*/

		// Add programmatically generated columns to TableView. Columns appear in order.
		fxRental_TableView.getColumns().clear();
		fxRental_TableView.getColumns().addAll( Arrays.asList(
			fxRental_TableCol_ID,
			tableCol_PRODUCTID,
			tableCol_CUSTOMERID,
			tableCol_RESERVEDON,
			tableCol_PICKEDUPON,
			tableCol_RENTEDUNTIL,
			tableCol_RETURNEDON,
			tableCol_PAID,
			tableCol_REFS,
			tableCol_STATUS

		));

		/*
		 * Define selection model that allows to select multiple rows.
		 */
		fxRental_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

		/*
		 * Allow horizontal column squeeze of TableView columns. Column width can be fixed
		 * with -fx-pref-width: 80px;
		 */
		fxRental_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


		/*
		 * Double-click on row: open update dialog.
		 */
		fxRental_TableView.setRowFactory( tv -> {
			TableRow<Rental> row = new TableRow<>();
			row.setOnMouseClicked( event -> {
				if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
					// Rental rowData = row.getItem();
					// fxRental_TableView.getSelectionModel().select( row.getIndex() );
					//table.getSelectionModel().select( Math.min( i, size - 1 ) );
					fxRental_Update();
				}
			});
			return row;
		});

		/*
		 * Load objects into TableView model.
		 */
		fxRental_TableView.getItems().clear();
		Collection<Rental> col = DS.findAllRentals();
		if( col != null ) {
			cellDataObservable.addAll( col );
		}
		fxRental_TableView.setItems( cellDataObservable );
	}

	@Override
	public void stop() {
	}


	@FXML
	void fxRental_Delete() {
		ObservableList<Rental> selection = fxRental_TableView.getSelectionModel().getSelectedItems();
		List<Rental> toDel = new ArrayList<Rental>();
		List<String> ids = new ArrayList<String>();
		for( Rental r : selection ) {
			toDel.add( r );
		}
		fxRental_TableView.getSelectionModel().clearSelection();
		for( Rental r : toDel ) {
			ids.add( r.getId() );
			// should not alter cellDataObservable while iterating over selection
			cellDataObservable.remove( r );
		}
		DS.deleteRentals( ids );
	}

	@FXML
	void fxRental_New() {
		Rental rental = DS.newRental( null , null, null);
		openUpdateDialog( rental, true );
	}

	@FXML
	void fxRental_Update() {
		Rental rental = fxRental_TableView.getSelectionModel().getSelectedItem();
		if( rental != null ) {
			openUpdateDialog( rental, false );
		//} else {
		//	System.err.println( "nothing selected." );
		}
	}

	@FXML
	void fxRental_Exit() {
		App.getInstance().stop();
	}


	/*
	 * Private helper methods.
	 */
	private final String SEP = ";";		// separates contacts in externalized String

	private void openUpdateDialog( Rental r, boolean newItem ) {
		List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
		String n = r.getId();
		String label = ( n==null || n.length()==0 )? r.getId() : n;

		PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
			new StringTestUpdateProperty( LABEL_ID, r.getId(), false ),
			new StringTestUpdateProperty( LABEL_PRODUCTID, r.getProductId(), false ),
			new StringTestUpdateProperty( LABEL_CUSTOMERID, r.getCustomerId(), false ),
			new StringTestUpdateProperty( LABEL_RESERVEDON, r.getReservedOn().toString(), false ),
			new StringTestUpdateProperty( LABEL_PICKEDUPON, r.getPickedUpOn().toString(), true ),
			new StringTestUpdateProperty( LABEL_RENTEDUNTIL, r.getRentedUntil().toString(), true ),
			new StringTestUpdateProperty( LABEL_RETURNEDON, r.getReturnedOn().toString(), true ),
			new StringTestUpdateProperty( LABEL_PAID, r.getPaid().toString(), true ),
			new StringTestUpdateProperty( LABEL_REFS, rentalRefsIds.toString(), false ),
			new StringTestUpdateProperty( LABEL_STATUS, r.getStatus().name(), true )
		));

		// called when "OK" button in EntityEntryDialog is pressed
		dialog.addEventHandler( ActionEvent.ACTION, event -> {
			updateObject( r, altered, newItem );
		});

		dialog.show();
	}

	private void updateObject( Rental rental, List<StringTestUpdateProperty> altered, boolean newItem ) {
		for( StringTestUpdateProperty dp : altered ) {
			String pName = dp.getName();
			String alteredValue = dp.getValue();
			//System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

			if( pName.equals( LABEL_PICKEDUPON ) ) {
				rental.setPickedUpOn( LocalDateTime.parse(alteredValue ));
			}
			if( pName.equals( LABEL_RENTEDUNTIL ) ) {
				rental.setRentedUntil( LocalDateTime.parse(alteredValue ));
			}
			if( pName.equals( LABEL_RETURNEDON ) ) {
				rental.setReturnedOn( LocalDateTime.parse(alteredValue ));
			}
			if( pName.equals( LABEL_PAID ) ) {
				rental.setPaid( Boolean.getBoolean(alteredValue) );
			}
			if( pName.equals( LABEL_STATUS ) ) {
				String av = alteredValue.toUpperCase();
				if( av.startsWith( "ACT" ) ) {
					rental.setStatus( RentalStatus.ACTIVE );
				}
				if( av.startsWith( "SUS" ) ) {
					rental.setStatus( RentalStatus.SUSPENDED );
				}
				if( av.startsWith( "TER" ) ) {
					rental.setStatus( RentalStatus.TERMINATED );
				}
			}
		}
		if( altered.size() > 0 ) {
			DS.updateRental( rental );	// update object in persistent store
			if( newItem ) {
				int last = cellDataObservable.size();
				cellDataObservable.add( last, rental );
			}
			// refresh TableView (trigger update
			fxRental_TableView.getColumns().get(0).setVisible(false);
			fxRental_TableView.getColumns().get(0).setVisible(true);

			altered.clear();	// prevent double save if multiple events fire
		}
	}

	/*
	private String contactsToString( List<String> con ) {
		StringBuffer sb = new StringBuffer();
		for( int i=0; i < con.size(); i++ ) {
			sb.append( con.get( i ) );
			if( i < con.size() - 1 ) {
				sb.append( SEP ).append( " " );
			}
		}
		return sb.toString();
	}
	*/

}
