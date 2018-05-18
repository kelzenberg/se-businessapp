package com.businessapp.fxgui;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.ProductDataIntf;
import com.businessapp.pojos.Product;
import com.businessapp.pojos.Product.ProductStatus;
import com.businessapp.pojos.LogEntry;
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
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * FXML Controller class for Product.fxml
 * 
 */
public class ProductFXMLController implements FXMLControllerIntf {
	private ProductDataIntf DS;

	/**
	 * FXML skeleton defined as:
	 * AnchorPane > GridPane > TableView	- GridPane as resizable container for TableView
	 * AnchorPane > HBox > Button			- buttons in footer area
	 * 
	 * Defined CSS style classes:
	 *   .tableview-product-column-id
	 *   .tableview-product-column-name
	 *   .tableview-product-column-status
	 *   .tableview-product-column-contacts
	 *   .tableview-product-column-notes
	 *   .tableview-product-column-notes-button
	 *   .tableview-product-hbox
	 */

	@FXML
	private AnchorPane fxProduct_AnchorPane;

	@FXML
	private GridPane fxProduct_GridPane;

	@FXML
	private TableView<Product> fxProduct_TableView;

	@FXML
	private TableColumn<Product,String> fxProduct_TableCol_ID;


	@FXML
	private HBox fxProduct_HBox;	// Bottom area container for buttons, search box, etc.

	/*
	 * TableView model.
	 */
	private final ObservableList<Product> cellDataObservable = FXCollections.observableArrayList();

	private final String LABEL_ID		= "IMDb ID";
	private final String LABEL_TITLE	= "Titel";
	private final String LABEL_PUBLISHER= "Publisher";
	private final String LABEL_STATUS	= "Status";
	private final String LABEL_NOTES	= "Anmerk.";


