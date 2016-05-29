package com.mcsh.smartshuffle.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mcsh.smartshuffle.R;
import com.mcsh.smartshuffle.models.Song;
import com.mcsh.smartshuffle.service.Initializer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Player extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private static final String TAG = "PLAYER";
    Initializer initializer;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.artist)
    TextView artist;

    @BindView(R.id.album)
    TextView album;

    @BindView(R.id.shuffle)
    ToggleButton shuffle;

    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ButterKnife.bind(this);

        connect();

    }

    private void connect() {
        if (initializer != null) return;

        Intent intent = new Intent(this, Initializer.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "connected");
                initializer = ((Initializer.Binder) service).getService();
                updateUI();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                initializer = null;
            }
        };
        bindService(intent, conn, BIND_AUTO_CREATE);
        startService(intent);
    }

    public void updateUI() {
        if (initializer == null) {
            connect();
            return;
        }
        Song current = initializer.getNowplaying();

        title.setText(current.getTitle());
        artist.setText(current.getArtist());

        shuffle.setChecked(initializer.getShuffle());
    }

    @OnClick(R.id.shuffle)
    public void shuffle(ToggleButton btn) {
        initializer.setShuffle(btn.isChecked());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) unbindService(conn);
    }

    @Override
    public void start() {
        //TODO
    }

    @Override
    public void pause() {
        //TODO
    }

    @Override
    public int getDuration() {
        return 0;//TODO
    }

    @Override
    public int getCurrentPosition() {
        return 0;//TODO
    }

    @Override
    public void seekTo(int pos) {
        //TODO
    }

    @Override
    public boolean isPlaying() {
        return false;//TODO
    }

    @Override
    public int getBufferPercentage() {
        return 0;//TODO
    }

    @Override
    public boolean canPause() {
        return false;//TODO
    }

    @Override
    public boolean canSeekBackward() {
        return false;//TODO
    }

    @Override
    public boolean canSeekForward() {
        return false;//TODO
    }

    @Override
    public int getAudioSessionId() {
        return 0;//TODO
    }
}
