package com.danny.player.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.danny.media.library.service.MusicPlayerService;
import com.danny.player.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseAcivity {
    private final static String TAG = SplashActivity.class.getSimpleName();

    private final static int PERMISSION_REQUEST_CODE = 100;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private List<String> notGrantedPermissions = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        checkSelfPermission();
    }

    private void checkSelfPermission(){
        for (String permisson : permissions){
            int result = ActivityCompat.checkSelfPermission(this, permisson);
            if (result != PackageManager.PERMISSION_GRANTED){
                Log.d(TAG,"permisson : " + permisson);
                notGrantedPermissions.add(permisson);
            }
        }

        Log.d(TAG,"checkSelfPermission notGrantedPermissions : " + notGrantedPermissions.size());
        if (notGrantedPermissions.isEmpty()){
            startMusicService();
            startMusicMainActivity();
        }else{
            ActivityCompat.requestPermissions(this,notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE){
            for (int i = 0; i < permissions.length; i++){
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED){
                    if (notGrantedPermissions.contains(permission)){
                        notGrantedPermissions.remove(permission);
                    }
                }
            }

            Log.d(TAG,"onRequestPermissionsResult notGrantedPermissions : " + notGrantedPermissions.size());
            if (notGrantedPermissions.isEmpty()){
                startMusicService();
                startMusicMainActivity();
            }else {
                finish();
            }
        }
    }

    private void startMusicService(){
        Log.d(TAG,"startMusicMainActivity");
        Intent intent = new Intent(this, MusicPlayerService.class);
        startService(intent);
    }

    private void startMusicMainActivity(){
        Log.d(TAG,"startMusicMainActivity");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
