package com.mywaytec.myway.model.http;


import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.AllBindingCarBean;
import com.mywaytec.myway.model.bean.AllPeopleBean;
import com.mywaytec.myway.model.bean.BlacklistBean;
import com.mywaytec.myway.model.bean.ChangeHeadImgBean;
import com.mywaytec.myway.model.bean.ClubDetailBean;
import com.mywaytec.myway.model.bean.ClubJoinListBean;
import com.mywaytec.myway.model.bean.ClubMessageBean;
import com.mywaytec.myway.model.bean.CommentListBean;
import com.mywaytec.myway.model.bean.DayRankingBean;
import com.mywaytec.myway.model.bean.DeleteClubUsersBean;
import com.mywaytec.myway.model.bean.DianziweilanBean;
import com.mywaytec.myway.model.bean.DynamicDetailBean;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.FenceWarningBean;
import com.mywaytec.myway.model.bean.FirmwareInfo;
import com.mywaytec.myway.model.bean.GoldInfoBean;
import com.mywaytec.myway.model.bean.LikeListBean;
import com.mywaytec.myway.model.bean.LoginInfo;
import com.mywaytec.myway.model.bean.MessageBean;
import com.mywaytec.myway.model.bean.MyAttentionBean;
import com.mywaytec.myway.model.bean.MyFansBean;
import com.mywaytec.myway.model.bean.NearbyActivityBean;
import com.mywaytec.myway.model.bean.NearbyBean;
import com.mywaytec.myway.model.bean.ObjBooleanBean;
import com.mywaytec.myway.model.bean.ObjIntBean;
import com.mywaytec.myway.model.bean.ObjStringBean;
import com.mywaytec.myway.model.bean.OtherMsgBean;
import com.mywaytec.myway.model.bean.PraiseBean;
import com.mywaytec.myway.model.bean.PublicRoutePathsBean;
import com.mywaytec.myway.model.bean.PublishBean;
import com.mywaytec.myway.model.bean.RongClubListBean;
import com.mywaytec.myway.model.bean.RongTokenBean;
import com.mywaytec.myway.model.bean.RouteDetailBean;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.model.bean.RoutePathsListBean;
import com.mywaytec.myway.model.bean.SearchCarBean;
import com.mywaytec.myway.model.bean.SearchClubBean;
import com.mywaytec.myway.model.bean.UploadRouteBean;
import com.mywaytec.myway.model.bean.UsedCarBean;
import com.mywaytec.myway.model.bean.UserInfo;
import com.mywaytec.myway.model.bean.VehicleLastLocationBean;
import com.mywaytec.myway.model.bean.VehicleTrackBean;
import com.mywaytec.myway.model.bean.VersionBean;
import com.mywaytec.myway.model.http.api.LongchuangApis;
import com.mywaytec.myway.model.http.api.MywayApis;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by codeest on 2016/8/3.
 */
public class RetrofitHelper {

    private MywayApis mywayService;
    private LongchuangApis longchuangApis;

    public RetrofitHelper(MywayApis mywayService, LongchuangApis longchuangApis) {
        this.mywayService = mywayService;
        this.longchuangApis = longchuangApis;
    }

    /**
     * 注册
     * @param phoneNumber
     * @param password
     * @return
     */
    public Observable<BaseInfo> registerInfo(String phoneNumber, String countryCode, String password,
                                             String vCode){
        return mywayService.getRegisterInfo(phoneNumber, countryCode,password, vCode);
    }

    /**
     * 登录
     * @param phoneNumber
     * @param password
     * @return
     */
    public Observable<LoginInfo> loginInfo(String phoneNumber, String password){
        return mywayService.getLoginInfo(phoneNumber, password);
    }

    /**
     * Token登录
     * @param uid
     * @param token
     * @return
     */
    public Observable<LoginInfo> tokenLogin(String uid, String token){
        return mywayService.tokenLogin(uid, token);
    }

    /**
     * 第三方登录
     * @param oauth_id
     * @param name
     * @return
     */
    public Observable<LoginInfo> oauthLogin(String oauth_id, String name, String iconurl, String platform){
        return mywayService.oauthLogin(oauth_id, name, iconurl, platform);
    }

