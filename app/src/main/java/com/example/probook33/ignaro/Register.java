package com.example.probook33.ignaro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.probook33.ignaro.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Activity {

    private EditText name,emailid,pwd,username;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "FIREBASE : ";
    private Button register,alreadyreg;
    private String email,password;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register");

        ActivityCompat.requestPermissions(Register.this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                1);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Intent intent=new Intent(Register.this,Dashboard.class);
            startActivity(intent);
        }

        name= (EditText) findViewById(R.id.name);
        pwd= (EditText) findViewById(R.id.password);
        emailid= (EditText) findViewById(R.id.email);
        username= (EditText) findViewById(R.id.username);
        register= (Button) findViewById(R.id.register);
        alreadyreg=(Button) findViewById(R.id.alreadyreg);



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                email=emailid.getText().toString();
                password=pwd.getText().toString();
                createAccount(email,password);
                pd = new ProgressDialog(Register.this);
                pd.setMessage("Registering... ");
                pd.show();

            }
        });

        alreadyreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Login.class);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Register.this, "Permission denied to use Location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void createAccount(final String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration Failed",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference mydata = database.getReference("users");
                            String userID=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                            User user=new User(username.getText().toString(),email,name.getText().toString());
                            mydata.child(userID).setValue(user);
                            Intent intent=new Intent(Register.this,Login.class);
                            startActivity(intent);
                        }

                        // ...
                    }
                });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(String.valueOf(name.getText())).build();
                    user.updateProfile(profileUpdates);
                    Intent intent=new Intent(Register.this,Login.class);
                    startActivity(intent);
                }
            }
        };
    }

    public static void signout()
    {
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onBackPressed() {
        this.finish(); // "Hide" your current Activity
    }

}
