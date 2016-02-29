package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFraagment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;
public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    String screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();

        try{
            Bundle b = this.getIntent().getExtras();
            if(b != null){
                user = b.getParcelable("user");
                screenName = user.getScreenName();
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }else {
                client.getUserInfo(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        user = User.fromJSON(response);
                        getSupportActionBar().setTitle("@" + user.getScreenName());
                        populateProfileHeader(user);
                    }
                });
            }
        }
        catch (Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
            Log.w("error", ex.getMessage().toString());
        }


        if(savedInstanceState == null){
            UserTimelineFraagment fragmentUserTimeline = UserTimelineFraagment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvFullName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollower = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());

        String followers = String.format("<B>" + user.getFollowersCount() + "</B>" + " <font color=\"#D3D3D3\"> FOLLOWERS </font>");
        tvFollower.setText(Html.fromHtml(followers));

        String following = String.format("<B>" + user.getFollowingsCount() + "</B>" + " <font color=\"#D3D3D3\"> FOLLOWING </font>");
        tvFollowing.setText(Html.fromHtml(following));

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

    }
}
