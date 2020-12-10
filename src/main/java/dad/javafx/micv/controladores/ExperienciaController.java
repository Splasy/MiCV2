package dad.javafx.micv.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.model.CV;
import dad.javafx.micv.model.Experiencia;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.fxml.Initializable;

public class ExperienciaController implements Initializable {
	private ListProperty<Experiencia> experiencia = new SimpleListProperty<Experiencia>();

	@FXML
    private BorderPane view;

	@FXML
	private Button addExeperienciaButton;

	@FXML
	private Button cancelarButton;

	@FXML
	private TableView<Experiencia> tableView;

	@FXML
	private TableColumn<Experiencia, LocalDate> desdeColumn;

	@FXML
	private TableColumn<Experiencia, LocalDate> hastaColumn;

	@FXML
	private TableColumn<Experiencia, String> denominacionColumn;

	@FXML
	private TableColumn<Experiencia, String> empleadorColumn;

	// Ventana para añadir
	@FXML
	private TextField denominacionText;

	@FXML
	private TextField empleadorText;

	@FXML
	private DatePicker datePickerDesde;

	@FXML
	private DatePicker datePickerHasta;

	private Stage stage;

	CV model = new CV();

	public ExperienciaController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExperienciaView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		experiencia.addListener((o, ov, nv) -> onExperienciaChanged(o, ov, nv));

		tableView.itemsProperty().bindBidirectional(model.experienciaProperty());

		cancelarButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

		desdeColumn.setCellValueFactory(v -> v.getValue().desdeProperty());
		hastaColumn.setCellValueFactory(v -> v.getValue().hastaProperty());
		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		empleadorColumn.setCellValueFactory(v -> v.getValue().empleadorProperty());

		desdeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		hastaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		empleadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

	}

	private void onExperienciaChanged(ObservableValue<? extends ObservableList<Experiencia>> o,
			ObservableList<Experiencia> ov, ObservableList<Experiencia> nv) {
		if (ov != null) {
			tableView.itemsProperty().unbindBidirectional(experiencia);
		}

		if (nv != null) {
			tableView.itemsProperty().bindBidirectional(experiencia);
		}

	}

	@FXML
	void onCancelarExperienciaAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void onCrearExperienciaAction(ActionEvent event) {
		Experiencia datos = new Experiencia();
		datos.setDesde(datePickerDesde.getValue());
		datos.setHasta(datePickerHasta.getValue());
		datos.setDenominacion(denominacionText.getText());
		datos.setEmpleador(empleadorText.getText());

		experiencia.add(datos);
	}

	@FXML
	void onAddExpAction(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddExperienciaView.fxml"));
		loader.setController(this);
		loader.load();

		stage = new Stage();
		Scene scene = new Scene(loader.getRoot(), 600, 300);
		stage.setTitle("Añadir experiencia");
		stage.initModality(Modality.WINDOW_MODAL);
		// Para que no se pueda clickar en la ventana padre mientras esta está abierta
		stage.initOwner(view.getScene().getWindow());
		stage.setScene(scene);
		((Stage) stage.getScene().getWindow()).getIcons().add(new Image("/images/cv64x64.png"));
		stage.setResizable(true);
		stage.show();
	}

	@FXML
	void onEliminarAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminar experiencia");
		alert.setContentText("¿Desea borrar esta experiencia?");
		Optional<ButtonType> a = alert.showAndWait();
		if (a.get().getText().equals("Aceptar")) {
			model.getExperiencia().remove(tableView.getSelectionModel().getSelectedIndex());
		}
	}

	public BorderPane getView() {
		return view;
	}

	public final ListProperty<Experiencia> experienciaProperty() {
		return this.experiencia;
	}

	public final ObservableList<Experiencia> getExperiencia() {
		return this.experienciaProperty().get();
	}

	public final void setExperiencia(final ObservableList<Experiencia> experiencia) {
		this.experienciaProperty().set(experiencia);
	}

}
