package com.danny.player.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.application.PlayerApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseAcivity {
    private final static String TAG = SplashActivity.class.getSimpleName();

    private final static int PERMISSION_REQUEST_CODE = 100;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private List<String> notGrantedPermissions = new ArrayList<>();

    @BindView(R.id.player_copyright)
    TextView copyrightText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        copyrightText.setText(getString(R.string.copyright,year));

        checkSelfPermission();
    }

    private void checkSelfPermission(){
        for (String permisson : permissions){
            int result = ActivityCompat.checkSelfPermission(this, permisson);
            if (result != PackageManager.PERMISSION_GRANTED){
                LogUtil.d(TAG,"permisson : " + permisson);
                notGrantedPermissions.add(permisson);
            }
        }

        LogUtil.d(TAG,"checkSelfPermission notGrantedPermissions : " + notGrantedPermissions.size());
        if (notGrantedPermissions.isEmpty()){
            startPlayerService();
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

            LogUtil.d(TAG,"onRequestPermissionsResult notGrantedPermissions : " + notGrantedPermissions.size());
            if (notGrantedPermissions.isEmpty()){
                startPlayerService();
            }else {
                finish();
            }
        }
    }

    private void startPlayerService(){
        LogUtil.d(TAG,"startPlayerService");

        MediaScannerConnection.scanFile(this,
                new String[] { Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Observable.just(1)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer aLong) throws Exception {
                                        PlayerApplication.getApplication().startMusicPlayerService();
                                        PlayerApplication.getApplication().startVideoPlayerService();
                                        startMusicMainActivity();
                                    }
                                });
                    }
                });




    }

    private void startMusicMainActivity(){
        LogUtil.d(TAG,"startMusicMainActivity");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
