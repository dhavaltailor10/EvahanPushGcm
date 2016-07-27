package com.evahanpushgcm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationDetailActivity extends AppCompatActivity implements GetResponse {

    private static String STATUS = "status";
    private static String TIME = "time";
    private static String TRAKING = "traking";
    private static String TRAKINGTIME = "time";
    private static String TRAKINGSTATUS = "status";

    private static String ORDERNO = "OrderNo";
    private static String SHIPPER = "Shipper";
    private static String CONSIGNEE = "Consignee";

    /*******************************************************/

    private static String REG_ID = "regId";
    private static String LSTATUS = "latest_status";
    private static String LTIME = "latest_time";


    /*******************************************************/

    TextView textViewStatus, txttime, output, orderno, shipper, cons;
    TextView textViewheadingstatus, textViewheadingtime, textViewheadingtraking, textViewheadingorder, textViewheadingshipper, textViewheadingcons;
    LinearLayout linearLayoutstatusdis, linearLayouttimedis, linearLayoutorderdis, linearLayoutshipper, linearLayoutconsdis, linearLayouttracking;


    HashMap<String, String> hasput = new HashMap<>();
    HashMap<String, String> mapArrayList = new HashMap<>();
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    ArrayList<HashMap<String, String>> mlist = new ArrayList<>();
    String awbno;
    private AsyncReuse requestServer;

    String regId, msg;
    SharedPreferences prefs;
    GoogleCloudMessaging gcm;
    Context context = this;
    ListView listViewtracking;
    public String outputdata = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        setTitle("Display Detail");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayoutstatusdis = (LinearLayout) findViewById(R.id.linearlayoutstatusdisplay);
        linearLayouttimedis = (LinearLayout) findViewById(R.id.linearlayouttimedisplay);
        linearLayoutorderdis = (LinearLayout) findViewById(R.id.linearlayoutorderdisplay);
        linearLayoutshipper = (LinearLayout) findViewById(R.id.linearlayoutshiperdisplay);
        linearLayoutconsdis = (LinearLayout) findViewById(R.id.linearlayoutconsdisplay);
        linearLayouttracking = (LinearLayout) findViewById(R.id.linearlayouttrakingdisplay);


        textViewheadingstatus = (TextView) findViewById(R.id.statusheadingdisplay);
        textViewheadingtime = (TextView) findViewById(R.id.timeheadingdisplay);
        textViewheadingtraking = (TextView) findViewById(R.id.trakingheadingdisplay);
        textViewheadingorder = (TextView) findViewById(R.id.ordernoheadingdisplay);
        textViewheadingshipper = (TextView) findViewById(R.id.shipperheadingdisplay);
        textViewheadingcons = (TextView) findViewById(R.id.consheadingdisplay);


        Intent i = getIntent();
        awbno = i.getStringExtra("awbno");

        textViewStatus = (TextView) findViewById(R.id.textstatusdisplay);
        txttime = (TextView) findViewById(R.id.txttimedisplay);
        output = (TextView) findViewById(R.id.outputdatadisplay);
        orderno = (TextView) findViewById(R.id.txtordernodisplay);
        shipper = (TextView) findViewById(R.id.txtshipperdisplay);
        cons = (TextView) findViewById(R.id.txtconsdisplay);

        listViewtracking = (ListView) findViewById(R.id.listviewtracking);

        new AsyncTaskDisplayDetail().execute();

        /****************************************************/
        gcm = GoogleCloudMessaging.getInstance(this);
        prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        registerInBackground();
        regId = getRegistrationId();

        /***************************************************/
    }

    @Override
    public Void getData(String objects) {
        try {
            JSONObject jsonObject = new JSONObject(objects.toString());
            Log.e("here", "==========" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class AsyncTaskDisplayDetail extends AsyncTask<Void, Void, Void> {
        boolean flag = false;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {


            linearLayoutstatusdis.setVisibility(View.GONE);
            linearLayouttimedis.setVisibility(View.GONE);
            linearLayoutorderdis.setVisibility(View.GONE);
            linearLayoutshipper.setVisibility(View.GONE);
            linearLayoutconsdis.setVisibility(View.GONE);
            linearLayouttracking.setVisibility(View.GONE);


            textViewheadingstatus.setVisibility(View.GONE);
            textViewheadingtime.setVisibility(View.GONE);
            textViewheadingtraking.setVisibility(View.GONE);
            textViewheadingorder.setVisibility(View.GONE);
            textViewheadingshipper.setVisibility(View.GONE);
            textViewheadingcons.setVisibility(View.GONE);


            progressDialog = new ProgressDialog(NotificationDetailActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setTitle("Shipment Detail");
            progressDialog.show();
            super.onPreExecute();
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

                        STATUS = object.getString("status");
                        TIME = object.getString("time");
                        ORDERNO = object.getString("OrderNo");
                        SHIPPER = object.getString("Shipper");
                        CONSIGNEE = object.getString("Consignee");

                        JSONArray jsonArray = object.optJSONArray("traking");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String TRAKINGTIME = data.getString("time");
                            String TRAKINGSTATUS = data.getString("status");
                            mapArrayList.put("ttime", TRAKINGTIME);
                            mapArrayList.put("tstatus", TRAKINGSTATUS);

                            outputdata += "Time :\t\t\t" + TRAKINGTIME + "\nStatus :\t\t" + TRAKINGSTATUS + "\n\n";
                            mlist.add(mapArrayList);
                        }


                        hasput.put("status", STATUS);
                        hasput.put("time", TIME);
                        hasput.put("OrderNo", ORDERNO);
                        hasput.put("Shipper", SHIPPER);
                        hasput.put("Consignee", CONSIGNEE);
                        list.add(hasput);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (flag == true) {

            } else {

                AddtoServer();
                linearLayoutstatusdis.setVisibility(View.VISIBLE);
                linearLayouttimedis.setVisibility(View.VISIBLE);
                linearLayoutorderdis.setVisibility(View.VISIBLE);
                linearLayoutshipper.setVisibility(View.VISIBLE);
                linearLayoutconsdis.setVisibility(View.VISIBLE);
                linearLayouttracking.setVisibility(View.VISIBLE);


                textViewheadingstatus.setVisibility(View.VISIBLE);
                textViewheadingtime.setVisibility(View.VISIBLE);
                textViewheadingtraking.setVisibility(View.VISIBLE);
                textViewheadingorder.setVisibility(View.VISIBLE);
                textViewheadingshipper.setVisibility(View.VISIBLE);
                textViewheadingcons.setVisibility(View.VISIBLE);

                try {
                    textViewStatus.setText(STATUS);
                    txttime.setText(TIME);
                    output.setText(outputdata);
                    orderno.setText(ORDERNO);
                    shipper.setText(SHIPPER);
                    cons.setText(CONSIGNEE);


                    //from = new String[]{"ttime", "tstate"};
                    // to = new int[]{R.id.texttrakingtime, R.id.texttrackingstatus};
                    // ListTrakingAdapter adapter = new ListTrakingAdapter(DisplayActivity.this, mlist);
                    //listViewtracking.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    /*************************/

    private String getRegistrationId() {
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            //     Log.i(TAG, "Registration not found.");
            return "";
        }
        return registrationId;
    }

    //save registration Id
    private void saveRegisterId(Context context, String regId) {
        SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        //   Log.i(TAG, "Saving regId on app version ");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.commit();
    }

    private void AddtoServer() {
        JSONObject jObject = new JSONObject();
        executeServerReq();
        try {
            jObject.put("awbno", awbno);
            jObject.put("regId", regId);
            jObject.put("latest_status", STATUS);
            jObject.put("latest_time", TIME);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestServer.getObjectQ(jObject);
        requestServer.execute();
    }

    private void executeServerReq() {
        requestServer = new AsyncReuse("http://evahanexpress.com/register.php", true, NotificationDetailActivity.this);
        requestServer.getResponse = (GetResponse) this;
    }


    public void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register("853162149840");
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                /*Toast.makeText(getApplicationContext(), "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                     .show();*/
                saveRegisterId(context, regId);
            }
        }.execute(null, null, null);
    }


}