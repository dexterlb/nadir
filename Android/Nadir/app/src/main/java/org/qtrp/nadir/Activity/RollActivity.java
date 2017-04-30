package org.qtrp.nadir.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.qtrp.nadir.R;

public class RollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_acticity);

        Intent intent = getIntent();
        Long roll_id = intent.getLongExtra("roll_id", -1);

        Log.i("Roll id: ", roll_id.toString());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
