package com.echo.primestudio.echomusicplayer;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Stack;

public class Main extends ActionBarActivity implements View.OnClickListener {

    //Declaration
    public static Context applicationContext;
    public static MediaPlayer player = new MediaPlayer();
    public static Bitmap defaultThumbnail;
    public static Bitmap defaultAlbumArt;
    public static Typeface echoPrimaryTypeFace, echoSecondaryTypeFace;
    private static Toolbar appBar;
    private ViewPager classificationPager;
    private SlidingTabLayout classificationTabs;
    public static View special, navigationDrawer, nowPlayingCurtain, nowPlayingDescription ;
    public static ListView artistSongList, albumSongList, genreSongList, playlistSongList , allSongsLV ;
    public static ImageView albumArtSpecialList, playButton, nextButton, previousButton, repeatButton, shuffleButton, albumArtNowPlayingCurtain, playPauseNowPlayingDescription, specialAlbumArt;
    public static TextView nowPlayingSongName, nowPlayingSongArtist, songNameNowPlayingDescription, songArtistNowPlayingDescription, currentTimeMin, currentTimeSec, maxTimeMin, maxTimeSec, currentTimeSeprator, totalTimeSeprator, timeSlash, songArtistAllSongsTemplate;
    public static Cursor playlistListCursor, genreListCursor, albumListCursor, artistListCursor, currentCursor;
    public static int repeat = 0, shuffle = 0, albumArtNowPlayingCurtainWidth = 0;
    public static HashMap<Integer, String> map = new HashMap<>();
    public static Stack<songData> previousSongs = new Stack<>();
    public static Visualizer songVisualizer;
    public static AudioManager audio;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor sharedPreferencesEditor;
    public static songData lastSongData;
    public Boolean firstSong = true;
    public static SeekBar seekBar;
    public static android.os.Handler seekHandler = new android.os.Handler();
    public static Window window;
    public static barVisualization barViz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Declarations
        nowPlayingCurtain = findViewById(R.id.now_playing_curtain);
        nowPlayingDescription = findViewById(R.id.now_playing_description);
        special = findViewById(R.id.special_view);
        navigationDrawer = findViewById(R.id.navigation_drawer);
        allSongsLV = (ListView) findViewById(R.id.all_songs_lv);
        artistSongList = (ListView) findViewById(R.id.list_view_artist_songs);
        albumSongList = (ListView) findViewById(R.id.list_view_album_songs);
        genreSongList = (ListView) findViewById(R.id.list_view_genre_songs);
        playlistSongList = (ListView) findViewById(R.id.list_view_playlist_songs);
        albumArtSpecialList = (ImageView) findViewById(R.id.album_art_special_view);
        currentTimeSeprator = (TextView) findViewById(R.id.current_time_separator_now_playing_curtain);
        totalTimeSeprator = (TextView) findViewById(R.id.total_time_separator_now_playing_curtain);
        timeSlash = (TextView) findViewById(R.id.time_slash_now_playing_curtain);
        nowPlayingSongName = (TextView) findViewById(R.id.current_song_name_now_playing_curtain);
        songNameNowPlayingDescription = (TextView) findViewById(R.id.song_name_now_playing_description);
        songArtistNowPlayingDescription = (TextView) findViewById(R.id.song_artist_now_playing_description);
        currentTimeMin = (TextView) findViewById(R.id.current_time_min_now_playing_curtain);
        currentTimeSec = (TextView) findViewById(R.id.current_time_sec_now_playing_curtain);
        maxTimeMin = (TextView) findViewById(R.id.total_time_min_now_playing_curtain);
        maxTimeSec = (TextView) findViewById(R.id.total_time_sec_now_playing_curtain);
        nowPlayingSongArtist = (TextView) findViewById(R.id.current_song_artist_now_playing_curtain);
        songArtistAllSongsTemplate = (TextView) findViewById(R.id.all_songs_template_song_artist);
        playButton = (ImageView) findViewById(R.id.play_pause_now_playing_curtain);
        playPauseNowPlayingDescription = (ImageView) findViewById(R.id.play_pause_now_playing_description);
        nextButton = (ImageView) findViewById(R.id.next_now_playing_curtain);
        previousButton = (ImageView) findViewById(R.id.previous_now_playing_curtain);
        repeatButton = (ImageView) findViewById(R.id.repeat_now_playing_curtain);
        shuffleButton = (ImageView) findViewById(R.id.shuffle_now_playing_curtain);
        specialAlbumArt = (ImageView) findViewById(R.id.album_art_special_view);
        albumArtNowPlayingCurtain = (ImageView) findViewById(R.id.album_art_now_playing_curtain);
        seekBar = (SeekBar) findViewById(R.id.seek_bar_now_playing_curtain);
        barViz = (barVisualization) findViewById(R.id.barVisualization_now_playing_curtain);

