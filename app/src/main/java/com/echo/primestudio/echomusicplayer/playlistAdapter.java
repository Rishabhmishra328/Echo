package com.echo.primestudio.echomusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Rishabh Mishra on 12/17/2015.
 */
public class playlistAdapter extends CursorAdapter {

    public playlistAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Declarations
        TextView playlistName ;


        //Initialisation
        playlistName = (TextView) view.findViewById(R.id.playlist_name_playlist_template);

        playlistName.setText(cursor.getString(1));
//        playlistName.setTypeface(Main.echoPrimaryTypeFace);


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflaterForLayout = LayoutInflater.from(context) ;
        View rowView = inflaterForLayout.inflate(R.layout.playlist_template , parent , false) ;

        return rowView;
    }
}
