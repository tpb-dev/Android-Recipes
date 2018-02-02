package club.thatpetbff.android_recipes.fragments;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import club.thatpetbff.android_recipes.App;
import club.thatpetbff.android_recipes.DetailActivity;
import club.thatpetbff.android_recipes.R;
import club.thatpetbff.android_recipes.Recipe;
import club.thatpetbff.android_recipes.Step;

import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

public class RecipeDetailFragment extends android.support.v4.app.Fragment implements  View.OnClickListener, VideoRendererEventListener {

    TextView tvDetails;
    ImageView thumbnailView;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    Step step = null;
    Gson gson = new Gson();
    View bigView;
    int pos;
    boolean playWhenReady;
    Long position;
    Uri mp4VideoUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();

        if(savedInstanceState == null){
            // Get back arguments
            if(getArguments() != null) {
                position = getArguments().getLong("position", C.TIME_UNSET);
                step = gson.fromJson(getArguments().getString("step"), Step.class);
                pos = getArguments().getInt("pos", -1);
            }

        } else {
            if(savedInstanceState.get("step") != null) {
                step = gson.fromJson(savedInstanceState.getString("step"), Step.class);
                position = savedInstanceState.getLong("position",  C.TIME_UNSET);
                playWhenReady = savedInstanceState.getBoolean("playWhenReady", false);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        int theCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        System.out.println("The count = " + theCount);

        String name = getActivity().getSupportFragmentManager().getBackStackEntryAt(theCount-2).getName();
        String name1 = getActivity().getSupportFragmentManager().getBackStackEntryAt(theCount-1).getName();
        System.out.println("Name1: " + name1);
        System.out.println("Name: " + name);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && step != null && step.getVideoURL() != null && !step.getVideoURL().equals("")) {
            //getActivity().setContentView(R.layout.activity_detail_full_screen_video);
//            Intent intent = new Intent(getContext(),
//                    FullScreenVideoActivity.class);
//            getContext().startActivity(intent);
            bigView = inflater.inflate(R.layout.fragment_full_screen_video, parent, false);
            return bigView;
        } else {
            // Get a support ActionBar corresponding to this toolbar
            ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();

            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);


            bigView = inflater.inflate(R.layout.fragment_recipe_detail, parent, false);
            Button backButton = (Button) bigView.findViewById(R.id.backButton);
            if(backButton != null)
                backButton.setOnClickListener(this);
            Button nextButton = (Button) bigView.findViewById(R.id.nextButton);
            if(nextButton != null)
                nextButton.setOnClickListener(this);
            return bigView;
        }


        // Inflate the xml file for the fragment
   }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set values for view here
        System.out.println("Look ma, it's a view!");
        bigView = view;
        if (step == null && savedInstanceState != null && savedInstanceState.getString("step") != null) {
            step = gson.fromJson(savedInstanceState.getString("step"), Step.class);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tvDetails = (TextView) view.findViewById(R.id.tvDetails);
            thumbnailView = (ImageView) view.findViewById(R.id.thumbnailView);

            // update view
            if(step != null) {
                tvDetails.setText(step.getDescription());
                if(step.getThumbnailURL().length() > 0) {
                    Picasso.with(getContext()).load(step.getThumbnailURL()).into(thumbnailView);
                    System.out.println("Sounds good");
                } else {
                    if(thumbnailView != null)
                        thumbnailView.setVisibility(View.GONE);
                }
            }
        } else {
            View flContainerView2 = getActivity().findViewById(R.id.flContainer2);
            flContainerView2.setVisibility(View.GONE);
            View detailToolbar = getActivity().findViewById(R.id.detail_toolbar);
            detailToolbar.setVisibility(View.GONE);
            View flContainerView = getActivity().findViewById(R.id.flContainer);
            flContainerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            System.out.println("Yayayay");
        }

        if(simpleExoPlayerView == null){
            simpleExoPlayerView = new SimpleExoPlayerView(getActivity());
            simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        }
        /*
        else {


            }

        } */

        initializePlayer();





