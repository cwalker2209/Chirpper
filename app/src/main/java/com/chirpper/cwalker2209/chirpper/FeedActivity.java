package com.chirpper.cwalker2209.chirpper;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.chirpper.cwalker2209.chirpper.database.AppDatabase;
import com.chirpper.cwalker2209.chirpper.database.Post;
import com.chirpper.cwalker2209.chirpper.database.Profile;

import java.util.List;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //database reference
    AppDatabase db;

    private int rowId;
    private int editTextId;
    private Bitmap userPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get database
        db = App.get().getDB();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final FloatingActionButton fabNew = (FloatingActionButton) findViewById(R.id.fabNew);
        final FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        final Context mContext = this;

        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewMessage(userPicture);
                fabNew.setVisibility(View.INVISIBLE);
                fabSave.setVisibility(View.VISIBLE);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new savePostTask(mContext).execute();
                fabSave.setVisibility(View.INVISIBLE);
                fabNew.setVisibility(View.VISIBLE);
            }
        });

        new getPostsTask().execute();
        new getUserPicture().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feed) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addMessage(String message, Bitmap picture) {
        TableLayout table = findViewById(R.id.feedTable);
        TableRow row = createChirp();
        ImageView image = new ImageView(this, null, R.style.ChirpImage);
        TextView text = new TextView(this, null , R.style.Chirp);

        image.setId(View.generateViewId());
        //TODO: Get picture from profile
        if (picture == null){
            image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            image.setImageBitmap(Bitmap.createScaledBitmap(picture, 72, 72, true));
        }
        image.setMaxWidth(72);
        image.setMaxHeight(72);

        text.setId(View.generateViewId());
        text.setText(message);

        row.setId(View.generateViewId());
        row.addView(image);
        row.addView(text);

        table.addView(row, 0);
        table.invalidate();
    }

    private void cleanPost(){
        TableRow row = findViewById(rowId);
        EditText editText = findViewById(editTextId);
        TextView textView = new TextView(this, null ,R.style.Chirp);

        textView.setId(View.generateViewId());
        textView.setText(editText.getText());

        row.removeView(editText);
        row.addView(textView);

        row.invalidate();
    }



    private void addNewMessage(Bitmap picture) {
        TableLayout table = findViewById(R.id.feedTable);
        TableRow row = createChirp();
        ImageView image = new ImageView(this, null, R.style.ChirpImage);
        EditText text = new EditText(this);

        image.setId(View.generateViewId());
        //TODO: Get picture from profile
        if (picture == null){
            image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            image.setImageBitmap(Bitmap.createScaledBitmap(picture, 72, 72, true));
        }
        image.setMaxWidth(72);
        image.setMaxHeight(72);


        editTextId = View.generateViewId();
        text.setId(editTextId);
        text.requestFocus();

        rowId = View.generateViewId();
        row.setId(rowId);
        row.addView(image);
        row.addView(text);

        table.addView(row, 0);
        table.invalidate();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
    }

    private TableRow createChirp(){
        TableRow row = new TableRow(this);

        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        int leftMargin=10;
        int topMargin=2;
        int rightMargin=10;
        int bottomMargin=2;

        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        row.setLayoutParams(tableRowParams);

        row.setBackgroundResource(R.drawable.chirp);
        row.setPadding(10,10,10,10);

        return row;
    }

    protected class getPostsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                TableLayout table = findViewById(R.id.feedTable);
                table.removeAllViewsInLayout();

                List<Post> posts = db.postDAO().getAll();
                //TODO: Fix with a proper db join
                for (Post post : posts) {
                    Profile profile = db.profileDAO().findByUserId(post.userId);
                    addMessage(post.text, profile.image);
                }
                return true;
            } catch (Exception e) {
                Log.e("DB", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Context context = getApplicationContext();
                CharSequence text = "Chirps sucessfully loaded";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Chirps failed to load";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    protected class savePostTask extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;

        public savePostTask (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            TableRow row = findViewById(rowId);
            EditText editText = findViewById(editTextId);
            TextView textView = new TextView(mContext);
            Post post = new Post(editText.getText().toString(), App.get().getUserId());

            try {
                db.postDAO().insert(post);
            }
            catch (Exception e){
                Log.e( "DBPOST", e.getMessage() );
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Context context = getApplicationContext();
                CharSequence text = "Chirp sucessfully saved";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                cleanPost();
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Chirp failed to load";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    protected class getUserPicture extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Profile profile = db.profileDAO().findByUserId(App.get().getUserId());
                userPicture = profile.image;
            }
            catch (Exception e){
                Log.e( "DBPOROFILE", e.getMessage() );
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
            } else {

            }
        }
    }
}
