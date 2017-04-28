package org.qtrp.nadir;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class RollActicity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_acticity);

        Intent intent = getIntent();
        Integer roll_id = intent.getIntExtra("roll_id", -1);

        Log.i("Roll id: ", roll_id.toString());
    }

}