//for listening to resolution change and  outputing the resolution
        //player.setVideoDebugListener(getActivity());

    }

    // Activity is calling this to update view on Fragment
    public void updateView(int position){

        tvDetails.setText(step.getDescription());

        if(step.getThumbnailURL().length() > 0) {
            Picasso.with(bigView.getContext()).load(step.getThumbnailURL()).into(thumbnailView);
        } else {
            if(thumbnailView != null)
                thumbnailView.setVisibility(View.GONE);
        }

        if(step.getVideoURL() != null && !step.getVideoURL().equals("")) {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            Uri mp4VideoUri = Uri.parse(step.getVideoURL());

            //Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//Produces DataSource instances through which media data is loaded.
            DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(
                    Util.getUserAgent(getActivity(), "your-user-agent"));
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource)
                    .createMediaSource(mp4VideoUri, null, null);
            final LoopingMediaSource loopingSource;
            loopingSource = new LoopingMediaSource(mediaSource);

            player.prepare(loopingSource);


            player.setPlayWhenReady(true); //run file/link when ready to play.
        } else {
           simpleExoPlayerView.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        if(step != null) {
            savedInstanceState.putString("step", gson.toJson(step));
            savedInstanceState.putBoolean("playWhenReady", playWhenReady);
            savedInstanceState.putLong("position", position);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initializePlayer() {
        if(player!=null){
            return;
        }
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        simpleExoPlayerView.setPlayer(player);

//        Uri mp4VideoUri = Uri.parse(step.getVideoURL());


        if (step != null && step.getVideoURL() != null && !step.getVideoURL().equals("")) {
            mp4VideoUri = Uri.parse(step.getVideoURL());

            //Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//Produces DataSource instances through which media data is loaded.
            DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(
                    Util.getUserAgent(getActivity(), "your-user-agent"));
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource)
                    .createMediaSource(mp4VideoUri, null, null);
            final LoopingMediaSource loopingSource;
            loopingSource = new LoopingMediaSource(mediaSource);

            if (position != C.TIME_UNSET) {
                player.seekTo(position);
            }

            player.prepare(loopingSource);


            player.setPlayWhenReady(playWhenReady); //run file/link when ready to play.

            player.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    Log.v(TAG, "Listener-onTimelineChanged...");
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    Log.v(TAG, "Listener-onTracksChanged...");
                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Log.v(TAG, "Listener-onLoadingChanged...isLoading:" + isLoading);
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    Log.v(TAG, "Listener-onRepeatModeChanged...");
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.v(TAG, "Listener-onPlayerError...");
                    if(player != null) {
                        player.stop();
                        player.prepare(loopingSource);
                        player.setPlayWhenReady(true);
                    }
                }

                @Override
                public void onPositionDiscontinuity(int n) {
                    Log.v(TAG, "Listener-onPositionDiscontinuity...");
                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                    Log.v(TAG, "Listener-onPlaybackParametersChanged...");
                }

                @Override
                public void onSeekProcessed() {
                    return;
                }

                @Override
                public void onShuffleModeEnabledChanged(boolean b) {
                    return;
                }
            });

            player.setPlayWhenReady(true); //run file/link when ready to play.
        }
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        Recipe recipe = ((DetailActivity)getActivity()).getRecipe();
        App app = (App)((DetailActivity)getActivity()).getApplication();
        recipe.__setDaoSession(app.getDaoSession());
        List<Step> steps = recipe.getSteps();
        System.out.println("qqqqqqqqqqqqqqqqq");
        switch (v.getId()) {
            case R.id.nextButton:
                pos += 1;
                if (pos >= steps.size()) {
                    pos = steps.size() - 1;
                }
                System.out.println("Hello!!!");
                break;
            case R.id.backButton:
                pos -= 1;
                if (pos < 0) {
                    pos = 0;

                }
                System.out.println("Byte!!!!!!");
                break;
        }
        step = steps.get(pos);
        updateView(pos);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(TAG, "onVideoSizeChanged ["  + " width: " + width + " height: " + height + "]");

    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }




//-------------------------------------------------------ANDROID LIFECYCLE---------------------------------------------------------------------------------------------

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()...");
        if(player != null) {
            player.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()...");
        if (mp4VideoUri != null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()...");
        if (player != null) {
            position = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        if(player != null)
            player.release();
    }

}
