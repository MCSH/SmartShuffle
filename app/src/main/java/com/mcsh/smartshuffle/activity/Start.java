package com.mcsh.smartshuffle.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;
import com.mcsh.smartshuffle.R;
import com.mcsh.smartshuffle.service.Initializer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Start extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {
    private static final int PLAYER_REQ = 1;
    private static final int UPDATE_SONG_LIST = 1;
    private static final int PLAY_ALL = 2;
    @BindView(R.id.bottom_sheet)
    SheetLayout msheet;

    @BindView(R.id.fab)
    FloatingActionButton mfab;

    private Initializer initializer;
    private ServiceConnection conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        msheet.setFab(mfab);
        msheet.setFabAnimationEndListener(this);

    }

    @OnClick(R.id.fab)
    void onFabClick() {
        msheet.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, Player.class);
        startActivityForResult(intent, PLAYER_REQ);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLAYER_REQ){
            msheet.contractFab();
        }
    }

    private void connect(final int task){
        if (initializer != null) return;

        Intent intent = new Intent(this, Initializer.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                initializer = ((Initializer.Binder) service).getService();


                switch (task){
                    case UPDATE_SONG_LIST:
                        updateSongList(findViewById(R.id.update));
                        break;
                    case PLAY_ALL:
                        playAll();
                }


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                initializer = null;
            }
        };
        bindService(intent, conn, BIND_AUTO_CREATE );
        startService(intent);
    }

    @OnClick(R.id.update)
    public void updateSongList(View view){
        //Initializer.getInstance(this).updateSongList();
        //bindService(new Intent(this, Initializer.class));
        if(initializer == null)
        {
            connect(UPDATE_SONG_LIST);
            return;
        }
        

        Snackbar.make(view, "Updated", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.update)
    public void playAll(){
        if(initializer == null) {
            connect(PLAY_ALL);
            return;
        }
        //TODO
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn!=null) unbindService(conn);
    }
}
