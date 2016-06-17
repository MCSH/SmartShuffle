package com.mcsh.smartshuffle.models;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class Song {
    @Column(indexed = true)
    public long id;
    @Column(indexed = true)
    public String title;
    @Column(indexed = true)
    @Nullable
    public String artist;
    @Column(indexed = true)
    @Nullable
    public String album;
    @Column(indexed = true)
    @Nullable
    public String genre;
    @Column(indexed = true)
    public double likeness;
    @Column(indexed = true)
    public double totalLikeness;

    public Song() {
    }

    public Song(long id, String title, String artist, String album, String genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    public double calculateLikeness(Context context) {

        totalLikeness = 1.0 / 4 * (likeness +
                SongManager.getDefault(context).getOrCreateAlbum(album).likeness +
                SongManager.getDefault(context).getOrCreateArtist(artist).likeness +
                SongManager.getDefault(context).getOrCreateGenre(genre).likeness);

        Log.d("PLAYER", "" + totalLikeness + " " + likeness + " " +
                SongManager.getDefault(context).getOrCreateAlbum(album).likeness + " " +
                SongManager.getDefault(context).getOrCreateArtist(artist).likeness + " " +
                SongManager.getDefault(context).getOrCreateGenre(genre).likeness);
        return totalLikeness;
    }
}