    /**
     * 找回密码
     * @param phoneNumber
     * @return
     */
    public Observable<BaseInfo> findPassword(String phoneNumber, String countryCode, String vCode, String newPassword){
        return mywayService.findPassword(phoneNumber, countryCode, vCode, newPassword);
    }

    /**
     * 修改密码
     * @param phoneNumber
     * @return
     */
    public Observable<BaseInfo> changePassword(String phoneNumber, String password, String newPassword){
        return mywayService.changePassword(phoneNumber, password, newPassword);
    }

    /**
     * 获取动态列表
     * @param uid
     * @return
     */
    public Observable<DynamicListBean> getDynamicList(String uid, String token, int currentPage){
        return mywayService.getDynamicList(uid, token, currentPage);
    }

    /**
     * 我的动态列表
     * @param uid
     * @return
     */
    public Observable<DynamicListBean> getMyDynamicList(String uid, String token, int currentPage){
        return mywayService.getMyDynamicList(uid, token, currentPage);
    }

    /**
     * 别人的动态列表
     * @param uid
     * @return
     */
    public Observable<DynamicListBean> getOtherDynamicList(String uid, String token, String other_uid, int currentPage){
        return mywayService.getOtherDynamicList(uid, token, other_uid, currentPage);
    }

    /**
     * 我的黑名单
     * @param uid
     * @return
     */
    public Observable<BlacklistBean> blackList(String uid, String token){
        return mywayService.blackList(uid, token);
    }

    /**
     * 删除动态黑名单
     * @param uid
     * @return
     */
    public Observable<BaseInfo> deleteBlackList(String uid, String token, String shield_uid){
        return mywayService.deleteBlackList(uid, token, shield_uid);
    }

    /**
     * 获取动态详情
     * @param sh_id
     * @return
     */
    public Observable<DynamicDetailBean> getDynamicDetail(String sh_id){
        return mywayService.getDynamicDetail(sh_id);
    }

    /**
     * 点赞或者取消点赞
     * @param uid
     * @return
     */
    public Observable<PraiseBean> like(String uid, String sh_id){
        return mywayService.like(uid, sh_id);
    }

    /**
     * 获取动态的所有评论
     * @return
     */
    public Observable<CommentListBean> getCommentList(String sh_id){
        return mywayService.getCommentList(sh_id);
    }

    /**
     * 获取动态的点赞人
     * @return
     */
    public Observable<LikeListBean> getLikeList(String sh_id){
        return mywayService.getLikeList(sh_id);
    }

    /**
     * 发表动态
     * @return
     */
    public Observable<PublishBean> publishDynamic(String uid, String token, boolean include_img , String content,
                                                  String latitude, String longitude, String country, String province,
                                                  String city, String district, String street, String streetnum,
                                                  String citycode, String adcode, String aoiname){
        return mywayService.publishDynamic(uid, token, include_img, content, latitude, longitude,
                country, province, city, district, street, streetnum, citycode, adcode, aoiname);
    }

    /**
     * 上传动态的图片（多个图片）
     * @return
     */
    public Observable<BaseInfo> upload(Map<String, RequestBody> bodyMap, int shid){
        return mywayService.upload(bodyMap, shid);
    }

    /**
     * 上传动态的视频（只能一个）
     * @return
     */
    public Observable<BaseInfo> uploadVideo(Map<String, RequestBody> bodyMap, int shid){
        return mywayService.uploadVideo(bodyMap, shid);
    }

    /**
     * 发表一级评论
     * @return
     */
    public Observable<BaseInfo> publishComment(String uid, String shid, String content){
        return mywayService.publishComment(uid, shid, content);
    }

    /**
     * 获取用户信息
     * @return
     */
    public Observable<UserInfo> getUserInfo(String uid){
        return mywayService.getUserInfo(uid);
    }

    /**
     * 查询积分和金币
     * @return
     */
    public Observable<GoldInfoBean> queryIntegralGold(String uid){
        return mywayService.queryIntegralGold(uid);
    }

    /**
     * 更新手机用户信息
     * @return
     */
    public Observable<BaseInfo> changeUserInfo(String uid, String email, String signature, String nickname, boolean gender,
                                               String username, String profession, String birthday,
                                               String address){
        return mywayService.changeUserInfo(uid, email, signature, nickname, gender, username, profession,
                                            birthday, address);
    }

