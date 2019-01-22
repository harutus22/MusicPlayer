package com.example.apple.musicplayer.song;


public class Song {

    private String title;
    private String artist;
    private String album;
    private String pic;
    private int id;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Song(){
    }

    public Song(String title, String artist, String album, int id, String pic, String location){
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.id = id;
        this.pic = pic;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getPic() {
        return pic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
