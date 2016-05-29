package com.mcsh.smartshuffle.models;


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
    public String artist;

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public Song(){}

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
}
