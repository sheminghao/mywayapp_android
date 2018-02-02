package com.mywaytec.myway.di.component;

import android.app.Activity;

import com.mywaytec.myway.di.module.ActivityModule;
import com.mywaytec.myway.di.scope.ActivityScope;
import com.mywaytec.myway.ui.about.AboutActivity;
import com.mywaytec.myway.ui.bindingCar.BindingCarActivity;
import com.mywaytec.myway.ui.blacklist.BlacklistActivity;
import com.mywaytec.myway.ui.bluetooth.BluetoothActivity;
import com.mywaytec.myway.ui.changePassword.ChangePasswordActivity;
import com.mywaytec.myway.ui.dynamicDetail.DynamicDetailActivity;
import com.mywaytec.myway.ui.fabuhuodong.FabuHuodongActivity;
import com.mywaytec.myway.ui.faultDetect.FaultDetectActivity;
import com.mywaytec.myway.ui.feedback.FeedbackActivity;
import com.mywaytec.myway.ui.fingerMark.FingerMarkActivity;
import com.mywaytec.myway.ui.firmwareUpdate.FirmwareUpdateActivity;
import com.mywaytec.myway.ui.forgetPassword.ForgetPasswordActivity;
import com.mywaytec.myway.ui.gprs.editFence.EditFenceActivity;
import com.mywaytec.myway.ui.gprs.electronicFence.ElectronicFenceActivity;
import com.mywaytec.myway.ui.gprs.vehicleLocation.VehicleLocationActivity;
import com.mywaytec.myway.ui.gprs.vehicleTrack.VehicleTrackActivity;
import com.mywaytec.myway.ui.gradeAndGold.GradeAndGoldActivity;
import com.mywaytec.myway.ui.huodongChengyuan.HuodongChengyuanActivity;
import com.mywaytec.myway.ui.huodongXiangqing.HuodongXiangqingActivity;
import com.mywaytec.myway.ui.huodongyueban.HuodongYuebanActivity;
import com.mywaytec.myway.ui.im.JoinClub.JoinClubActivity;
import com.mywaytec.myway.ui.im.clubMember.ClubMemberActivity;
import com.mywaytec.myway.ui.im.clubMember.ClubMemberView;
import com.mywaytec.myway.ui.im.clubVerify.ClubVerifyActivity;
import com.mywaytec.myway.ui.im.conversation.ConversationActivity;
import com.mywaytec.myway.ui.im.createClub.CreateClubActivity;
import com.mywaytec.myway.ui.im.exitClub.ExitClubActivity;
import com.mywaytec.myway.ui.im.modifyClub.ModifyClubActivity;
import com.mywaytec.myway.ui.im.searchClub.SearchClubActivity;
import com.mywaytec.myway.ui.location.LocationActivity;
import com.mywaytec.myway.ui.login.LoginActivity;
import com.mywaytec.myway.ui.lookoverAllCar.LookoverAllCarActivity;
import com.mywaytec.myway.ui.main.MainActivity;
import com.mywaytec.myway.ui.message.MessageActivity;
import com.mywaytec.myway.ui.messageDetail.MessageDetailActivity;
import com.mywaytec.myway.ui.moreCarInfo.MoreCarInfoActivity;
import com.mywaytec.myway.ui.mydynamic.MyDynamicActivity;
import com.mywaytec.myway.ui.myprofile.MyProfileActivity;
import com.mywaytec.myway.ui.mytrack.MyTrackActivity;
import com.mywaytec.myway.ui.peopleNearby.PeopleNearbyActivity;
import com.mywaytec.myway.ui.personalCenter.PersonalCenterActivity;
import com.mywaytec.myway.ui.publish.PublishActivity;
import com.mywaytec.myway.ui.rankingList.RankingListActivity;
import com.mywaytec.myway.ui.register.RegisterActivity;
import com.mywaytec.myway.ui.scFirmwareUpdate.ScFirmwareUpdateActivity;
import com.mywaytec.myway.ui.selectCountry.SelectCountryActivity;
import com.mywaytec.myway.ui.selectVenue.SelectVenueActivity;
import com.mywaytec.myway.ui.setting.SettingActivity;
import com.mywaytec.myway.ui.switchLanguage.SwitchLanguageActivity;
import com.mywaytec.myway.ui.switchUnit.SwitchUnitActivity;
import com.mywaytec.myway.ui.track.TrackRecordActivity;
import com.mywaytec.myway.ui.trackResult.TrackResultActivity;
import com.mywaytec.myway.ui.userDynamic.UserDynamicActivity;
import com.mywaytec.myway.ui.wayDetail.WayDetailActivity;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(TrackRecordActivity trackRecordActivity);

    void inject(PublishActivity publishActivity);

    void inject(LocationActivity locationActivity);

    void inject(WayDetailActivity wayDetailActivity);

    void inject(RegisterActivity registerActivity);

    void inject(ForgetPasswordActivity forgetPasswordActivity);

    void inject(MyProfileActivity myProfileActivity);

    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(MoreCarInfoActivity moreCarInfoActivity);

    void inject(BluetoothActivity bluetoothActivity);

    void inject(DynamicDetailActivity dynamicDetailActivity);

    void inject(FaultDetectActivity faultDetectActivity);

    void inject(MyDynamicActivity myDynamicActivity);

    void inject(MyTrackActivity myTrackActivity);

    void inject(TrackResultActivity trackResultActivity);

    void inject(MessageActivity messageActivity);

    void inject(RankingListActivity rankingListActivity);

    void inject(AboutActivity aboutActivity);

    void inject(MessageDetailActivity messageDetailActivity);

    void inject(FeedbackActivity feedbackActivity);

    void inject(FirmwareUpdateActivity firmwareUpdateActivity);

    void inject(ScFirmwareUpdateActivity scFirmwareUpdateActivity);

    void inject(SwitchLanguageActivity switchLanguageActivity);

    void inject(SwitchUnitActivity switchUnitActivity);

    void inject(BindingCarActivity bindingCarActivity);

    void inject(LookoverAllCarActivity lookoverAllCarActivity);

    void inject(BlacklistActivity blacklistActivity);

    void inject(PeopleNearbyActivity peopleNearbyActivity);

    void inject(UserDynamicActivity userDynamicActivity);

    void inject(SettingActivity settingActivity);

    void inject(GradeAndGoldActivity gradeAndGoldActivity);

    void inject(HuodongYuebanActivity huodongYuebanActivity);

    void inject(FabuHuodongActivity fabuHuodongActivity);

    void inject(HuodongXiangqingActivity huodongXiangqingActivity);

    void inject(HuodongChengyuanActivity huodongChengyuanActivity);

    void inject(FingerMarkActivity fingerMarkActivity);

    void inject(SelectVenueActivity selectVenueActivity);

    void inject(VehicleTrackActivity vehicleTrackActivity);

    void inject(VehicleLocationActivity vehicleLocationActivity);

    void inject(ElectronicFenceActivity electronicFenceActivity);

    void inject(EditFenceActivity editFenceActivity);

    void inject(SelectCountryActivity selectCountryActivity);

    void inject(ConversationActivity conversationActivity);

    void inject(CreateClubActivity createClubActivity);

    void inject(JoinClubActivity joinClubActivity);

    void inject(SearchClubActivity searchClubActivity);

    void inject(ExitClubActivity exitClubActivity);

    void inject(ClubVerifyActivity clubVerifyActivity);

    void inject(ClubMemberActivity clubMemberActivity);

    void inject(PersonalCenterActivity personalCenterActivity);

    void inject(ModifyClubActivity modifyClubActivity);
}
