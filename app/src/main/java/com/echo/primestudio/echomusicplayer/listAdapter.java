package com.echo.primestudio.echomusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Rishabh Mishra on 12/17/2015.
 */
public class listAdapter extends CursorAdapter {

//    Uri albumArtUri ;

    public listAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Declarations
        TextView songTitle , songArtist ;
        ImageView songAlbumArt ;
        String songPath ;

        songPath = cursor.getString(0) ;

        //Initialisation
        songTitle = (TextView) view.findViewById(R.id.all_songs_template_song_name);
        songArtist = (TextView) view.findViewById(R.id.all_songs_template_song_artist);
        songAlbumArt = (ImageView) view.findViewById(R.id.all_songs_template_song_album_art);

        songTitle.setText( cursor.getString(3) ) ;
//        songTitle.setTypeface(Main.echoPrimaryTypeFace);
        songArtist.setText(cursor.getString(2));
//        songArtist.setTypeface(Main.echoPrimaryTypeFace);

        String albumArtPath = songPopulator.getAlbumArt( context , cursor.getString(5) ) ;

        try {

            Bitmap albumArtBitmap = BitmapFactory.decodeFile(albumArtPath) ;
            Bitmap albumArtThumbnail = ThumbnailUtils.extractThumbnail(albumArtBitmap, 50 , 50) ;

            songAlbumArt.setImageBitmap(albumArtThumbnail);

        } catch (NullPointerException e) {
            e.printStackTrace();
            songAlbumArt.setImageResource(R.drawable.echo_logo);
        }



    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflaterForLayout = LayoutInflater.from(context) ;
        View rowView = inflaterForLayout.inflate(R.layout.all_songs_template , parent , false) ;

        return rowView;
    }


}