    /**
     * 更新第三方登录用户信息
     * @return
     */
    public Observable<BaseInfo> changeOauthUserInfo(String uid, String phoneNumber, String email, String signature, String nickname, boolean gender,
                                               String username, String profession, String birthday,
                                               String address){
        return mywayService.changeOauthUserInfo(uid, phoneNumber, email, signature, nickname, gender, username, profession,
                birthday, address);
    }

    /**
     * 更新用户头像
     * @return
     */
    public Observable<ChangeHeadImgBean> changeUserHeadportrait(Map<String, RequestBody> bodyMap){
        return mywayService.changeUserHeadportrait(bodyMap);
    }

    /**
     * 每日签到
     * @return
     */
    public Observable<BaseInfo> userSign(String uid, String token){
        return mywayService.userSign(uid, token);
    }

    /**
     * 屏蔽某人的动态
     * @return
     */
    public Observable<BaseInfo> shield(String uid, String shield_uid){
        return mywayService.shield(uid, shield_uid);
    }

    /**
     * 举报动态
     * @return
     */
    public Observable<BaseInfo> report(String uid, String token, String sh_id, String reason){
        return mywayService.report(uid, token, sh_id, reason);
    }

    /**
     * 删除动态
     * @return
     */
    public Observable<BaseInfo> deleteDynamic(String uid, String token, String sh_id){
        return mywayService.deleteDynamic(uid, token, sh_id);
    }

    /**
     * 未读消息的列表
     * @return
     */
    public Observable<MessageBean> getUnreadMessageList(String uid){
        return mywayService.getUnreadMessageList(uid);
    }

    /**
     * 获取所有消息列表
     * @return
     */
    public Observable<MessageBean> getAllMessageList(String uid){
        return mywayService.getAllMessageList(uid);
    }

    /**
     * 获取未读消息数目
     * @return
     */
    public Observable<ObjIntBean> getUnreadCount(String uid){
        return mywayService.getUnreadCount(uid);
    }

    /**
     * 阅读消息
     * @return
     */
    public Observable<BaseInfo> readMessage(String uid, String mid){
        return mywayService.readMessage(uid, mid);
    }

    /**
     * 路线列表
     * @return
     */
    public Observable<RouteListBean> getRouteList(String uid, String token, int currentPage){
        return mywayService.getRouteList(uid, token, currentPage);
    }

    /**
     * 路线详情
     * @return
     */
    public Observable<RouteDetailBean> getRouteDetail(String uid, String token, int route_id){
        return mywayService.getRouteDetail(uid, token, route_id);
    }

    /**
     * 分享路线和轨迹
     * @return
     */
    public Observable<UploadRouteBean> shareRoutePath(Map<String, RequestBody> bodyMap, int scenery_star,
                                                      int difficulty_star, int legend, int endurance_claim){
        return mywayService.shareRoutePath(bodyMap, scenery_star, difficulty_star, legend, endurance_claim);
    }

    /**
     * 上传路线的轨迹(线)
     * @return
     */
    public Observable<BaseInfo> uploadRoutePath(Map<String, RequestBody> bodyMap, int route_id){
        return mywayService.uploadRoutePath(bodyMap, route_id);
    }

    /**
     * 上传路线的封面图片
     * @return
     */
    public Observable<BaseInfo> uploadCoverImg(Map<String, RequestBody> bodyMap, int route_id){
        return mywayService.uploadCoverImg(bodyMap, route_id);
    }

    /**
     * 上传路线的图片
     * @return
     */
    public Observable<BaseInfo> uploadRouteImg(Map<String, RequestBody> bodyMap, int route_id){
        return mywayService.uploadRouteImg(bodyMap, route_id);
    }

    /**
     * 路线点赞
     * @return
     */
    public Observable<BaseInfo> routeLike(String uid, String route_id){
        return mywayService.routeLike(uid, route_id);
    }

    /**
     * 我的路线
     * @return
     */
    public Observable<RouteListBean> myRoute(String uid, String token, int currentPage){
        return mywayService.myRoute(uid, token, currentPage);
    }

    /**
     * 获取Android最新版本
     * @return
     */
    public Observable<VersionBean> getAppInfo(){
        return mywayService.getAppInfo();
    }

