package dad.javafx.micv.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contacto {
	private ListProperty<Web> webs = new SimpleListProperty<Web>(FXCollections.observableArrayList());
	private ListProperty<Telefono> telefono = new SimpleListProperty<Telefono>(FXCollections.observableArrayList());
	private ListProperty<Email> Email = new SimpleListProperty<Email>(FXCollections.observableArrayList());

	public final ListProperty<Web> websProperty() {
		return this.webs;
	}

	public final ObservableList<Web> getWebs() {
		return this.websProperty().get();
	}

	public final void setWebs(final ObservableList<Web> webs) {
		this.websProperty().set(webs);
	}

	public final ListProperty<Telefono> telefonoProperty() {
		return this.telefono;
	}

	public final ObservableList<Telefono> getTelefono() {
		return this.telefonoProperty().get();
	}

	public final void setTelefono(final ObservableList<Telefono> telefono) {
		this.telefonoProperty().set(telefono);
	}

	public final ListProperty<Email> EmailProperty() {
		return this.Email;
	}

	public final ObservableList<Email> getEmail() {
		return this.EmailProperty().get();
	}

	public final void setEmail(final ObservableList<Email> Email) {
		this.EmailProperty().set(Email);
	}

}
