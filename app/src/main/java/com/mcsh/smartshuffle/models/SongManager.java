package com.mcsh.smartshuffle.models;

import android.content.Context;

/**
 * Created by sajjad on 6/13/16.
 */

public class SongManager {

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
        }
        return instance;
    }

    public static OrmaDatabase getOrma() {
        return instance.orma;
    }

    public Song getOrCreate(long id, String title, String artist, String album, String genre) {
        Song song = orma.selectFromSong().titleEq(title).artistEq(artist).albumEq(album).genreEq(genre).getOrNull(0);
        if (song == null) {
            song = new Song(id, title, artist, album, genre);
            song.setDefaultLikeness();
            orma.insertIntoSong(song);
        }
        return song;
    }
}
