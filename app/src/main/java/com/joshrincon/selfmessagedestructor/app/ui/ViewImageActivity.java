package com.joshrincon.selfmessagedestructor.app.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import com.joshrincon.selfmessagedestructor.app.R;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;


public class ViewImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Uri imageUri = getIntent().getData();

        // take picasso with this context and load the image into this imageview
        Picasso.with(this).load(imageUri.toString()).into(imageView);

        // set to view the image for only 10 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // delete the emssage
                finish();
            }
        }, 10000);

    }
}
