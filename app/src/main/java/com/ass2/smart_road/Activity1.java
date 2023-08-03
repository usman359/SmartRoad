package com.ass2.smart_road;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Activity1 extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH";
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    Dialog dialog;

    EditText phone;
    Button continuebutton;
    String phoneNo;
    CountryCodePicker ccp;
    DatabaseReference mDatabaseRef;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        mAuth = FirebaseAuth.getInstance();


        phone = findViewById(R.id.phonenum);
        continuebutton = findViewById(R.id.continuebutton);
        ccp = findViewById(R.id.ccp);

        firebaseDatabase = FirebaseDatabase.getInstance();

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = mAuth.getCurrentUser();
                phoneNo = ccp.getTextView_selectedCountry().getText().toString() + phone.getText().toString();


                if (phone.getText().toString().isEmpty() == true) {
                    Toast.makeText(getApplicationContext(), "Phone number can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {

                    Intent intent = new Intent(getApplicationContext(), Activity2.class);
                    intent.putExtra("phoneNo", phoneNo);
                    startActivity(intent);

                }
            }
        });





        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        dialog = new Dialog(Activity1.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait1);
        dialog.setCanceledOnTouchOutside(false);
        // Getting the Continue with google Button Click
        Button signInWithGooglebtn = findViewById(R.id.google_signIn);
        signInWithGooglebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            dialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                dialog.dismiss();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // for creating a users folder entry in the real time database
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
                            String uploadId = mDatabaseRef.push().getKey();
//                            Log.d(mAuth.getCurrentUser().getUid(), "UID");

                            SignInDetails signInDetails = new SignInDetails(user.getDisplayName(), user.getEmail(), "", "", "");
                            mDatabaseRef.child(mAuth.getCurrentUser().getUid()).setValue(signInDetails);



                            Intent i = new Intent(Activity1.this,Activity3.class);
                            i.putExtra("email", user.getEmail());
                            startActivity(i);
                            finish();
                            dialog.dismiss();
                            //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //  Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);
                            dialog.dismiss();
                            Toast.makeText(Activity1.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mAuth.getCurrentUser()!=null) {
            Intent i = new Intent(Activity1.this,Activity3.class);
            startActivity(i);
            finish();
        }
    }




}

