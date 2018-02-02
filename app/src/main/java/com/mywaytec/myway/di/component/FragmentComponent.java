package com.mywaytec.myway.di.component;

import android.app.Activity;

import com.mywaytec.myway.di.module.FragmentModule;
import com.mywaytec.myway.di.scope.FragmentScope;
import com.mywaytec.myway.fragment.car.CarFragment;
import com.mywaytec.myway.fragment.chat.ChatFragment;
import com.mywaytec.myway.fragment.club.ClubFragment;
import com.mywaytec.myway.fragment.comment.CommentFragment;
import com.mywaytec.myway.fragment.dynamic.DynamicFragment;
import com.mywaytec.myway.fragment.praise.PraiseFragment;
import com.mywaytec.myway.fragment.way.WayFragment;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(CarFragment carFragment);

    void inject(DynamicFragment dynamicFragment);

    void inject(WayFragment wayFragment);

    void inject(CommentFragment commentFragment);

    void inject(PraiseFragment praiseFragment);

    void inject(ChatFragment chatFragment);

    void inject(ClubFragment clubFragment);
}
