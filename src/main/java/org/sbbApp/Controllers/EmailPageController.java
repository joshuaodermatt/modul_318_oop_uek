package org.sbbApp.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.sbbApp.MailService;


public class EmailPageController {

    @FXML
    TextField textFieldEmail;

    @FXML
    TextField textFieldSubject;

    @FXML
    Button buttonSend;

    @FXML
    Label labelEmailMessage;

    @FXML
    Label labelSubjectMessage;

    @FXML
    Label labelMailErrorMessage;


    @FXML
    private void onButtonSendClick() {
        resetLabels();
        String email = textFieldEmail.getText();
        String subject = textFieldSubject.getText();

        email = email.replaceAll("[ ]", "%20");
        subject = subject.replaceAll("[ ]", "%20");

        if (validateInputs(email, subject)) {
            try {
                MailService.mail(email, subject);
            } catch (Exception exception) {
                labelMailErrorMessage.setText("Mail Client could not be opened");
            }
        }
    }

    private boolean validateInputs(String email, String subject) {
        if (email.equals("")) {
            labelEmailMessage.setText("Email is not entered");
            return false;
        }
        if (subject.equals("")) {
            labelSubjectMessage.setText("Subject is not entered");
            return false;
        }
        return true;
    }

    private void resetLabels() {
        labelMailErrorMessage.setText("");
        labelEmailMessage.setText("");
        labelSubjectMessage.setText("");
    }


}
