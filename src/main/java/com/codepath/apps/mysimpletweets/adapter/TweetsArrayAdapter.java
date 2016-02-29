package com.codepath.apps.mysimpletweets.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * Created by josevillanuva on 2/19/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private Context mContext;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Tweet tweet = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfile);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent moreIntent=new Intent(getContext(),ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", tweet.getUser());

                moreIntent.putExtras(bundle);
                v.getContext().startActivity(moreIntent);
            }
        });


        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvRelativeDate = (TextView) convertView.findViewById(R.id.tvRelativeDate);


        String combinedUserScreenName = String.format("<B>" + tweet.getUser().getName() + "</B>" + " <font color=\"#D3D3D3\"> @" + tweet.getUser().getScreenName() + "</font>");
        tvUserName.setText(Html.fromHtml(combinedUserScreenName));

        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        try {
            tvRelativeDate.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public String getRelativeTimeAgo(String rawJsonDate) throws ParseException {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateMillis);
            //calendar.add(Calendar.HOUR_OF_DAY, -3);
            Date postDate = calendar.getTime();

            java.util.Date currentDateTime = new java.util.Date();

            long minutesElapsed = (currentDateTime.getTime() - postDate.getTime()) / 60000;
            int hours = (int) minutesElapsed / 60;

            if (hours < 1) {
                return "" + minutesElapsed + "m";
            } else if (hours < 24) {
                return "" + hours + "h";
            } else if (hours < 168) {
                int days = hours / 24;
                return "" + days + "d";
            } else {
                int weeks = hours / 168;
                return "" + weeks + "w";
            }
        } catch (Exception ex){

        }

        return "";
    }
}