    /**
     * 获取最新的固件
     * @return
     */
    public Observable<FirmwareInfo> getFirmware(String vehicle_version){
        return mywayService.getFirmware(vehicle_version);
    }

    /**
     * 侧边栏信息
     * @return
     */
    public Observable<OtherMsgBean> getOtherMsg(String uid, String token){
        return mywayService.getOtherMsg(uid, token);
    }

    /**
     * 意见反馈
     * @return
     */
    public Observable<BaseInfo> feedback(String uid, String token, String content,
                                         boolean isAndroid){
        return mywayService.feedback(uid, token, content, isAndroid);
    }

    /**
     * 使用车辆
     * @return
     */
    public Observable<BaseInfo> useCar(String uid, String token, String sn_code,
                                         int mileage){
        return mywayService.useCar(uid, token, sn_code, mileage);
    }

    /**
     * 使用过的车辆列表
     * @return
     */
    public Observable<UsedCarBean> usedCarList(String uid, String token){
        return mywayService.usedCarList(uid, token);
    }

    /**
     * 搜索车辆
     * @return
     */
    public Observable<SearchCarBean> searchCar(String uid, String token, String sn_code){
        return mywayService.searchCar(uid, token, sn_code);
    }

    /**
     * 绑定车辆
     * @return
     */
    public Observable<BaseInfo> bindingCar(String uid, String token, String sn_code){
        return mywayService.bindingCar(uid, token, sn_code);
    }

    /**
     * 查询所有绑定的车辆
     * @return
     */
    public Observable<AllBindingCarBean> allBindingCar(String uid, String token){
        return mywayService.allBindingCar(uid, token);
    }

    /**
     * 日榜
     * @return
     */
    public Observable<DayRankingBean> dayRanking(String uid){
        return mywayService.dayRanking(uid);
    }

    /**
     * 月榜
     * @return
     */
    public Observable<DayRankingBean> monthRanking(String uid){
        return mywayService.monthRanking(uid);
    }

    /**
     * 总榜
     * @return
     */
    public Observable<DayRankingBean> totalRanking(String uid) {
        return mywayService.totalRanking(uid);
    }

    /**
     * 上传位置信息
     * @return
     */
    public Observable<BaseInfo> nearbyUpload(String uid, String token, double latitude, double longitude,
                                             String country, String province, String city, String district,
                                             String street, String streetnum, String citycode,
                                             String adcode, String aoiname){
        return mywayService.nearbyUpload(uid, token, latitude, longitude, country, province, city,
                district, street, streetnum, citycode, adcode, aoiname);
    }

    /**
     * 查看附近的人
     * @return
     */
    public Observable<NearbyBean> nearbyList(String uid, String token){
        return mywayService.nearbyList(uid, token);
    }

    /**
     * 发布活动
     * @return
     */
    public Observable<BaseInfo> launchActivity(Map<String, RequestBody> bodyMap, double latitude, double longitude){
        return mywayService.launchActivity(bodyMap, latitude, longitude);
    }

    /**
     * 参加活动
     * @return
     */
    public Observable<BaseInfo> joinActivity(String uid, String token, String aid){
        return mywayService.joinActivity(uid, token, aid);
    }

    /**
     * 附近活动
     * @return
     */
    public Observable<NearbyActivityBean> nearbyActivity(String uid, String token, double latitude, double longitude){
        return mywayService.nearbyActivity(uid, token, latitude, longitude);
    }

    /**
     * 我的活动
     * @return
     */
    public Observable<NearbyActivityBean> myActivity(String uid, String token){
        return mywayService.myActivity(uid, token);
    }

    /**
     * 活动的所有参与人
     * @return
     */
    public Observable<AllPeopleBean> allPeopleActivity(String aid){
        return mywayService.allPeopleActivity(aid);
    }

    /**
     * 退出活动
     * @return
     */
    public Observable<BaseInfo> exitActivity(String uid, String token, String aid){
        return mywayService.exitActivity(uid, token, aid);
    }

    /**
     * 活动签到
     * @return
     */
    public Observable<BaseInfo> signinActivity(String uid, String token, String aid){
        return mywayService.signinActivity(uid, token, aid);
    }

