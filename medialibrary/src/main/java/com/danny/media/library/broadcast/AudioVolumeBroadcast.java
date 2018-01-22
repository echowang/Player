package com.danny.media.library.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

/**
 * Created by tingw on 2018/1/22.
 * 监听系统AudioManager.STREAM_MUSIC的音量变化
 */

public class AudioVolumeBroadcast extends BroadcastReceiver {
    private final static String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private OnAudioVolumeChangeListener onAudioVolumeChangeListener;

    public static AudioVolumeBroadcast registerAudioVolumeBroadcast(Context context,OnAudioVolumeChangeListener onAudioVolumeChangeListener){
        if (context == null){
            return null;
        }
        AudioVolumeBroadcast audioVolumeBroadcast = new AudioVolumeBroadcast();
        audioVolumeBroadcast.onAudioVolumeChangeListener = onAudioVolumeChangeListener;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VOLUME_CHANGED_ACTION);
        context.registerReceiver(audioVolumeBroadcast,intentFilter);
        return audioVolumeBroadcast;
    }

    public static void unRegisterAudioVolumeBroadcast(Context context,AudioVolumeBroadcast audioVolumeBroadcast){
        if (context == null || audioVolumeBroadcast == null){
            return;
        }
        context.unregisterReceiver(audioVolumeBroadcast);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (VOLUME_CHANGED_ACTION.equalsIgnoreCase(action)){
            if (onAudioVolumeChangeListener != null){
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                onAudioVolumeChangeListener.onChange(volume);
            }
        }
    }

    public interface OnAudioVolumeChangeListener{
        void onChange(int volume);
    }
}
