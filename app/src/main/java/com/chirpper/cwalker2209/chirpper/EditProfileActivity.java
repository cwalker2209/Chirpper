package com.chirpper.cwalker2209.chirpper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.chirpper.cwalker2209.chirpper.database.AppDatabase;
import com.chirpper.cwalker2209.chirpper.database.Profile;

public class EditProfileActivity extends AppCompatActivity {

    private AppDatabase db;
    Profile profile = null;
    EditText editTextName;
    EditText editTextDescripiton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //get database
        db = App.get().getDB();
    }



    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        editTextName = findViewById(R.id.editTextName);
        editTextDescripiton = findViewById(R.id.editTextDescription);

        try{
            profile = db.profileDAO().findByUserId(App.get().getUserId());
        }
        catch (Exception e){
            Log.e("DB", e.getMessage());
        }
        editTextName.setText(profile.name);
        editTextDescripiton.setText(profile.description);
    }

    protected void save(){
        profile.name = editTextName.getText().toString();
        profile.description = editTextDescripiton.getText().toString();

        db.profileDAO().update(profile);
    }
}
