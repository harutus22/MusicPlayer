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
import android.widget.Toast;

import com.example.apple.musicplayer.song.Song;

import java.util.List;


public class PlayMusicService  extends IntentService {

    public static final int PLAY_MUSIC = 8;
    public static final int PAUSE_MUSIC = 7;
    public static final int SET_SONG_LIST = 6;
    public static final int NEXT_SONG = 9;
    public static final int PREVIOUS_SONG = 10;
    public static final int CHOOSE_SONG = 11;

    private MediaPlayer mediaPlayer;
    private int playbackLength;
    public static boolean isPlaying = false;
    public static boolean isPaused = false;
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

    public PlayMusicService() {
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
                case CHOOSE_SONG:
                    chooseSong(msg.arg1);
                    break;
                default:
            }
        }
    }

    private void chooseSong(int location) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
    }

    private void previousSong(int location) {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
        if (!isPaused) {
            mediaPlayer.start();
        }
    }

    private void nextSong(int location) {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
        if (!isPaused) {
            mediaPlayer.start();
        }
    }

    void play(int location) {
        if (mediaPlayer == null) {
            Toast.makeText(getBaseContext(), "Choose a song to play", Toast.LENGTH_LONG).show();
            //TODO add handler to  mainActivity to not change the icon, while song is not selected
        } else if (isPaused && !isPlaying) {
            if (mediaPlayer.getAudioSessionId() == location) {
                mediaPlayer.seekTo(playbackLength);
                mediaPlayer.start();
            }
            else{
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
                mediaPlayer.start();
            }
            isPlaying = true;
            isPaused = false;
        } else if (!isPaused && !isPlaying) {
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
            mediaPlayer.start();
            isPlaying = true;
        }
        else if (!isPaused){
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(musicList.get(location).getLocation()));
            mediaPlayer.start();
        }
    }


    void pause(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            playbackLength = mediaPlayer.getCurrentPosition();
            isPaused = true;
            isPlaying = false;
        }
    }

    public void setMusicList(List<Song> musicList) {
        this.musicList = musicList;
    }
}
