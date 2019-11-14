package com.example.learner2drive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OffenseActivity extends AppCompatActivity {

    public static final String URL_OFFENSE="http://vscan.000webhostapp.com/TestDataApp/offense.php?learner_lic=";
    public static final String KEY_ISSUEDON="charged";
    public static final String KEY_CHARGE="offense";
    public static final String KEY_AMOUNT="amnt";
    public static final String JSON_ARRAY="result";

    private TextView learner;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offense);

        //label=findViewById(R.id.ofnselbl);
        learner=findViewById(R.id.learner_id);

        listView=findViewById(R.id.datalist);
        Intent intent=getIntent();
        String lic=intent.getStringExtra("learner_lic");
        learner.setText(lic);
        //learner.setVisibility(View.GONE);

        getData();
    }

    private void getData(){

        String url=URL_OFFENSE+learner.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OffenseActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        ArrayList<HashMap<String,String>>list=new ArrayList<HashMap<String,String>>();

        try {

            JSONObject jsonObject=new JSONObject(response);
            JSONArray result=jsonObject.getJSONArray(JSON_ARRAY);

            if(result.length()>0){
                for (int i=0;i<result.length();i++){

                    JSONObject jo=result.getJSONObject(i);
                    String charged=jo.getString(KEY_ISSUEDON);
                    String offense=jo.getString(KEY_CHARGE);
                    String amnt=jo.getString(KEY_AMOUNT);

                    final HashMap<String,String> offenses=new HashMap<>();
                    offenses.put(KEY_ISSUEDON,"Charged On: "+charged);
                    offenses.put(KEY_CHARGE,"Offense: "+offense);
                    offenses.put(KEY_AMOUNT,"Fine: "+amnt);

                    list.add(offenses);
                }
            }else {
                Toast.makeText(OffenseActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(OffenseActivity.this, "Error: "+e, Toast.LENGTH_LONG).show();
        }
        ListAdapter adapter=new SimpleAdapter(
                OffenseActivity.this,list,R.layout.activity_olist,
                new String[]{KEY_ISSUEDON,KEY_CHARGE,KEY_AMOUNT},
                new int[]{R.id.chargedon,R.id.charge,R.id.fineamnt}
        );

            listView.setAdapter(adapter);
    }
}
