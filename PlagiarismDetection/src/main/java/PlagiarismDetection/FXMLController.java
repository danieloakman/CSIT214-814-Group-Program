/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;
import javax.script.ScriptException;

public class FXMLController implements Initializable {
    
    @FXML
    private AnchorPane inputScreen;
    @FXML
    private Button testTranslateButton;
    @FXML
    private TextArea inputTextArea;
    @FXML
    private Button continueButton;
    @FXML
    private Button debugTestButton;
    @FXML
    private AnchorPane selectLanguageScreen;
    @FXML
    private Button checkForPlagiarismButton;
    @FXML
    private AnchorPane resultsScreen;
    @FXML
    private Button startNewCheckButton;
    @FXML
    private AnchorPane debugTestScreen;
    @FXML
    private Button exitDebug;
    @FXML
    private Button fetchButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextArea searchTextArea;
    @FXML
    private TextArea resultsText;
    
    @FXML
    void searchButtonAction(ActionEvent event){
        String searchText = searchTextArea.getText();
        WebSearch.start(searchText);
        ArrayList<ResultCompare> display = new ArrayList<>();
        resultsText.setText(WebSearch.results);
    }

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
    
    @FXML
    void debugButtonAction(ActionEvent event) {
        System.out.println("Debug/test screen...");
        inputScreen.setDisable(true);
        inputScreen.setVisible(false);
        debugTestScreen.setDisable(false);
        debugTestScreen.setVisible(true);
    }

    @FXML
    void exitDebugAction(ActionEvent event) {
        System.out.println("Exiting debug/test screen...");
        debugTestScreen.setDisable(true);
        debugTestScreen.setVisible(false);
        inputScreen.setDisable(false);
        inputScreen.setVisible(true);
    }
    
    @FXML
    void fetchButtonAction(ActionEvent event) {
        System.out.println("Attempting database fetch...");
        Firebase.getDocumentFromDatabase();
        System.out.println("Fetched...");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Firebase.initialise();
    }      
}
