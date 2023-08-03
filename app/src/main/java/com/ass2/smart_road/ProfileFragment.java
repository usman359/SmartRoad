package com.ass2.smart_road;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    ImageView profileImage;
    GoogleSignInClient mGoogleSignInClient;
    String nameOfAccount;
    TextView name, signout;
    ImageView signoutdrawable;
    FirebaseAuth mAuth;
    String phone;

    String email;
    TextView personalinformation, notifications;
    TextView help;
    TextView language, preferences;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            profileImage.setImageURI(data.getData());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity())
//                .inflate(R.layout.fragment_profile, null);


        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        profileImage = view.findViewById(R.id.profileImage);
        personalinformation = view.findViewById(R.id.personalinformation);
//        notifications = view.findViewById(R.id.notifications);

        help = view.findViewById(R.id.help);

//        language = view.findViewById(R.id.language);
//        preferences = view.findViewById(R.id.preferences);






//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HelpFragment(), null).addToBackStack(null).commit();


        name = view.findViewById(R.id.name);

        name.setVisibility(View.GONE);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, 1000);
            }
        });

        signout = view.findViewById(R.id.signout);
        signoutdrawable = view.findViewById(R.id.signoutdrawable);
        mAuth = FirebaseAuth.getInstance();
        DialogInterface.OnClickListener listener = null;



//        Bundle data = this.getArguments();
//        if (data != null) {
//            phone = data.getString("phone");
////            Log.d(name, "Name is:");
//        }

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid()).child("phoneNumber");

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phone = snapshot.getValue().toString();
                if (!phone.isEmpty()) {
                    name.setText(phone);
                    name.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(), phone, Toast.LENGTH_SHORT).show();
//                    Log.d(phone, "Name is:");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            nameOfAccount = acct.getDisplayName();
            email = acct.getEmail();
            name.setVisibility(View.VISIBLE);
            name.setText(nameOfAccount);
        }


        personalinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalInformation.class);
                intent.putExtra("name", nameOfAccount);
                intent.putExtra("mobilenumber", phone);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new HelpFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment).commit();
            }
        });

//            name.setText(phone);


        signoutdrawable.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Log.d(String.valueOf(acct.getDisplayName()), "Username is");

                SignInDetails signInDetails = new SignInDetails();
                if (signInDetails.getPhoneNumber() != "") {
                    startActivity(new Intent(getActivity(), Activity1.class));
                    getActivity().finish();


                }
                mGoogleSignInClient.signOut();
                mAuth.signOut();
                startActivity(new Intent(getActivity(), Activity1.class));
                getActivity().finish();

//                                                   DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//                                                       @Override
//                                                       public void onClick(DialogInterface dialogInterface, int i) {
//                                                           switch (i) {
//                                                               case DialogInterface.BUTTON_POSITIVE:
//                                                                   startActivity(new Intent(getActivity(), Activity1.class));
//                                                                   getActivity().finish();
//                                                                   break;
//
//                                                               case DialogInterface.BUTTON_NEGATIVE:
//                                                                   break;
//                                                           }
//                                                       }
//                                                   };
//                                               }
//                                           });


//                // Build the alert dialog
//                return new AlertDialog.Builder(getActivity())
//                        .setTitle("Changing Message")
//                        .setView(view)
//                        .setPositiveButton(android.R.string.ok, listener)
//                        .setNegativeButton(android.R.string.cancel, listener)
//                        .create();
//            }


//                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
//
//                        // title
//                        alertDialogBuilder.setTitle("AlertDialog");
//
//                        // dialogmessage
//                        alertDialogBuilder.setMessage("Confirm to exit")
//                                .setCancelable(false)
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        // IF YES IS CLICKED, YOU WILL EXIT
//                                        startActivity(new Intent(getActivity(), Activity1.class));
//                                    }
//                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        // IF NO IS CLICKED, THE DIALOG WOULD CLOSE
//                                        dialogInterface.cancel();
//                                    }
//                                });
//
//                    }
//                });
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(String.valueOf(acct.getDisplayName()), "Username is");
                SignInDetails signInDetails = new SignInDetails();
                if (signInDetails.getPhoneNumber() != "") {
                    startActivity(new Intent(getActivity(), Activity1.class));
                    getActivity().finish();
                }
               mGoogleSignInClient.signOut();
                mAuth.signOut();
                startActivity(new Intent(getActivity(), Activity1.class));
                getActivity().finish();

//                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//
//                // title
//                alertDialogBuilder.setTitle("AlertDialog");
//
//                // dialogmessage
//                alertDialogBuilder.setMessage("Confirm to exit")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
////                                Toast.makeText(getActivity(), "Dialog box", Toast.LENGTH_SHORT).show();
//                                // IF YES IS CLICKED, YOU WILL EXIT
//                                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        startActivity(new Intent(getActivity(), Activity1.class));
//                                    }
//                                });
//
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // IF NO IS CLICKED, THE DIALOG WOULD CLOSE
//                                dialogInterface.cancel();
//                            }
//                        });
//
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}


