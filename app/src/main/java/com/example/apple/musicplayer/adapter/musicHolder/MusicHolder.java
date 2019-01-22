package com.example.apple.musicplayer.adapter.musicHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.musicplayer.R;

public class MusicHolder extends RecyclerView.ViewHolder {

    private TextView title, artist, album;
    private ImageView picture;
    private OnItemClickListener OnItemClickListener;

    private View.OnClickListener mOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(OnItemClickListener != null) {
                OnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    };

    public TextView getTitle() { return title; }

    public TextView getArtist() { return artist; }

    public TextView getAlbum() { return album; }

    public ImageView getPicture() { return picture; }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.OnItemClickListener = onItemClickListener;
    }

    public MusicHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.songTitle);
        artist = itemView.findViewById(R.id.songArtist);
        album = itemView.findViewById(R.id.songAlbum);
        picture = itemView.findViewById(R.id.songCover);
        itemView.setOnClickListener(mOnclick);
    }

    public interface OnItemClickListener{
        void onItemClick(int adapterPosition);
    }
}
