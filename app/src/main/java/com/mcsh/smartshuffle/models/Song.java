package com.mcsh.smartshuffle.models;


import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class Song {
    @PrimaryKey
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
    @Column
    @Nullable
    public int likeness;

    public Song(){}

    public Song(long id, String title, String artist, String album, String genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public int getLikeness() {
        return likeness;
    }

    public void setLikeness(int likeness) {
        likeness = likeness;
    }

    public void setDefaultLikeness() {
        likeness = 50;
    }
}
