package com.example.appnghenhac;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    TextView txtTitle,txttimesong,txttimetotal;
    ImageButton btnprev,btnplay,btnstop,btnnext;
    SeekBar seekBar;
    ImageView imgHinh;
    ArrayList<Song> arrayListSong;
    int position = 0;

    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        AnhXa();
        AddSong();
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        mediaPlayer = MediaPlayer.create(MainActivity.this, arrayListSong.get(position).getFile());
        txtTitle.setText(arrayListSong.get(position).getTitle());

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    // nếu đang phát ->pause ->đổi hình play
                    mediaPlayer.pause();
                    btnplay.setImageResource(R.drawable.play);
                }else{
                    // đang ngừng ->phát ->đổi hình pause
                    mediaPlayer.start();
                    btnplay.setImageResource(R.drawable.pause);
                }
                SetTimeTotal();
                UpdatetimeSong();
                imgHinh.startAnimation(animation);
            }
        });
        KhoiTaoMediaPlayer();
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position > arrayListSong.size() - 1){
                    position = 0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                btnplay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdatetimeSong();
            }
        });
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position < 0){
                    position = arrayListSong.size() - 1 ;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                btnplay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdatetimeSong();

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnplay.setImageResource(R.drawable.play);
                KhoiTaoMediaPlayer();
            }
        });
    }

    private void AddSong() {
        arrayListSong = new ArrayList<>();
        int tpt = R.raw.thephongtinh;
        arrayListSong.add(new Song("Thế phong tình ", 2131623941 ));
        arrayListSong.add(new Song(tpt + "", R.raw.muonnoivoiem));
        arrayListSong.add(new Song("Đời người anh em ", R.raw.doinguoianhemremix));
        arrayListSong.add(new Song("Người yêu giản đơn - Chi Dân", R.raw.nguoiyeugiandon));
        arrayListSong.add(new Song("Shape Of You - Ed Sean", R.raw.shapeofyou));
        arrayListSong.add(new Song("See You Again", R.raw.seeyouagain));
    }

    private void AnhXa(){
        txtTitle = (TextView) findViewById(R.id.TestviewTitle);
        txttimesong = (TextView) findViewById(R.id.timesong);
        txttimetotal = (TextView) findViewById(R.id.timetotal);
        btnprev = (ImageButton) findViewById(R.id.buttonPrev);
        btnplay = (ImageButton) findViewById(R.id.buttonPlay);
        btnstop = (ImageButton) findViewById(R.id.buttonStop);
        btnnext = (ImageButton) findViewById(R.id.buttonNext);
        seekBar = (SeekBar) findViewById(R.id.SeekBar);
        imgHinh = (ImageView) findViewById(R.id.imageViewDisc);

    }
    private void KhoiTaoMediaPlayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arrayListSong.get(position).getFile());
        txtTitle.setText(arrayListSong.get(position).getTitle());
    }
    private void UpdatetimeSong(){//định dạng thời gian đầu
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
            txttimesong.setText(dinhDangGio.format(mediaPlayer.getCurrentPosition()));
            //update progress seekbar
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            //kiểm tra thời gian bài hát -> nếu kết thúc -> next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position > arrayListSong.size() - 1){
                            position = 0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        KhoiTaoMediaPlayer();
                        mediaPlayer.start();
                        btnplay.setImageResource(R.drawable.pause);
                        SetTimeTotal();
                        UpdatetimeSong();
                    }
                });
            handler.postDelayed(this, 500);
            }
        },100);

    }
    private void SetTimeTotal(){//định dạng thời gian cuối
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        txttimetotal.setText(dinhDangGio.format(mediaPlayer.getDuration()));
        //gán max của seekBar  = mediaPlayer.getDuration
        seekBar.setMax(mediaPlayer.getDuration());
    }

}