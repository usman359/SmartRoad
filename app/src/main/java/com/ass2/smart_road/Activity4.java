package com.ass2.smart_road;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity4 extends AppCompatActivity {

    LoadingDialogForActivity4 loadingDialogForActivity4;
    Bitmap bitmap;
    String Encoded_img_1;
    String Encoded_img_2;
    String Encoded_img_3;
    String Encoded_img_4;
    Uri imageuri;
    Intent goNxt;
    int numRequests = 4;
    boolean server_off = false;
    // Array list to store class names and confidences
    ArrayList<String> signBoard_classes = new ArrayList<String>();
    ArrayList<Double> signBoard_conf = new ArrayList<Double>();
    ArrayList<String> signRecommend_classes = new ArrayList<String>();
    ArrayList<Double> signRecommend_conf = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        loadingDialogForActivity4 = new LoadingDialogForActivity4(this);
        loadingDialogForActivity4.ShowDialog("Getting Results...");
        // ***************************************************************************
        // Getting data from the FourImages Activity
        // retrieve image's uri
        Intent i = getIntent();
        goNxt = new Intent(Activity4.this, Activity5.class);
        try {
            // Getting image from Uri and converting it to bitmap
            // Image 1
            imageuri = (Uri) i.getParcelableExtra("Img-1");
            // saving it in intent for pass it to next
            goNxt.putExtra("Img-1", imageuri);
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
            Encoded_img_1 = get_encodedImg(bitmap);
            // Image 2
            imageuri = (Uri) i.getParcelableExtra("Img-2");
            // saving it in intent for pass it to next
            goNxt.putExtra("Img-2", imageuri);
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
            Encoded_img_2 = get_encodedImg(bitmap);
            // Image 3
            imageuri = (Uri) i.getParcelableExtra("Img-3");
            // saving it in intent for pass it to next
            goNxt.putExtra("Img-3", imageuri);
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
            Encoded_img_3 = get_encodedImg(bitmap);
            // Image 4
            imageuri = (Uri) i.getParcelableExtra("Img-4");
            // saving it in intent for pass it to next
            goNxt.putExtra("Img-4", imageuri);
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
            Encoded_img_4 = get_encodedImg(bitmap);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //  using volley to send request to server
        Make_Server_Request_N_Get_Results(Encoded_img_1);
        Make_Server_Request_N_Get_Results(Encoded_img_2);
        Make_Server_Request_N_Get_Results(Encoded_img_3);
        Make_Server_Request_N_Get_Results(Encoded_img_4);
    }
    public void Make_Server_Request_N_Get_Results(String Img){
        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, IPServer.getURL(), new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("Sign Detection : ", obj.get("name").toString());
                    Log.d("Sign Recommendation : ", obj.get("recommended_sign_name").toString());


                    if(obj.get("confidence").equals(null))
                        signBoard_conf.add(0.0);
                    else
                        signBoard_conf.add(obj.getDouble("confidence"));

                    if(obj.get("name").equals(null))
                        signBoard_classes.add("");
                    else
                        signBoard_classes.add(obj.getString("name"));

                    if(obj.get("recommended_sign_name").equals(null))
                        signRecommend_classes.add("");
                    else
                        signRecommend_classes.add(obj.getString("recommended_sign_name"));


                    if(obj.get("recommended_sign_confidence").equals(null))
                        signRecommend_conf.add(0.0);
                    else
                        signRecommend_conf.add(obj.getDouble("recommended_sign_confidence"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    // count down the request value when the response is received
                    move_to_nxt_activity(--numRequests);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(!server_off) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Server not Responding"
                                    , Toast.LENGTH_LONG
                            ).show();
                            Log.d("onErrorResponse: ", error.toString().trim());
                            // if not getting connected with server
                            // then return back to parent activity which is Activity 3
                            loadingDialogForActivity4.HideDialog();
                            startActivity(new Intent(Activity4.this, Activity3.class));
                            finish();
                        }
                        server_off = true;
                    }
                })
        {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", Img);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(request);

    }
    // this function gets Image Uri as input and returns image in string encoded form
    public String get_encodedImg(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ImgbyteArray = stream.toByteArray();
        final String imageString = Base64.encodeToString(ImgbyteArray, Base64.DEFAULT);
        return imageString;
    }
    public void move_to_nxt_activity(int numRequests){
        // checking status for moving to next activity
        if (numRequests == 0) {
            //now first make decision of traffic sign
            make_traffic_sign_decision(signBoard_classes, signRecommend_classes, signBoard_conf, signRecommend_conf);
            loadingDialogForActivity4.HideDialog();
            startActivity(goNxt);
            finish();
        }
    }
    private void make_traffic_sign_decision(ArrayList<String> st1, ArrayList<String> st2, ArrayList<Double> conf1, ArrayList<Double> conf2){
        // iterate through each class's confidence if exists
        //traffic sign board detection counter
        int left_signBoard_detection_count = 0,
                right_signBoard_detection_count = 0,
                uturn_signBoard_detection_count = 0;
        //traffic sign board recommendation counter
        int left_sign_recommendation_count = 0,
                right_sign_recommendation_count = 0,
                uturn_sign_recommendation_count = 0;
        // Iterating ArrayList using loop
        for(int i =0 ; i < st1.size(); ++i){
            // calculating sign board detections
            if(st1.get(i).equals("left"))
                left_signBoard_detection_count++;
            else if (st1.get(i).equals("right"))
                right_signBoard_detection_count++;
            else if (st1.get(i).equals("uturn"))
                uturn_signBoard_detection_count++;
            //calculating sign recommendations
            if(st2.get(i).equals("leftcurve"))
                left_sign_recommendation_count++;
            else if (st2.get(i).equals("rightcurve"))
                right_sign_recommendation_count++;
            else if (st2.get(i).equals("uturn"))
                uturn_sign_recommendation_count++;

        }
        //Resultant String
        String result1 = " ";
        // decision for trafffic sign board
        if(left_signBoard_detection_count >= 2)
            result1 = "Left sign Board";
        else if (right_signBoard_detection_count >= 2)
            result1 = "Right sign Board";
        else if (uturn_signBoard_detection_count >= 2)
            result1 = "Uturn sign Board";


        String result2 = " ";
        // decision for traffic sign recommendation
        if(left_sign_recommendation_count > 2)
            result2 = "Left sign";
        else if (right_sign_recommendation_count > 2)
            result2 = "Right sign";
        else if (uturn_sign_recommendation_count > 2)
            result2 = "Uturn sign";

        goNxt.putExtra("Board",result1);
        goNxt.putExtra("Sign",result2);

        // check if results are empty then go back
        if(result1.equals(" ") && result2.equals(" ")) {
            Toast.makeText(
                    getApplicationContext(),
                    "Not Applicable on this"
                    , Toast.LENGTH_LONG
            ).show();
            // if no results from server
            // then return back to parent activity which is Activity 3
//            Intent intent = new Intent(Activity4.this, Activity3.class);
//            loadingDialogForActivity4.HideDialog();
//            startActivity(intent);
//            finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        // if all the responses received successfully then navigate to Activity 5
        move_to_nxt_activity(numRequests);
    }

}
