package com.joshrincon.selfmessagedestructor.app;

import android.app.Application;
import com.parse.Parse;

//this class is the entry point for an entire app

public class SelfDestructApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "JNtqKjM1ByFjHdMLPAwwg9fwJJKwm54Gxf2uz6Hu", "xdJPzmFSTb29tvxg9EdjbkLS4eMUZxUxaH8hQ24l");
    }

}
