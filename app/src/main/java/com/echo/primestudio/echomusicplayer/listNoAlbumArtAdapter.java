package com.echo.primestudio.echomusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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

public class listNoAlbumArtAdapter extends CursorAdapter {

//    Uri albumArtUri ;

    public listNoAlbumArtAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Declarations
        TextView songTitle , songArtist ;
        //Initialisation
        songTitle = (TextView) view.findViewById(R.id.songs_no_album_art_template_song_name);
        songArtist = (TextView) view.findViewById(R.id.songs_no_album_art_template_song_artist);

        songTitle.setText( cursor.getString(3) ) ;
//        songTitle.setTypeface(Main.echoPrimaryTypeFace);
        songArtist.setText(cursor.getString(2));
//        songArtist.setTypeface(Main.echoPrimaryTypeFace);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflaterForLayout = LayoutInflater.from(context) ;
        View rowView = inflaterForLayout.inflate(R.layout.songs_no_album_art_template , parent , false) ;

        return rowView;
    }


}