    /**
     * 获取车辆轨迹
     * @return
     */
    public Observable<VehicleTrackBean> getVehicleTrackList(String deviceId, String start, String end){
        return longchuangApis.getVehicleTrackList(deviceId, start, end);
    }

    /**
     * 添加电子围栏
     * @return
     */
    public Observable<BaseInfo> addVehicleFence(Map<String, String> bodyMap,
                                                int fenceType, int radius){
        return longchuangApis.addVehicleFence(bodyMap, fenceType, radius);
    }
//    public Observable<DianziweilanBean> addVehicleFence(String deviceId, String name, int fenceType,
//                                                        int radius, double latitude, double longitude){
//        return longchuangApis.addVehicleFence(deviceId, name, fenceType, radius, latitude, longitude);
//    }

    /**
     * 查询电子围栏
     * @return
     */
    public Observable<DianziweilanBean> getVehicleFenceList(String deviceId){
        return longchuangApis.getVehicleFenceList(deviceId);
    }

    /**
     * 查询电子围栏
     * @return
     */
    public Observable<BaseInfo> updateVehicleFence(String objectId, String newName){
        return longchuangApis.updateVehicleFence(objectId, newName);
    }

    /**
     * 打开电子围栏
     * @return
     */
    public Observable<BaseInfo> openVehicleFence(String objectId){
        return longchuangApis.openVehicleFence(objectId);
    }

    /**
     * 关闭电子围栏
     * @return
     */
    public Observable<BaseInfo> closeVehicleFence(String objectId){
        return longchuangApis.closeVehicleFence(objectId);
    }

    /**
     * 删除电子围栏
     * @return
     */
    public Observable<BaseInfo> deleteVehicleFence(String objectId){
        return longchuangApis.deleteVehicleFence(objectId);
    }

    /**
     * 获取车辆最后的位置
     * @return
     */
    public Observable<VehicleLastLocationBean> vehicleLastLocation(String objectId){
        return longchuangApis.vehicleLastLocation(objectId);
    }

    /**
     * 获取设备所有警报
     * @return
     */
    public Observable<FenceWarningBean> getVehicleFenceWarningList(String deviceId){
        return longchuangApis.getVehicleFenceWarningList(deviceId);
    }

    /**
     * 删除警报
     * @return
     */
    public Observable<BaseInfo> deleteVehicleFenceWarning(String deviceId, String objectId){
        return longchuangApis.deleteVehicleFenceWarning(deviceId, objectId);
    }

    /**
     * 未发布的轨迹列表
     * @return
     */
    public Observable<RoutePathsListBean> routePathsList(String uid, String token){
        return mywayService.routePathsList(uid, token);
    }

    /**
     * 保存轨迹到服务端
     * @return
     */
    public Observable<BaseInfo> saveRoutePaths(Map<String, RequestBody> bodyMap, int legend,
                                               int duration){
        return mywayService.saveRoutePaths(bodyMap, legend, duration);
    }

    /**
     * 保存路线
     * @return
     */
    public Observable<UploadRouteBean> saveRoute(Map<String, RequestBody> bodyMap, int scenery_star,
                                                 int difficulty_star,
                                                 int legend,
                                                 int endurance_claim,
                                                 int score,
                                                 int route_id){
        return mywayService.saveRoute(bodyMap, scenery_star, difficulty_star, legend, endurance_claim,
        score, route_id);
    }

    /**
     * 发布轨迹
     * @return
     */
    public Observable<PublicRoutePathsBean> publicRoutePaths(Map<String, RequestBody> bodyMap, int scenery_star,
                                                             int difficulty_star,
                                                             int legend,
                                                             int endurance_claim,
                                                             int score,
                                                             int route_id){
        return mywayService.publicRoutePaths(bodyMap, scenery_star, difficulty_star, legend, endurance_claim,
                    score,route_id);
    }

    /**
     * 判断车主
     * @return
     */
    public Observable<ObjStringBean> vehicleUserOwner(String uid, String sn_code){
        return mywayService.vehicleUserOwner(uid, sn_code);
    }

    /**
     * 验证用户是否已经注册过
     * @return
     */
    public Observable<ObjBooleanBean> userRepeatabilityVerify(String phoneNumber){
        return mywayService.userRepeatabilityVerify(phoneNumber);
    }

