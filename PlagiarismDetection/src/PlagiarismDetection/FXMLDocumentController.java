package PlagiarismDetection;

/*
 * CSIT214/814 GROUP ALPHA
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane inputScreen;
    @FXML
    private Button continueButton;
    @FXML
    private AnchorPane selectLanguageScreen;
    @FXML
    private Button checkForPlagiarismButton;
    @FXML
    private AnchorPane resultsScreen;
    @FXML
    private Button startNewCheckButton;
    @FXML
    private TextArea inputTextArea;
    @FXML
    private Button testTranslateButton;

    @FXML
    void startNewCheckButtonAction(ActionEvent event) {
        System.out.println("Starting new plagiarism check...");
        resultsScreen.setVisible(false);
        resultsScreen.setDisable(true);
        inputScreen.setDisable(false);
        inputScreen.setVisible(true);
    }
    
    @FXML
    void checkForPlagiarismButtonAction(ActionEvent event) {
        System.out.println("Checking for plagiarism...");
        selectLanguageScreen.setVisible(false);
        selectLanguageScreen.setDisable(true);
        resultsScreen.setVisible(true);
        resultsScreen.setDisable(false);
    }
    
    @FXML
    private void continueButtonAction(ActionEvent event) {
        System.out.println("Continue...");
//        label.setText("Hello World!");
        inputScreen.setDisable(true);
        inputScreen.setVisible(false);
        selectLanguageScreen.setVisible(true);
        selectLanguageScreen.setDisable(false);
    }
    
    @FXML
    void testTranslateButtonAction(ActionEvent event) {
        try {
            String text = inputTextArea.getText();
            System.out.println("Before translation: " + text);
            String params = "&to=de&to=it"; // get german and italian translation. Probably will just change this to English only
            String response = MicrosoftTextTranslate.Translate (text, params);
            String printOutThis = MicrosoftTextTranslate.prettify (response);
            System.out.println ("After translation(this is a JSON):" + printOutThis);
            inputTextArea.appendText("\nThis is a JSON. We can change this later to just return the text we need.\n" + printOutThis);
        }
        catch (Exception e) {
            System.out.println (e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
