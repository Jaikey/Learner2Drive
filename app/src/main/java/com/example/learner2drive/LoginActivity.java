package com.example.learner2drive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username,password;
    private Button btn_login;
    private TextView lnk_reg;
    private ProgressBar progressBar;
    private static String URL_LOGIN="http://vscan.000webhostapp.com/user_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar=findViewById(R.id.progressbar);
        username=findViewById(R.id.uname);
        password=findViewById(R.id.pass);
        btn_login=findViewById(R.id.btn_login);
        lnk_reg=findViewById(R.id.lnk_reg);

        lnk_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_name=username.getText().toString().trim();
                String pswd=password.getText().toString().trim();

                if (!u_name.isEmpty() || !pswd.isEmpty()) {
                    Login();
                }else{
                    username.setError("Enter username");
                    password.setError("Enter password");
                }

                lnk_reg.setVisibility(View.GONE);

            }
        });
    }

    private void Login(){

        progressBar.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        final String user=this.username.getText().toString().trim();
        final String pass=this.password.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject=new JSONObject(response);
                            String success=jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("login");
                            //String learner ="KL43/0018451/2019";
                            if (success.equals("1")){

                               for (int i=0;i<jsonArray.length();i++) {
                                   JSONObject object = jsonArray.getJSONObject(i);

                                   String name = object.getString("uname").trim();
                                   String learner = object.getString("learner_lic").trim();

                                   Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
                                   //intent1.putExtra("uname",name);
                                   intent1.putExtra("learner_lic", learner);
                                   startActivity(intent1);
                                   Log.i("intent",learner);
                                   finish();
                                   //Toast.makeText(LoginActivity.this,"Welcome "+name+"\nLearner license:"+learner,Toast.LENGTH_LONG).show();
                                   progressBar.setVisibility(View.GONE);

                               }
                            }else{
                                progressBar.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "Invalid username or password ", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this,"Error "+e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Unsuccessfull.."+error.toString(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                    }

                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{

                Map<String,String> params=new HashMap<>();
                params.put("user",user);
                params.put("pass",pass);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
