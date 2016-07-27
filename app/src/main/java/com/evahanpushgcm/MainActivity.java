package com.evahanpushgcm;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private static String AWBNO = "awbno";


    HashMap<String, String> mapArrayList = new HashMap<>();
    ArrayList<HashMap<String, String>> mlist = new ArrayList<>();


    HashMap<String, String> hasput = new HashMap<>();
    ArrayList<String> list = new ArrayList<>();
    RelativeLayout relativeLayoutmain;
    EditText editTextawbno;
    Button btnsend;
    String awbno;


    //*********************************************
    public static final int NOTIFICATION_ID = 1;
    public static final String REG_ID = "regId";
    private static String LSTATUS = "latest_status";
    private static String LTIME = "latest_time";

    public static String TAG = "NotifyActivity";
    NotificationCompat.Builder builder;
    String regId, msg;
    SharedPreferences prefs;
    GoogleCloudMessaging gcm;
    Context context = this;
    private NotificationManager mNotificationManager;
    private AsyncReuse requestServer;
    public String outputdata = "";

    //*********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextawbno = (EditText) findViewById(R.id.edtawbno);
        btnsend = (Button) findViewById(R.id.btnsend);
        relativeLayoutmain = (RelativeLayout) findViewById(R.id.relativemain);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                awbno = editTextawbno.getText().toString();
                if (awbno.equals("")) {
                    Snackbar snackbar = Snackbar.make(relativeLayoutmain, "Please Enter Your AirwayBill No.", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
                if (awbno.length() < 5) {
                    Snackbar snackbar = Snackbar.make(relativeLayoutmain, "Invalid AirwayBill No.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    new AsyncTaskAwbNoToAddDeviceIdToServer().execute();

                }

            }
        });
        //********************************

        gcm = GoogleCloudMessaging.getInstance(this);
        prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        // registerInBackground();
        //regId = getRegistrationId();

        //********************************

    }

    private class AsyncTaskAwbNoToAddDeviceIdToServer extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        boolean flag = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url = "http://s.evahanexpress.com/Traking/Check_service/trackingappapi?awbno=" + awbno;
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject object = jsonObject.optJSONObject("Shiptrack");
                    if (object == null) {
                        flag = true;
                    } else {
                        AWBNO = object.getString("awbno");
                        hasput.put("AWBNO", AWBNO);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (flag == true) {

                Snackbar snackbar = Snackbar.make(relativeLayoutmain, "Something went wrong", Snackbar.LENGTH_LONG);
                snackbar.show();

            } else {
                // uploadToServer();
                Intent intent = new Intent(MainActivity.this, NotificationDetailActivity.class);
                intent.putExtra("awbno", awbno);
                startActivity(intent);
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }

}
