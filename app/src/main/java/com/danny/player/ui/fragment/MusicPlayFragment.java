package com.danny.player.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.danny.media.library.provider.MediaProviderFactory;
import com.danny.media.library.provider.MusicProvider;
import com.danny.media.library.model.Lrc;
import com.danny.media.library.model.Song;
import com.danny.media.library.service.PlayerService;
import com.danny.media.library.utils.LogUtil;
import com.danny.player.R;
import com.danny.player.adapter.PlayPagerAdapter;
import com.danny.player.application.PlayerApplication;
import com.danny.player.glide.PlayerGlide;
import com.danny.player.ui.widget.MusicAlbumCoverView;
import com.danny.player.ui.widget.MusicPlayControllerBar;
import com.tmall.ultraviewpager.UltraViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.wcy.lrcview.LrcView;

/**
 * Created by tingw on 2018/1/17.
 */
public class MusicPlayFragment extends BaseFragment implements PlayerService.IServiceUIRefreshListener<Song>, MusicPlayControllerBar.MusicPlayControllerBarListener {
    private final static String TAG = MusicPlayFragment.class.getSimpleName();

    @BindView(R.id.music_play_container_bg)
    ImageView containerBgView;
    @BindView(R.id.music_play_controller_bar)
    MusicPlayControllerBar musicPlayControllerBar;
    @BindView(R.id.music_play_viewpager)
    UltraViewPager musicPlayViewPager;

    private List<View> mViewPagerContent;
    private MusicAlbumCoverView albumCoverView;
    private LrcView mLrcViewSingle;

    private PlayerService<Song> playerService;

    @Override
    protected int getLayout() {
        return R.layout.fragment_music_info;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarBackStatue(true);

        initViewPager();
        initViewEvent();

        playerService = PlayerApplication.getApplication().getMusicPlayerService();
        playerService.setUIRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        Song song = playerService.getPlaySource();
        updateMusicInfo(song);
    }

    private void initViewPager(){
        PlayerGlide.loadLocalBlurImage(getContext(),R.mipmap.play_default_bg,4,3,containerBgView);

        View coverView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_play_page_cover, null);
        albumCoverView = coverView.findViewById(R.id.album_cover_view);
        mLrcViewSingle = coverView.findViewById(R.id.lrc_view_single);
        mViewPagerContent = new ArrayList<>(1);
        mViewPagerContent.add(coverView);

        musicPlayViewPager.initIndicator();
        musicPlayViewPager.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.GREEN)
                .setNormalColor(Color.WHITE)
                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        musicPlayViewPager.setAdapter(new PlayPagerAdapter(mViewPagerContent));
    }

    private void initViewEvent(){
        musicPlayControllerBar.setPlayControllerBarListener(this);
    }

    private void updateMusicInfo(Song song){
        if (song == null){
            return;
        }
        setToolBarTitle(song.getTitle());

        MusicProvider musicProvider = MediaProviderFactory.getInstance().getMusciProvideo(getContext());
        Lrc lrc = musicProvider.findLrcBySong(song);
        if (lrc != null){
            LogUtil.i(TAG,"lrc type : " + lrc.getType());
            if (Lrc.LrcType.LOCAL == lrc.getType()){
                File lrcFile = lrc.getLrcFile();
                if (lrcFile.exists()){
                    LogUtil.i(TAG,"lrc path : " + lrcFile.getPath());
                    mLrcViewSingle.loadLrc(lrcFile);
                }
            }
        }
        Bitmap albumBitmap = musicProvider.getAlbumImage(getContext(),song);
        if (albumBitmap == null){
            albumBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.default_artist);
        }
        albumCoverView.setCoverBitmap(albumBitmap);
        if (playerService.isPlaying()){
            albumCoverView.startAnimation();
        }else{
            albumCoverView.stopAnimation();
        }

        musicPlayControllerBar.updatePlaySource(song,playerService.getPlayProgress());
        musicPlayControllerBar.updatePlayStatue(playerService.isPlaying());
    }

    //MusicPlayControllerBarListener
    @Override
    public void onPlayOrPauseClick(boolean isPlay) {
        if (isPlay){
            playerService.resume();
            albumCoverView.startAnimation();
        }else{
            playerService.pause();
            albumCoverView.stopAnimation();
        }
    }

    @Override
    public void onNextClick() {
        playerService.next();
    }

    @Override
    public void onPrevClick() {
        playerService.prev();
    }

    //IServiceUIRefreshListener
    @Override
    public void onRefreshMusicList(List<Song> songList) {

    }

    @Override
    public void onPublish(Song song, int progress) {
        musicPlayControllerBar.updatePlayProgress(progress);
        if (mLrcViewSingle.hasLrc()){
            mLrcViewSingle.updateTime(progress);
        }
    }

    @Override
    public void onBufferingUpdate(Song song, int percent) {

    }

    @Override
    public void onMusicChange(Song song) {
        updateMusicInfo(song);
    }
}
