package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.Extras.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
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
public class HomeTimeLineFragment extends TweetsListFragment {

    private TwitterClient client;
    private long highestId = 0;
    private long lowestId = 0;
    private String callfrom = "";
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline(1, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);

                //Toast.makeText(getApplicationContext(), "refreshing", Toast.LENGTH_LONG).show();

                callfrom = "refresh";
                populateTimeline(highestId, 0);
            }
        });

        return v;
    }

    private void populateTimeline(long sinceId, long maxId) {
        try{
            client.getHometimeline(sinceId, maxId, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                    if(callfrom != "" && callfrom == "refresh"){
                        ArrayList<Tweet> refreshedTweets = Tweet.fromJSONArray(json);
                        for(int i = 0; i < refreshedTweets.size(); i++){
                            aTweets.insert(refreshedTweets.get(i), i);
                            if(refreshedTweets.get(i).getUid() > highestId){
                                highestId = refreshedTweets.get(i).getUid();
                            }
                        }

                        swipeContainer.setRefreshing(false);
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
        //Toast.makeText(getContext(), "loading more from home", Toast.LENGTH_LONG).show();
        populateTimeline(1, lowestId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCocde, Intent data){
        if(resultCocde == 20 && requestCode == 10){
            String status = data.getStringExtra("status");
            //Toast.makeText(this, status, Toast.LENGTH_LONG).show();

            client.postTweet(status, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json){
                 //Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                    Tweet tweet = Tweet.fromJSON(json);
                    //Log.w("my profile image url", tweet.getUser().getProfileImageUrl() );
                    aTweets.insert(tweet, 0);

                    if(tweet.getUid() > highestId){
                        highestId = tweet.getUid();
                    }
                 }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorRespond) {
                    //Toast.makeText(getApplicationContext(), "Failed to post status", Toast.LENGTH_LONG).show();
                }

            } );
        }
    }
}
