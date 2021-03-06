package dad.javafx.micv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import dad.javafx.micv.controladores.ConocimientosController;
import dad.javafx.micv.controladores.ContactoController;
import dad.javafx.micv.controladores.ExperienciaController;
import dad.javafx.micv.controladores.FormacionController;
import dad.javafx.micv.controladores.PersonalController;
import dad.javafx.micv.model.CV;
import dad.javafx.micv.utils.JSONUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController implements Initializable {

	// controllers

	private PersonalController personalController = new PersonalController();
	private ContactoController contactoController = new ContactoController();
	private FormacionController formacionController = new FormacionController();
	private ExperienciaController experianciaController = new ExperienciaController();
	private ConocimientosController conocimientosController = new ConocimientosController();

	// model

	private ObjectProperty<CV> cv = new SimpleObjectProperty<>();

	// view

	@FXML
	private BorderPane view;

	@FXML
	private Tab personalTab;

	@FXML
	private Tab contactoTab;

	@FXML
	private Tab formacionTab;

	@FXML
	private Tab experienciaTab;

	@FXML
	private Tab conocimientosTab;
	
	String ruta;


	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}

	public BorderPane getView() {
		return view;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		personalTab.setContent(personalController.getView());
		contactoTab.setContent(contactoController.getView());
		formacionTab.setContent(formacionController.getView());
		experienciaTab.setContent(experianciaController.getView());
		conocimientosTab.setContent(conocimientosController.getView());

		cv.addListener((o, ov, nv) -> onCVChanged(o, ov, nv));

		cv.set(new CV());

	}

	private void onCVChanged(ObservableValue<? extends CV> o, CV ov, CV nv) {

		if (ov != null) {

			personalController.personalProperty().unbind(); // desbindeo personalProperty del CV anterior
			contactoController.contactoProperty().unbind();
			formacionController.formacionProperty().unbind();
			experianciaController.experienciaProperty().unbind();
			conocimientosController.conocimientoProperty().unbind();

		}

		if (nv != null) {

			personalController.personalProperty().bind(nv.personalProperty()); // bindeo personalProperty del nuevo CV
			contactoController.contactoProperty().bind(nv.contactoProperty());
			formacionController.formacionProperty().bind(nv.formacionProperty());
			experianciaController.experienciaProperty().bind(nv.experienciaProperty());
			conocimientosController.conocimientoProperty().bind(nv.conocimientosProperty());
		}

	}

	@FXML
	void onAbrirAction(ActionEvent event) {
		
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir un currículum");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Currículum Vitae (*.cv)", "*.cv"));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos", "."));
        File cvFile = fileChooser.showOpenDialog(App.getPrimaryStage());
        if (cvFile != null) {
            try {
            	ruta = cvFile.getAbsolutePath();
                cv.set(JSONUtils.fromJson(cvFile, CV.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

	}

	@FXML
	void onAcercaDeAction(ActionEvent event) {

	}

	@FXML
	void onGuardarAction(ActionEvent event) {
		File cvFile = new File(ruta);
		if(cvFile != null) {
			try {
				JSONUtils.toJson(cvFile, cv.get());
			} catch (IOException e) {
			}
		}
	}

	@FXML
	void onGuardarComoAction(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar un currículum");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Currículum Vitae (*.cv)", "*.cv"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos", "*.*"));
		File cvFile = fileChooser.showSaveDialog(App.getPrimaryStage());
		if (cvFile != null) {
			try {
				JSONUtils.toJson(cvFile, cv.get());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@FXML
	void onNuevoAction(ActionEvent event) {
		System.out.println("Has pulsado nuevo");
		cv.set(new CV());
	}

	@FXML
	void onSalirAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Salir");
		alert.setHeaderText("Va a salir del programa");
		alert.setContentText("¿Seguro que quiere salir?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.exit(0);
		}
	}

}
