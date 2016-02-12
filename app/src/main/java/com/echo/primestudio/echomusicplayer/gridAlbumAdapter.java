package com.echo.primestudio.echomusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rishabh Mishra on 12/17/2015.
 */
public class gridAlbumAdapter extends CursorAdapter {


    public gridAlbumAdapter(Context context, Cursor c) {
        super(context, c);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Declarations
        TextView songAlbum ;
        ImageView songAlbumArt ;

        //Initialisation
        songAlbum = (TextView) view.findViewById(R.id.album_template_album);
        songAlbumArt = (ImageView) view.findViewById(R.id.album_template_album_art);

        songAlbum.setText(cursor.getString(1));
//        songAlbum.setTypeface(Main.echoPrimaryTypeFace);

        String albumArtPath = songPopulator.getAlbumArt( context , cursor.getString(0) ) ;

            try {

                Bitmap albumArtBitmap = BitmapFactory.decodeFile(albumArtPath) ;
//                Bitmap albumArtThumbnail = ThumbnailUtils.extractThumbnail(albumArtBitmap , 99 , 99) ;

                songAlbumArt.setImageBitmap(albumArtBitmap);

            } catch (NullPointerException e) {
                e.printStackTrace();
                songAlbumArt.setImageResource(R.drawable.echo_logo);
            }

        songAlbumArt.setScaleType(ImageView.ScaleType.FIT_CENTER);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflaterForLayout = LayoutInflater.from(context) ;
        View gridView = inflaterForLayout.inflate(R.layout.album_template , parent , false) ;

        return gridView;
    }

}
