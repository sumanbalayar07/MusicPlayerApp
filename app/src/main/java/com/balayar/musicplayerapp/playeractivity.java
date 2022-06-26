package com.balayar.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class playeractivity extends AppCompatActivity {

    Button btnplay, btnnext,btnprev;
    TextView txtname,txtstart,txtstop;
    SeekBar seek;
    BarVisualizer barVisualizer;
    ImageView imageView;
    static MediaPlayer mediaPlayer;
    int pos;
    String sname;
    public static final String EXTRA_NAME="song_name";
    ArrayList mysongs;
    Thread updateseek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeractivity);
        btnplay = findViewById(R.id.btnplay);
        btnnext = findViewById(R.id.skipf);
        btnprev = findViewById(R.id.skipb);
        txtname = findViewById(R.id.txt);
        txtstart = findViewById(R.id.txtstart);
        txtstop = findViewById(R.id.txtstart2);
        seek = findViewById(R.id.seek);
        barVisualizer = findViewById(R.id.blast);
        imageView = findViewById(R.id.imag);
        if ( mediaPlayer !=null )
        {
            mediaPlayer.stop ( ) ;
            mediaPlayer.release ( ) ;
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mysongs= (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songsname") ;
        pos=bundle.getInt("pos",0);
        txtname.setSelected ( true ) ;
        Uri uri = Uri.parse(mysongs.get(pos).toString());
        sname=mysongs.get(pos).toString();
        txtname.setText(sname);

        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        updateseek=new Thread()
        {
            public void run()
            {
                int totalduration=mediaPlayer.getDuration();
                int curr=0;
                while(curr<totalduration)
                {
                    try{
                        sleep(500);
                        curr=mediaPlayer.getCurrentPosition();
                        seek.setProgress(curr);
                    }
                    catch (IllegalStateException | InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        seek.setMax(mediaPlayer.getDuration());
        updateseek.start();
        seek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seek.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        String endtime=createTime(mediaPlayer.getDuration());
        txtstop.setText(endtime);

        final Handler handler=new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime=createTime(mediaPlayer.getCurrentPosition());
                txtstart.setText(currentTime);
                handler.postDelayed(this,delay);

            }
        },delay);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    btnplay.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    btnplay.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();;
                }
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                pos=((pos+1)%mysongs.size());
                Uri u=Uri.parse(mysongs.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                sname=String.valueOf(mysongs.get(pos));
                txtname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);
            }
        });
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                pos=((pos-1)<0)?(mysongs.size()-1):(pos-1);
                Uri u=Uri.parse(mysongs.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                sname=String.valueOf(mysongs.get(pos));
                txtname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnext.performClick(); }
        });
    }
    public void startAnimation(View view)
    {
        ObjectAnimator animator= ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animator);
        animator.start();

    }
    public String createTime(int duration)
    {
        String time="";
        int min=duration/1000/60;
        int sec=duration/1000%60;
        time+=min+":";
        if(sec<10)
        {
            time+="0";
        }
        time+=sec;

        return time;
    }
}