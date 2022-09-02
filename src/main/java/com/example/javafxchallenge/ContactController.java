package com.example.javafxchallenge;

import com.example.javafxchallenge.datamodel.Contact;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ContactController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField notesField;

    public Contact getNewContact() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String notes = notesField.getText();


        return new Contact(firstName, lastName, phoneNumber, notes);
    }

    public void editeContact(Contact oldContact) {

        firstNameField.setText(oldContact.getFirstName());
        lastNameField.setText(oldContact.getLastName());
        phoneNumberField.setText(oldContact.getPhoneNumber());
        notesField.setText(oldContact.getNotes());

    }

    public void updateContact(Contact oldContact) {
        oldContact.setFirstName(firstNameField.getText());
        oldContact.setLastName(lastNameField.getText());
        oldContact.setPhoneNumber(phoneNumberField.getText());
        oldContact.setNotes(notesField.getText());
    }
}
