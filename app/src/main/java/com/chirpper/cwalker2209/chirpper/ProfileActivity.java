package com.chirpper.cwalker2209.chirpper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chirpper.cwalker2209.chirpper.database.AppDatabase;
import com.chirpper.cwalker2209.chirpper.database.Profile;

public class ProfileActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get database
        db = App.get().getDB();

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        TextView name = findViewById(R.id.textViewName);
        TextView date = findViewById(R.id.textViewDate);
        TextView descripiton = findViewById(R.id.textViewDescription);
        Profile profile = null;

        try{
            profile = db.profileDAO().findByUserId(App.get().getUserId());
        }
        catch (Exception e){
            Log.e("DB", e.getMessage());
        }
        name.setText(profile.name);
        date.setText(profile.created.toString());
        descripiton.setText(profile.description);
    }

}
