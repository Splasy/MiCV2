package dad.javafx.micv.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.model.CV;
import dad.javafx.micv.model.Nacionalidad;
import dad.javafx.micv.model.Personal;
import dad.javafx.micv.utils.CargarPaisesYNacionalidades;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PersonalController implements Initializable {

	// model

	private ObjectProperty<Personal> personal = new SimpleObjectProperty<>();
	private ObjectProperty<Nacionalidad> selected = new SimpleObjectProperty<Nacionalidad>();//Hace falta para eliminar el elemento seleccionado

	// view

	@FXML
	private GridPane view;

	@FXML
	private TextField identificacionText;

	@FXML
	private TextField nombreText;

	@FXML
	private TextField apellidosText;

	@FXML
	private DatePicker fechaNacimientoDate;

	@FXML
	private TextArea direccionText;

	@FXML
	private TextField codigoPostalText;

	@FXML
	private TextField localidadText;

	@FXML
	private ListView<Nacionalidad> nacionalidadesList;

	@FXML
	private ComboBox<String> paisCombo;

	@FXML
	private Button nuevaNacionalidadButton;

	@FXML
	private Button quitarNacionalidadButton;

	CV model = new CV();

	public PersonalController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PersonalView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		paisCombo.getItems().setAll(CargarPaisesYNacionalidades.cargarPaises());

		quitarNacionalidadButton.disableProperty()
				.bind(nacionalidadesList.getSelectionModel().selectedItemProperty().isNull());

		selected.bind(nacionalidadesList.getSelectionModel().selectedItemProperty());

//		model.getPersonal().identificacionProperty().bindBidirectional(identificacionText.textProperty());
//		model.getPersonal().nombreProperty().bindBidirectional(nombreText.textProperty());
//		model.getPersonal().apellidosProperty().bindBidirectional(apellidosText.textProperty());
//		model.getPersonal().fechaNacimientoProperty().bindBidirectional(fechaNacimientoDate.valueProperty());
//		model.getPersonal().direccionProperty().bindBidirectional(direccionText.textProperty());
//		model.getPersonal().codigoPostalProperty().bindBidirectional(codigoPostalText.textProperty());
//		model.getPersonal().localidadProperty().bindBidirectional(localidadText.textProperty());
//		model.getPersonal().paisProperty().bindBidirectional(paisCombo.valueProperty());
		model.getPersonal().nacionalidadesProperty().bindBidirectional(nacionalidadesList.itemsProperty());

		personal.addListener((o, ov, nv) -> onPersonalChanged(o, ov, nv));

	}

	private void onPersonalChanged(ObservableValue<? extends Personal> o, Personal ov, Personal nv) {

		System.out.println("ov=" + ov + "/nv=" + nv);

		if (ov != null) {

			identificacionText.textProperty().unbindBidirectional(ov.identificacionProperty());
			nombreText.textProperty().unbindBidirectional(ov.nombreProperty());
			apellidosText.textProperty().unbindBidirectional(ov.apellidosProperty());
			fechaNacimientoDate.valueProperty().unbindBidirectional(ov.fechaNacimientoProperty());
			direccionText.textProperty().unbindBidirectional(ov.direccionProperty());
			codigoPostalText.textProperty().unbindBidirectional(ov.codigoPostalProperty());
			localidadText.textProperty().unbindBidirectional(ov.localidadProperty());
			paisCombo.valueProperty().unbindBidirectional(ov.paisProperty());
			nacionalidadesList.itemsProperty().unbindBidirectional(ov.nacionalidadesProperty());

		}

		if (nv != null) {

			identificacionText.textProperty().bindBidirectional(nv.identificacionProperty());
			nombreText.textProperty().bindBidirectional(nv.nombreProperty());
			apellidosText.textProperty().bindBidirectional(nv.apellidosProperty());
			fechaNacimientoDate.valueProperty().bindBidirectional(nv.fechaNacimientoProperty());
			direccionText.textProperty().bindBidirectional(nv.direccionProperty());
			codigoPostalText.textProperty().bindBidirectional(nv.codigoPostalProperty());
			localidadText.textProperty().bindBidirectional(nv.localidadProperty());
			paisCombo.valueProperty().bindBidirectional(nv.paisProperty());
			nacionalidadesList.itemsProperty().bindBidirectional(nv.nacionalidadesProperty());

		}

	}

	public GridPane getView() {
		return view;
	}

	@FXML
	void onNuevaNacionalidadAction(ActionEvent event) {
		List<String> choices = new ArrayList<>();
		choices.addAll(CargarPaisesYNacionalidades.cargarNacionalidades());

		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Nueva nacionalidad");
		dialog.setHeaderText("Añadir nueva nacionalidad");
		dialog.setContentText("Seleccione una nacionalidad");

		Optional<String> result = dialog.showAndWait();
		Nacionalidad aux = new Nacionalidad();
		aux.setDenominacion(result.get());
		if (!model.getPersonal().getNacionalidades().contains(aux)) {
			model.getPersonal().getNacionalidades().add(aux);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Algo no funca");
			alert.setContentText("Ya has añadido esa nacionalidad");
			alert.showAndWait();
		}
	}

	@FXML
	void onQuitarNacionalidadAction(ActionEvent event) {
		model.getPersonal().getNacionalidades().remove(selected.get());
	}

	public final ObjectProperty<Personal> personalProperty() {
		return this.personal;
	}

	public final Personal getPersonal() {
		return this.personalProperty().get();
	}

	public final void setPersonal(final Personal personal) {
		this.personalProperty().set(personal);
	}

}
