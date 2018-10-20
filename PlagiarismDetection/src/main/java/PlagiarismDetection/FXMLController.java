/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import static PlagiarismDetection.FXMLController.executor;
import com.google.firebase.auth.UserRecord;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import java.util.concurrent.*;

public class FXMLController implements Initializable {
    @FXML
    private AnchorPane loginScreen;
    @FXML
    private AnchorPane accountLogInOrSignUpPane;
    @FXML
    private Button goToCreateAccountButton;
    @FXML
    private Button goToSignInButton;
    @FXML
    private AnchorPane createAccountPane;
    @FXML
    private Button createAccountBackButton;
    @FXML
    private TextField createAccountEmailField;
    @FXML
    private PasswordField createAccountPasswordField;
    @FXML
    private Button createAccountButton;
    @FXML
    private Label createAccountErrorLabel;
    @FXML
    private ImageView createAccountLoadingGif;
    @FXML
    private AnchorPane signInPane;
    @FXML
    private Button signInBackButton;
    @FXML
    private TextField signInEmailField;
    @FXML
    private PasswordField signInPasswordField;
    @FXML
    private Button signInButton;
    @FXML
    private Label signInErrorLabel;
    @FXML
    private ImageView signInLoadingGif;
    @FXML
    private AnchorPane mainScreen;
    @FXML
    private Button debugTestButton;
    @FXML
    private Button mainScreenSignOutButton;
    @FXML
    private Button testTranslateButton;
    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextField filePathTextField;
    @FXML
    private TextField titleField;
    @FXML
    private Button browseForAFileButton;
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
    private AnchorPane inputScreen;
    @FXML
    private Button exitDebug;
    @FXML
    private Button storeButton;
    @FXML
    private Button fetchButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button uploadButton;
    @FXML
    private TextArea searchTextArea;
    @FXML
    private TextArea finalResText;
    @FXML
    private Label HPercent;
    @FXML
    private Label LPercent;
    @FXML
    private Label matches;
    @FXML
    private Label initialiseErrorLabel;
    @FXML
    private ImageView logInOrSignUpLoadingGif;
    
    static String errorLabel;
    static ExecutorService executor;// = Executors.newSingleThreadExecutor();
    
    @FXML
    void mainScreenSignOutButtonAction(ActionEvent event) {
        Firebase.loggedIn = false;
        Firebase.currentUser.email = "";
        Firebase.currentUser.password = "";
        mainScreen.setDisable(true);
        mainScreen.setVisible(false);
        loginScreen.setDisable(false);
        loginScreen.setVisible(true);
    }
    
    //performs a search based on translated text
    //No longer connects to Debug/Text, it will need to be called at some point
    void toSearch(String translatedText){
        
        WebSearch.start(translatedText);
        ArrayList<ResultCompare> display = new ArrayList<>();
        
        for(int i = 0; i < WebSearch.santitizedText.size(); i += 2){
            display.add(new ResultCompare(WebSearch.santitizedText.get(i), WebSearch.santitizedText.get(i+1), translatedText));
        }
        
        Collections.sort(display);
        
        HPercent.setText(Double.toString(display.get(0).percent) + "%");
        
        LPercent.setText(Double.toString((100 - display.get(0).percent)) + "%");
        
        matches.setText(Integer.toString(display.size()));
        
        for (int i = 0; i < display.size(); i++) {
            finalResText.setText(finalResText.getText() + i + ". " + display.get(i).toString() + "\n");
        }
        
    }

    @FXML
    void startNewCheckButtonAction(ActionEvent event) {
        System.out.println("Starting new plagiarism check...");
        resultsScreen.setVisible(false);
        resultsScreen.setDisable(true);
        selectLanguageScreen.setVisible(true);
        inputScreen.setDisable(false);
        inputScreen.setVisible(true);
    }
    
    @FXML
    void checkForPlagiarismButtonAction(ActionEvent event) {
        System.out.println("Checking for plagiarism...");
        selectLanguageScreen.setVisible(false);
        selectLanguageScreen.setDisable(true);
        inputScreen.setDisable(true);
        inputScreen.setVisible(false);
        resultsScreen.setVisible(true);
        resultsScreen.setDisable(false);
    }
    
