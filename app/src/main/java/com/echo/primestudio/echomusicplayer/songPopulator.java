package com.echo.primestudio.echomusicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Rishabh Mishra on 12/22/2015.
 */
public class songPopulator {


    public static Cursor getTrackCursor(Context context)
    {
        Cursor allSongsCursor ;

        final String track_id = MediaStore.Audio.Media._ID;
        final String track_no =MediaStore.Audio.Media.TRACK;
        final String track_name =MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String duration = MediaStore.Audio.Media.DURATION;
        final String album = MediaStore.Audio.Media.ALBUM;
        final String album_id = MediaStore.Audio.Media.ALBUM_ID;
        final String composer = MediaStore.Audio.Media.COMPOSER;
        final String year = MediaStore.Audio.Media.YEAR;
        final String songPath = MediaStore.Audio.Media.DATA;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={track_no, track_id , artist, track_name,songPath , album_id , duration, album, year, composer};
        allSongsCursor = cr.query(uri,columns,null,null,track_name);
        allSongsCursor.moveToFirst();
        return allSongsCursor;
    }

    public static Cursor getAlbumCursor(Context context)
    {
        Cursor albumCursor ;

        final String _id = MediaStore.Audio.Albums._ID;
        final String album = MediaStore.Audio.Albums.ALBUM;
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={_id, album};
        albumCursor = cr.query(uri,columns,null,null,album);
        albumCursor.moveToFirst();
        return albumCursor;
    }

    public static Cursor getArtistCursor(Context context)
    {
        Cursor artistCursor ;

        final String _id = MediaStore.Audio.Artists._ID;
        final String artist = MediaStore.Audio.Artists.ARTIST;
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={_id, artist};
        artistCursor = cr.query(uri,columns,null,null,artist);
        artistCursor.moveToFirst();
        return artistCursor;
    }


    public static Cursor getGenreCursor(Context context)
    {
        Cursor genreCursor ;

        final String genre_id = MediaStore.Audio.Genres._ID;
        final String genre = MediaStore.Audio.Genres.NAME;
        Uri uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={genre_id, genre};
        genreCursor = cr.query(uri,columns,null,null,genre);
        genreCursor.moveToFirst();
        return genreCursor;
    }


    public static Cursor getPlaylistCursor(Context context)
    {
        Cursor playlistCursor ;

        final String playlist_id = MediaStore.Audio.Playlists._ID ;
        final String playlist = MediaStore.Audio.Playlists.NAME ;
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={playlist_id, playlist};
        playlistCursor = cr.query(uri,columns,null,null,playlist);
        playlistCursor.moveToFirst();
        return playlistCursor;
    }


    public static Cursor getAlbumSongsCursor (Context context, Cursor cursor){

        Cursor albumFetcher = cursor ;

        Cursor albumSongsCursor ;

        String checkingAlbumId = albumFetcher.getString(0);

        final String _Id = MediaStore.Audio.Albums._ID;
        final String albumId = MediaStore.Audio.Albums.ALBUM_ID;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String songPath = MediaStore.Audio.Media.DATA ;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={songPath,_Id, artist, track_name, songPath, albumId};

        String where = MediaStore.Audio.Media.ALBUM_ID + "=" + checkingAlbumId ;

        albumSongsCursor = cr.query( uri, columns, where, null, track_name );

        albumSongsCursor.moveToFirst() ;

        return albumSongsCursor ;
    }

    public static Cursor getArtistSongsCursor (Context context, Cursor cursor){

        Cursor artistFetcher = cursor ;

        Cursor artistSongsCursor ;

        String checkingArtistId = artistFetcher.getString(0);

        final String _Id = MediaStore.Audio.Artists._ID;
        final String artistId = MediaStore.Audio.Artists.ARTIST_KEY ;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String songPath = MediaStore.Audio.Media.DATA ;
        final String albumID = MediaStore.Audio.Media.ALBUM_ID ;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={artistId,_Id, artist, track_name, songPath, albumID};

        String where = MediaStore.Audio.Media.ARTIST_ID + "=" + checkingArtistId ;

        artistSongsCursor = cr.query( uri, columns, where, null, track_name );

        artistSongsCursor.moveToFirst() ;

        return artistSongsCursor ;
    }

    public static Cursor getGenreSongsCursor (Context context, Cursor cursor){

        Cursor genreFetcher = cursor ;

        Cursor genreSongsCursor ;

        String checkingGenreId = genreFetcher.getString(0);

        final String _Id = MediaStore.Audio.Genres._ID;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String songPath = MediaStore.Audio.Media.DATA ;
        final String albumID = MediaStore.Audio.Media.ALBUM_ID ;
        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", Long.parseLong(checkingGenreId));

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={artist , _Id , artist , track_name , songPath, albumID};

        genreSongsCursor = cr.query( uri, columns, null, null, track_name );

        genreSongsCursor.moveToFirst() ;

        return genreSongsCursor ;
    }


