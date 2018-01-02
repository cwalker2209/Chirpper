package com.chirpper.cwalker2209.chirpper;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.chirpper.cwalker2209.chirpper.database.AppDatabase;
import com.chirpper.cwalker2209.chirpper.database.Post;
import com.chirpper.cwalker2209.chirpper.database.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //database reference
    AppDatabase db;

    private ListView list;
    private BaseAdapter adapter;
    private int rowId;
    private int editTextId;
    private Profile userProfile;
    private List<Post> posts;
    private List<Profile> profiles;
    private Context context;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        //get database
        db = App.get().getDB();

        //get listview
        list = findViewById(R.id.list);

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
                newChirp();
                //fabNew.setVisibility(View.INVISIBLE);
                //fabSave.setVisibility(View.VISIBLE);
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

        //Remove original toolbar tittle
        getSupportActionBar().setTitle(null);
    }

    @Override
    protected void onStart () {
        super.onStart();
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

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feed) {

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void newChirp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Chirp");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                message = input.getText().toString();
                new savePostTask(context).execute();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /*private void addMessage(Post post, Profile profile, boolean isNew) {
        ImageView image = new ImageView(this, null, R.style.ChirpImage);

        image.setId(View.generateViewId());
        //TODO: Get picture from profile
        if (profile.image == null){
            image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            image.setImageBitmap(Bitmap.createScaledBitmap(profile.image, 72, 72, true));
        }
        image.setMaxWidth(72);
        image.setMaxHeight(72);



        rowId = View.generateViewId();

        if (isNew){
            EditText editText = new EditText(this);

            editTextId = View.generateViewId();
            editText.setId(editTextId);
            editText.requestFocus();
        }
        else{
        }
        if(isNew){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
    }*/

    protected class getPostsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                posts = db.postDAO().getAll();
                profiles = db.profileDAO().getAll();

                return true;
            } catch (Exception e) {
                Log.e("DB", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                adapter = new ChirpAdapter(context, posts, profiles);
                list.setAdapter(adapter);

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
        private Post post;

        public savePostTask (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            post = new Post(message, App.get().getUserId());

            try {
                db.postDAO().insert(post);
                posts.add(0, post);
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
                adapter.notifyDataSetChanged();

                Context context = getApplicationContext();
                CharSequence text = "Chirp sucessfully saved";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            } else {
                Context context = getApplicationContext();
                CharSequence text = "Chirp failed to save";
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
                userProfile = profile;
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
