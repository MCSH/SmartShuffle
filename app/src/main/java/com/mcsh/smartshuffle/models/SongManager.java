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

    public Song getOrCreateSong(long id, String title, String artist, String album, String genre) {
        Song song = orma.selectFromSong().titleEq(title).artistEq(artist).albumEq(album).genreEq(genre).getOrNull(0);
        if (song == null) {
            song = new Song(id, title, artist, album, genre);
            song.likeness = 0.5;
            song.calculateLikeness(context);
            orma.insertIntoSong(song);
        }
        song.calculateLikeness(context);
        orma.updateSong().titleEq(title).artistEq(artist).albumEq(album).genreEq(genre).id(id).
                totalLikeness(song.totalLikeness).execute();
        song.id = id;
        return song;
    }

    public Album getOrCreateAlbum(String albumName) {
        Album album = orma.selectFromAlbum().albumEq(albumName).getOrNull(0);
        if (album == null) {
            album = new Album();
            album.likeness = 0.5;
            orma.insertIntoAlbum(album);
        }
        return album;
    }

    public Artist getOrCreateArtist(String artistName) {
        Artist artist = orma.selectFromArtist().artistEq(artistName).getOrNull(0);
        if (artist == null) {
            artist = new Artist();
            artist.likeness = 0.5;
            orma.insertIntoArtist(artist);
        }
        return artist;
    }

    public Genre getOrCreateGenre(String genreName) {
        Genre genre = orma.selectFromGenre().genreEq(genreName).getOrNull(0);
        if (genre == null) {
            genre = new Genre();
            genre.likeness = 0.5;
            orma.insertIntoGenre(genre);
        }
        return genre;
    }

    public void songSkipped(Song song, float skippedAt) {
        double likeness = song.likeness;
        Log.d(TAG, "" + likeness);
        if (skippedAt >= 0.5) {
            likeness += 1;
            if (likeness > 100)
                likeness = 100;

        } else {
            likeness--;
            if (likeness < 0)
                likeness = 0;
        }
        song.likeness = likeness;
        Log.d(TAG, "" + likeness);
        orma.updateSong().titleEq(song.title).artistEq(song.artist).albumEq(song.album).genreEq(song.genre).
                likeness(likeness).execute();
    }
}