        //Initialisation
        albumArtNowPlayingCurtainWidth = albumArtNowPlayingCurtain.getMaxWidth();


        //Curtain Call
        nowPlayingDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDown();
            }
        });

        //Shared Preferences
        sharedPref = getPreferences(MODE_PRIVATE);
        sharedPreferencesEditor = sharedPref.edit();


        repeat = sharedPref.getInt("repeat", 0);
        shuffle = sharedPref.getInt("shuffle", 0);
        lastSongData = new songData(sharedPref.getString("lastSongAlbumId", "N/A"), sharedPref.getString("lastSongArtist", "N/A"), sharedPref.getString("lastSongId", "N/A"), sharedPref.getString("lastSongName", "N/A"), sharedPref.getString("lastSongPath", "N/A"));


        //Shared Preferences Changes
        sharedPreferencesChanges();

        //Setting up OnClickListeners
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nowPlayingCurtain.setOnClickListener(this);
        repeatButton.setOnClickListener(this);
        shuffleButton.setOnClickListener(this);
        playPauseNowPlayingDescription.setOnClickListener(this);


        //Application bar
        appBar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHideOffset(0);
        window = getWindow();

        //Declaring Context static variable
        Context appContext = getApplicationContext();
        applicationContext = appContext;


        //Audio Stream for volume
        AudioManager thisAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio = thisAudioManager;


        //Pager and PagerAdapter setup
        classificationPager = (ViewPager) findViewById(R.id.classificationPager);
        classificationPager.setAdapter(new classificationPagerAdapter(getSupportFragmentManager()));

        //Set notification
//        startService();


        //Tabs setup
        classificationTabs = (SlidingTabLayout) findViewById(R.id.classificationTab);
        classificationTabs.setViewPager(classificationPager);

        //Changing Font
        echoPrimaryTypeFace = Typeface.createFromAsset(getAssets(), "fonts/code_bold.otf");
        echoSecondaryTypeFace = Typeface.createFromAsset(getAssets(), "fonts/code_light.otf");
//        changeFonts();

        //Placing album art on curtain
        Rect albumArtRectangle = new Rect();
        albumArtNowPlayingCurtain.setTranslationX(150 - albumArtNowPlayingCurtain.getWidth());
        albumArtNowPlayingCurtain.setTranslationY(albumArtNowPlayingCurtain.getWidth() - 150);
        albumArtNowPlayingCurtain.setScaleX((float) 1.25);
        albumArtNowPlayingCurtain.setScaleY((float) 1.25);