    public static Cursor getPlaylistSongsCursor (Context context, Cursor cursor){

        Cursor playlistFetcher = cursor ;

        Cursor playlistSongsCursor ;

        String checkingPlaylistId = playlistFetcher.getString(0);

        final String _Id = MediaStore.Audio.Playlists._ID;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String songPath = MediaStore.Audio.Media.DATA ;
        final String albumID = MediaStore.Audio.Media.ALBUM_ID ;
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(checkingPlaylistId));

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={artist , _Id , artist , track_name , songPath, albumID};

        playlistSongsCursor = cr.query( uri, columns, null, null, track_name );

        playlistSongsCursor.moveToFirst() ;

        return playlistSongsCursor ;
    }

    public static Cursor selectArtistSongCursor (Context context , Cursor parentCursor , int position){

        Cursor artistSongsCursor ;

        String checkingArtistId = parentCursor.getString(0);

        final String _Id = MediaStore.Audio.Artists._ID;
        final String artistId = MediaStore.Audio.Artists.ARTIST_KEY ;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String album = MediaStore.Audio.Media.ALBUM;
        final String songPath = MediaStore.Audio.Media.DATA ;
        final String albumId = MediaStore.Audio.Media.ALBUM_ID ;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={artistId,_Id, album, track_name, songPath , albumId};

        String where = MediaStore.Audio.Media.ARTIST_ID + "=" + checkingArtistId ;

        artistSongsCursor = cr.query( uri, columns, where, null, track_name );


        artistSongsCursor.moveToPosition(position);

        return artistSongsCursor ;

    }

    public static Cursor selectAlbumSongCursor (Context context , Cursor parentCursor , int position){

        Cursor albumSongsCursor ;

        String checkingAlbumId = parentCursor.getString(0);

        final String _Id = MediaStore.Audio.Albums._ID;
        final String albumId = MediaStore.Audio.Albums.ALBUM_ID;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String songPath = MediaStore.Audio.Media.DATA ;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={albumId,_Id, artist, track_name, songPath , albumId};

        String where = MediaStore.Audio.Media.ALBUM_ID + "=" + checkingAlbumId ;

        albumSongsCursor = cr.query( uri, columns, where, null, track_name );
        albumSongsCursor.moveToPosition(position);

        return albumSongsCursor ;

    }


    public static Cursor selectGenreSongCursor (Context context , Cursor parentCursor , int position){

        Cursor genreSongsCursor ;

        String checkingGenreId = parentCursor.getString(0);

        final String _Id = MediaStore.Audio.Genres._ID;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String songPath = MediaStore.Audio.Media.DATA ;
        final String albumId = MediaStore.Audio.Media.ALBUM_ID ;
        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", Long.parseLong(checkingGenreId));

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={artist , _Id , artist , track_name , songPath , albumId};

        genreSongsCursor = cr.query( uri, columns, null, null, track_name );
        genreSongsCursor.moveToPosition(position) ;

        return genreSongsCursor ;

    }


    public static Cursor selectPlaylistSongCursor (Context context , Cursor parentCursor , int position){

        Cursor playlistSongsCursor ;

        String checkingPlaylistId = parentCursor.getString(0);

        final String _Id = MediaStore.Audio.Playlists._ID;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String songPath = MediaStore.Audio.Media.DATA ;
        final String albumId = MediaStore.Audio.Media.ALBUM_ID ;
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(checkingPlaylistId));

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={artist , _Id , artist , track_name , songPath , albumId};

        playlistSongsCursor = cr.query( uri, columns, null, null, track_name );
        playlistSongsCursor.moveToPosition(position) ;

        return playlistSongsCursor ;

    }


    public static String getAlbumArt (Context context , String albumIdToBeRetrieved){

        Cursor albumSongsCursor ;

        String checkingAlbumId = albumIdToBeRetrieved;

        final String _Id = MediaStore.Audio.Albums._ID;
        final String albumArt = MediaStore.Audio.Albums.ALBUM_ART ;
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI ;

        ContentResolver cr =  context.getContentResolver();
        final String[]columns={_Id, albumArt};

        String where = MediaStore.Audio.Media._ID + "=" + checkingAlbumId ;

        albumSongsCursor = cr.query( uri, columns, where, null, null );
        albumSongsCursor.moveToFirst();

        String albumArtPath = null ;

        if (albumSongsCursor.getCount() > 0)
            albumArtPath = albumSongsCursor.getString(1);

        return albumArtPath ;

    }



}
