package com.evahanpushgcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayActivity extends AppCompatActivity {


    private static String STATUS = "status";
    private static String TIME = "time";
    private static String TRAKING = "traking";
    private static String TRAKINGTIME = "time";
    private static String TRAKINGSTATUS = "status";

    private static String ORDERNO = "OrderNo";
    private static String SHIPPER = "Shipper";
    private static String CONSIGNEE = "Consignee";
    String[] from;
    int[] to;

    ListTrakingAdapter adapter;

    TextView textViewStatus, txttime, output, orderno, shipper, cons;
    TextView textViewheadingstatus, textViewheadingtime, textViewheadingtraking, textViewheadingorder, textViewheadingshipper, textViewheadingcons;
    LinearLayout linearLayoutstatus, linearLayouttime, linearLayoutorderno, linearLayoutshiper, linearLayoutcons, linearLayouttraking;


    HashMap<String, String> hasput = new HashMap<>();
    HashMap<String, String> mapArrayList = new HashMap<>();
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    ArrayList<HashMap<String, String>> mlist = new ArrayList<>();
    String awbno;


    ListView listViewtracking;
    public String outputdata = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setTitle("Tracking History");


        linearLayoutstatus = (LinearLayout) findViewById(R.id.linearlayoutstatus);
        linearLayouttime = (LinearLayout) findViewById(R.id.linearlayouttime);
        linearLayoutorderno = (LinearLayout) findViewById(R.id.linearlayoutorder);
        linearLayoutshiper = (LinearLayout) findViewById(R.id.linearlayoutshiper);
        linearLayoutcons = (LinearLayout) findViewById(R.id.linearlayoutcons);
        linearLayouttraking = (LinearLayout) findViewById(R.id.linearlayouttraking);

        textViewheadingstatus = (TextView) findViewById(R.id.statusheading);
        textViewheadingtime = (TextView) findViewById(R.id.timeheading);
        textViewheadingtraking = (TextView) findViewById(R.id.trakingheading);
        textViewheadingorder = (TextView) findViewById(R.id.ordernoheading);
        textViewheadingshipper = (TextView) findViewById(R.id.shipperheading);
        textViewheadingcons = (TextView) findViewById(R.id.consheading);


        Intent i = getIntent();
        awbno = i.getStringExtra("Notif");


        textViewStatus = (TextView) findViewById(R.id.textstatus);
        txttime = (TextView) findViewById(R.id.txttime);
        output = (TextView) findViewById(R.id.outputdata);
        orderno = (TextView) findViewById(R.id.txtorderno);
        shipper = (TextView) findViewById(R.id.txtshipper);
        cons = (TextView) findViewById(R.id.txtcons);

        listViewtracking = (ListView) findViewById(R.id.listviewtracking);

        new AsyncTaskDisplay().execute();

    }


    private class AsyncTaskDisplay extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            linearLayoutstatus.setVisibility(View.GONE);
            linearLayouttime.setVisibility(View.GONE);
            linearLayoutorderno.setVisibility(View.GONE);
            linearLayoutshiper.setVisibility(View.GONE);
            linearLayoutcons.setVisibility(View.GONE);
            linearLayouttraking.setVisibility(View.GONE);


            textViewheadingstatus.setVisibility(View.GONE);
            textViewheadingtime.setVisibility(View.GONE);
            textViewheadingtraking.setVisibility(View.GONE);
            textViewheadingorder.setVisibility(View.GONE);
            textViewheadingshipper.setVisibility(View.GONE);
            textViewheadingcons.setVisibility(View.GONE);


            progressDialog = new ProgressDialog(DisplayActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setTitle("Shipment Detail");
            progressDialog.show();

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


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

            linearLayoutstatus.setVisibility(View.VISIBLE);
            linearLayouttime.setVisibility(View.VISIBLE);
            linearLayoutorderno.setVisibility(View.VISIBLE);
            linearLayoutshiper.setVisibility(View.VISIBLE);
            linearLayoutcons.setVisibility(View.VISIBLE);
            linearLayouttraking.setVisibility(View.VISIBLE);

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
                awbno = "";

                //from = new String[]{"ttime", "tstate"};
                // to = new int[]{R.id.texttrakingtime, R.id.texttrackingstatus};
                // ListTrakingAdapter adapter = new ListTrakingAdapter(DisplayActivity.this, mlist);
                //listViewtracking.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }


            super.onPostExecute(result);
        }
    }
}
