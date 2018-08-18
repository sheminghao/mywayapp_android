package com.luck.picture.lib.ui;

import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.R;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.observable.ImagesObservable;
import com.luck.picture.lib.widget.PreviewViewPager;
import com.yalantis.ucrop.MultiUCrop;
import com.yalantis.ucrop.dialog.OptAnimationLoader;
import com.yalantis.ucrop.dialog.SweetAlertDialog;
import com.yalantis.ucrop.entity.EventEntity;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.rxbus2.RxBus;
import com.yalantis.ucrop.rxbus2.Subscribe;
import com.yalantis.ucrop.rxbus2.ThreadMode;
import com.yalantis.ucrop.util.LightStatusBarUtils;
import com.yalantis.ucrop.util.ToolbarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.ui
 * email：893855882@qq.com
 * data：16/12/31
 */
public class PicturePreviewActivity extends PictureBaseActivity implements View.OnClickListener, Animation.AnimationListener {
    private ImageView picture_left_back;
    private TextView tv_img_num, tv_title, tv_ok;
    private RelativeLayout select_bar_layout;
    private PreviewViewPager viewPager;
    private int position;
    private RelativeLayout rl_title;
    private LinearLayout ll_check;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();
    private TextView check;
    private SimpleFragmentAdapter adapter;
    private Animation animation;
    private boolean refresh;
    private SweetAlertDialog dialog;
    private SoundPool soundPool;//声明一个SoundPool
    private int soundID;//创建某个声音对应的音频ID

