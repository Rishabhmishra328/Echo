package com.echo.primestudio.echomusicplayer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * Created by Rishabh Mishra on 12/25/2015.
 */
public class nowPlaying extends Activity {

    private static String currentSongId;
    private static Boolean fromPrevious = false;
    public static int majorColor;


    public static void play(final Context context, String trackId, String songPath, String trackName, String trackArtist, String albumId) {

        Boolean checkingForSameTrack = true;


        if (currentSongId != null) {

            checkingForSameTrack = !currentSongId.equals(trackId);

        }

        if (checkingForSameTrack || !Main.player.isPlaying()) {

            //Changing player status
            Main.player.stop();
            Uri songUri = Uri.parse(songPath);
            Main.player = MediaPlayer.create(context, songUri);
            Main.playButton.setBackgroundResource(R.drawable.pause);
            Main.playPauseNowPlayingDescription.setBackgroundResource(R.drawable.pause);
            Main.setupVisualizerFxAndUI();
            Main.songVisualizer.setEnabled(true);
            Main.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Main.player.start();
                }
            });

            //Updating information
            Main.nowPlayingSongName.setText(trackName);
            Main.nowPlayingSongArtist.setText(trackArtist);
            Main.songNameNowPlayingDescription.setText(trackName);
            Main.songArtistNowPlayingDescription.setText(trackArtist);

            //Changing album arts
            if (songPopulator.getAlbumArt(context, albumId) != null) {
                Uri albumArtUri = Uri.parse(songPopulator.getAlbumArt(context, albumId));
                Main.albumArtNowPlayingCurtain.setImageURI(albumArtUri);
            } else {
                Main.albumArtNowPlayingCurtain.setImageResource(R.drawable.echo_album_art);
            }

                //SeekUpdation
                Main.seekBar.setMax(Main.player.getDuration());

                int maxTime = Main.player.getDuration();
                maxTime = maxTime / 1000;
                Main.maxTimeMin.setText(String.format("%02d", (maxTime / 60)));
                Main.maxTimeSec.setText(String.format("%02d", (maxTime % 60)));


                //Removing from Hash Map
                Main.map.remove(Integer.parseInt(trackId));


            }


            currentSongId = trackId;

            //Player Configuration
            Main.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Main.playButton.setBackgroundResource(R.drawable.play);
                    Main.playPauseNowPlayingDescription.setBackgroundResource(R.drawable.play);
                    nowPlaying.next(context, Main.currentCursor);
                }
            });


            Main.lastSongData.setSongPath(songPath);
            Main.lastSongData.setSongId(trackId);
            Main.lastSongData.setSongName(trackName);
            Main.lastSongData.setSongArtist(trackArtist);
            Main.lastSongData.setSongAlbumId(albumId);

            if (!fromPrevious) {
                Main.previousSongs.push(new songData(albumId, trackArtist, trackId, trackName, songPath));
                Log.d("STACK", "PUSHED" + trackName);
            }
