package com.mcsh.smartshuffle.models;

import android.content.Context;
import android.util.Log;

/**
 * Created by sajjad on 6/13/16.
 */

public class SongManager {

    private static String TAG = "PLAYER";
    private static SongManager instance = new SongManager();
    private Context context;
    private OrmaDatabase orma;

    private SongManager() {
    }

    public static SongManager getDefault(Context context) {
        if (instance.context == null) {
            instance.context = context;

            instance.orma = OrmaDatabase.builder(context).build();
            OrmaDatabase orma = instance.orma;
            orma.migrate();
        }
        return instance;
    }

    public static OrmaDatabase getOrma(Context context) {
        if (instance.context == null) {
            instance.context = context;

            instance.orma = OrmaDatabase.builder(context).build();
            OrmaDatabase orma = instance.orma;
            orma.migrate();
        }
        return instance.orma;
    }

    public Song getOrCreate(long id, String title, String artist, String album, String genre) {
        Song song = orma.selectFromSong().titleEq(title).artistEq(artist).albumEq(album).genreEq(genre).getOrNull(0);
        if (song == null) {
            song = new Song(id, title, artist, album, genre);
            song.setDefaultLikeness();
            orma.insertIntoSong(song);
        }
        orma.updateSong().titleEq(title).artistEq(artist).albumEq(album).genreEq(genre).id(id).execute();
        song.id = id;
        return song;
    }

    public void songSkipped(Song song, float skippedAt){
        int likeness = song.getLikeness();
        Log.d(TAG, ""+ likeness);
        if(skippedAt >= 0.5){
            likeness += 1;
            if(likeness >100)
                likeness = 100;

        } else {
            likeness --;
            if(likeness < 0)
                likeness = 0;
        }
        song.setLikeness(likeness);
        Log.d(TAG, ""+ likeness);
        orma.updateSong().titleEq(song.getTitle()).artistEq(song.getArtist()).albumEq(song.getAlbum()).genreEq(song.getGenre()).
                likeness(likeness).execute();
    }
}
