package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by josevillanuva on 2/27/16.
 */
public class UserTimelineFraagment extends TweetsListFragment {
    private TwitterClient client;
    private long highestId = 0;
    private long lowestId = 0;
//    private SwipeRefreshLayout swipeContainer;
    private String callfrom = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline(1, 0);
    }

    public static UserTimelineFraagment newInstance(String screen_name) {
        UserTimelineFraagment userFragment = new UserTimelineFraagment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    private void populateTimeline(long sinceId, long maxId) {
        try {
            String screeName = getArguments().getString("screen_name");
            client.getUserTimeline(sinceId, maxId, screeName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                    //addAll(Tweet.fromJSONArray(json));

                    if(callfrom != "" && callfrom == "refresh"){
                        ArrayList<Tweet> refreshedTweets = Tweet.fromJSONArray(json);
                        for(int i = 0; i < refreshedTweets.size(); i++){
                            aTweets.insert(refreshedTweets.get(i), i);
                            if(refreshedTweets.get(i).getUid() > highestId){
                                highestId = refreshedTweets.get(i).getUid();
                            }
                        }

                        //swipeContainer.setRefreshing(false);
                        callfrom = "";
                    }else {
                        addAll(Tweet.fromJSONArray(json));

                        highestId = tweets.get(0).getUid();
                        lowestId = tweets.get(tweets.size() - 1).getUid();
                    }
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorRespond) {
                    Log.d("DEBUG", errorRespond.toString());
                    //Toast.makeText(getApplicationContext(), "failed to load", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception ex){
            Log.d("exception", ex.toString());
        }
    }

    @Override
    public void customLoadMoreDataFromApi(int page) {
        //Toast.makeText(getContext(), "loading more from user", Toast.LENGTH_LONG).show();
        populateTimeline(1, lowestId);
    }
}
