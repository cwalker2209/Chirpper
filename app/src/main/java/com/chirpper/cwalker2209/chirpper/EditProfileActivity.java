package com.chirpper.cwalker2209.chirpper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.File;

public class EditProfileActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private AppDatabase db;
    Profile profile = null;
    EditText editTextName;
    EditText editTextDescripiton;
    ImageView imageViewAvatar;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = findViewById(R.id.imageViewAvatar);
            String absolutePath = new File (getFilesDir(), picturePath).getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
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
}