//        albumArtNowPlayingCurtain.getDrawingRect(albumArtRectangle);
//        albumArtTop.setX(albumArtRectangle.centerX());
//        albumArtTop.setY(albumArtRectangle.centerY());

        //Call listener
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    player.pause();
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    player.start();
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    player.pause();
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }


        //Seek Bar initiation
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                player.seekTo(progress);
                seekUpdation();
            }
        });


        //Initiating global variables
        defaultThumbnail = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.echo_logo);
        defaultAlbumArt = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.echo_logo);

        //Setting up adapter and properties in aSyncTask to avoid Null Exception


        //List View On Click Listener


    }


    //Seekbar

    //SeekBar Runnable
    static Runnable seekRunnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };


    //Seekbar Update
    public static void seekUpdation() {
        int currentPosition = player.getCurrentPosition();
        seekBar.setProgress(player.getCurrentPosition());

        currentPosition = currentPosition / 1000;

        currentTimeMin.setText(String.format("%02d", (currentPosition / 60)));
        currentTimeSec.setText(String.format("%02d", (currentPosition % 60)));

        seekHandler.postDelayed(seekRunnable, 1000);
    }


    @Override
    public void onBackPressed() {
        if (isCurtainShown()) {
            slideUpDown();
        } else if (isSpecialShown()) {
            slideFromRight();
        } else if (!isCurtainShown() && !isSpecialShown()) {
            this.moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {

            case R.id.play_pause_now_playing_curtain:

                if (firstSong) {
                    nowPlaying.play(getContext(), lastSongData.getSongId(), lastSongData.getSongPath(), lastSongData.getSongName(), lastSongData.getSongArtist(), lastSongData.getSongAlbumId());
                    firstSong = false;
                }
                if (player.isPlaying()) {
                    findViewById(R.id.play_pause_now_playing_curtain).setBackgroundResource(R.drawable.play);
                    findViewById(R.id.play_pause_now_playing_description).setBackgroundResource(R.drawable.play);
                    player.pause();
                } else {
                    findViewById(R.id.play_pause_now_playing_curtain).setBackgroundResource(R.drawable.pause);
                    findViewById(R.id.play_pause_now_playing_description).setBackgroundResource(R.drawable.pause);
                    player.start();
                }

                changeColor(nowPlaying.majorColor);

                break;

            case R.id.repeat_now_playing_curtain:
                repeat++;

                switch (repeat % 3) {
                    case 0:
                        Main.repeatButton.setImageDrawable(getResources().getDrawable(R.drawable.repeat));
                        break;
                    case 1:
                        Main.repeatButton.setImageDrawable(getResources().getDrawable(R.drawable.repeat_selected));
                        break;
                    case 2:
                        Main.repeatButton.setImageDrawable(getResources().getDrawable(R.drawable.repeat_one));
                        break;
                }

                break;

            case R.id.shuffle_now_playing_curtain:
                shuffle++;

                switch (shuffle % 2) {
                    case 0:
                        Main.shuffleButton.setImageDrawable(getResources().getDrawable(R.drawable.shuffle));
                        break;
                    case 1:
                        Main.shuffleButton.setImageDrawable(getResources().getDrawable(R.drawable.shuffle_selected));
                        break;
                }

                break;

            case R.id.next_now_playing_curtain:
                nowPlaying.next(getContext(), currentCursor);
                Log.d("NEXT", "current position " + currentCursor.getPosition() + "repeat " + repeat + "shuffle " + shuffle);
                break;

            case R.id.play_pause_now_playing_description:
                if (firstSong) {
                    nowPlaying.play(getContext(), lastSongData.getSongId(), lastSongData.getSongPath(), lastSongData.getSongName(), lastSongData.getSongArtist(), lastSongData.getSongAlbumId());
                    firstSong = false;
                }
                if (player.isPlaying()) {
                    findViewById(R.id.play_pause_now_playing_curtain).setBackgroundResource(R.drawable.play);
                    findViewById(R.id.play_pause_now_playing_description).setBackgroundResource(R.drawable.play);
                    player.pause();
                } else {
                    findViewById(R.id.play_pause_now_playing_curtain).setBackgroundResource(R.drawable.pause);
                    findViewById(R.id.play_pause_now_playing_description).setBackgroundResource(R.drawable.pause);
                    player.start();
                }
                break;

            case R.id.previous_now_playing_curtain:
                nowPlaying.previous(getContext(), currentCursor);
                break;

        }

    }


    //Pager Adapter
    class classificationPagerAdapter extends FragmentPagerAdapter {

        String[] tabNames;

        public classificationPagerAdapter(FragmentManager fm) {

            super(fm);

            tabNames = getResources().getStringArray(R.array.tabNames);

        }


        @Override
        public Fragment getItem(int position) {

            classificationFragments fragment = classificationFragments.getInstance(position);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    //Implementing Single Fragment

    public static class classificationFragments extends Fragment {

        private ListView fragmentListView;
        private GridView fragmentGridView;

        public static classificationFragments getInstance(int position) {

            classificationFragments fragment = new classificationFragments();

            //Creating a bundle to keep track of where we are
            Bundle classificationBundle = new Bundle();
            classificationBundle.putInt("page", position);
            fragment.setArguments(classificationBundle);

            return fragment;

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View fragmentView = inflater.inflate(R.layout.fragment_artist, container, false);


            Bundle recyclerViewBundle = getArguments();


            int pageNo = recyclerViewBundle.getInt("page");

            if (recyclerViewBundle != null) {

                switch (pageNo) {

                    case 0:

                        fragmentView = inflater.inflate(R.layout.fragment_all_songs, container, false);

                        fragmentListView = (ListView) fragmentView.findViewById(R.id.all_songs_lv);

                        listAdapter allSongsAdapter;

                        allSongsAdapter = new listAdapter(getContext(), songPopulator.getTrackCursor(getContext()));
                        fragmentListView.setAdapter(allSongsAdapter);

                        fragmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Cursor allSongsCursor = songPopulator.getTrackCursor(getContext());

                                //Setting current cursor
                                currentCursor = allSongsCursor;

                                //Eligible song mapping
                                mapping(songPopulator.getTrackCursor(getContext()));
//                                map.remove(Integer.parseInt(currentCursor.getString(1)));


                                allSongsCursor.moveToPosition(position);

                                playButton.setBackgroundResource(R.drawable.pause);

                                nowPlaying.play(getContext(), allSongsCursor.getString(1), allSongsCursor.getString(4), allSongsCursor.getString(3), allSongsCursor.getString(2), allSongsCursor.getString(5));

                                nowPlayingSongName.setText(allSongsCursor.getString(3));
                                nowPlayingSongArtist.setText(allSongsCursor.getString(2));

                                slideUpDown();

                            }
                        });

                        break;

                    case 1:

                        fragmentView = inflater.inflate(R.layout.fragment_artist, container, false);

                        fragmentGridView = (GridView) fragmentView.findViewById(R.id.artist_gv);

                        gridArtistAdapter artistAdapter;

                        artistListCursor = songPopulator.getArtistCursor(getContext());

                        artistAdapter = new gridArtistAdapter(getContext(), artistListCursor);
                        fragmentGridView.setAdapter(artistAdapter);

                        fragmentGridView.setOnItemClickListener(new GridView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                artistSongList.setVisibility(View.VISIBLE);
                                albumSongList.setVisibility(View.GONE);
                                genreSongList.setVisibility(View.GONE);
                                playlistSongList.setVisibility(View.GONE);

                                artistListCursor.moveToPosition(position);


                                Cursor tableCursor = songPopulator.getArtistSongsCursor(getContext(), artistListCursor);

                                listNoAlbumArtAdapter artistSongAdapter;

                                artistSongAdapter = new listNoAlbumArtAdapter(getContext(), tableCursor);
                                artistSongList.setAdapter(artistSongAdapter);

                                albumArtSpecialList.setVisibility(View.GONE);


                                slideFromRight();

                            }
                        });

                        artistSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Cursor getSong = songPopulator.selectArtistSongCursor(getContext(), artistListCursor, position);

                                //Setting current cursor
                                currentCursor = getSong;

                                //Eligible song position selector
                                mapping(currentCursor);

                                playButton.setBackgroundResource(R.drawable.pause);

                                nowPlaying.play(getContext(), getSong.getString(1), getSong.getString(4), getSong.getString(3), getSong.getString(2), getSong.getString(5));

                                nowPlayingSongName.setText(getSong.getString(3));
                                nowPlayingSongArtist.setText(getSong.getString(2));


                                slideUpDown();

                            }
                        });

                        break;

                    case 2:

                        fragmentView = inflater.inflate(R.layout.fragment_album, container, false);

                        fragmentGridView = (GridView) fragmentView.findViewById(R.id.album_gv);

                        gridAlbumAdapter albumAdapter;

                        albumListCursor = songPopulator.getAlbumCursor(getContext());

                        albumAdapter = new gridAlbumAdapter(getContext(), albumListCursor);
                        fragmentGridView.setAdapter(albumAdapter);

                        fragmentGridView.setOnItemClickListener(new GridView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                artistSongList.setVisibility(View.GONE);
                                albumSongList.setVisibility(View.VISIBLE);
                                genreSongList.setVisibility(View.GONE);
                                playlistSongList.setVisibility(View.GONE);

                                albumListCursor.moveToPosition(position);

                                Cursor tableCursor = songPopulator.getAlbumSongsCursor(getContext(), albumListCursor);

                                listNoAlbumArtAdapter albumSongAdapter;

                                albumSongAdapter = new listNoAlbumArtAdapter(getContext(), tableCursor);
                                albumSongList.setAdapter(albumSongAdapter);

                                albumArtSpecialList.setVisibility(View.VISIBLE);


                                try {
                                    specialAlbumArt.setImageBitmap(BitmapFactory.decodeFile(songPopulator.getAlbumArt(getContext(), albumListCursor.getString(0))));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    specialAlbumArt.setImageResource(R.drawable.echo_album_art);
                                }


                                slideFromRight();

                            }
                        });


                        albumSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                Cursor getSong = songPopulator.selectAlbumSongCursor(getContext(), albumListCursor, position);

                                //Setting current cursor
                                currentCursor = getSong;

                                //Eligible song position selector
                                mapping(currentCursor);

                                playButton.setBackgroundResource(R.drawable.pause);

                                nowPlaying.play(getContext(), getSong.getString(1), getSong.getString(4), getSong.getString(3), getSong.getString(2), getSong.getString(5));

                                nowPlayingSongName.setText(getSong.getString(3));
                                nowPlayingSongArtist.setText(getSong.getString(2));


                                slideUpDown();

                            }
                        });

                        break;


                    case 3:

                        fragmentView = inflater.inflate(R.layout.fragment_genre, container, false);

                        fragmentGridView = (GridView) fragmentView.findViewById(R.id.genre_gv);

                        gridGenreAdapter genreAdapter;

                        genreListCursor = songPopulator.getGenreCursor(getContext());

                        genreAdapter = new gridGenreAdapter(getContext(), genreListCursor);
                        fragmentGridView.setAdapter(genreAdapter);

                        fragmentGridView.setOnItemClickListener(new GridView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                artistSongList.setVisibility(View.GONE);
                                albumSongList.setVisibility(View.GONE);
                                genreSongList.setVisibility(View.VISIBLE);
                                playlistSongList.setVisibility(View.GONE);


                                genreListCursor.moveToPosition(position);

                                Cursor tableCursor = songPopulator.getGenreSongsCursor(getContext(), genreListCursor);

                                listNoAlbumArtAdapter genreAdapter;

                                genreAdapter = new listNoAlbumArtAdapter(getContext(), tableCursor);
                                genreSongList.setAdapter(genreAdapter);

                                albumArtSpecialList.setVisibility(View.GONE);

                                slideFromRight();

                            }
                        });

                        genreSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                Cursor getSong = songPopulator.selectGenreSongCursor(getContext(), genreListCursor, position);

                                //Setting current cursor
                                currentCursor = getSong;

                                //Eligible song position selector
                                mapping(currentCursor);

                                playButton.setBackgroundResource(R.drawable.pause);

                                nowPlaying.play(getContext(), getSong.getString(1), getSong.getString(4), getSong.getString(3), getSong.getString(2), getSong.getString(5));

                                nowPlayingSongName.setText(getSong.getString(3));
                                nowPlayingSongArtist.setText(getSong.getString(2));


                                slideUpDown();

                            }
                        });

                        break;

                    case 4:

                        fragmentView = inflater.inflate(R.layout.fragment_playlist, container, false);

                        fragmentListView = (ListView) fragmentView.findViewById(R.id.playlist_lv);

                        final playlistAdapter playlistAdapter;

                        playlistListCursor = songPopulator.getPlaylistCursor(getContext());

                        playlistAdapter = new playlistAdapter(getContext(), playlistListCursor);
                        fragmentListView.setAdapter(playlistAdapter);

                        fragmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                artistSongList.setVisibility(View.GONE);
                                albumSongList.setVisibility(View.GONE);
                                genreSongList.setVisibility(View.GONE);
                                playlistSongList.setVisibility(View.VISIBLE);

                                playlistListCursor.moveToPosition(position);

                                Cursor getPlaylistSongsCursor = songPopulator.getPlaylistSongsCursor(getContext(), playlistListCursor);

                                playlistListCursor.moveToPosition(position);

                                listNoAlbumArtAdapter playlistAdapter;

                                playlistAdapter = new listNoAlbumArtAdapter(getContext(), getPlaylistSongsCursor);
                                playlistSongList.setAdapter(playlistAdapter);

                                albumArtSpecialList.setVisibility(View.VISIBLE);

                                slideFromRight();

                            }
                        });

                        playlistSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                Cursor getSong = songPopulator.selectPlaylistSongCursor(getContext(), playlistListCursor, position);

                                //Setting current cursor
                                currentCursor = getSong;

                                //Eligible song position selector
                                mapping(currentCursor);

                                playButton.setBackgroundResource(R.drawable.pause);

                                nowPlaying.play(getContext(), getSong.getString(1), getSong.getString(4), getSong.getString(3), getSong.getString(2), getSong.getString(5));

                                nowPlayingSongName.setText(getSong.getString(3));
                                nowPlayingSongArtist.setText(getSong.getString(2));

                                slideUpDown();

                            }
                        });
                        break;

                }
            }


            return fragmentView;

        }
    }


    //Font Changer
    public static void changeFonts() {

//        nowPlayingSongName.setTypeface(echoPrimaryTypeFace);
//        nowPlayingSongArtist.setTypeface(echoPrimaryTypeFace);
//        songNameNowPlayingDescription.setTypeface(echoPrimaryTypeFace);
//        songArtistNowPlayingDescription.setTypeface(echoPrimaryTypeFace);


    }


    //Visualizer setup

    public static void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.
        player.setVolume(1, 1);
        songVisualizer = new Visualizer(player.getAudioSessionId());
        songVisualizer.setCaptureSize(1024);
        songVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {

                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {

//                        barViz.updateVisualizer(bytes);

                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, true);

    }


    //Context method
    public static Context getContext() {
        return applicationContext;
    }

    //SharedPreferences changes
    public void sharedPreferencesChanges() {

        //Repeat
        switch (repeat % 3) {
            case 0:
                Main.repeatButton.setImageDrawable(getResources().getDrawable(R.drawable.repeat));
                break;
            case 1:
                Main.repeatButton.setImageDrawable(getResources().getDrawable(R.drawable.repeat_selected));
                break;
            case 2:
                Main.repeatButton.setImageDrawable(getResources().getDrawable(R.drawable.repeat_one));
                break;
        }


        //Shuffle
        switch (shuffle % 2) {
            case 0:
                Main.shuffleButton.setImageDrawable(getResources().getDrawable(R.drawable.shuffle));
                break;
            case 1:
                Main.shuffleButton.setImageDrawable(getResources().getDrawable(R.drawable.shuffle_selected));
                break;
        }


        Main.songNameNowPlayingDescription.setText(lastSongData.getSongName());
        Main.songArtistNowPlayingDescription.setText(lastSongData.getSongArtist());
        Main.nowPlayingSongName.setText(lastSongData.getSongName());
        Main.nowPlayingSongArtist.setText(lastSongData.getSongArtist());
        try {
            albumArtNowPlayingCurtain.setImageURI(Uri.parse(songPopulator.getAlbumArt(getContext(), lastSongData.getSongAlbumId())));
        } catch (NullPointerException e) {
            e.printStackTrace();
            albumArtNowPlayingCurtain.setImageResource(R.drawable.echo_album_art);
        }


    }


    //Curtain View animation call
    public static void slideUpDown() {
        if (!isCurtainShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.curtain_up);

            nowPlayingCurtain.startAnimation(bottomUp);
            nowPlayingCurtain.setVisibility(View.VISIBLE);
        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(getContext(), R.anim.curtain_down);

            nowPlayingCurtain.startAnimation(bottomDown);
            nowPlayingCurtain.setVisibility(View.GONE);
        }
    }

    public static void slideFromRight() {
        if (!isSpecialShown()) {
            // Show the panel
            Animation rightIn = AnimationUtils.loadAnimation(Main.getContext(), R.anim.right_in);

            special.startAnimation(rightIn);
            special.setVisibility(View.VISIBLE);
        } else {
            // Hide the Panel

            Animation rightOut = AnimationUtils.loadAnimation(Main.getContext(), R.anim.right_out);

            special.startAnimation(rightOut);
            special.setVisibility(View.GONE);
        }
    }


    private static boolean isSpecialShown() {
        return special.getVisibility() == View.VISIBLE;
    }


    private static boolean isCurtainShown() {
        return nowPlayingCurtain.getVisibility() == View.VISIBLE;
    }


    //On Stop
    @Override
    protected void onStop() {

        super.onStop();

        //Putting in changes in shared Preferences
        sharedPreferencesEditor.putInt("repeat", repeat);
        sharedPreferencesEditor.putInt("shuffle", shuffle);
        sharedPreferencesEditor.putString("lastSongPath", lastSongData.getSongPath());
        sharedPreferencesEditor.putString("lastSongId", lastSongData.getSongId());
        sharedPreferencesEditor.putString("lastSongName", lastSongData.getSongName());
        sharedPreferencesEditor.putString("lastSongAlbumId", lastSongData.getSongAlbumId());
        sharedPreferencesEditor.putString("lastSongArtist", lastSongData.getSongArtist());


        //Committing shared preferences
        sharedPreferencesEditor.commit();

    }

    //Populating Hash Map
    public static void mapping(Cursor cursor) {

        map.clear();
        int cursorPosition = cursor.getPosition();
        cursor.moveToFirst();

        do {
            map.put(Integer.parseInt(cursor.getString(1)), cursor.getString(3));
            Log.d("MAPPING", cursor.getString(1) + " " + cursor.getString(3));
        } while (cursor.moveToNext());

        cursor.moveToPosition(cursorPosition);

    }

    public static void changeColor(int currentColor) {

        //Images color change
        changeDrawableColor(playPauseNowPlayingDescription.getBackground(), currentColor, 0.75f, 0.55f);
        changeDrawableColor(playButton.getBackground(), currentColor, 0.75f, 0.55f);
        changeDrawableColor(nextButton.getBackground(), currentColor, 0.8f, 0.8f);
        changeDrawableColor(previousButton.getBackground(), currentColor, 0.8f, 0.8f);


        //Text color change
        nowPlayingSongName.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.75f, 0.6f}));
