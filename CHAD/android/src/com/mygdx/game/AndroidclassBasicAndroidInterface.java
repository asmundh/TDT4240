package com.mygdx.game;

import android.util.Log;

public class AndroidclassBasicAndroidInterface implements AndroidInterface {

    AndroidLauncher mAndroidLauncher = null;

    public AndroidclassBasicAndroidInterface(AndroidLauncher androidLauncher){
        this.mAndroidLauncher = androidLauncher;
    }

    public String getMessage(){
        return "Hello from android module.";
    }

    public void changeView(){
        mAndroidLauncher.changeView();
    }

    public String getPlayerId(){
        return mAndroidLauncher.getPlayerId();
    }

    public String getDisplayname(){
        return mAndroidLauncher.getDisplayName();
    }
}
