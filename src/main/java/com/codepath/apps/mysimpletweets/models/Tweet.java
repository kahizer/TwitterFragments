package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by josevillanuva on 2/19/16.
 */


public class Tweet {

    private String body;
    private long uid;
    private User user;
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    /*{
        "coordinates": null,
        "truncated": false,
        "created_at": "Tue Aug 28 21:16:23 +0000 2012",
        "favorited": false,
        "id_str": "240558470661799936",
        "in_reply_to_user_id_str": null,
        "entities": {
            "urls": [],
            "hashtags": [],
            "user_mentions": []
        },
        "text": "just another test",
        "contributors": null,
        "id": 240558470661799936,
        "retweet_count": 0,
        "in_reply_to_status_id_str": null,
        "geo": null,
        "retweeted": false,
        "in_reply_to_user_id": null,
        "place": null,
        "source": "<a href="//realitytechnicians.com%5C%22" rel="\"nofollow\"">OAuth Dancer Reborn</a>",

    */

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}
