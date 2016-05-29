package com.mcsh.smartshuffle.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;

import com.github.gfx.android.orma.annotation.OnConflict;
import com.mcsh.smartshuffle.models.OrmaDatabase;
import com.mcsh.smartshuffle.models.Song;

import java.util.ArrayList;

import es.dmoral.prefs.Prefs;

/**
 * Created by sajjad on 5/28/16.
 */

public class Initializer extends Service {

    private static final String SHUFFLE = "shuffle";
    static Initializer instance = new Initializer();

    private Context context;

    private OrmaDatabase orma;

    private Song nowplaying;

    private boolean shuffle;

    private ArrayList<Song> songs;


    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public Song getNowplaying() {
        return nowplaying;
    }

    public void onCreate() {
        context = getApplicationContext();

        shuffle = Prefs.with(context).readBoolean(SHUFFLE, false);

        orma = OrmaDatabase.builder(context).build();


        nowplaying = orma.selectFromSong().orderByTitleAsc().getOrNull(0);

    }


    //This only adds new ones
    public void updateSongList() {
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

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
                long id = musicCursor.getLong(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);

                orma.prepareInsertIntoSong(OnConflict.REPLACE, true)
                        .execute(new Song(id, title, artist));


            }
            while (musicCursor.moveToNext());
        }

    }

    public boolean getShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
        Prefs.with(context).writeBoolean(SHUFFLE, shuffle);
    }



    public class Binder extends android.os.Binder{
        public Initializer getService() {
            return Initializer.this;
        }
    }

}
