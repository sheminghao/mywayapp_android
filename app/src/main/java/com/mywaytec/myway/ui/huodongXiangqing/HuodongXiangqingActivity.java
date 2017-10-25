package com.mywaytec.myway.ui.huodongXiangqing;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mywaytec.myway.R;
import com.mywaytec.myway.base.BaseActivity;
import com.mywaytec.myway.base.GlideImageLoader;
import com.mywaytec.myway.model.bean.NearbyActivityBean;
import com.mywaytec.myway.ui.huodongChengyuan.HuodongChengyuanActivity;
import com.mywaytec.myway.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import butterknife.BindView;
import butterknife.OnClick;

public class HuodongXiangqingActivity extends BaseActivity<HuodongXiangqingPresenter> implements HuodongXiangqingView{

    public static final String ACTIVITY_INFO = "activityInfo";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_activity_name)
    TextView tvActivityName;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_current_num)
    TextView tvCurrentNum;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.tv_canyu)
    TextView tvCanyu;
    @BindView(R.id.tv_signin)
    TextView tvSignin;

    private NearbyActivityBean.ObjBean activityInfo;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_huodong_xiangqing;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(R.string.activity_detail);
        activityInfo = (NearbyActivityBean.ObjBean) getIntent().getSerializableExtra(ACTIVITY_INFO);
        if (null != activityInfo) {
            tvActivityName.setText(activityInfo.getTitle());
            //TODO
            tvJifen.setText("+"+0);
            tvCurrentNum.setText(activityInfo.getCurrentNum()+"");
            tvTotalNum.setText("/"+activityInfo.getNum());
            tvTime.setText("· "+activityInfo.getStart()+"~"+activityInfo.getEnd());
            if (null != activityInfo.getLocation())
            tvAddress.setText("· "+activityInfo.getLocation().getProvince()+activityInfo.getLocation()
                    .getCity()+activityInfo.getLocation().getStreet());
            tvIntro.setText(activityInfo.getIntro());
            tvContact.setText(activityInfo.getContact());
            if (activityInfo.isParticipant()){
                tvSignin.setVisibility(View.VISIBLE);
                tvCanyu.setText(R.string.quit);
            }else {
                tvSignin.setVisibility(View.GONE);
                tvCanyu.setText(R.string.join_now);
            }
            if (activityInfo.isLanucher()){
                tvCanyu.setVisibility(View.GONE);
            }else {
                tvCanyu.setVisibility(View.VISIBLE);
            }
            if (activityInfo.isSign()){
                tvSignin.setText(R.string.signed_up_already_activity);
                tvSignin.setBackgroundResource(R.mipmap.yueban_btn_yiguoqi);
            }else {
                tvSignin.setText(R.string.sign_in_activity);
                tvSignin.setBackgroundResource(R.mipmap.yueban_btn_qiandao);
            }
        }

        //设置banner样式
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        if (null != activityInfo && null != activityInfo.getPhotos()) {
            banner.setImages(activityInfo.getPhotos());
        }
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Accordion);
        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    protected void updateViews() {

    }

    @OnClick({R.id.tv_canyu, R.id.layout_people_num, R.id.tv_signin})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_canyu://立即参与
                if (null != activityInfo) {
                    if (getResources().getString(R.string.join_now).equals(tvCanyu.getText().toString())) {
                        mPresenter.joinActivity(tvCanyu, tvSignin, activityInfo.getId() + "");
                    }else if (getResources().getString(R.string.quit).equals(tvCanyu.getText().toString())){
                        mPresenter.exitActivity(tvCanyu, tvSignin, activityInfo.getId() + "");
                    }
                }
                break;
            case R.id.layout_people_num://活动人数
                if (null != activityInfo) {
                    Intent intent = new Intent(HuodongXiangqingActivity.this, HuodongChengyuanActivity.class);
                    intent.putExtra("aid", activityInfo.getId()+"");
                    startActivity(intent);
                }
                break;
            case R.id.tv_signin://签到
                if (activityInfo != null) {
                    mPresenter.signin(activityInfo.getId()+"", tvSignin);
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
