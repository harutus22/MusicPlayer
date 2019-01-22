package com.example.apple.musicplayer.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.example.apple.musicplayer.song.Song;

import java.util.List;

public class PlayMusicService  extends IntentService{

    public static final int PLAY_MUSIC = 8;
    public static final int PAUSE_MUSIC = 7;
    public static final int SET_SONG_LIST = 6;
    public static final int NEXT_SONG = 9;
    public static final int PREVIOUS_SONG = 10;

    private MediaPlayer mediaPlayer;
    private Messenger messenger = new Messenger(new PlayerHandler());
    private List<Song> musicList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public PlayMusicService(){
        super(PlayMusicService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    class PlayerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_MUSIC:
                    play(msg.arg1);
                    break;
                case PAUSE_MUSIC:
                    pause();
                    break;
                case SET_SONG_LIST:
                    setMusicList((List<Song>) msg.obj);
                    break;
                case NEXT_SONG:
                    nextSong(msg.arg1);
                    break;
                case PREVIOUS_SONG:
                    previousSong(msg.arg1);
                    break;
                default:
            }
        }
    }

    private void previousSong(int location) {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
        mediaPlayer.start();
    }

    private void nextSong(int location) {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
        mediaPlayer.start();
    }

    void play(int location){
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
            mediaPlayer.start();
        }
        else {
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
            mediaPlayer.start();
        }
    }

    void pause(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    public void setMusicList(List<Song> musicList) {
        this.musicList = musicList;
    }
}
