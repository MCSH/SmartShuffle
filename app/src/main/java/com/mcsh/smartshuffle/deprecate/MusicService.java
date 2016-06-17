package com.mcsh.smartshuffle.deprecate;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.mcsh.smartshuffle.R;
import com.mcsh.smartshuffle.activity.ListActivity;
import com.mcsh.smartshuffle.models.Song;
import com.mcsh.smartshuffle.models.SongManager;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "PLAYER";
    private static final int NOTIFY_ID = 1;
    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private String songTitle = "";
    private boolean shuffle = false;
    private Random rand;

    public void onCreate() {
        //create the service
        super.onCreate();
        //initialize position
        songPosn = 0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();

        rand = new Random();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }


    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() == 0) {
            //mp.reset();
            playNext();
        } else {
            //mp.reset();
            playNext();
        }
    }

    public void playSong() {
        //play a song

        calculate();

        player.reset();

        //get song
        Song playSong = songs.get(songPosn);

        //Set song title
        songTitle = playSong.getTitle();

        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }

    public void playPrev() {
        songPosn--;
        if (songPosn > 0) songPosn = songs.size() - 1;
        playSong();
    }

    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn == songs.size()) songPosn = 0;
        }
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();

        Intent notIntent = new Intent(this, ListActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void calculate() {
        //TODO
        Log.d(TAG, "I'm called!");
        Log.d(TAG, "" + getPosn() + " " + getDur());
        SongManager.getDefault(getApplicationContext()).songSkipped(songs.get(songPosn),
                getPosn() / (float) getDur());
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}