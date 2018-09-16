package com.rangerthings.bakingapp.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.rangerthings.bakingapp.DB.SharedViewModel;
import com.rangerthings.bakingapp.Model.Step;
import com.rangerthings.bakingapp.R;
import com.rangerthings.bakingapp.Utils.ApiUtils;


public class DetailFragment extends Fragment {

    private SharedViewModel model;

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = false;
    private Uri uri;
    private BottomNavigationView bottomNavigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //get reference to activity viewmodel (same instance as initial detail activity)
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        //grab the current current Step from the viewmodel (set in the StepAdapter onclick)
        Step currentStep = model.getCurrentStep();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //if it is a tablet inflate the bottom nav
        if (!model.getIsTablet()) {
            bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }


        //get a reference to various views
        playerView = view.findViewById(R.id.pv_exoplayer);
        TextView tvDescription = view.findViewById(R.id.tv_stepDesc);

        //set the text box with step description
        tvDescription.setText(currentStep.getDescription());

        //Build a exoplayer using the currentSteps pulled from the currentRecipe in the viewmodel
        //if one of the two video sources are not blank...
        Boolean bothBlank;
        if (!currentStep.getThumbnailURL().equals("") || !currentStep.getVideoURL().equals("")) {
            bothBlank = false;

            if (!currentStep.getVideoURL().equals("")) {
                //get the video url from this method if it is not blank

                uri = Uri.parse(currentStep.getVideoURL());
                if (ApiUtils.isOnline(getContext())) {
                    initializePlayer(uri);
                }
                else {
                    playerView.setVisibility(View.GONE);
                }

            } else if (!currentStep.getThumbnailURL().equals("")) {
                //or get the video url from this method if it is not blank

                if (ApiUtils.isOnline(getContext())) {
                    initializePlayer(uri);
                }
                else {
                    playerView.setVisibility(View.GONE);
                }
            }
        } else {
            //Both video urls were blank
            Log.d("DetailFrag:", "both videos are blank");
            bothBlank = true;
            playerView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onStop() {
        releasePlayer();
        showSystemUI();
        super.onStop();

    }

    @Override
    public void onDestroy() {
        showSystemUI();
        releasePlayer();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //start the exoplayer
        if (ApiUtils.isOnline(getContext())) {
            initializePlayer(uri);
        }
        else {
            playerView.setVisibility(View.GONE);
        }
    }


    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    //modified from google codelabs
    private void initializePlayer(Uri uri) {
        //check the current orientation, and set it in model
        int currentOrientation = getResources().getConfiguration().orientation;
        setCurrentOrientation(currentOrientation);

        if (model.getOrientation().equals("landscape") && !model.getIsTablet()) {
            hideSystemUi();
        }

        //instance of a player with the default selector and control
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        //place player in the playerview
        playerView.setPlayer(player);

        //player is currently not set to autoplay
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        //build te source
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-detail")).
                createMediaSource(uri);
    }

    //Called to release the exoplayer resource and free up system space
    private void releasePlayer() {
        if (player != null) {
            Log.d("Release player", " fired.");
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private void hideSystemUi() {
        //hide the bottom bar
        bottomNavigationView.setVisibility(View.GONE);
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        //show the bottom bar

        Log.d("DetailFrag:", "showSystemUI fired.");
        if (!model.getIsTablet()) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void setCurrentOrientation(int currentOrientation) {

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            model.setOrientation("landscape");
        } else {
            // Portrait
            model.setOrientation("portrait");
        }
    }


}
