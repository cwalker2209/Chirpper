package com.chirpper.cwalker2209.chirpper;

/**
 * Created by C on 2018-01-01.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chirpper.cwalker2209.chirpper.database.Post;
import com.chirpper.cwalker2209.chirpper.database.Profile;

public class ChirpAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private final List<Post> posts;
    private final List<Profile> profiles;

    public ChirpAdapter(Context context, List<Post> posts, List<Profile> profiles) {
        this.context = context;
        this.posts = posts;
        this.profiles = profiles;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null){
            view = inflater.inflate(R.layout.list, parent, false);
            convertView = view;
        }
        else {
            view = convertView;
        }

        TextView name = view.findViewById(R.id.itemName);
        TextView message = view.findViewById(R.id.itemText);
        TextView date = view.findViewById(R.id.itemDate);
        ImageView image = view.findViewById(R.id.itemImage);

        // Setting all values in listview
        Post post = posts.get(position);
        Profile profile = new Profile("empty", "empty", null, 0);
        for (Profile p: profiles) {
            if (p.userId == post.userId){
                profile = p;
                break;
            }
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss");
        String formatedDate = fmt.format(post.created);

        name.setText(profile.name);
        message.setText(post.text);
        date.setText(formatedDate);
        image.setImageBitmap(profile.image);

        return view;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        if (posts !=null){
            return posts.size();
        }
        return 0;
    }
}
