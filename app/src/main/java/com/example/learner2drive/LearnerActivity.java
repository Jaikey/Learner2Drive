package com.example.learner2drive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LearnerActivity extends AppCompatActivity {

    //private TextView name;
    private TextView learner,issuedon,username,guardian,uaddress,udob,bloodgroup,validfrom,validto,vehclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner);

        learner=findViewById(R.id.no);
        issuedon=findViewById(R.id.dateissued);
        username=findViewById(R.id.user);
        guardian=findViewById(R.id.guardian);
        uaddress=findViewById(R.id.address);
        udob=findViewById(R.id.dob);
        bloodgroup=findViewById(R.id.blood);
        validfrom=findViewById(R.id.validfrom);
        validto=findViewById(R.id.validto);
        vehclass=findViewById(R.id.vehclass);

        Intent intent=getIntent();
        String learner_lic=intent.getStringExtra("learner_lic");
        Log.i("intent value",learner_lic);
        learner.setText(learner_lic);
        learner.setVisibility(View.GONE);
        getData();
    }

    private void getData(){

        String value=learner.getText().toString().trim();
        final String url=Config5.DATA_URL+value.trim();
        Log.i("sting url",url);

        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray result=jsonObject.getJSONArray(Config5.JSON_ARRAY);

                    if (result.length()>0){
                        for (int i=0;i<result.length();i++){
                            JSONObject jo=result.getJSONObject(i);
                            String user=jo.getString(Config5.KEY_NAME);
                            String address=jo.getString(Config5.KEY_ADDRESS);
                            String issued=jo.getString(Config5.KEY_DATEISSUED);
                            String dob=jo.getString(Config5.KEY_DOB);
                            String guard=jo.getString(Config5.KEY_GUARDIAN);
                            String vfrom=jo.getString(Config5.KEY_VALFRM);
                            String vto=jo.getString(Config5.KEY_VALTO);
                            String ltype=jo.getString(Config5.KEY_VCLASS);
                            String blood=jo.getString(Config5.KEY_BLOOD);

                            issuedon.setText(issued);
                            username.setText(user);
                            uaddress.setText(address);
                            udob.setText(dob);
                            guardian.setText(guard);
                            validfrom.setText(vfrom);
                            validto.setText(vto);
                            vehclass.setText(ltype);
                            bloodgroup.setText(blood);
                        }
                    }else{
                        Toast.makeText(LearnerActivity.this, "Else error:", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LearnerActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }

                }catch (JSONException e){
                    Toast.makeText(LearnerActivity.this,"json error: "+e,Toast.LENGTH_LONG).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"Error: "+error,Toast.LENGTH_LONG);
                    }
                });
            RequestQueue requestQueue=Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

    }

}

