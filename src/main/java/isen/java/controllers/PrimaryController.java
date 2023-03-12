package isen.java.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import isen.java.App;
import isen.java.db.DatabaseExporter;
import isen.java.db.daos.PersonDao;
import isen.java.db.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class PrimaryController {

    private PersonDao personDao = new PersonDao();

    private ObservableList<Person> observablePeopleList;
    @FXML
    private Button addButton;

    @FXML
    private TextField address;

    @FXML
    private DatePicker birthDate;

    @FXML
    private Button browseButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField email;

    @FXML
    private Button exportButton;

    @FXML
    private TextField exportDirectory;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField nickname;

    @FXML
    private ListView<Person> peopleList;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Button updateButton;

    @FXML
    void onMouseClicked(MouseEvent event) {
        Person selectedPerson = getListSelectedPerson();
        if (selectedPerson != null) {
            selectPerson(selectedPerson);
        }
    }

    @FXML
    void onBrowse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select People VCards Export Directory");
        directoryChooser.setInitialDirectory(new File("/"));
        File selectedDirectory =
                directoryChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());
        exportDirectory.setText(selectedDirectory.toPath().toAbsolutePath().toString());
    }

    @FXML
    void onExport(ActionEvent event) {
        DatabaseExporter.exportDatabase(Paths.get(exportDirectory.getText()));
    }

    @FXML
    void onUpdate(ActionEvent event) {
        Person updatedPerson = getPerson();
        updatedPerson.setId(getListSelectedPerson().getId());
        personDao.updatePerson(updatedPerson);
        updatePeopleList();
    }

    @FXML
    void onDelete(ActionEvent event) {
        personDao.deletePerson(getListSelectedPerson().getId());
        updatePeopleList();
        clearForm();
    }

    @FXML
    void onAdd(ActionEvent event) {
        personDao.addPerson(getPerson());
        updatePeopleList();
    }

    @FXML
    void onClear(ActionEvent event) {
        clearForm();
    }

    @FXML
    void initialize() {
        updateButton.setDisable(true);
        addButton.setDisable(true);
        deleteButton.setDisable(true);

        nickname.textProperty().addListener(arg0 -> updateAddButtonDisable());
        firstname.textProperty().addListener(arg0 -> updateAddButtonDisable());
        lastname.textProperty().addListener(arg0 -> updateAddButtonDisable());

        exportDirectory.textProperty().addListener(arg0 -> updateExportButtonDisable());

        observablePeopleList = FXCollections.observableArrayList(personDao.listPeople());
        peopleList.setItems(observablePeopleList);
        peopleList.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
            @Override
            public ListCell<Person> call(ListView<Person> list) {
                return new PersonCell();
            }
        });
    }

    private void fillForm(String Nickname, String Firstname, String Lastname, String PhoneNumber,
            String Address, String EmailAddress, LocalDate BirthDate) {
        nickname.setText(Nickname);
        firstname.setText(Firstname);
        lastname.setText(Lastname);
        phoneNumber.setText(PhoneNumber);
        address.setText(Address);
        email.setText(EmailAddress);
        birthDate.setValue(BirthDate);
    }

    private void fillForm(Person person) {
        fillForm(person.getNickname(), person.getFirstname(), person.getLastname(),
                person.getPhoneNumber(), person.getAddress(), person.getEmailAddress(),
                person.getBirthDate());
    }

    private void selectPerson(Person person) {
        fillForm(person);
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    private Person getListSelectedPerson() {
        return peopleList.getSelectionModel().getSelectedItem();
    }

    private Person getPerson() {
        Person person = new Person(null, lastname.getText(), firstname.getText(),
                nickname.getText(), phoneNumber.getText(), address.getText(), email.getText(),
                birthDate.getValue());
        return person;
    }

    private void updatePeopleList() {
        observablePeopleList.setAll(personDao.listPeople());
    }

    private void clearForm() {
        peopleList.getSelectionModel().clearSelection();
        fillForm(null, null, null, null, null, null, null);
        updateButton.setDisable(true);
        addButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void updateAddButtonDisable() {
        addButton.setDisable(nickname.getText() == null || firstname.getText() == null
                || lastname.getText() == null || nickname.getText().isEmpty()
                || firstname.getText().isEmpty() || lastname.getText().isEmpty());
    }

    private void updateExportButtonDisable() {
        exportButton
                .setDisable(exportDirectory.getText() == null || exportDirectory.getText().isEmpty()
                        || Files.notExists(Paths.get(exportDirectory.getText())));
    }

    static class PersonCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getFirstname() + " \"" + item.getNickname() + "\" "
                        + item.getLastname());
            } else {
                // There may be a better way of doing this.
                setText("");
            }
        }
    }
}
