package com.example.apple.musicplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.apple.musicplayer.R;
import com.example.apple.musicplayer.adapter.musicHolder.MusicHolder;
import com.example.apple.musicplayer.song.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicHolder> {

    private List<Song> musicList = new ArrayList<>();
    private OnItemSelectedListener onItemSelect;

    private MusicHolder.OnItemClickListener onItemClickListener = new MusicHolder.OnItemClickListener() {
        @Override
        public void onItemClick(int adapterPosition) {
            if(onItemSelect != null){
                onItemSelect.onItemSelected(musicList.get(adapterPosition));
            }
        }
    };

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelect) {
        this.onItemSelect = onItemSelect;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MusicHolder musicHolder = new MusicHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.albumslayout, viewGroup, false));
        musicHolder.setOnItemClickListener(onItemClickListener);
        return musicHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder musicHolder, int i) {
        Song song = musicList.get(i);
        musicHolder.getTitle().setText(song.getTitle());
        musicHolder.getArtist().setText(song.getArtist());
        musicHolder.getAlbum().setText(song.getAlbum());
//        musicHolder.getPicture().setImageBitmap(song.getPic());
    }

    @Override
    public int getItemCount() { return musicList.size(); }

    public void setMusicList(List<Song> musicList) { this.musicList = musicList; }

    public List<Song> getMusicList() { return musicList; }

    public interface OnItemSelectedListener{
        void onItemSelected(Song song);
    }
}
