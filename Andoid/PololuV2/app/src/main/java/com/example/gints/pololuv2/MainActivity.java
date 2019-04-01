package com.example.gints.pololuv2;
import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {

    //Rest api url and constant values
    String C_REST_API_URL = "http://10.41.121.55:5000/api/controls";
    int C_HALF_OF_SEEKBAR = 1000;

    //Class elements
    CallService cs = new CallService();
    private SeekBar sBarLeft,sBarRight;
    private Button stop,resume;
    private Switch connect_disconnect;
    private TextView tViewLeft,tViewRight,responseView;
    private EditText  ipAddressEditText;
    private JSONObject pololuJson;
    private int leftSpeed = 0;
    private int rightSpeed = 0;
    private String jsonValue;
    private JSONArray jsonArry;
    private boolean deviceIsOn;
    String  ipAdress;

    // Generate json string
    private String generateJson(int speed, String direction, String stop_start,String connectToDevice ) {
        jsonArry = new JSONArray();
        try {

            pololuJson.put("direction", direction);
            pololuJson.put("speed", speed);
            pololuJson.put("stopResume", stop_start);
            pololuJson.put("connection", connectToDevice);
            jsonValue = pololuJson.toString();
            jsonValue =  pololuJson.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonValue;
    }

    // calculate speed- if speed > 100 then + else -
    private int calculateSpeed(int speed) {
        int calculatedSpeed = 0;

        try {

            if (speed > C_HALF_OF_SEEKBAR) {

                calculatedSpeed = speed - C_HALF_OF_SEEKBAR;
            } else {
                calculatedSpeed = speed - C_HALF_OF_SEEKBAR;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return calculatedSpeed;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Buttons
         stop = (Button) findViewById(R.id.stop);
         resume = (Button) findViewById(R.id.resume);

        //Progress  Bar
        sBarLeft = (SeekBar) findViewById(R.id.seekBarLeft);
        sBarRight = (SeekBar) findViewById(R.id.seekBarRight);

        //Add start values
        sBarLeft.setProgress(C_HALF_OF_SEEKBAR);
        sBarRight.setProgress(C_HALF_OF_SEEKBAR);

        //Swich
        connect_disconnect = (Switch) findViewById(R.id.connect_disconnect);

        //text fields
        tViewLeft = (TextView) findViewById(R.id.seekBarLeftValue);
        tViewRight = (TextView) findViewById(R.id.seekBarRightValue);
        responseView = (TextView) findViewById(R.id.responseText);

        //Edit text
        ipAddressEditText = (EditText) findViewById(R.id.ipAdress);

        //jsonObject
        pololuJson = new JSONObject();

        //Default ip
         ipAdress =  C_REST_API_URL;


        ipAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ipAdress =  C_REST_API_URL;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ipAddressEditText.getText().toString().length() > 0) {
                    ipAdress = ipAddressEditText.getText().toString();
                }else{
                    ipAdress =  C_REST_API_URL;
                }
            }
        });

         sBarLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 leftSpeed = calculateSpeed(progress);
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {
                 //write custom code to on start progress
             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
                 if (deviceIsOn != false) {
                     tViewLeft.setText(leftSpeed + "/" + seekBar.getMax());
                     jsonValue = generateJson(leftSpeed, "left", "","connect");
                     responseView.setText(cs.doInBackground(jsonValue, ipAdress));
                 } else {
                     Toast.makeText(getApplicationContext(), "Please turn on device!", Toast.LENGTH_LONG).show(); // display the current state for switch's
                 }
             }
         });

         stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (deviceIsOn != false) {
                    jsonValue = generateJson(leftSpeed, "", "stop", "connect");
                    responseView.setText(cs.doInBackground(jsonValue, ipAdress));

                } else {
                    Toast.makeText(getApplicationContext(), "Please turn on device!", Toast.LENGTH_LONG).show(); // display the current state for switch's
                }
            }});

        resume.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (deviceIsOn != false) {
                    jsonValue = generateJson(leftSpeed, "", "resume", "connect");
                    responseView.setText(cs.doInBackground(jsonValue, ipAdress));
                } else {
                    Toast.makeText(getApplicationContext(), "Please turn on device!", Toast.LENGTH_LONG).show(); // display the current state for switch's
                }
            }});

         sBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 rightSpeed = calculateSpeed(progress);
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {
                 //write custom code to on start progress
             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
                 if (deviceIsOn != false){
                 tViewRight.setText(rightSpeed + "/" + seekBar.getMax());
                 jsonValue = generateJson(rightSpeed, "right", "","connect");
                 responseView.setText(cs.doInBackground(jsonValue, ipAdress));
             }
             else{
                     Toast.makeText(getApplicationContext(), "Please turn on device!", Toast.LENGTH_LONG).show();
                 }
             }
         });



        connect_disconnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String statusSwitch;

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    statusSwitch = "Connected";
                    jsonValue = generateJson(0, "left", "","connect");
                    responseView.setText(cs.doInBackground(jsonValue, ipAdress));
                    deviceIsOn = true;
                } else {
                    statusSwitch = "Disconnected";
                    jsonValue = generateJson(0, "", "","discconnect");
                    responseView.setText(cs.doInBackground(jsonValue, ipAdress));
                    deviceIsOn = false;
                    Toast.makeText(getApplicationContext(), "Switch :" + statusSwitch + "\n", Toast.LENGTH_LONG).show();

                }
            }
        });


    }

}