//        songNameNowPlayingDescription.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.75f, 0.6f}));
        nowPlayingSongArtist.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.5f, 0.6f}));
        songArtistNowPlayingDescription.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.75f, 0.5f}));
        currentTimeMin.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.4f, 0.6f}));
        currentTimeSec.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.4f, 0.6f}));
        maxTimeMin.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.4f, 0.6f}));
        maxTimeSec.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.4f, 0.6f}));
        currentTimeSeprator.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.4f, 0.6f}));
        totalTimeSeprator.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.4f, 0.6f}));
        timeSlash.setTextColor(Color.HSVToColor(new float[]{currentColor, 0.4f, 0.6f}));

        //List color changes
        SlidingTabLayout.setSelectedIndicatorColors(Color.HSVToColor(new float[]{currentColor, 0.8f, 0.8f}));

        //Application morphing
        appBar.setBackgroundColor(Color.HSVToColor(new float[]{currentColor, 0.5f, 0.9f}));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.HSVToColor(new float[]{currentColor, 0.75f, 0.6f}));
        }

        //All songs Thumb
//        allSongsLV.setCacheColorHint(Color.HSVToColor(new float[]{currentColor, 0.75f, 0.6f}));


        //Visualization color
        barVisualization.barVizPaint.setColor(Color.HSVToColor(new float[]{currentColor, 0.6f, 0.8f}));
        barVisualization.barVizPaint.setAlpha(150);

        //Main marker color


        //Changing Template Color
        if (songArtistAllSongsTemplate == null)
            Log.d("songArtist all songs", "is NULL");
        else
            songArtistAllSongsTemplate.setTextColor(Color.HSVToColor(255, new float[]{currentColor, 0.6f, 0.8f}));


        //Gradient color change
        GradientDrawable gradDrawable = new GradientDrawable(GradientDrawable.Orientation.TR_BL, new int[]{Color.HSVToColor(new float[]{currentColor, 0.75f, 0.75f}), Color.HSVToColor(new float[]{currentColor, 0f, 1f})});
        gradDrawable.setCornerRadius(0.5f);
        nowPlayingCurtain.setBackground(gradDrawable);

        //SeekBar color change
        seekBar.getProgressDrawable().setColorFilter(Color.HSVToColor(new float[]{currentColor, 0.75f, 0.55f}), PorterDuff.Mode.MULTIPLY);


    }


    public static void changeDrawableColor(Drawable draw, int hue, float sat, float val) {

        int setColor = Color.HSVToColor(new float[]{hue, sat, val});
        draw.setColorFilter(setColor, PorterDuff.Mode.MULTIPLY);

    }


}