    /**
     * 一键阅读所有消息
     * @return
     */
    public Observable<BaseInfo> readAllMessage(String uid, String token){
        return mywayService.readAllMessage(uid, token);
    }

    /**
     * 融云token
     * @return
     */
    public Observable<RongTokenBean> clubRongToken(String uid){
        return mywayService.clubRongToken(uid);
    }

    /**
     * 创建俱乐部
     * @return
     */
    public Observable<BaseInfo> createRongClub(Map<String, RequestBody> bodyMap){
        return mywayService.createRongClub(bodyMap);
    }

    /**
     * 更新俱乐部
     * @return
     */
    public Observable<BaseInfo> updateRongClub(Map<String, RequestBody> bodyMap, int gid){
        return mywayService.updateRongClub(bodyMap, gid);
    }

    /**
     * 申请加入俱乐部
     * @return
     */
    public Observable<BaseInfo> joinRongClub(String uid, String token, int gid){
        return mywayService.joinRongClub(uid, token, gid);
    }

    /**
     * 退出俱乐部
     * @return
     */
    public Observable<BaseInfo> exitRongClub(String uid, String token, int gid){
        return mywayService.exitRongClub(uid, token, gid);
    }

    /**
     * 设置消息免打扰
     * @return
     */
    public Observable<BaseInfo> disturbingClubMessage(String uid, String token, int gid, boolean no_disturbing){
        return mywayService.disturbingClubMessage(uid, token, gid, no_disturbing);
    }

    /**
     * 解散俱乐部
     * @return
     */
    public Observable<BaseInfo> dissolveRongClub(String uid, String token, int gid){
        return mywayService.dissolveRongClub(uid, token, gid);
    }

    /**
     * 获取俱乐部详情
     * @return
     */
    public Observable<ClubDetailBean> rongClubDetail(String uid, String token, int gid){
        return mywayService.rongClubDetail(uid, token, gid);
    }

    /**
     * 俱乐部列表
     * @return
     */
    public Observable<RongClubListBean> rongClubList(String uid, String token){
        return mywayService.rongClubList(uid, token);
    }

    /**
     * 推荐和搜索俱乐部
     * @return
     */
    public Observable<SearchClubBean> searchClubList(String uid, String token, String name){
        return mywayService.searchClubList(uid, token, name);
    }

    /**
     * 俱乐部通知列表
     * @return
     */
    public Observable<ClubJoinListBean> clubJoinList(String uid, String token){
        return mywayService.clubJoinList(uid, token);
    }

    /**
     * 是否有俱乐部通知
     * @return
     */
    public Observable<ClubMessageBean> clubJoinVerify(String uid, String token){
        return mywayService.clubJoinVerify(uid, token);
    }

    /**
     * 同意加入俱乐部的申请
     * @return
     */
    public Observable<BaseInfo> agreeJoinClub(String uid, String token, String fromUid,
                                                      int gid){
        return mywayService.agreeJoinClub(uid, token, fromUid, gid);
    }

    /**
     * 拒绝加入俱乐部的申请
     * @return
     */
    public Observable<BaseInfo> denyJoinClub(String uid, String token, String fromUid,
                                              int gid){
        return mywayService.denyJoinClub(uid, token, fromUid, gid);
    }

    /**
     * 删除俱乐部成员
     * @return
     */
    public Observable<DeleteClubUsersBean> deleteClubUsers(String uid, String token,
                                                           int gid, String deleted_uid){
        return mywayService.deleteClubUsers(uid, token, gid, deleted_uid);
    }

    /**
     * 关注
     * @return
     */
    public Observable<BaseInfo> attention(String uid, String token,  String idols_uid){
        return mywayService.attention(uid, token, idols_uid);
    }

    /**
     * 取消关注
     * @return
     */
    public Observable<BaseInfo> cancelAttention(String uid, String token,
                                          String idols_uid){
        return mywayService.cancelAttention(uid, token, idols_uid);
    }

    /**
     * 我的关注
     * @return
     */
    public Observable<MyAttentionBean> myAttention(String uid, String token){
        return mywayService.myAttention(uid, token);
    }

    /**
     * 我的粉丝
     * @return
     */
    public Observable<MyAttentionBean> myFans(String uid, String token){
        return mywayService.myFans(uid, token);
    }

}
