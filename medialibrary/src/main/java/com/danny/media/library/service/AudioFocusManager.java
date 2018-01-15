package com.danny.media.library.service;

import android.content.Context;
import android.media.AudioManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by dannywang on 2018/1/15.
 */

public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {
    private static final AudioFocusManager ourInstance = new AudioFocusManager();

    private Observable<Boolean> observable;
    private ObservableEmitter<Boolean> audioFocusEmitter;

    public static AudioFocusManager getInstance() {
        return ourInstance;
    }

    private AudioFocusManager() {
        observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                audioFocusEmitter = emitter;
            }
        });
    }

    public Observable<Boolean> getObservable(){
        return observable;
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
        if (audioFocusEmitter != null){
            switch (focus){
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:{
                    //短暂失去焦点
                    audioFocusEmitter.onNext(false);
                    break;
                }
                case AudioManager.AUDIOFOCUS_GAIN:{
                    //获取焦点
                    audioFocusEmitter.onNext(true);
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS:{
                    //失去焦点
                    audioFocusEmitter.onNext(false);
                    break;
                }
            }

        }
    }
}
