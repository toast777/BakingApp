package com.chuck.android.bakingapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.URI;

import static android.support.constraint.Constraints.TAG;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListStepActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String APP_NAME = RecipeStepDetailFragment.class.getSimpleName();

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_DESCRIPTION = "item_description";
    public static final String ARG_ITEM_LONG_DESCRIPTION = "item_long_description";
    public static final String ARG_ITEM_VIDEO_URL = "item_video_url";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;



    /**
     * The dummy content this fragment is presenting.
     */
    private String id;
    private String description;
    private String longDescription;
    private String videoURL;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            id = getArguments().getString(ARG_ITEM_ID);
            description = getArguments().getString(ARG_ITEM_DESCRIPTION);
            longDescription = getArguments().getString(ARG_ITEM_LONG_DESCRIPTION);
            videoURL = getArguments().getString(ARG_ITEM_VIDEO_URL);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(description);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        mPlayerView = rootView.findViewById(R.id.recipe_detail_video_view);

        if (videoURL != null) {
            // 1. Create a default TrackSelector
            Handler mainHandler = new Handler();
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            SimpleExoPlayer player =
                    ExoPlayerFactory.newSimpleInstance(container.getContext(), trackSelector);

            // Bind the player to the view.
            mPlayerView.setPlayer(player);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(container.getContext(),
                    Util.getUserAgent(container.getContext(), "yourApplicationName"), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videoURL));
            // Prepare the player with the source.
            player.prepare(videoSource);
        }
        if (longDescription != null) {
            ((TextView)rootView.findViewById(R.id.recipe_detail_text)).setText(longDescription);
        }
        return rootView;
    }
    public void releaseVideo(){
        if (mExoPlayer != null)
        {
            mExoPlayer.stop();
            mExoPlayer.release();
            Log.i(TAG, "onDestroyView: Video Player Released");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseVideo();
    }
}