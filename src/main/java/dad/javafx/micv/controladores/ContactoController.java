package dad.javafx.micv.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.model.CV;
import dad.javafx.micv.model.Contacto;
import dad.javafx.micv.model.Email;
import dad.javafx.micv.model.Telefono;
import dad.javafx.micv.model.TipoTelefono;
import dad.javafx.micv.model.Web;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ContactoController implements Initializable {
	private ObjectProperty<Contacto> contacto = new SimpleObjectProperty<>();

	@FXML
	private TableView<Telefono> tablaTelefonos;

	@FXML
	private TableColumn<Telefono, String> numeroColumn;

	@FXML
	private TableColumn<Telefono, TipoTelefono> tipoColumn;

	@FXML
	private AnchorPane view;

	@FXML
	private Button addTlfButton;

	@FXML
	private Button delTlfButton;

	@FXML
	private TableView<Email> tablaCorreo;

	@FXML
	private TableColumn<Email, String> correoColumn;

	@FXML
	private Button addEmailButton;

	@FXML
	private Button delEmailButton;

	@FXML
	private TableView<Web> tablaWebs;

	@FXML
	private TableColumn<Web, String> urlColumn;
	@FXML
	private Button addURLButton;

	@FXML
	private Button delURLButton;

	CV model = new CV();

	public CV getModel() {
		return model;
	}

	public ContactoController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactoView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		contacto.addListener((o, ov, nv) -> onContactChanged(o, ov, nv));
		
		// Bindeo de los objetos de la listView
		tablaTelefonos.itemsProperty().bindBidirectional(model.getContacto().telefonoProperty());
		tablaCorreo.itemsProperty().bindBidirectional(model.getContacto().EmailProperty());
		tablaWebs.itemsProperty().bindBidirectional(model.getContacto().websProperty());

		delTlfButton.disableProperty().bind(tablaTelefonos.getSelectionModel().selectedItemProperty().isNull());
		delEmailButton.disableProperty().bind(tablaCorreo.getSelectionModel().selectedItemProperty().isNull());
		delURLButton.disableProperty().bind(tablaWebs.getSelectionModel().selectedItemProperty().isNull());

		// Bindeo de los Cell value factory
		// tlf
		numeroColumn.setCellValueFactory(v -> v.getValue().telefonoProperty());
		tipoColumn.setCellValueFactory(v -> v.getValue().tipoProperty());
		// email
		correoColumn.setCellValueFactory(v -> v.getValue().direccionProperty());
		// web
		urlColumn.setCellValueFactory(v -> v.getValue().urlProperty());

		// Cell factories
		// tlf
		numeroColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		tipoColumn.setCellFactory(ComboBoxTableCell.forTableColumn(TipoTelefono.values()));
		// correo
		correoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		// web
		urlColumn.setCellFactory(TextFieldTableCell.forTableColumn());

	}
	//Listener para dejar todo vacío al crear nuevo cv
	private void onContactChanged(ObservableValue<? extends Contacto> o, Contacto ov, Contacto nv) {
		if(ov != null) {
			tablaTelefonos.itemsProperty().unbindBidirectional(ov.telefonoProperty());
			tablaCorreo.itemsProperty().unbindBidirectional(ov.EmailProperty());
			tablaWebs.itemsProperty().unbindBidirectional(ov.websProperty());
			
		}
		
		if(nv != null) {
			tablaTelefonos.itemsProperty().bindBidirectional(nv.telefonoProperty());
			tablaCorreo.itemsProperty().bindBidirectional(nv.EmailProperty());
			tablaWebs.itemsProperty().bindBidirectional(nv.websProperty());
		}
	}

	@FXML
	void onAddTelefono(ActionEvent event) {
		Dialog<Pair<String, TipoTelefono>> nuevoTelefono = new Dialog<>();
		((Stage) nuevoTelefono.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/images/cv64x64.png"));
		nuevoTelefono.setTitle("Nuevo teléfono");
		nuevoTelefono.setHeaderText("Introduzca el nuevo número de teléfono.");

		TextField telefonoText = new TextField();
		telefonoText.setPromptText("Número de teléfono");
		telefonoText.setPrefWidth(150);

		ComboBox<TipoTelefono> tipo = new ComboBox<>();
		tipo.getItems().addAll(TipoTelefono.values());
		tipo.setPromptText("Seleccione un  Tipo");

		// Añade los botones dándoles el tipo
		ButtonType anadirButtonType = new ButtonType("Añadir", ButtonData.OK_DONE);
		nuevoTelefono.getDialogPane().getButtonTypes().addAll(anadirButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		grid.add(new Label("Número"), 0, 0);
		grid.add(telefonoText, 1, 0);
		grid.add(new Label("Tipo"), 0, 1);
		grid.add(tipo, 1, 1);

		nuevoTelefono.getDialogPane().setContent(grid);

		// Prueba cerrar ventana
		nuevoTelefono.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		Node closeButton = nuevoTelefono.getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.managedProperty().bind(closeButton.visibleProperty());
		closeButton.setVisible(false);
		
		//Es necesario para retornar un resultado de tipo int
		nuevoTelefono.setResultConverter(dialogButton -> {
			try {
				Integer.parseInt(telefonoText.getText());
			} catch (NumberFormatException e) {
				return null;
			}
			if (tipo.getValue() == null)
				return null;
			return new Pair<>(telefonoText.getText(), tipo.getValue());
		});

		Optional<Pair<String, TipoTelefono>> result = nuevoTelefono.showAndWait();

		if (!result.isEmpty()) {
			Telefono aux = new Telefono();
			aux.setTelefono(result.get().getKey());
			aux.setTipo(result.get().getValue());
			model.getContacto().getTelefono().add(aux);
		}
	}

	@FXML
	void onDeleteTelefono(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminar Teléfono");
		alert.setContentText("¿Desea borrar este teléfono?");
		Optional<ButtonType> a = alert.showAndWait();
		if (a.get().getText().equals("Aceptar")) {
			model.getContacto().getTelefono().remove(tablaTelefonos.getSelectionModel().getSelectedIndex());
		}
	}

	@FXML
	void onAddCorreo(ActionEvent event) {
		TextInputDialog dialogCorreo = new TextInputDialog();
		dialogCorreo.setTitle("Nuevo e-mail");
		dialogCorreo.setHeaderText("Crea una nueva dirección de correo");
		dialogCorreo.setContentText("E-mail");

		Optional<String> result = dialogCorreo.showAndWait();
		try {
			if (!result.get().equals("")) {
				Email aux = new Email();
				aux.setDireccion(result.get());

				model.getContacto().getEmail().add(aux);

			}
		} catch (NoSuchElementException e) {
		}
	}

	@FXML
	void onDeleteCorreo(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminar E-mail");
		alert.setContentText("¿Desea borrar este E-mail?");
		Optional<ButtonType> a = alert.showAndWait();
		if (a.get().getText().equals("Aceptar")) {
			model.getContacto().getEmail().remove(tablaCorreo.getSelectionModel().getSelectedIndex());
		}
	}

	@FXML
	void onAddURL(ActionEvent event) {
		TextInputDialog dialogURL = new TextInputDialog("http://");
		dialogURL.setTitle("Nueva web");
		dialogURL.setHeaderText("Crea una nueva dirección web");
		dialogURL.setContentText("URL: ");

		Optional<String> result = dialogURL.showAndWait();
		try {
			if (!result.get().equals("http://") && !result.get().equals("")) {
				Web aux = new Web();
				aux.setUrl(result.get());

				model.getContacto().getWebs().add(aux);

			}
		} catch (NoSuchElementException e) {
		}
	}

	@FXML
	void onDeleteURL(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminar Web");
		alert.setContentText("¿Desea borrar esta web?");
		Optional<ButtonType> a = alert.showAndWait();
		if (a.get().getText().equals("Aceptar")) {
			model.getContacto().getWebs().remove(tablaWebs.getSelectionModel().getSelectedIndex());
		}
	}

	public AnchorPane getView() {
		return view;
	}

	public final ObjectProperty<Contacto> contactoProperty() {
		return this.contacto;
	}
	

	public final Contacto getContacto() {
		return this.contactoProperty().get();
	}
	

	public final void setContacto(final Contacto contacto) {
		this.contactoProperty().set(contacto);
	}
	
}
