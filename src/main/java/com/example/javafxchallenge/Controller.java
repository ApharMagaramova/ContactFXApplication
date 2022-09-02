package com.example.javafxchallenge;

import com.example.javafxchallenge.datamodel.Contact;
import com.example.javafxchallenge.datamodel.ContactData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TableView<Contact> contactTable;
    @FXML
    private MenuItem deleteMenuItem;
    private ContactData data;

    public void initialize() {

        data = new ContactData();
        data.loadContacts();

        contactTable.setItems(data.getContacts());

        // delete contact using menu
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
                if (selectedContact == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Contact Selected");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select contact you want to delete");
                    alert.showAndWait();
                    return;
                }
                deleteContactFromList(selectedContact);
            }
        });
    }

    @FXML
    public void showAddContactDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Contact");
//        dialog.setHeaderText("Use this dialog create new Contact");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // add new contact
            ContactController contactController = fxmlLoader.getController();
            Contact newContact = contactController.getNewContact();
            data.addContact(newContact);

            // save contact to xml file
            data.saveContacts();
            contactTable.getSelectionModel().select(newContact);
        }
    }

    @FXML
    public void showEditContactDialog() {
        Contact oldContact = contactTable.getSelectionModel().getSelectedItem();

        if (oldContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select contact you want to edite");
            alert.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Contact");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ContactController contactController = fxmlLoader.getController();
        contactController.editeContact(oldContact);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contactController.updateContact(oldContact);
            // save contact to xml file
            data.saveContacts();
            contactTable.getSelectionModel().select(oldContact);
        }
    }


    @FXML
    public void deleteContactFromList(Contact contact) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText("Delete " + contact.getFirstName() + " " + contact.getLastName() + " from contact.");
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            data.deleteContact(contact);
            data.saveContacts();
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Contact selectedItem = contactTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteContactFromList(selectedItem);
            }
        }
    }

}