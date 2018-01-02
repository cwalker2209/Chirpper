package com.chirpper.cwalker2209.chirpper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chirpper.cwalker2209.chirpper.database.AppDatabase;
import com.chirpper.cwalker2209.chirpper.database.Profile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class EditProfileActivity extends AppCompatActivity {

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int RESULT_LOAD_IMG = 1;
    private String selectedImagePath;

    private AppDatabase db;
    Profile profile = null;
    EditText editTextName;
    EditText editTextDescripiton;
    ImageView imageViewAvatar;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //get database
        db = App.get().getDB();

        new getProfileTask().execute();

        Button editProfileButton = findViewById(R.id.buttonSaveProfile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new setProfileTask().execute();
            }
        });

        Button buttonChangeAvatar = findViewById(R.id.buttonChangeAvatar);
        buttonChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // select a file
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        Button buttonDownload = findViewById(R.id.buttonDownload);
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new DownloadImageTask((ImageView) findViewById(R.id.imageViewAvatar))
                        .execute("https://raw.githubusercontent.com/NicolasBonet/Caleto/master/example/example_merged.bmp");
            }
        });

        //Remove original toolbar tittle
        //getSupportActionBar().setTitle(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageViewAvatar = findViewById(R.id.imageViewAvatar);
                imageViewAvatar.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    protected class getProfileTask extends AsyncTask<Void, Void, Boolean> {

        protected Profile profile;

        @Override
        protected Boolean doInBackground(Void... params){

            try{
                long id = App.get().getUserId();
                profile = db.profileDAO().findByUserId(id);
                return true;
            }
            catch (Exception e){
                Log.e("DB", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                editTextName = findViewById(R.id.editTextName);
                editTextDescripiton = findViewById(R.id.editTextDescription);
                imageViewAvatar = findViewById(R.id.imageViewAvatar);

                editTextName.setText(profile.name);
                editTextDescripiton.setText(profile.description);
                imageViewAvatar.setImageBitmap(profile.image);
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Profile failed to load";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    protected class setProfileTask extends AsyncTask<Void, Void, Boolean> {

        protected Profile profile;

        @Override
        protected Boolean doInBackground(Void... params){

            editTextName = findViewById(R.id.editTextName);
            editTextDescripiton = findViewById(R.id.editTextDescription);
            imageViewAvatar = findViewById(R.id.imageViewAvatar);
            BitmapDrawable drawable = (BitmapDrawable) imageViewAvatar.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            try{
                long id = App.get().getUserId();
                profile = db.profileDAO().findByUserId(id);
                profile.name = editTextName.getText().toString();
                profile.description = editTextDescripiton.getText().toString();
                profile.image = bitmap;
                db.profileDAO().update(profile);
                return true;
            }
            catch (Exception e){
                Log.e("DBEDITPRO", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Context context = getApplicationContext();
                CharSequence text = "Profile sucessfully saved";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                EditProfileActivity.super.finish();
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Profile failed to save";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

                Context context = getApplicationContext();
                CharSequence text = "Failed to get image";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            Context context = getApplicationContext();
            CharSequence text = "Successfully changed image";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}


