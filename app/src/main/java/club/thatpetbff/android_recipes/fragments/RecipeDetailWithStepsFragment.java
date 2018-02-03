package club.thatpetbff.android_recipes.fragments;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import club.thatpetbff.android_recipes.R;
import club.thatpetbff.android_recipes.Step;

import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

public class RecipeDetailWithStepsFragment extends android.support.v4.app.Fragment implements VideoRendererEventListener {
        //implements VideoRendererEventListener {

    String position;
    TextView tvDetails;
    ImageView thumbnailView;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    Step step = null;
    Gson gson = new Gson();
    View bigView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();

        if(getArguments() != null) {
            position = getArguments().getString("step", null);
            step = gson.fromJson(position, Step.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        bigView = inflater.inflate(R.layout.fragment_recipe_detail_with_steps, parent, false);
        return bigView;

        // Inflate the xml file for the fragment
    }

    public void updateFragment(Step thisStep) {
        step = thisStep;
        // update view
        if (step != null) {
            tvDetails.setText(step.getDescription());
        }
        if (step.getThumbnailURL().length() > 0) {
            Picasso.with(getActivity().getApplicationContext()).load(step.getThumbnailURL()).into(thumbnailView);
        } else {
            if (thumbnailView != null)
                thumbnailView.setVisibility(View.GONE);
        }
        player=null;
        initializePlayer();
    }

    public void initializePlayer() {

        if (player != null) {
            return;
        }



        Uri mp4VideoUri = Uri.parse(step.getVideoURL());


        if (step != null && step.getVideoURL() != null && !step.getVideoURL().equals("")) {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
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
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

            player.prepare(loopingSource);


            player.setPlayWhenReady(true); //run file/link when ready to play.


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
                    player.stop();
                    player.prepare(loopingSource);
                    player.setPlayWhenReady(true);
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
            simpleExoPlayerView.setUseController(true);
            simpleExoPlayerView.requestFocus();

            simpleExoPlayerView.setPlayer(player);
        } else {
            simpleExoPlayerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set values for view here
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(getActivity());
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        System.out.println("Look ma, it's a view!");
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tvDetails = (TextView) view.findViewById(R.id.tvDetails);
            thumbnailView = (ImageView) view.findViewById(R.id.thumbnailStepsView);
            if (step == null && savedInstanceState != null && savedInstanceState.getString("step") != null) {
                step = gson.fromJson(savedInstanceState.getString("step"), Step.class);
            }
            // update view
            if(step != null) {
                tvDetails.setText(step.getDescription());
                if(step.getThumbnailURL().length() > 0) {
                    Picasso.with(getActivity().getApplicationContext()).load(step.getThumbnailURL()).into(thumbnailView);
                } else {
                    if(thumbnailView != null)
                        thumbnailView.setVisibility(View.GONE);
                }
            }
        } else {
            tvDetails = (TextView) view.findViewById(R.id.tvDetails);
            if(tvDetails != null && step != null) {
                tvDetails.setText(step.getDescription());
            }
            if(step.getThumbnailURL().length() > 0) {
                Picasso.with(getActivity().getApplicationContext()).load(step.getThumbnailURL()).into(thumbnailView);
            } else {
                if(thumbnailView != null)
                    thumbnailView.setVisibility(View.GONE);
            }
        }
        initializePlayer();
        System.out.println("Whatsapp");
    }



    /*
    // Activity is calling this to update view on Fragment
    public void updateView(int position){

        tvDetails.setText(step.getDescription());
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
    */

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        if(step != null) {
            savedInstanceState.putString("step", gson.toJson(step));
        }
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
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.v(TAG, "onPause()...");
        if(player != null) {
            player.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        if(player != null) {
            player.release();
        }
    }

}
