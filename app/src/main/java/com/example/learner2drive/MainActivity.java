package com.example.learner2drive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText learner,uname,pass;
    private Button regbtn;
    private ProgressBar progressBar;
    private TextView lnk_login;

    private static String URL="http://vscan.000webhostapp.com/user_register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setting permission for all
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        learner=findViewById(R.id.learnerno);
        uname=findViewById(R.id.username);
        pass=findViewById(R.id.pass);
        //cpass=findViewById(R.id.confirmpass);
        progressBar=findViewById(R.id.progressBar);
        regbtn=findViewById(R.id.btn_reg);
        lnk_login=findViewById(R.id.lnk_login);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        lnk_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Register(){
        progressBar.setVisibility(View.VISIBLE);
        regbtn.setVisibility(View.GONE);

        final String learner_id=this.learner.getText().toString().trim();
        final String u_name=this.uname.getText().toString().trim();
        final String pswd=this.pass.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        String success=jsonObject.getString("success");

                        if(success.equals("1")){
                            Toast.makeText(MainActivity.this,"Registration Succesfull",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"Invalid Learner license. ",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            regbtn.setVisibility(View.VISIBLE);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"Sorry"+e.toString(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        regbtn.setVisibility(View.VISIBLE);
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Sorry"+error.toString(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        regbtn.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String,String>getParams() throws AuthFailureError{
                Map<String,String>params=new HashMap<>();
                params.put("learner_id",learner_id);
                params.put("u_name",u_name);
                params.put("pswd",pswd);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }

}
