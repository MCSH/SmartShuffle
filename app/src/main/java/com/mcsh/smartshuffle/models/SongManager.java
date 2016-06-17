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
            album.album = albumName;
            album.likeness = 0.5;
            orma.insertIntoAlbum(album);
        }
        return album;
    }

    public Artist getOrCreateArtist(String artistName) {
        Artist artist = orma.selectFromArtist().artistEq(artistName).getOrNull(0);
        if (artist == null) {
            artist = new Artist();
            artist.artist = artistName;
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
            genre.genre = genreName;
            orma.insertIntoGenre(genre);
        }
        return genre;
    }

    private double increase(double a) {
        a += 1.0 / 10 * (1 - a);
        if (a > 1) a = 1;
        return a;
    }

    private double decrease(double a) {
        a -= 1.0 / 10 * a;
        if (a < 0) a = 0;
        return a;
    }

    public void songSkipped(Song song, float skippedAt) {
        double likeness = song.likeness;
        double albumLikeness = getOrCreateAlbum(song.album).likeness;
        double artistLikeness = getOrCreateArtist(song.artist).likeness;
        double genreLikeness = getOrCreateGenre(song.genre).likeness;
        //TODO bias
        Log.d(TAG, "" + likeness);
        if (skippedAt >= 0.5) {
            likeness = increase(likeness);
            albumLikeness = increase(albumLikeness);
            artistLikeness = increase(artistLikeness);
            genreLikeness = increase(genreLikeness);
        } else {
            likeness = decrease(likeness);
            albumLikeness = decrease(albumLikeness);
            artistLikeness = decrease(artistLikeness);
            genreLikeness = decrease(genreLikeness);
        }
        orma.updateAlbum().albumEq(song.album).likeness(albumLikeness).execute();
        orma.updateArtist().artistEq(song.artist).likeness(artistLikeness).execute();
        orma.updateGenre().genreEq(song.genre).likeness(genreLikeness).execute();

        song.likeness = likeness;
        song.totalLikeness = song.calculateLikeness(context);
        orma.updateSong().titleEq(song.title).artistEq(song.artist).albumEq(song.album).genreEq(song.genre).
                likeness(likeness).totalLikeness(song.totalLikeness).execute();
    }
}
