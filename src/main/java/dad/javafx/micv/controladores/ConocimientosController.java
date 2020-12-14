package dad.javafx.micv.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.model.CV;
import dad.javafx.micv.model.Conocimiento;
import dad.javafx.micv.model.Idioma;
import dad.javafx.micv.model.Nivel;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class ConocimientosController implements Initializable {
	private ListProperty<Conocimiento> conocimiento = new SimpleListProperty<Conocimiento>();
	
	@FXML
    private BorderPane view;

	@FXML
	private TableView<Conocimiento> tableView;

	@FXML
	private TableColumn<Conocimiento, String> denominacionColumn;

	@FXML
	private TableColumn<Conocimiento, Nivel> nivelColumn;

	@FXML
	private Button eliminarButton;

	// VentanaAddConocimiento
	@FXML
	private TextField denominacionTextCono;

	@FXML
	private ComboBox<Nivel> conocimientoCombo;

	// Ventana AddIdioma
	@FXML
	private TextField denominacionText;

	@FXML
	private ComboBox<Nivel> idiomaCombo;

	@FXML
	private TextField certificacionText;

	private Stage stage;

	CV model = new CV();

	public ConocimientosController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConocimientosView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		conocimiento.addListener((o, ov, nv) -> onConocimientoChanged(o, ov, nv));
		// Por qué tengo que meterlos cada 1 en un try catch para que ambos carguen lo
		// mismo? Si lo pongo sin try catch, solo carga los datos el primero que los
		// cargue
		try {
			conocimientoCombo.getItems().setAll(Nivel.values());
		} catch (NullPointerException e) {
		}
		try {
			idiomaCombo.getItems().setAll(Nivel.values());
		} catch (NullPointerException e) {
		}

		tableView.itemsProperty().bindBidirectional(model.conocimientosProperty());

		eliminarButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		nivelColumn.setCellValueFactory(v -> v.getValue().nivelProperty());

		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nivelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Nivel.values()));
	}

	private void onConocimientoChanged(ObservableValue<? extends ObservableList<Conocimiento>> o,
			ObservableList<Conocimiento> ov, ObservableList<Conocimiento> nv) {
		if (ov != null) {
			tableView.itemsProperty().unbindBidirectional(conocimiento);
		}

		if (nv != null) {
			tableView.itemsProperty().bindBidirectional(conocimiento);
		}

	}

	@FXML
	void onAddConocimientoAction(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddConocimientoView.fxml"));
		loader.setController(this);
		loader.load();

		stage = new Stage();
		Scene scene = new Scene(loader.getRoot(), 500, 100);
		stage.setTitle("Añadir experiencia");
		// Para que no se pueda clickar en la ventana padre mientras esta está abierta
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(view.getScene().getWindow());
		stage.setScene(scene);
		((Stage) stage.getScene().getWindow()).getIcons().add(new Image("/images/cv64x64.png"));
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	void onCancelarConocimientoAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void onCrearConocimientoAction(ActionEvent event) {
		Conocimiento datosCono = new Conocimiento();
		datosCono.setDenominacion(denominacionTextCono.getText());
		datosCono.setNivel(conocimientoCombo.getValue());

		conocimiento.add(datosCono);
	}

	@FXML
	void onAddIdiomaAction(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddIdiomaView.fxml"));
		loader.setController(this);
		loader.load();
		stage = new Stage();
		Scene scene = new Scene(loader.getRoot(), 500, 140);
		stage.setTitle("Añadir experiencia");
		// Para que no se pueda clickar en la ventana padre mientras esta está abierta
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(view.getScene().getWindow());
		stage.setScene(scene);
		((Stage) stage.getScene().getWindow()).getIcons().add(new Image("/images/cv64x64.png"));
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	void onCancelarIdiomaAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void onCrearIdiomaAction(ActionEvent event) {
		Idioma idioma = new Idioma();
		idioma.setCertificacion(certificacionText.getText());

		Conocimiento datosIdioma = new Conocimiento();
		datosIdioma.setDenominacion(denominacionText.getText());
		datosIdioma.setNivel(idiomaCombo.getValue());
		datosIdioma.setIdioma(idioma);

		conocimiento.add(datosIdioma);
	}

	@FXML
	void onEliminarAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminar conocimiento");
		alert.setContentText("¿Desea borrar este conocimiento?");
		Optional<ButtonType> a = alert.showAndWait();
		if (a.get().getText().equals("Aceptar")) {
			model.getConocimientos().remove(tableView.getSelectionModel().getSelectedIndex());
		}
	}

	@FXML
	void onVaciarCombo(ActionEvent event) {
		idiomaCombo.getSelectionModel().clearSelection();
	}

	@FXML
	void onVaciarCombo2(ActionEvent event) {
		conocimientoCombo.getSelectionModel().clearSelection();
	}
	
	public BorderPane getView() {
		return view;
	}

	public final ListProperty<Conocimiento> conocimientoProperty() {
		return this.conocimiento;
	}

	public final ObservableList<Conocimiento> getConocimiento() {
		return this.conocimientoProperty().get();
	}

	public final void setConocimiento(final ObservableList<Conocimiento> conocimiento) {
		this.conocimientoProperty().set(conocimiento);
	}

}
