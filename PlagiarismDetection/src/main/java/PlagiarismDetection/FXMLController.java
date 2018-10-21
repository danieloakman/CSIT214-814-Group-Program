/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import static PlagiarismDetection.FXMLController.executor;
import com.google.firebase.auth.UserRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.FileChooser;
import java.util.concurrent.*;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.util.Collections;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
    private Button translateButton;
    @FXML
    private TextArea inputTextArea;
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
    private Button fetchButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button uploadButton;
    @FXML
    private TextArea searchTextArea;
    @FXML
    private TextArea webSourcesMatchedTextArea;
    @FXML
    private Label highestMatchedPercentageLabel;
    @FXML
    private Label uniquePercentageLabel;
    @FXML
    private Label numberOfMatchesLabel;
    @FXML
    private Label initialiseErrorLabel;
    @FXML
    private ImageView logInOrSignUpLoadingGif;
    @FXML
    private Label inputScreenErrorLabel;
    @FXML
    private Label currentUserLoggedInLabel;
    @FXML
    private ImageView uploadDocumentLoadingGif;
    @FXML
    private ChoiceBox targetLanguageChoiceBox;
    @FXML
    private TextArea translatedTextArea;
    @FXML
    private ImageView translateTextLoadingGif;
    @FXML
    private Label translateTextErrorLabel;
    @FXML
    private TextArea testDebugTextArea;
    @FXML
    private ImageView checkingForPlagiarismLoadingGif;
    @FXML
    private TextArea documentSimilaritiesTextArea;
    @FXML
    private Label checkingForPlagiarismWaitLabel;
    
    static String errorLabel;
    static ExecutorService executor;
    static FileChooser fileChooser;
