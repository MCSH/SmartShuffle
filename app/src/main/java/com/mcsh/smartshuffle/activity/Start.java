package com.mcsh.smartshuffle.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mcsh.smartshuffle.R;
import com.mcsh.smartshuffle.deprecate.Song;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Start extends AppCompatActivity {

    private static final String TAG = "PLAYER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.scan)
    public void scan() {

    }

    @OnClick(R.id.play)
    public void play() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        ArrayList<Song> songList = new ArrayList();
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

        Log.d(TAG, "Sending events");
        EventBus.getDefault().postSticky(songList);

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);


    }

    @OnClick(R.id.random_play)
    public void randomPlay() {

    }

    @OnClick(R.id.list)
    public void list() {

    }


}
