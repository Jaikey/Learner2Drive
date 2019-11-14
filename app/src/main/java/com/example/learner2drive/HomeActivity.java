package com.example.learner2drive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private ImageButton licensebtn,offensebtn;
    private TextView learner;
    //private TextView uname,learner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //uname=findViewById(R.id.uname);
        learner=findViewById(R.id.learner_lic);
        licensebtn=findViewById(R.id.btn_license);
        offensebtn=findViewById(R.id.btn_offense);

        /*Intent intent=getIntent();
        String extraName=intent.getStringExtra("uname");
        String extralic=intent.getStringExtra("learner_lic");

        uname.setText(extraName);
        learner.setText(extralic);*/

        Intent intent=getIntent();
        final String extralic=intent.getStringExtra("learner_lic");
        learner.setText(extralic);
        learner.setVisibility(View.GONE);
        final String learner_id=learner.getText().toString().trim();

        licensebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String extraName=getIntent().getStringExtra("uname");

                Intent i=new Intent(getApplicationContext(),LearnerActivity.class);
                //i.putExtra("uname",extraName);
                i.putExtra("learner_lic",learner_id);
                startActivity(i);
            }
        });

        offensebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),OffenseActivity.class);
                intent.putExtra("learner_lic",learner_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
                case R.id.item1:
                    Toast.makeText(HomeActivity.this, "logging out.", Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
