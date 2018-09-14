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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Firebase {
    static public void initialise () {
        FileInputStream serviceAccount = null;
        FirebaseOptions options = null;
        try {
            serviceAccount = new FileInputStream("serviceAccountKey.json");
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://csit314-814-group-project.firebaseio.com/")
                    .build();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        FirebaseApp.initializeApp(options);
        System.out.println("Successfully initialised FirebaseApp.");
    }
    
    static public void createUserWithUid (String uid, String email, String phoneNumber) throws InterruptedException, ExecutionException, FirebaseAuthException {
        CreateRequest request = new CreateRequest()
        .setUid(uid)
        .setEmail(email)
        .setPhoneNumber(phoneNumber);
        UserRecord userRecord = FirebaseAuth.getInstance().createUserAsync(request).get();
        System.out.println("Successfully created new user: " + userRecord.getEmail());
    }
    
    static public void deleteUserByUid (String uid) throws InterruptedException, ExecutionException {
        FirebaseAuth.getInstance().deleteUserAsync(uid).get();
        System.out.println("Successfully deleted user with Uid: " + uid);
    }
    
    static public UserRecord getUserById (String uid) throws InterruptedException, ExecutionException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserAsync(uid).get();
        System.out.println("Successfully fetched user data: " + userRecord.getUid());
        return userRecord;
    }
    
    static public UserRecord getUserByEmail (String email) throws InterruptedException, ExecutionException {
        UserRecord userRecord = FirebaseAuth.getInstance(FirebaseApp.getInstance()).getUserByEmailAsync(email).get();
        System.out.println("Successfully fetched user data: " + userRecord.getEmail());
        return userRecord;
    }
}
