package com.echo.primestudio.echomusicplayer;

/**
 * Created by Rishabh Mishra on 1/7/2016.
 */
public class songData {

    private String SongPath , SongArtist , SongName , SongId , SongAlbumId ;

    public songData(String songAlbumId, String songArtist, String songId, String songName, String songPath) {
        SongAlbumId = songAlbumId;
        SongArtist = songArtist;
        SongId = songId;
        SongName = songName;
        SongPath = songPath;
    }

    public String getSongAlbumId() {
        return SongAlbumId;
    }

    public void setSongAlbumId(String songAlbumId) {
        SongAlbumId = songAlbumId;
    }

    public String getSongArtist() {
        return SongArtist;
    }

    public void setSongArtist(String songArtist) {
        SongArtist = songArtist;
    }

    public String getSongId() {
        return SongId;
    }

    public void setSongId(String songId) {
        SongId = songId;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getSongPath() {
        return SongPath;
    }

    public void setSongPath(String songPath) {
        SongPath = songPath;
    }
}