//    PlagiarismResults pResults = new PlagiarismResults();
    public ArrayList<ResultCompare> results = new ArrayList<>();
    
    @FXML
    void mainScreenSignOutButtonAction(ActionEvent event) {
        // set firebase static currentUser attributes to empty
        Firebase.loggedIn = false;
        Firebase.currentUser.email = "";
        Firebase.currentUser.password = "";
        // reset UI
        currentUserLoggedInLabel.setText("Logged in:");
        debugTestButton.setVisible(false);
        titleField.clear();
        inputTextArea.clear();
        selectLanguageScreen.setDisable(true);
        translatedTextArea.clear();
        // change screens:
        mainScreen.setDisable(true);
        mainScreen.setVisible(false);
        loginScreen.setDisable(false);
        loginScreen.setVisible(true);
    }
    
    @FXML
    void searchButtonAction (ActionEvent event) {
        
    }

    @FXML
    void startNewCheckButtonAction(ActionEvent event) {
        System.out.println("Starting new plagiarism check...");
        resultsScreen.setVisible(false);
        resultsScreen.setDisable(true);
        selectLanguageScreen.setVisible(true);
        inputScreen.setDisable(false);
        inputScreen.setVisible(true);
        // clear the results screen:
        translatedTextArea.clear();
        results.clear();
        webSourcesMatchedTextArea.clear();
        documentSimilaritiesTextArea.clear();
        // Empty input title and text fields:
        titleField.clear();
        inputTextArea.clear();
    }
    
    @FXML
    void checkForPlagiarismButtonAction(ActionEvent event) {
        System.out.println("Checking for plagiarism...");
        checkingForPlagiarismLoadingGif.setVisible(true);
        checkingForPlagiarismWaitLabel.setVisible(true);
        final String textToCompare;
        if (translatedTextArea.getText().isEmpty()) // if the user didn't translate their document, this would be empty
            textToCompare = inputTextArea.getText().replaceAll("\n", " ");
        else
            textToCompare = translatedTextArea.getText();
        // Run the following in a new thread:
        new Thread(new Runnable() {
            @Override
            // Performs a search based on translated text:
            public void run() {
                // Set language:
                String language = "English";
                if (!targetLanguageChoiceBox.getSelectionModel().isEmpty())
                    language = targetLanguageChoiceBox.getSelectionModel().getSelectedItem().toString();
                // Construct searchQuery from textToCompare
                String searchQuery = textToCompare;
                if (textToCompare.length() > 100) { // if textToCompare is larger than 100 characters, then only search by the first 100 characters:
                    searchQuery = textToCompare.substring(0, 100);
                }
                // Starts websearch:
                WebSearch.start(searchQuery);
                for(int i = 0; i < WebSearch.santitizedText.size(); i += 3){
                    // Add web search results to results:
                    results.add(new ResultCompare(
                            WebSearch.santitizedText.get(i),
                            WebSearch.santitizedText.get(i+1),
                            WebSearch.santitizedText.get(i+2),
                            language
                    ));
                }
                // The percentage calculated from these (as of now) are how much of the search result snippet has in comparison with textToCompare
                System.out.println("Looking through the snippets of all search results.");
                for (int i = 0; i < results.size(); i++) {
                    System.out.println("URL: " + results.get(i).url);
                    results.get(i).calcPercent(results.get(i).snippet, textToCompare);
                }
                // Delete any results that have near 0% match
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).percent < 1) {
                        results.remove(i);
                        i = 0;
                    }
                }
                // Sort by percent, see ResultCompare's compareTo method:
                Collections.sort(results);
                // Look through all web results and further compare through the whole webpage for each one:
                System.out.println("Now looking through some webpages:");
                for (int i = 0; i < results.size(); i++) {
                    System.out.println("URL: " + results.get(i).url);
                    try {
                        URL webpageUrl = new URL(results.get(i).url);
                        BufferedReader in = new BufferedReader(new InputStreamReader(webpageUrl.openStream()));
                        String st, webpageContents = "";
                        while ((st = in.readLine()) != null) {
                            webpageContents += st;
                        }
                        in.close();
                        // Parse the raw html webpage into a usable string:
                        webpageContents = Jsoup.parse(webpageContents).text();
                        // Get the new percentage result from the full webpage:
                        results.get(i).calcPercent(webpageContents, textToCompare);
                        results.get(i).lookedThroughWebpage = true;
                        // If this current webpage was greater than 90% match, don't look at any other web pages:
                        if (results.get(i).percent > 90) {
                            break;
                        }
                    } catch (MalformedURLException ex) {
                        System.out.println(ex);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
                // Delete any results that have near 0% match again
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).percent < 1) {
                        results.remove(i);
                        i = 0;
                    }
                }
                // Sort the results now that some webpages have been compared:
                Collections.sort(results);
                // Update the UI on the normal Java FX UI thread:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // turn off loading gif
                        checkingForPlagiarismLoadingGif.setVisible(false);
                        checkingForPlagiarismWaitLabel.setVisible(false);
                        // Change screens:
                        selectLanguageScreen.setVisible(false);
                        selectLanguageScreen.setDisable(true);
                        inputScreen.setDisable(true);
                        inputScreen.setVisible(false);
                        resultsScreen.setVisible(true);
                        resultsScreen.setDisable(false);
                        // shows results in UI
                        if (!results.isEmpty()) {
                            highestMatchedPercentageLabel.setText("Highest match: " + Double.toString(Math.round(results.get(0).percent)) + "%");
                            uniquePercentageLabel.setText("Unique: " + Double.toString(Math.round(100 - results.get(0).percent)) + "%");
                            numberOfMatchesLabel.setText("Number of matches: " + Integer.toString(results.size()));
                            documentSimilaritiesTextArea.setText(textToCompare);
                            for (int i = 0; i < results.size(); i++) {
                                webSourcesMatchedTextArea.setText(
                                    webSourcesMatchedTextArea.getText() +
                                    (i+1) + " - " + results.get(i).percent + "% match. " + results.get(i).toString() + "\n\n"
                                );
                            }
                        } else { // if no search results had any similarities
                            highestMatchedPercentageLabel.setText("Highest match: 0%");
                            uniquePercentageLabel.setText("Unique: 100%");
                            numberOfMatchesLabel.setText("Number of matches: 0");
                            documentSimilaritiesTextArea.setText(textToCompare);
                            webSourcesMatchedTextArea.setText("No similar articles found on the web.");
                        }
                    }
                });
            }
        }).start();
        
    }
    
    /*
     *  upload document to database and link to currently logged in user
     */
    @FXML
    void uploadButtonAction(ActionEvent event) {
        uploadDocumentLoadingGif.setVisible(true);
        inputScreenErrorLabel.setText("");
        final String title = titleField.getText();
        final String text = inputTextArea.getText();
        // check if title or text field is empty:
        if (title.isEmpty() || text.isEmpty()) {
            inputScreenErrorLabel.setText("Error, title or text field is empty.");
            System.out.println("Error, title or text field is empty.");
            uploadDocumentLoadingGif.setVisible(false);
            return;
        }
        // Run the following in a new thread:
        new Thread(new Runnable() {
            @Override
            public void run() {
                // store the document in the database, along with linking it to the user that uploaded it
                Firebase.storeDocumentInDatabase(text, Firebase.currentUser.email, title);
                // Run this after document has been stored in database:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        uploadDocumentLoadingGif.setVisible(false);
                        // Allow user to access translate screen:
                        selectLanguageScreen.setDisable(false);
                    }
                });
            }
        }).start();
    }
    
    @FXML
    void translateButtonAction(ActionEvent event) {
        translateTextErrorLabel.setText("");
        final MicrosoftTextTranslate translator = new MicrosoftTextTranslate();
        final String text = inputTextArea.getText().replaceAll("\n", " ");
        translateTextLoadingGif.setVisible(true);
        if (targetLanguageChoiceBox.getSelectionModel().isEmpty()) { // if there is no selected language
            translateTextErrorLabel.setText("No target language currently selected.");
            System.out.println("No target language currently selected.");
            translateTextLoadingGif.setVisible(false);
            return;
        }
        translator.setParams(targetLanguageChoiceBox.getValue().toString());
        System.out.println("Target language: " + targetLanguageChoiceBox.getValue().toString());
        // Run the following in a new thread:
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    translator.translateAString(text);
                } catch (Exception ex) {
                    // maybe add some set error label messages here to say what level error it was, i.e. 400, 401, etc, and their descriptions
                    System.out.println(ex.getMessage());
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        translateTextLoadingGif.setVisible(false);
                        // only set text area if translation was successful
                        if (!translator.translatedText.isEmpty()) {
                            System.out.println("Successfully translated text!");
                            System.out.println("After translation:\n" + translator.prettify(translator.translatedText));
                            translatedTextArea.setText(translator.getTextFromJson(translator.translatedText));
                        } else {
                            System.out.println("Something went wrong, translatedText is empty.");
                            translateTextErrorLabel.setText("Error, failed to translate.");
                        }
                    }
                });
            }
        }).start();
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
        System.out.println("Fetching all documents from database...");
        Firebase.getAllDocumentsFromDatabase();
        testDebugTextArea.appendText("Documents printed in console.");
        System.out.println("Fetched...");
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
                            if (Firebase.signIn(email, password, false)) {
                                System.out.println("Now logging the user in...");
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
                            currentUserLoggedInLabel.setText("Logged in: " + email);
                            if(Firebase.currentUser.admin) {
                                debugTestButton.setVisible(true);
                            }
                            signInErrorLabel.setText("");
                            signInEmailField.clear();
                            signInPasswordField.clear();
                            signInLoadingGif.setVisible(false);
                            // change screens:
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
                        Firebase.signIn(email, password, true);
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
                            createAccountPasswordField.clear();
                            System.out.println("createAccountButtonAction() successfully created a user.");
                            // Automatically sign in the newly created user
                            currentUserLoggedInLabel.setText("Logged in: " + email);
                            if(Firebase.currentUser.admin) {
                                debugTestButton.setVisible(true);
                            }
                            // Change screens:
                            loginScreen.setDisable(true);
                            loginScreen.setVisible(false);
                            mainScreen.setDisable(false);
                            mainScreen.setVisible(true);
                            System.out.println("Newly created user is now signed in.");
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
        inputScreenErrorLabel.setText("");
        fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) { // if there was a file chosen
            String fileText = "";
            System.out.println("file name: " + file.getName());
            try {
                // determine what the file extension is:
                if (file.getName().matches(".+.txt")) { // matches [anyCharacters].txt
                    // read from the .txt file and store in the relevant text fields and areas:
                    BufferedReader in = new BufferedReader (new FileReader (file.getAbsolutePath()));
                    String line = in.readLine();
                    while (line != null) {
                        fileText += line + "\n";
                        line = in.readLine();
                    }
                    in.close();
                    System.out.println("fileText: " + fileText);
                    inputTextArea.setText(fileText);
                    titleField.setText(file.getName().substring(0, file.getName().indexOf(".txt")));
//                    filePathTextField.setText(file.getAbsolutePath());
                } else if (file.getName().matches(".+.docx")) { // matches [anyCharacters].docx
                    // read from the .docx file and store in the relevant text fields and areas:
                    FileInputStream in;
                    in = new FileInputStream(file.getAbsolutePath());
                    try {
                        XWPFDocument document = new XWPFDocument(in);
                        List<XWPFParagraph> paragraphs = document.getParagraphs();
                        for (XWPFParagraph para : paragraphs)
                            fileText += para.getText() + "\n";
                        in.close();
                        System.out.println("fileText: \n" + fileText);
                        inputTextArea.setText(fileText);
                        titleField.setText(file.getName().substring(0, file.getName().indexOf(".docx")));
                    } catch (Exception ex) {
                        System.out.println("Something is wrong with the .docx file.");
                        inputScreenErrorLabel.setText("Something is wrong with this .docx file. Please use a different file.");
                    }
                } else {
                    inputScreenErrorLabel.setText("Error, either a .docx or .txt file must be selected.");
                }
            } catch (IOException ex) {
                System.out.println("IOException: reading '" + file.getName() + "'.");
                inputScreenErrorLabel.setText("Error while reading '" + file.getName() + "'.");
            }
        } else {
            System.out.println("No file was selected.");
        }
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
        // Some of thse languages are not being used because they have different characters,
        // that aren't being handled.
        targetLanguageChoiceBox.setItems(FXCollections.observableArrayList(
            /*"Arabic", "Chinese Simplified", "Chinese Traditional",*/
            "English", "French", "German", /*"Hindi",*/
            "Italian", /*"Russian",*/ "Spanish"
        ));
    }
}

abstract class timeoutRunnable implements Callable<Boolean> {
    @Override
    abstract public Boolean call() throws Exception;
}