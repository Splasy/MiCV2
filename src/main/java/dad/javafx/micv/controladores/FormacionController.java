package dad.javafx.micv.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.model.CV;
import dad.javafx.micv.model.Titulo;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

public class FormacionController implements Initializable {
	private ListProperty<Titulo> formacion = new SimpleListProperty<Titulo>();

	@FXML
    private BorderPane view;

	@FXML
	private TableView<Titulo> tableView;

	@FXML
	private TableColumn<Titulo, LocalDate> desdeColumn;

	@FXML
	private TableColumn<Titulo, LocalDate> hastaColumn;

	@FXML
	private TableColumn<Titulo, String> denominacionColumn;

	@FXML
	private TableColumn<Titulo, String> organizadorColumn;

	@FXML
	private Button addTituloButton;

	@FXML
	private Button cancelarButton;

	// Ventana para añadir
	@FXML
	private TextField denominacionText;

	@FXML
	private TextField organizadorText;

	@FXML
	private DatePicker datePickerDesde;

	@FXML
	private DatePicker datePickerHasta;

	private Stage stage;

	CV model = new CV();

	public FormacionController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormacionView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		formacion.addListener((o, ov, nv) -> onFormacionChanged(o, ov, nv));

		tableView.itemsProperty().bindBidirectional(model.formacionProperty());

		cancelarButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

		desdeColumn.setCellValueFactory(v -> v.getValue().desdeProperty());
		hastaColumn.setCellValueFactory(v -> v.getValue().hastaProperty());
		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		organizadorColumn.setCellValueFactory(v -> v.getValue().organizadorProperty());

		desdeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		hastaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		organizadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	private void onFormacionChanged(ObservableValue<? extends ObservableList<Titulo>> o, ObservableList<Titulo> ov,
			ObservableList<Titulo> nv) {
		if (ov != null) {
			tableView.itemsProperty().unbindBidirectional(formacion);
		}

		if (nv != null) {
			tableView.itemsProperty().bindBidirectional(formacion);
		}

	}

	@FXML
	void onCancelarFormacionButton(ActionEvent event) {
		stage.close();
	}

	@FXML
	void onCrearFormacionButton(ActionEvent event) {
		Titulo titulo = new Titulo();
		titulo.setDenominacion(denominacionText.getText());
		titulo.setOrganizador(organizadorText.getText());
		titulo.setDesde(datePickerDesde.getValue());
		titulo.setHasta(datePickerHasta.getValue());

		formacion.add(titulo);
		stage.close();
	}

	@FXML
	void onAddTituloAction(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddFormacionView.fxml"));
		loader.setController(this);
		loader.load();
		stage = new Stage();
		Scene scene = new Scene(loader.getRoot(), 600, 250);
		stage.setTitle("Añadir formación");
		// Para que no se pueda clickar en la ventana padre mientras esta está abierta
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(view.getScene().getWindow());
		stage.setScene(scene);
		((Stage) stage.getScene().getWindow()).getIcons().add(new Image("/images/cv64x64.png"));
		stage.setResizable(false);
		stage.showAndWait();
	}

	@FXML
	void onEliminarAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminar formación");
		alert.setContentText("¿Desea borrar esta formación?");
		Optional<ButtonType> a = alert.showAndWait();
		if (a.get().getText().equals("Aceptar")) {
			model.getFormacion().remove(tableView.getSelectionModel().getSelectedIndex());
		}
	}

	public BorderPane getView() {
		return view;
	}

	public final ListProperty<Titulo> formacionProperty() {
		return this.formacion;
	}

	public final ObservableList<Titulo> getFormacion() {
		return this.formacionProperty().get();
	}

	public final void setFormacion(final ObservableList<Titulo> formacion) {
		this.formacionProperty().set(formacion);
	}

}