	@Override
	public void inject( ControllerIntf dep ) {
		this.DS = (ProductDataIntf)dep;
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
		// Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
		fxProduct_HBox.prefWidthProperty().bind( ((AnchorPane) fxProduct_AnchorPane).widthProperty().subtract( 12 ) );
		fxProduct_HBox.prefHeightProperty().bind( ((AnchorPane) fxProduct_AnchorPane).heightProperty() );

		fxProduct_GridPane.prefWidthProperty().bind( ((AnchorPane) fxProduct_AnchorPane).widthProperty().subtract( 16 ) );
		fxProduct_GridPane.prefHeightProperty().bind( ((AnchorPane) fxProduct_AnchorPane).heightProperty().subtract( 70 ) );

		/*
		 * Bottom area HBox extends from the top across the entire AnchorPane hiding
		 * GridPane/TableView underneath (depending on z-stacking order). This prevents
		 * Mouse events from being propagated to TableView.
		 * 
		 * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
		 * to the underlying GridPane/TableView layer (Mouse event "transparency").
		 */
		fxProduct_HBox.setPickOnBounds( false );

		/*
		 * Visualize resizing propagation by colored bounding boxes.
		 */
		//fxProduct_GridPane.setStyle( "-fx-border-color: red;" );
		//fxProduct_HBox.setStyle( "-fx-border-color: blue;" );

		fxProduct_HBox.getStyleClass().add( "tableview-product-hbox" );


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
		fxProduct_TableCol_ID.getStyleClass().add( "tableview-product-column-id" );
		fxProduct_TableCol_ID.setText( LABEL_ID );
		fxProduct_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "id" ) );

		/*
		 * Variation 2: Programmatically construct TableView columns.
		 */
		TableColumn<Product,String> tableCol_TITLE = new TableColumn<>( LABEL_TITLE );
		tableCol_TITLE.getStyleClass().add( "tableview-product-column-title" );
		tableCol_TITLE.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Product p = cellData.getValue();
			observable.set( p.getTitle() );
			return observable;
		});

		TableColumn<Product,String> tableCol_PUBLISHER = new TableColumn<>( LABEL_PUBLISHER );
		tableCol_PUBLISHER.getStyleClass().add( "tableview-product-column-publisher" );
		tableCol_PUBLISHER.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Product p = cellData.getValue();
			observable.set( p.getPublisher() );
			return observable;
		});

		TableColumn<Product,String> tableCol_STATUS = new TableColumn<>( LABEL_STATUS );
		tableCol_STATUS.getStyleClass().add( "tableview-product-column-status" );
		tableCol_STATUS.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			// Render status as 3-letter shortcut of Product state enum.
			Product p = cellData.getValue();
			observable.set( p.getStatus().name().substring( 0, 3 ) );
			return observable;
		});

		// TableColumn<Product,String> tableCol_NOTES = new TableColumn<>( "Notes" );
		TableColumn<Product,String> tableCol_NOTES = new TableColumn<>( LABEL_NOTES );
		tableCol_NOTES.getStyleClass().add( "tableview-product-column-notes" );

		tableCol_NOTES.setCellFactory(

			// Complex rendering of Notes column as clickable button with number of notes indicator.
			new Callback<TableColumn<Product,String>, TableCell<Product, String>>() {

				@Override
				public TableCell<Product, String> call( TableColumn<Product, String> col ) {

					col.setCellValueFactory( cellData -> {
						Product p = cellData.getValue();
						StringProperty observable = new SimpleStringProperty();
						observable.set( p.getId() );
						return observable;
					});

					TableCell<Product, String> tc = new TableCell<Product, String>() {
						final Button btn = new Button();

						@Override public void updateItem( final String item, final boolean empty ) {
							super.updateItem( item, empty );
							int rowIdx = getIndex();
							ObservableList<Product> prod = fxProduct_TableView.getItems();

							if( rowIdx >= 0 && rowIdx < prod.size() ) {
								Product product = prod.get( rowIdx );
								setGraphic( null );		// always clear, needed for refresh
								if( product != null ) {
									btn.getStyleClass().add( "tableview-product-column-notes-button" );
									List<LogEntry> nL = product.getNotes();
									btn.setText( "notes: " + nL.size() );
									setGraphic( btn );	// set button as rendering of cell value
	
									//Event updateEvent = new ActionEvent();
									btn.setOnMouseClicked( event -> {
										String n = product.getTitle();
										String label = ( n==null || n.length()==0 )? product.getId() : n;
	
										PopupNotes popupNotes = new PopupNotes( label, nL );
	
										popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
											// Notification that List<Note> has been updated.
											// update button label [note: <count>]
											btn.setText( "notes: " + product.getNotes().size() );
											// -> save node
											DS.updateProduct( product );
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

		// Add programmatically generated columns to TableView. Columns appear in order.
		fxProduct_TableView.getColumns().clear();
		fxProduct_TableView.getColumns().addAll( Arrays.asList(
			fxProduct_TableCol_ID,
			tableCol_TITLE,
			tableCol_PUBLISHER,
			tableCol_STATUS,
			tableCol_NOTES
		));

		/*
		 * Define selection model that allows to select multiple rows.
		 */
		fxProduct_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

		/*
		 * Allow horizontal column squeeze of TableView columns. Column width can be fixed
		 * with -fx-pref-width: 80px;
		 */
		fxProduct_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


		/*
		 * Double-click on row: open update dialog.
		 */
		fxProduct_TableView.setRowFactory( tv -> {
			TableRow<Product> row = new TableRow<>();
			row.setOnMouseClicked( event -> {
				if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
					// Product rowData = row.getItem();
					// fxProduct_TableView.getSelectionModel().select( row.getIndex() );
					//table.getSelectionModel().select( Math.min( i, size - 1 ) );
					fxProduct_Update();
				}
			});
			return row;
		});

		/*
		 * Load objects into TableView model.
		 */
		fxProduct_TableView.getItems().clear();
		Collection<Product> col = DS.findAllProducts();
		if( col != null ) {
			cellDataObservable.addAll( col );
		}
		fxProduct_TableView.setItems( cellDataObservable );
	}

	@Override
	public void stop() {
	}


	@FXML
	void fxProduct_Delete() {
		ObservableList<Product> selection = fxProduct_TableView.getSelectionModel().getSelectedItems();
		List<Product> toDel = new ArrayList<Product>();
		List<String> ids = new ArrayList<String>();
		for( Product p : selection ) {
			toDel.add( p );
		}
		fxProduct_TableView.getSelectionModel().clearSelection();
		for( Product p : toDel ) {
			ids.add( p.getId() );
			// should not alter cellDataObservable while iterating over selection
			cellDataObservable.remove( p );
		}
		DS.deleteProducts( ids );
	}

	@FXML
	void fxProduct_New() {
		Product product = DS.newProduct( null , null);
		openUpdateDialog( product, true );
	}

	@FXML
	void fxProduct_Update() {
		Product product = fxProduct_TableView.getSelectionModel().getSelectedItem();
		if( product != null ) {
			openUpdateDialog( product, false );
		//} else {
		//	System.err.println( "nothing selected." );
		}
	}

	@FXML
	void fxProduct_Exit() {
		App.getInstance().stop();
	}


	/*
	 * Private helper methods.
	 */
	private final String SEP = ";";		// separates contacts in externalized String

	private void openUpdateDialog( Product p, boolean newItem ) {
		List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
		String n = p.getTitle();
		String label = ( n==null || n.length()==0 )? p.getId() : n;

		PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
			new StringTestUpdateProperty( LABEL_ID, p.getId(), false ),
			new StringTestUpdateProperty( LABEL_TITLE, p.getTitle(), true ),
			new StringTestUpdateProperty( LABEL_PUBLISHER, p.getPublisher(), true ),
			new StringTestUpdateProperty( LABEL_STATUS, p.getStatus().name(), true )
		));

		// called when "OK" button in EntityEntryDialog is pressed
		dialog.addEventHandler( ActionEvent.ACTION, event -> {
			updateObject( p, altered, newItem );
		});

		dialog.show();
	}

	private void updateObject( Product product, List<StringTestUpdateProperty> altered, boolean newItem ) {
		for( StringTestUpdateProperty dp : altered ) {
			String pName = dp.getName();
			String alteredValue = dp.getValue();
			//System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

			if( pName.equals( LABEL_TITLE ) ) {
				product.setTitle( alteredValue );
			}
			if( pName.equals( LABEL_PUBLISHER ) ) {
				product.setPublisher( alteredValue );
			}
			if( pName.equals( LABEL_STATUS ) ) {
				String av = alteredValue.toUpperCase();
				if( av.startsWith( "ACT" ) ) {
					product.setStatus( ProductStatus.ACTIVE );
				}
				if( av.startsWith( "SUS" ) ) {
					product.setStatus( ProductStatus.SUSPENDED );
				}
				if( av.startsWith( "TER" ) ) {
					product.setStatus( ProductStatus.TERMINATED );
				}
			}
		}
		if( altered.size() > 0 ) {
			DS.updateProduct( product );	// update object in persistent store
			if( newItem ) {
				int last = cellDataObservable.size();
				cellDataObservable.add( last, product );
			}
			// refresh TableView (trigger update
			fxProduct_TableView.getColumns().get(0).setVisible(false);
			fxProduct_TableView.getColumns().get(0).setVisible(true);

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