    //EventBus 3.0 回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventEntity obj) {
        switch (obj.what) {
            case FunctionConfig.CLOSE_FLAG:
                dismiss();
                closeActivity();
                break;
            case FunctionConfig.CLOSE_PREVIEW_FLAG:
                closeActivity();
                break;
        }
    }

    // 关闭activity
    protected void closeActivity() {
        finish();
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_image_preview);
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
        if (isImmersive) {
            LightStatusBarUtils.setLightStatusBar(this, false);
        }

        if (clickVideo) {
            if (soundPool == null) {
                soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
                soundID = soundPool.load(mContext, R.raw.music, 1);
            }
        }

        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        select_bar_layout = (RelativeLayout) findViewById(R.id.select_bar_layout);
        check = (TextView) findViewById(R.id.check);
        picture_left_back.setOnClickListener(this);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_img_num = (TextView) findViewById(R.id.tv_img_num);
        tv_title = (TextView) findViewById(R.id.picture_title);
        tv_ok.setOnClickListener(this);
        position = getIntent().getIntExtra(FunctionConfig.EXTRA_POSITION, 0);
        tv_ok.setTextColor(completeColor);
        if (isNumComplete) {
            tv_ok.setText(getString(R.string.picture_done));
        } else {
            tv_ok.setText(getString(R.string.picture_please_select));
        }
        select_bar_layout.setBackgroundColor(previewBottomBgColor);
        rl_title.setBackgroundColor(previewTopBgColor);
        ToolbarUtil.setColorNoTranslucent(this, previewTopBgColor);
        animation = OptAnimationLoader.loadAnimation(this, R.anim.modal_in);
        animation.setAnimationListener(this);
        boolean is_bottom_preview = getIntent().getBooleanExtra(FunctionConfig.EXTRA_BOTTOM_PREVIEW, false);
        if (is_bottom_preview) {
            // 底部预览按钮过来
            images = (List<LocalMedia>) getIntent().getSerializableExtra(FunctionConfig.EXTRA_PREVIEW_LIST);
        } else {
            images = ImagesObservable.getInstance().readLocalMedias();
        }

        if (is_checked_num) {
            tv_img_num.setBackgroundResource(cb_drawable);
            tv_img_num.setSelected(true);
        }

        selectImages = (List<LocalMedia>) getIntent().getSerializableExtra(FunctionConfig.EXTRA_PREVIEW_SELECT_LIST);

        initViewPageAdapterData();
        ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 刷新图片列表中图片状态
                boolean isChecked;
                if (!check.isSelected()) {
                    isChecked = true;
                    check.setSelected(true);
                    check.startAnimation(animation);
                } else {
                    isChecked = false;
                    check.setSelected(false);
                }
                if (selectImages.size() >= maxSelectNum && isChecked) {
                    Toast.makeText(PicturePreviewActivity.this, getString(R.string.picture_message_max_num, maxSelectNum), Toast.LENGTH_LONG).show();
                    check.setSelected(false);
                    return;
                }
                LocalMedia image = images.get(viewPager.getCurrentItem());
                if (isChecked) {
                    playSound();
                    selectImages.add(image);
                    image.setNum(selectImages.size());
                    if (is_checked_num) {
                        check.setText(image.getNum() + "");
                    }
                } else {
                    for (LocalMedia media : selectImages) {
                        if (media.getPath().equals(image.getPath())) {
                            selectImages.remove(media);
                            subSelectPosition();
                            notifyCheckChanged(media);
                            break;
                        }
                    }
                }
                onSelectNumChange(true);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_title.setText(position + 1 + "/" + images.size());
                if (is_checked_num) {
                    LocalMedia media = images.get(position);
                    check.setText(media.getNum() + "");
                    notifyCheckChanged(media);
                }
                onImageChecked(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initViewPageAdapterData() {
        tv_title.setText(position + 1 + "/" + images.size());
        adapter = new SimpleFragmentAdapter(getSupportFragmentManager());
        check.setBackgroundResource(cb_drawable);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        onSelectNumChange(false);
        onImageChecked(position);
        if (is_checked_num) {
            tv_img_num.setBackgroundResource(cb_drawable);
            tv_img_num.setSelected(true);
            LocalMedia media = images.get(position);
            check.setText(media.getNum() + "");
            notifyCheckChanged(media);
        }
    }

    /**
     * 选择按钮更新
     */
    private void notifyCheckChanged(LocalMedia imageBean) {
        if (is_checked_num) {
            check.setText("");
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(imageBean.getPath())) {
                    imageBean.setNum(media.getNum());
                    check.setText(String.valueOf(imageBean.getNum()));
                }
            }
        }
    }

    /**
     * 更新选择的顺序
     */
    private void subSelectPosition() {
        for (int index = 0, len = selectImages.size(); index < len; index++) {
            LocalMedia media = selectImages.get(index);
            media.setNum(index + 1);
        }
    }

    /**
     * 判断当前图片是否选中
     *
     * @param position
     */
    public void onImageChecked(int position) {
        if (images != null && images.size() > 0) {
            check.setSelected(isSelected(images.get(position)));
        } else {
            check.setSelected(false);
        }
    }

    /**
     * 当前图片是否选中
     *
     * @param image
     * @return
     */
    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新图片选择数量
     */

    public void onSelectNumChange(boolean isRefresh) {
        this.refresh = isRefresh;
        updateSelector(refresh);
        boolean enable = selectImages.size() != 0;
        if (enable) {
            tv_ok.setEnabled(true);
            if (isNumComplete) {
                tv_ok.setText(getString(R.string.picture_done_front_num, selectImages.size(), maxSelectNum));
            } else {
                tv_img_num.startAnimation(animation);
                tv_img_num.setVisibility(View.VISIBLE);
                tv_img_num.setText(selectImages.size() + "");
                tv_ok.setText(getString(R.string.picture_completed));
            }
        } else {
            tv_ok.setEnabled(false);
            if (isNumComplete) {
                tv_ok.setText(getString(R.string.picture_done));
            } else {
                tv_img_num.setVisibility(View.INVISIBLE);
                tv_ok.setText(getString(R.string.picture_please_select));
            }
        }
    }

    /**
     * 更新图片列表选中效果
     *
     * @param isRefresh
     */
    private void updateSelector(boolean isRefresh) {
        if (isRefresh) {
            EventEntity obj = new EventEntity(FunctionConfig.UPDATE_FLAG, selectImages);
            RxBus.getDefault().post(obj);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        updateSelector(refresh);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }


    public class SimpleFragmentAdapter extends FragmentPagerAdapter {

        public SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PictureImagePreviewFragment fragment = PictureImagePreviewFragment.getInstance(images.get(position).getPath(), true, "", selectImages);
            return fragment;
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.picture_left_back) {
            finish();
        } else if (id == R.id.tv_ok) {
            // 如果设置了图片最小选择数量，则判断是否满足条件
            int size = selectImages.size();
            if (minSelectNum > 0) {
                if (size < minSelectNum && selectMode == FunctionConfig.MODE_MULTIPLE) {
                    switch (type) {
                        case FunctionConfig.TYPE_IMAGE:
                            showToast(getString(R.string.picture_min_img_num, options.getMinSelectNum()));
                            return;
                        case FunctionConfig.TYPE_VIDEO:
                            showToast(getString(R.string.picture_min_video_num, options.getMinSelectNum()));
                            return;
                        default:
                            break;
                    }
                }
            }
            if (selectMode == FunctionConfig.MODE_MULTIPLE && enableCrop && type == FunctionConfig.TYPE_IMAGE) {
                // 是图片和选择压缩并且是多张，调用批量压缩
                startMultiCrop(selectImages);
            } else {
                onResult(selectImages);
                finish();
                Log.i("TAG", "------选择图片完成");
            }
        }
    }

    public void onResult(List<LocalMedia> images) {
        // 因为这里是单一实例的结果集，重新用变量接收一下在返回，不然会产生结果集被单一实例清空的问题
        List<LocalMedia> result = new ArrayList<>();
        for (LocalMedia media : images) {
            result.add(media);
        }
        EventEntity obj = new EventEntity(FunctionConfig.CROP_FLAG, result);
        RxBus.getDefault().post(obj);
        // 如果开启了压缩，先不关闭此页面，PictureImageGridActivity压缩完在通知关闭
        if (!isCompress) {
            finish();
            overridePendingTransition(0, R.anim.slide_bottom_out);
        } else {
            showPleaseDialog(getString(R.string.picture_please));
        }
    }

    /**
     * 多图裁剪
     *
     * @param medias
     */
    protected void startMultiCrop(List<LocalMedia> medias) {
        if (medias != null && medias.size() > 0) {
            LocalMedia media = medias.get(0);
            String path = media.getPath();
            // 去裁剪
            MultiUCrop uCrop = MultiUCrop.of(Uri.parse(path), Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".jpg")));
            MultiUCrop.Options options = new MultiUCrop.Options();
            if (offsetX > 0 && offsetY > 0) {
                options.withAspectRatio(offsetX, offsetY);
            } else {
                switch (copyMode) {
                    case FunctionConfig.CROP_MODEL_DEFAULT:
                        options.withAspectRatio(0, 0);
                        break;
                    case FunctionConfig.CROP_MODEL_1_1:
                        options.withAspectRatio(1, 1);
                        break;
                    case FunctionConfig.CROP_MODEL_3_2:
                        options.withAspectRatio(3, 2);
                        break;
                    case FunctionConfig.CROP_MODEL_3_4:
                        options.withAspectRatio(3, 4);
                        break;
                    case FunctionConfig.CROP_MODEL_16_9:
                        options.withAspectRatio(16, 9);
                        break;
                }
            }
            // 圆形裁剪
            if (circularCut) {
                options.setCircleDimmedLayer(true);// 是否为椭圆
                options.setShowCropFrame(false);// 外部矩形
                options.setShowCropGrid(false);// 内部网格
                options.withAspectRatio(1, 1);
            }
            options.setLocalMedia(medias);
            options.setFreeStyleCropEnabled(freeStyleCrop);
            options.setCompressionQuality(compressQuality);
            options.withMaxResultSize(cropW, cropH);
            options.background_color(backgroundColor);
            options.setIsCompress(isCompress);
            options.copyMode(copyMode);
            options.setLeftBackDrawable(leftDrawable);
            options.setCircularCut(circularCut);
            options.setTitleColor(title_color);
            options.setRightColor(right_color);
            options.setStatusBar(statusBar);
            options.setImmersiver(isImmersive);
            options.setRotateEnabled(rotateEnabled);
            options.setScaleEnabled(scaleEnabled);
            uCrop.withOptions(options);
            uCrop.start(PicturePreviewActivity.this);
        }

    }

    private void showPleaseDialog(String msg) {
        if (!isFinishing()) {
            dialog = new SweetAlertDialog(PicturePreviewActivity.this);
            dialog.setTitleText(msg);
            dialog.show();
        }
    }

    private void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this);
        }
        if (animation != null) {
            animation.cancel();
            animation = null;
        }

        if (soundPool != null) {
            soundPool.stop(soundID);
        }
    }


    /**
     * 播放点击声音
     */
    private void playSound() {
        if (clickVideo) {
            soundPool.play(
                    soundID,
                    0.1f,   //左耳道音量【0~1】
                    0.5f,   //右耳道音量【0~1】
                    0,     //播放优先级【0表示最低优先级】
                    1,     //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                    1     //播放速度【1是正常，范围从0~2】
            );
        }
    }
}
