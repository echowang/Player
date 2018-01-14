package com.danny.media.library.service;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by dannywang on 2018/1/15.
 */

public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {
    private static final AudioFocusManager ourInstance = new AudioFocusManager();

    static AudioFocusManager getInstance() {
        return ourInstance;
    }

    private AudioFocusManager() {
    }

    public void requestAudioFocus(Context context){
        if (context == null){
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
    }

    public void abanodAudioFocus(Context context){
        if (context == null){
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focus) {

    }
}
