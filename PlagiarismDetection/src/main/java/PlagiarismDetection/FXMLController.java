/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable {
    
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
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
    private Button storeButton;
    @FXML
    private Button fetchButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextArea searchTextArea;
    @FXML
    private TextArea resultsText;
    @FXML
    private AnchorPane loginScreen;
    @FXML
    private AnchorPane accountLogInOrSignUpPane;
    @FXML
    private Button goToAccountCreateButton;
    @FXML
    private Button goToSignInButton;
    @FXML
    private AnchorPane signInPane;
    @FXML
    private AnchorPane createAccountPane;
    @FXML
    private TextField createAccountEmailField;
    @FXML
    private PasswordField createAccountPasswordField;
    @FXML
    private Button createAccountBackButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private TextField signInEmailField;
    @FXML
    private PasswordField signInPasswordField;
    @FXML
    private Button signInButton;
    @FXML
    private Button signInBackButton;
    @FXML
    private Label signInErrorLabel;
    @FXML
    private Label createAccountErrorLabel;
    @FXML
    private Button inputScreenSignOutButton;
    
    @FXML
    void inputScreenSignOutButtonAction(ActionEvent event) {
        Firebase.loggedIn = false;
        Firebase.currentUser.email = "";
        Firebase.currentUser.password = "";
        inputScreen.setDisable(true);
        inputScreen.setVisible(false);
        loginScreen.setDisable(false);
        loginScreen.setVisible(true);
    }
    
    @FXML
    void searchButtonAction(ActionEvent event){
        String searchText = searchTextArea.getText();
        WebSearch.start(searchText);
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
    
    @FXML
    void storeButtonAction(ActionEvent event) {
        Firebase.storeDocumentInDatabase("something", "guy", "title1");
    }
    
    @FXML
    void goToSignInButtonAction(ActionEvent event) {
        accountLogInOrSignUpPane.setDisable(true);
        accountLogInOrSignUpPane.setVisible(false);
        signInPane.setDisable(false);
        signInPane.setVisible(true);
    }

    @FXML
    void goToCreateAccountButtonAction(ActionEvent event) {
        accountLogInOrSignUpPane.setDisable(true);
        accountLogInOrSignUpPane.setVisible(false);
        createAccountPane.setDisable(false);
        createAccountPane.setVisible(true);
    }
    
    @FXML
    void signInBackButtonAction(ActionEvent event) {
        signInPane.setDisable(true);
        signInPane.setVisible(false);
        accountLogInOrSignUpPane.setDisable(false);
        accountLogInOrSignUpPane.setVisible(true);
    }
    
    @FXML
    void createAccountBackButton(ActionEvent event) {
        createAccountPane.setDisable(true);
        createAccountPane.setVisible(false);
        accountLogInOrSignUpPane.setDisable(false);
        accountLogInOrSignUpPane.setVisible(true);
    }
    
    @FXML
    void signInButtonAction(ActionEvent event) {
        signInErrorLabel.setText("");
        String email = signInEmailField.getText();
        String password = signInPasswordField.getText();
        // check if either field is empty:
        if (email.isEmpty() || password.isEmpty()) {
            signInErrorLabel.setText("Error, email or password field is empty.");
            System.err.println("Error, email or password field is empty.");
            return;
        }
        // check if password is valid:
        if (password.length() < 6) {
            signInErrorLabel.setText("Password must be atleast 6 characters long.");
            System.err.println("Password must be atleast 6 characters long.");
            return;
        }
        // check if email is valid:
        if (!Firebase.isEmailValid(email)) {
            signInErrorLabel.setText("Error, email is not valid.");
            System.err.println("Error, email is not valid.");
            return;
        }
        // Check for a user with that email first:
        try {
            if (Firebase.getUserByEmail(email) != null) {
                System.out.println("A user with that email exists, continue...");
            }
        } catch (InterruptedException | ExecutionException ex) {
            signInErrorLabel.setText("Error, no user was found with that email.");
            System.out.println("Error, no user was found with that email.");
            return;
        }
        try {
            if (!Firebase.signIn(email, password)) {
                System.out.println("Error, password is incorrect.");
                signInErrorLabel.setText("Error, password is incorrect.");
                return;
            }
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("Error, password is incorrect.");
            signInErrorLabel.setText("Error, password is incorrect.");
            return;
        }
        
        // Logged user in successfully.
        System.out.println("Logged in successfully.");
        signInEmailField.setText("");
        signInPasswordField.setText("");
        loginScreen.setDisable(true);
        loginScreen.setVisible(false);
        inputScreen.setDisable(false);
        inputScreen.setVisible(true);
    }
    
    @FXML
    void createAccountButtonAction(ActionEvent event) {
        System.out.println("createAccountButtonAction() ...");
        createAccountErrorLabel.setText(""); // clear any previous error
        String email = createAccountEmailField.getText();
        String password = createAccountPasswordField.getText();
        // check if either field is empty:
        if (email.isEmpty() || password.isEmpty()) {
            createAccountErrorLabel.setText("Error, email or password field is empty.");
            System.err.println("Error, email or password field is empty.");
            return;
        }
        // check if email is valid:
        if (!Firebase.isEmailValid(email)) {
            createAccountErrorLabel.setText("Error, email is not valid.");
            System.err.println("Error, email is not valid.");
            return;
        }
        // check if password is valid:
        if (password.length() < 6) {
            createAccountErrorLabel.setText("Password must be atleast 6 characters long.");
            System.err.println("Password must be atleast 6 characters long.");
            return;
        }
        // Check for a user with that email first:
        try {
            if (Firebase.getUserByEmail(email) != null) {
                createAccountErrorLabel.setText("There is already a user with that email.");
                System.out.println("There is already a user with that email.");
                return;
            }
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("No user was found with that email, continue...");
        }
        // try and create the account:
        try {
            Firebase.createUserWithEmailAndPassword(email, password);
        } catch (InterruptedException | ExecutionException ex) {
            createAccountErrorLabel.setText("Error, could not create user.");
            System.err.println("createAccount error: " + ex.getMessage());
            return;
        }
        
        // Store password in database for signing in later.
        Firebase.storePasswordInDatabase(email, password);
        
        createAccountEmailField.setText("");
        createAccountPasswordField.setText("");
        System.out.println("createAccountButtonAction() successfully created a user.");
        
        // Automatically sign in the newly created user
        Firebase.loggedIn = true;
        Firebase.currentUser.email = email;
        Firebase.currentUser.password = password;
        loginScreen.setDisable(true);
        loginScreen.setVisible(false);
        inputScreen.setDisable(false);
        inputScreen.setVisible(true);
        System.out.println("Newly created user in now signed in.");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Firebase.initialise();
    }
}