//
//        if (fromPrevious){
//            Main.previousSongs.push(Main.lastSongData);
//        }

            //Seek Update
            Main.seekUpdation();

            //Changing Colors
            if (songPopulator.getAlbumArt(context, albumId) != null) {
                try {
                    Log.d("ALBUMID",albumId);
                    Main.changeColor(getAlbumArtColor(BitmapFactory.decodeFile(songPopulator.getAlbumArt(context, albumId))));
                }catch(Exception e){
                    e.printStackTrace();
                    Main.changeColor(345);
                }
            } else {
                Main.changeColor(345);
            }
            Log.d("NOWPLAYING", "PLAY");


    }


    public static void next(Context context, Cursor cursor) {

        //0 for off, 1 for all , 2 for repeat one
        int repeatStatus = Main.repeat % 3;
        //0 for no, 1 for yes
        int shuffleStatus = Main.shuffle % 2;

        int currentCursorPosition = cursor.getPosition();

        if (fromPrevious) {
            Main.previousSongs.push(Main.lastSongData);
            Log.d("STACK", "PUSHED" + Main.lastSongData.getSongName());
        }

        fromPrevious = false;

        Log.d("NOWPLAYING", "NEXT");

//        if (!fromPrevious) {
//            Main.previousSongs.push(new songData(albumId, trackArtist, trackId, trackName, songPath));
//            Main.previousSongs.push(new songData(cursor.getString(5),cursor.getString(2),cursor.getString(1),cursor.getString(3),cursor.getString(4)));
//            Log.d("NOWPLAYING", "NOT FROM PREVIOUS PLAY");
//        }


        if (repeatStatus == 0 && shuffleStatus == 0) {


            if (currentCursorPosition < cursor.getCount()) {
                currentCursorPosition++;
            }
            if (currentCursorPosition >= cursor.getCount()) {
                currentCursorPosition = currentCursorPosition % cursor.getCount();
            }

            if (Main.map.isEmpty()) {
                if (!Main.player.isPlaying()) {
                    Main.mapping(cursor);
                }
                if (Main.player.isPlaying()) {
                    Main.player.seekTo(0);
                    Main.player.pause();
                    Main.currentTimeMin.setText(String.format("%02d", 0));
                    Main.currentTimeSec.setText(String.format("%02d", 0));
                }
            }


            if (!Main.map.isEmpty()) {
                cursor.moveToPosition(currentCursorPosition);
                play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));


            }


        }

        if (repeatStatus == 1 && shuffleStatus == 0) {

            if (currentCursorPosition < cursor.getCount()) {
                currentCursorPosition++;
            }
            if (currentCursorPosition >= cursor.getCount()) {
                currentCursorPosition = currentCursorPosition % cursor.getCount();
            }
            cursor.moveToPosition(currentCursorPosition);
            play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));

        }


        if (repeatStatus == 0 && shuffleStatus == 1) {


            Random rng = new Random();

//            currentCursorPosition = rng.nextInt(cursor.getCount());
//            cursor.moveToPosition(currentCursorPosition);


            if (Main.map.isEmpty()) {
                if (!Main.player.isPlaying()) {
                    Main.mapping(cursor);
                    Log.d("NOWPLAYING", "Entering Mapping");
                }
                if (Main.player.isPlaying()) {
                    Main.player.seekTo(0);
                    Main.player.pause();
                    Main.currentTimeMin.setText(String.format("%02d", 0));
                    Main.currentTimeSec.setText(String.format("%02d", 0));
                }
            }


            while (!Main.map.isEmpty()) {

                currentCursorPosition = rng.nextInt(cursor.getCount());
                cursor.moveToPosition(currentCursorPosition);

                Log.d("NOWPLAYING", "STUCK" + "\t" + Integer.toString(currentCursorPosition) + "\t" + cursor.getString(3) + "\t" + Main.map.containsValue(cursor.getString(3)));

                Set<Map.Entry<Integer, String>> st = Main.map.entrySet();
                Log.d("ITERATOR", "\t" + st.toString());

                if (Main.map.containsValue(cursor.getString(3))) {
                    break;
                }

            }

            if (!Main.map.isEmpty()) {
                play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));
            }


        }


        if (repeatStatus == 1 && shuffleStatus == 1) {

            Random rng = new Random();

            currentCursorPosition = rng.nextInt(cursor.getCount());
            cursor.moveToPosition(currentCursorPosition);
            play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));

        }

        if (repeatStatus == 2) {
            cursor.moveToPosition(currentCursorPosition);
            Main.player.seekTo(0);
            play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));
        }


    }


    public static void previous(Context context, Cursor cursor) {
//
//        if (Main.player.getCurrentPosition() > 1000) {
//            Main.player.seekTo(0);
//        }
//
//        if (Main.player.getCurrentPosition() < 1000) {

        //0 for off, 1 for all , 2 for repeat one
        int repeatStatus = Main.repeat % 3;
        //0 for no, 1 for yes
        int shuffleStatus = Main.shuffle % 2;

        int prevCursor = cursor.getPosition();

        fromPrevious = true;

        Log.d("NOWPLAYING", "PREVIOUS");

        if (Main.previousSongs.isEmpty()) {
            next(Main.getContext(), cursor);
        }

        if (repeatStatus == 0 && shuffleStatus == 0) {

            if (prevCursor < cursor.getCount()) {
                prevCursor--;
            }
            if (prevCursor <= 0) {
                prevCursor = prevCursor % cursor.getCount();
            }
            if (prevCursor >= 0) {
                cursor.moveToPosition(prevCursor);
                play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));
            } else {
                Main.player.stop();
                Main.playButton.setBackgroundResource(R.drawable.play);
            }

        }

        if (repeatStatus == 1 && shuffleStatus == 0) {

            if (prevCursor < cursor.getCount()) {
                prevCursor--;
            }
            if (prevCursor < 0) {
                prevCursor = prevCursor + cursor.getCount();
            }
            cursor.moveToPosition(prevCursor);
            play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));

        }

        if (repeatStatus == 0 && shuffleStatus == 1 && !Main.previousSongs.isEmpty()) {

            songData playPreviousSongData = Main.previousSongs.pop();
            Log.d("STACK", "POPPED" + playPreviousSongData.getSongName());
            play(context, playPreviousSongData.getSongId(), playPreviousSongData.getSongPath(), playPreviousSongData.getSongName(), playPreviousSongData.getSongArtist(), playPreviousSongData.getSongAlbumId());

        }

        if (repeatStatus == 1 && shuffleStatus == 1 && !Main.previousSongs.isEmpty()) {

            songData playPreviousSongData = Main.previousSongs.pop();
            Log.d("STACK", "POPPED" + playPreviousSongData.getSongName());
            Log.d("PREVIOUS", "GOING TO PLAY PREVIOUS : " + playPreviousSongData.getSongName());
            play(context, playPreviousSongData.getSongId(), playPreviousSongData.getSongPath(), playPreviousSongData.getSongName(), playPreviousSongData.getSongArtist(), playPreviousSongData.getSongAlbumId());

        }

        if (repeatStatus == 2) {
            cursor.moveToPosition(prevCursor);
            Main.player.seekTo(0);
            play(context, cursor.getString(1), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getString(5));
        }


    }


//    }


    public static int getAlbumArtColor(Bitmap albumArt) {

        final Palette albumPalette = Palette.generate(albumArt);

        Palette.Swatch albumSwatch = albumPalette.getVibrantSwatch();

        if (albumSwatch == null) {
            albumSwatch = albumPalette.getLightVibrantSwatch();
        } else if (albumSwatch == null) {
            albumSwatch = albumPalette.getDarkVibrantSwatch();
        } else if (albumSwatch == null) {
            albumSwatch = albumPalette.getMutedSwatch();
        } else if (albumSwatch == null) {
            return majorColor = 345;
        }

        if (albumSwatch != null) {
            float[] hsl = albumSwatch.getHsl();

            majorColor = (int) hsl[0];

            Log.d("COLOR :", "\t" + Integer.toString(majorColor));
        }

        return majorColor;

    }


}
