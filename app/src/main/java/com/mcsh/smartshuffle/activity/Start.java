package com.mcsh.smartshuffle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mcsh.smartshuffle.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.scan)
    public void scan(){

    }

    @OnClick(R.id.play)
    public void play(){

    }

    @OnClick(R.id.random_play)
    public void randomPlay(){

    }

    @OnClick(R.id.list)
    public void list(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }


}