    @FXML
    void uploadButtonAction(ActionEvent event) {
        // upload document to database and link to currently logged in user
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
        mainScreen.setDisable(true);
        mainScreen.setVisible(false);
        debugTestScreen.setDisable(false);
        debugTestScreen.setVisible(true);
    }

    @FXML
    void exitDebugAction(ActionEvent event) {
        System.out.println("Exiting debug/test screen...");
        debugTestScreen.setDisable(true);
        debugTestScreen.setVisible(false);
        mainScreen.setDisable(false);
        mainScreen.setVisible(true);
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
        // turn on the loading gif
        signInLoadingGif.setVisible(true);
        signInErrorLabel.setText("");
        final String email = signInEmailField.getText();
        final String password = signInPasswordField.getText();
        // check if either field is empty:
        if (email.isEmpty() || password.isEmpty()) {
            signInErrorLabel.setText("Error, email or password field is empty.");
            System.out.println("Error, email or password field is empty.");
            signInLoadingGif.setVisible(false);
            return;
        }
        // check if password is valid:
        if (password.length() < 6) {
            signInErrorLabel.setText("Password must be atleast 6 characters long.");
            System.err.println("Password must be atleast 6 characters long.");
            signInLoadingGif.setVisible(false);
            return;
        }
        // check if email is valid:
        if (!Firebase.isEmailValid(email)) {
            signInErrorLabel.setText("Error, email is not valid.");
            System.err.println("Error, email is not valid.");
            signInLoadingGif.setVisible(false);
            return;
        }
        
        // run Firebase fetching processes on another thread:
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Check to see if that user exists:
                    if (Firebase.getUserByEmail(email) != null) {
                        System.out.println("A user with that email exists, continue...");
                        // If user does exist, then attempt logging in:
                        try {
                            if (Firebase.signIn(email, password)) {
                                Firebase.loggedIn = true;
                            } else {
                                System.out.println("Error, password is incorrect.");
                                errorLabel = "Error, password is incorrect.";
                            }
                        } catch (InterruptedException | ExecutionException ex) {
                            System.out.println("Error, password is incorrect.");
                            errorLabel = "Error, password is incorrect.";
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    errorLabel = "Error, no user was found with that email.";
                    System.out.println("Error, no user was found with that email.");
                }
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        signInErrorLabel.setText(errorLabel);
                        if (Firebase.loggedIn) {
                            // Logged user in successfully.
                            System.out.println("Logged in successfully.");
                            signInErrorLabel.setText("");
                            signInEmailField.setText("");
                            signInPasswordField.setText("");
                            signInLoadingGif.setVisible(false);
                            loginScreen.setDisable(true);
                            loginScreen.setVisible(false);
                            mainScreen.setDisable(false);
                            mainScreen.setVisible(true);
                        }
                        // turn off the loading gif
                        signInLoadingGif.setVisible(false);
                    }
                });
            }
        }).start();
    }
    
    @FXML
    void createAccountButtonAction(ActionEvent event) {
        createAccountLoadingGif.setVisible(true); // turn on the loading gif
        errorLabel = "";
        System.out.println("createAccountButtonAction() ...");
        createAccountErrorLabel.setText(""); // clear any previous error
        final String email = createAccountEmailField.getText();
        final String password = createAccountPasswordField.getText();
        // check if either field is empty:
        if (email.isEmpty() || password.isEmpty()) {
            createAccountErrorLabel.setText("Error, email or password field is empty.");
            System.err.println("Error, email or password field is empty.");
            createAccountLoadingGif.setVisible(false);
            return;
        }
        // check if email is valid:
        if (!Firebase.isEmailValid(email)) {
            createAccountErrorLabel.setText("Error, email is not valid.");
            System.err.println("Error, email is not valid.");
            createAccountLoadingGif.setVisible(false);
            return;
        }
        // check if password is valid:
        if (password.length() < 6) {
            createAccountErrorLabel.setText("Password must be atleast 6 characters long.");
            System.err.println("Password must be atleast 6 characters long.");
            createAccountLoadingGif.setVisible(false);
            return;
        }
        // run Firebase processes on a new thread:
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Check for a user with that email first:
                    if (Firebase.getUserByEmail(email) != null) {
                        errorLabel = "There is already a user with that email.";
                        System.out.println("There is already a user with that email.");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("No user was found with that email, continue...");
                    try {
                        // try and create the account:
                        Firebase.createUserWithEmailAndPassword(email, password);
                        // Store password in database for signing in later.
                        Firebase.storePasswordInDatabase(email, password);
                    } catch (InterruptedException | ExecutionException er) {
                        errorLabel = "Error, could not create user.";
                        System.err.println("createAccount error: " + ex.getMessage());
                    }
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        createAccountErrorLabel.setText(errorLabel);
                        if (errorLabel.isEmpty()) { // If there was no errors, meaning it was a successful account creation attempt
                            createAccountErrorLabel.setText("");
                            createAccountEmailField.setText("");
                            createAccountPasswordField.setText("");
                            System.out.println("createAccountButtonAction() successfully created a user.");
                            // Automatically sign in the newly created user
                            Firebase.loggedIn = true;
                            Firebase.currentUser.email = email;
                            Firebase.currentUser.password = password;
                            // Change screens:
                            loginScreen.setDisable(true);
                            loginScreen.setVisible(false);
                            mainScreen.setDisable(false);
                            mainScreen.setVisible(true);
                            System.out.println("Newly created user in now signed in.");
                        }
                        // Turn off the loading gif
                        createAccountLoadingGif.setVisible(false);
                    }
                });
            }
        }).start();
    }
    
    @FXML
    void browseForAFileButtonAction(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
    }
    
    void checkFirebaseConnection () {
        System.out.println("called checkFirebaseConnection()");
        executor = Executors.newSingleThreadExecutor(); // assign a new thread to executor
        logInOrSignUpLoadingGif.setVisible(true); // turn the loading gif on
        // run the following in a new thread:
        new Thread(new Runnable() {
            @Override
            // Run this first:
            public void run() {
                timeoutRunnable validateFirebaseRunnable = new timeoutRunnable() {
                    @Override
                    public Boolean call() throws Exception { // override the abstract method call() of timeoutRunnable class
                        try {
                            // test connection to firebase by trying to get a known email that will always be in the database:
                            UserRecord user = Firebase.getUserByEmail("test@test.com");
                            if (user != null) { // if the user was found
                                Firebase.isInitialised = true;
                                System.out.println("Firebase test connection was successful.");
                                return true;
                            } else { // if the user wasn't found
                                System.out.println("Firebase testconnection was unsuccessful.");
                                return false;
                            }
                        } catch (InterruptedException | ExecutionException ex) {
                            // if an exception occurred with the test, then also return false
                            System.out.println("Firebase test connection was unsuccessful.");
                            return false;
                        }
                    }
                };
                // load the validateFirebaseRunnable into the executor thread:
                Future<Boolean> future = executor.submit(validateFirebaseRunnable);
                try {
                    // Run the overriden call() function above with a timeout of 5 seconds.
                    // This means if call() isn't completed within 5 seconds from now, then interrupt it.
                    future.get(5, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    // 5 seconds went by and wasn't able to fully execute call()
                    System.out.println("Timeout exception on checking firebase connection.");
                }
                
                // Run this only when the above run() is finished:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        executor.shutdownNow(); // shut down the new thread in executor
                        logInOrSignUpLoadingGif.setVisible(false); // turn off the loading gif
                        if (!Firebase.isInitialised) {
                            // show an error message and don't allow the user to continue:
                            initialiseErrorLabel.setVisible(true);
                        } else {
                            // allow the user to continue:
                            goToSignInButton.setDisable(false);
                            goToCreateAccountButton.setDisable(false);
                        }
                    }
                });
            }
        }).start();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialise and validate the connection to the Firebase API.
        Firebase.initialise();
        checkFirebaseConnection();
    }
}

abstract class timeoutRunnable implements Callable<Boolean> {
    @Override
    abstract public Boolean call() throws Exception;
}