package com.example.apple.musicplayer.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.apple.musicplayer.R;
import com.example.apple.musicplayer.adapter.MusicAdapter;
import com.example.apple.musicplayer.service.PlayMusicService;
import com.example.apple.musicplayer.song.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 98;
    private MusicAdapter musicAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Song> songList;
    public static boolean isPlaying = false;
    private Messenger mes;
    private boolean isBound = false;
    private FloatingActionButton play, next, previous;
    private int location;
    private SeekBar seekBar;
    private MusicAdapter.OnItemSelectedListener onItemSelected = new MusicAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(Song song) {
            location = song.getId();
            if(!isPlaying){
                sendMessage(PlayMusicService.CHOOSE_SONG, location);
            }
            else{
                sendMessage(PlayMusicService.PLAY_MUSIC, location);
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mes = new Messenger(service);
            isBound = true;
            setSongList();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mes = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicAdapter = new MusicAdapter();
        musicAdapter.setOnItemSelectedListener(onItemSelected);
        songList = new ArrayList<>();
        recyclerView = findViewById(R.id.songsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(musicAdapter);
        recyclerView.setEnabled(false);
        Intent intent = new Intent(MainActivity.this, PlayMusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            return;
        }
        getMusicList();
        play = findViewById(R.id.btnPlay);
        next = findViewById(R.id.btnNext);
        previous = findViewById(R.id.btnPrevious);
        seekBar = findViewById(R.id.playBack);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSong();
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
                
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMusicList();
            } else {
                return;
            }
        }
    }

    public void getMusicList(){
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getContentResolver();
            Cursor musicCursor = contentResolver.query(uri, null, null, null, null);
            if (musicCursor != null && musicCursor.moveToNext()) {
                int i = 0;
                int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int coverColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int locationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                do {
                    String title = musicCursor.getString(titleColumn);
                    String artist = musicCursor.getString(artistColumn);
                    String album = musicCursor.getString(albumColumn);
                    int id = i;
                    String cover = musicCursor.getString(coverColumn);
                    String location = musicCursor.getString((locationColumn));
                    songList.add(new Song(title, artist, album, id, cover, location));
                    ++i;
                } while (musicCursor.moveToNext());
                musicCursor.close();
            }
            musicAdapter.setMusicList(songList);
    }

    private void playMusic(){
        if(isBound) {
            if (!isPlaying) {
                play.setImageDrawable(getDrawable(R.mipmap.ic_pause_foreground));
                sendMessage(PlayMusicService.PLAY_MUSIC, location);
                    isPlaying = true;
            }else{
                play.setImageDrawable(getDrawable(R.mipmap.ic_play_foreground));
                sendMessage(PlayMusicService.PAUSE_MUSIC, location);
                isPlaying = false;
//                    unbindService(serviceConnection);
            }
        }
    }

    private void previousSong() {
        --location;
        if(location < 0) {
            location = songList.size() - 1;
        }
        sendMessage(PlayMusicService.PREVIOUS_SONG, location);
    }

    private void nextSong() {
        ++location;
        if(location >= songList.size()) {
            location = 0;
        }
        sendMessage(PlayMusicService.NEXT_SONG, location);
    }

    public void setSongList(){
        Message message = new Message();
        message.what = PlayMusicService.SET_SONG_LIST;
        message.obj = songList;
        try {
            mes.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int order, int location) {
        Message message = new Message();
        message.what = order;
        message.arg1 = location;
        try {
            mes.send(message);
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
