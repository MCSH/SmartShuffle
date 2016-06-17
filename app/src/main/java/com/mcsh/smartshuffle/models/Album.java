package com.mcsh.smartshuffle.models;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

/**
 * Created by sajjad on 6/17/16.
 */

@Table
public class Album {
    @PrimaryKey
    public String album;
    @Column(indexed = true)
    public double likeness;
}
