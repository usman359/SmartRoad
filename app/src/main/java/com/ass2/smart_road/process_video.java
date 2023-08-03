package com.ass2.smart_road;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class process_video {
    private MediaMetadataRetriever retriever;
    private String videoPath;
    private static final int frameRate = 30;
    private int frames;
    private ArrayList<Double> latitudeArray;
    private ArrayList<Double> longitudeArray;

    private Context context;
    boolean server_off = false;
    // Array list to store class names and confidences
    ArrayList<String> signBoard_classes = new ArrayList<String>();
    ArrayList<Double> signBoard_conf = new ArrayList<Double>();
    ArrayList<String> signRecommend_classes = new ArrayList<String>();
    ArrayList<Double> signRecommend_conf = new ArrayList<Double>();

    int numRequests;
    process_video(String videoPath, Context c,
                  ArrayList<Double> latti_arr, ArrayList<Double> longi_arr) {
        this.videoPath = videoPath;
        this.context = c;
        this.retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);

        // Get the duration of the video
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationMs = Long.parseLong(durationStr);

        // Calculate the number of frames to extract
        frames = (int) (durationMs / 1000.0 * frameRate);
        Log.d("frames", String.valueOf(frames));

        numRequests = frames /15; // considering to use only two frames in 1 second, 1 sec has 30 frames

        latitudeArray = latti_arr;
        longitudeArray = longi_arr;

        Log.d("Latitude size", String.valueOf(latitudeArray.size()));
        Log.d("Longitude size", String.valueOf(longitudeArray.size()));
        // Step 6: Print the coordinates in a loop
        for (int i = 0; i < latitudeArray.size(); i++) {
            double latitude = latitudeArray.get(i);
            double longitude = longitudeArray.get(i);
            Log.d("Coordinates", "Latitude: " + latitude + ", Longitude: " + longitude);
        }
    }

    public void release() {
        retriever.release();
    }

    public void get_recommendation() {
        for (int i = 0; i < frames; i += 15) {
            long timeUs = i * 1000000 / frameRate;
            Log.d("frame" + i, String.valueOf(timeUs));
            Bitmap frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Make_Server_Request_N_Get_Results(get_encodedImg(frame));
            if(server_off){
                // making iterating variable to frames size so loop will exit
                break;
            }

            // Do something with the frame, like save it to a new video file
        }

    }

    private String get_encodedImg(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ImgbyteArray = stream.toByteArray();
        final String imageString = Base64.encodeToString(ImgbyteArray, Base64.DEFAULT);
        return imageString;
    }
    private void make_traffic_sign_decision(ArrayList<String> st1, ArrayList<String> st2, ArrayList<Double> conf1, ArrayList<Double> conf2){
        Log.d("Recommendation Size : ", Integer.toString(st1.size()));
//        for(int i = 0; i < st1.size() - 3; ++i){
//            if(st1.get(i).equals(st1.get(i+1)) && st1.get(i+1).equals(st1.get(i+2))){
//                while (i < st1.size() && st1.get(i+2).equals(st1.get(i+3))){
//                    ++i;
//                }
//            }
//        }
//        // iterate through each class's confidence if exists
//        //traffic sign board detection counter
//        int left_signBoard_detection_count = 0,
//                right_signBoard_detection_count = 0,
//                uturn_signBoard_detection_count = 0;
//        //traffic sign board recommendation counter
//        int left_sign_recommendation_count = 0,
//                right_sign_recommendation_count = 0,
//                uturn_sign_recommendation_count = 0;
//        // Iterating ArrayList using loop
//        for(int i =0 ; i < st1.size(); ++i){
//            // calculating sign board detections
//            if(st1.get(i).equals("left"))
//                left_signBoard_detection_count++;
//            else if (st1.get(i).equals("right"))
//                right_signBoard_detection_count++;
//            else if (st1.get(i).equals("uturn"))
//                uturn_signBoard_detection_count++;
//            //calculating sign recommendations
//            if(st2.get(i).equals("leftcurve"))
//                left_sign_recommendation_count++;
//            else if (st2.get(i).equals("rightcurve"))
//                right_sign_recommendation_count++;
//            else if (st2.get(i).equals("uturn"))
//                uturn_sign_recommendation_count++;
//
//        }
//        //Resultant String
//        String result1 = " ";
//        // decision for traffic sign board
//        if(left_signBoard_detection_count >= 2)
//            result1 = "Left sign Board";
//        else if (right_signBoard_detection_count >= 2)
//            result1 = "Right sign Board";
//        else if (uturn_signBoard_detection_count >= 2)
//            result1 = "Uturn sign Board";
//
//
//        String result2 = " ";
//        // decision for traffic sign recommendation
//        if(left_sign_recommendation_count > 2)
//            result2 = "Left sign";
//        else if (right_sign_recommendation_count > 2)
//            result2 = "Right sign";
//        else if (uturn_sign_recommendation_count > 2)
//            result2 = "Uturn sign";
//
//        goNxt.putExtra("Board",result1);
//        goNxt.putExtra("Sign",result2);
//
//        // check if results are empty then go back
//        if(result1.equals(" ") && result2.equals(" ")) {
//            Toast.makeText(
//                    getApplicationContext(),
//                    "Not Applicable on this"
//                    , Toast.LENGTH_LONG
//            ).show();
//            // if no results from server
//            // then return back to parent activity which is Activity 3
////            Intent intent = new Intent(Activity4.this, Activity3.class);
////            loadingDialogForActivity4.HideDialog();
////            startActivity(intent);
////            finish();
//        }
    }
    private void move_to_nxt_activity(int numRequests){
        // checking status for moving to next activity
        if (numRequests == 0) {
            Log.d("move_to_nxt_activity: ", "Working....");
            //now first make decision of traffic sign
            make_traffic_sign_decision(signBoard_classes, signRecommend_classes, signBoard_conf, signRecommend_conf);
        }
    }

    private void Make_Server_Request_N_Get_Results(String Img){
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
                                    context,
                                    "Server not Responding"
                                    , Toast.LENGTH_LONG
                            ).show();
                            Log.d("onErrorResponse: ", error.toString().trim());
                            // if not getting connected with server
                            // then return back to parent activity which is Activity 3
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

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);

    }

}