package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.HomeTimeLineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelinefragment;

//import com.activeandroid.util.Log;

public class timelineActivity extends AppCompatActivity {

//    private TweetsListFragment fragmentTweets;
//    private TwitterClient client;


//    private ArrayList<Tweet> tweets;
//    private TweetsArrayAdapter aTweets;
//    private ListView lvTweets;
//    private long highestId = 0;
//    private long lowestId = 0;
//    private SwipeRefreshLayout swipeContainer;
//    private String callfrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

//        if(savedInstanceState == null){
//            fragmentTweets = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
//        }

//        lvTweets = (ListView) findViewById(R.id.lvTweets);
//        tweets = new ArrayList<>();
//        aTweets = new TweetsArrayAdapter(this, tweets);
//        lvTweets.setAdapter(aTweets);
//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to your AdapterView
//                customLoadMoreDataFromApi(page);
//                // or customLoadMoreDataFromApi(totalItemsCount);
//                return true; // ONLY if more data is actually being loaded; false otherwise.
//            }
//        });

//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                //fetchTimelineAsync(0);
//
//                //Toast.makeText(getApplicationContext(), "refreshing", Toast.LENGTH_LONG).show();
//
//                callfrom = "refresh";
//                populateTimeline(highestId, 0);
//            }
//        });

//        client = TwitterApplication.getRestClient();
//        populateTimeline(1, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.compose, menu);
        inflater.inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()){
                case R.id.action_compose:
                    Toast.makeText(this, "composing..", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(timelineActivity.this, ComposeActivity.class);
                    //startActivityForResult(i, 10);
                    startActivity(i);
                    return true;
            }


        }
        catch (Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new HomeTimeLineFragment();
            } else if(position == 1){
                return new MentionsTimelinefragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCocde, Intent data){
//        if(resultCocde == 20 && requestCode == 10){
//            String status = data.getStringExtra("status");
//            //Toast.makeText(this, status, Toast.LENGTH_LONG).show();
//
//            client.postTweet(status, new JsonHttpResponseHandler(){
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject json){
//                 //Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
//                    Tweet tweet = Tweet.fromJSON(json);
//                    Log.w("my profile image url", tweet.getUser().getProfileImageUrl() );
//                    aTweets.insert(tweet, 0);
//
//                    if(tweet.getUid() > highestId){
//                        highestId = tweet.getUid();
//                    }
//                 }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorRespond) {
//                    Toast.makeText(getApplicationContext(), "Failed to post status", Toast.LENGTH_LONG).show();
//                }
//
//            } );
//        }
//    }

//    private void customLoadMoreDataFromApi(int page) {
//        populateTimeline(1, lowestId);
//    }

//    private void populateTimeline(long sinceId, long maxId) {
//        try{
////            client.getHometimeline(new JsonHttpResponseHandler(){
//            client.getHometimeline(sinceId, maxId, new JsonHttpResponseHandler(){
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//
//                    if(callfrom != "" && callfrom == "refresh"){
//                        ArrayList<Tweet> refreshedTweets = Tweet.fromJSONArray(json);
//                        for(int i = 0; i < refreshedTweets.size(); i++){
//                            aTweets.insert(refreshedTweets.get(i), i);
//
//                            if(refreshedTweets.get(i).getUid() > highestId){
//                                highestId = refreshedTweets.get(i).getUid();
//                            }
//                        }
//
//                        swipeContainer.setRefreshing(false);
//                        callfrom = "";
//                    }else {
//                        fragmentTweets.addAll(Tweet.fromJSONArray(json));
//
//                        highestId = tweets.get(0).getUid();
//                        lowestId = tweets.get(tweets.size() - 1).getUid();
//                    }
//
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorRespond) {
//                    Log.d("DEBUG", errorRespond.toString());
//                    Toast.makeText(getApplicationContext(), "failed to load", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//        catch (Exception ex){
//            Log.d("exception", ex.toString());
//        }
//    }
}
