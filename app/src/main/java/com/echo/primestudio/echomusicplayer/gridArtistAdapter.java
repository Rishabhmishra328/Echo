package com.echo.primestudio.echomusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rishabh Mishra on 12/17/2015.
 */
public class gridArtistAdapter extends CursorAdapter {

    public gridArtistAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Declarations
        TextView songArtist ;


        //Initialisation
        songArtist = (TextView) view.findViewById(R.id.artist_template_artist);

        songArtist.setText( cursor.getString(1) );
//        songArtist.setTypeface(Main.echoPrimaryTypeFace);



    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflaterForLayout = LayoutInflater.from(context) ;
        View gridView = inflaterForLayout.inflate(R.layout.artist_template , parent , false) ;

        return gridView;
    }
}
