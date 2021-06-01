package com.example.apitest2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, profileBtn, searchBtn, stopBtn, customMatchBtn;
    EditText email, password;
    TextView details;
    ProgressBar loadingPB;

    String token = "";
    JSONObject responseData, bodyData, headerData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.submitBtn);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        details = findViewById(R.id.details);
        loadingPB = findViewById(R.id.progressBar);

        profileBtn = findViewById(R.id.profileBtn);
        searchBtn = findViewById(R.id.searchBtn);
        stopBtn = findViewById(R.id.stopSearchBtn);
        customMatchBtn = findViewById(R.id.matchBtn);

        ApiDataService apiDataService = new ApiDataService(MainActivity.this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                apiDataService.driverLoginData(email.getText().toString(), password.getText().toString(), new ApiDataService.VolleyResponseListener() {
                    @Override
                    public void onError(Object message) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(message.toString());
                    }

                    @Override
                    public void onResponse(Object responseObject) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(responseObject.toString());
                        try {
                            responseData = new JSONObject(responseObject.toString());
                            System.out.println(responseData);

                            bodyData = (JSONObject) responseData.get("body");
                            headerData = (JSONObject) responseData.get("headers");

                            System.out.println("body:" + bodyData);
                            System.out.println("headers:" + headerData);
                            System.out.println(headerData.keys());

                            if (headerData.has("auth-token")) token = (String) headerData.get("auth-token");
                            else token = (String) headerData.get("Auth-Token");
                            System.out.println("token:" + token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //data.setText(String.format("%s %s", email.getText(), password.getText()));

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                apiDataService.getDriverProfileData( token, new ApiDataService.VolleyResponseListener() {
                    @Override
                    public void onError(Object message) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(message.toString());
                    }

                    @Override
                    public void onResponse(Object responseObject) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(responseObject.toString());
                        try {
                            responseData = new JSONObject(responseObject.toString());
                            System.out.println(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //data.setText(String.format("%s %s", email.getText(), password.getText()));
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                apiDataService.searchPassenger( token, new ApiDataService.VolleyResponseListener() {
                    @Override
                    public void onError(Object message) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(message.toString());
                    }

                    @Override
                    public void onResponse(Object responseObject) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(responseObject.toString());


                        try {
                            responseData = new JSONObject(responseObject.toString());
                            System.out.println(responseData);

                            if(responseData.has("passengerInfo")) {
                                JSONObject passengerInfo = (JSONObject) responseData.get("passengerInfo");

                                JSONObject passengerData = (JSONObject) passengerInfo.get("passengerData");
                                JSONArray pickUpPoint = (JSONArray) passengerInfo.get("pickUpPoint");

                                double lat = Double.parseDouble(pickUpPoint.getString(0));
                                double lon = Double.parseDouble(pickUpPoint.getString(1));

                                System.out.println("passengerData: " + passengerData);
                                System.out.println("pickUpPoint: " + lat + " , " + lon);
                            } else {
                                String message =(String) responseData.get("message");
                                System.out.println(message);
                                //first time or no match so nothing I guess
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //data.setText(String.format("%s %s", email.getText(), password.getText()));
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                apiDataService.stopSearchPassenger( token, new ApiDataService.VolleyResponseListener() {
                    @Override
                    public void onError(Object message) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(message.toString());
                    }

                    @Override
                    public void onResponse(Object responseObject) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(responseObject.toString());


                        try {
                            responseData = new JSONObject(responseObject.toString());
                            System.out.println(responseData);

                            if(responseData.has("passengerInfo")) {
                                JSONObject passengerInfo = (JSONObject) responseData.get("passengerInfo");
                                System.out.println(passengerInfo);
                            } else {
                                String message =(String) responseData.get("message");
                                System.out.println(message);
                                //first time or no match so nothing I guess
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //data.setText(String.format("%s %s", email.getText(), password.getText()));
            }
        });

        customMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                apiDataService.customMatching("607478178c29c1408cfad295", new ApiDataService.VolleyResponseListener() {
                    @Override
                    public void onError(Object message) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(message.toString());
                    }

                    @Override
                    public void onResponse(Object responseObject) {
                        loadingPB.setVisibility(View.GONE);
                        details.setText(responseObject.toString());
                        try {
                            responseData = new JSONObject(responseObject.toString());
                            System.out.println(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //data.setText(String.format("%s %s", email.getText(), password.getText()));
            }
        });
    }
}