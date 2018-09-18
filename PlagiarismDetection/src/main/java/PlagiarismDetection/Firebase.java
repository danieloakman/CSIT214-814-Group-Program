/*
 * CSIT214/814 GROUP ALPHA
 */

package PlagiarismDetection;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.database.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Firebase {
    static public void initialise (){
        try {
            FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    //          .setServiceAccountId("firebase-adminsdk-x0na7@csit314-814-group-project.iam.gserviceaccount.com")
                .setDatabaseUrl("https://csit314-814-group-project.firebaseio.com/")
                .build();
            FirebaseApp.initializeApp(options);
            System.out.println("Successfully initialised FirebaseApp.");
        } catch (IOException ex) {
            System.err.println("Firebase.initialise() error: " + ex.getMessage());
        }
    }
    
    static public void createUserWithEmailAndPassword (String email, String password) throws InterruptedException, ExecutionException {
        String uid = email.substring(0, email.indexOf("@")); // johnsmith@gmail.com becomes uid = johnsmith
        CreateRequest request = new CreateRequest()
            .setUid(uid)
            .setEmail(email)
            .setPassword(password);
        FirebaseAuth.getInstance().createUserAsync(request).get();
    }
    
    static public void deleteUserByEmail (String email) throws InterruptedException, ExecutionException {
        UserRecord userRecord = getUserByEmail(email);
        FirebaseAuth.getInstance().deleteUserAsync(userRecord.getUid()); 
    }
    
    static public UserRecord getUserByEmail (String email) throws InterruptedException, ExecutionException {
        return FirebaseAuth.getInstance(FirebaseApp.getInstance()).getUserByEmailAsync(email).get();
    }
    
    static public void updateUserPasswordByEmail (String email, String password) throws InterruptedException, ExecutionException {
        UserRecord userRecord = getUserByEmail(email);
        UpdateRequest request = new UpdateRequest(userRecord.getUid())
            .setPassword(password);
        FirebaseAuth.getInstance().updateUserAsync(request);
    }
    
    static public ArrayList<UserRecord> getAllUserRecords () throws InterruptedException, ExecutionException {
        ArrayList<UserRecord> userRecordList = new ArrayList();
        ListUsersPage page = FirebaseAuth.getInstance().listUsersAsync(null).get();
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                userRecordList.add(user);
            }
            page = page.getNextPage();
        }
        return userRecordList;
    }
    
    /*
    * A Custom token is used for signing in a user later
    */
    static public String getCustomTokenByEmail(String email) throws InterruptedException, ExecutionException, FirebaseAuthException {
        return FirebaseAuth.getInstance().createCustomToken(getUserByEmail(email).getUid());
    }
    
    static public void signIn (String email, String password) throws InterruptedException, ExecutionException, FirebaseAuthException {
        String customToken = getCustomTokenByEmail(email);
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(customToken);
        System.out.println(decodedToken.toString());
    }
    
    static public void storeDocumentInDatabase (String documentText, String nameOfUploader, String title) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        
        // add a document to Documents tree
        Document doc = new Document(title, dtf.format(now), documentText, nameOfUploader);
        dbRef.child("Documents").push().setValueAsync(doc);
        
        // add name of document to user's tree
//        Map<String, Object> data = new HashMap();
//        data.put("Document title", nameOfDocument);
//        dbRef.child("Users/" + nameOfUploader + "/Uploaded Documents").push().updateChildrenAsync(data);
    }
    
	/*
	* Doesn't work yet.
	*/
    static public void getDocumentFromDatabase () {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Documents");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Document doc = dataSnapshot.getValue(Document.class);
              System.out.println(doc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
              System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

class Document {
    public String timeOfUpload;
    public String text;
    public String uploader;
    public String title;

    public Document(String title ,String timeOfUpload, String text, String uploader) {
        this.title = title;
        this.timeOfUpload = timeOfUpload;
        this.text = text;
        this.uploader = uploader;
    }
}