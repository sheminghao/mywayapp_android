package com.mywaytec.myway.model.http.api;


import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.AllBindingCarBean;
import com.mywaytec.myway.model.bean.AllPeopleBean;
import com.mywaytec.myway.model.bean.BlacklistBean;
import com.mywaytec.myway.model.bean.ChangeHeadImgBean;
import com.mywaytec.myway.model.bean.CommentListBean;
import com.mywaytec.myway.model.bean.DayRankingBean;
import com.mywaytec.myway.model.bean.DynamicDetailBean;
import com.mywaytec.myway.model.bean.DynamicListBean;
import com.mywaytec.myway.model.bean.FirmwareInfo;
import com.mywaytec.myway.model.bean.GoldInfoBean;
import com.mywaytec.myway.model.bean.LikeListBean;
import com.mywaytec.myway.model.bean.LoginInfo;
import com.mywaytec.myway.model.bean.MessageBean;
import com.mywaytec.myway.model.bean.NearbyActivityBean;
import com.mywaytec.myway.model.bean.NearbyBean;
import com.mywaytec.myway.model.bean.ObjBooleanBean;
import com.mywaytec.myway.model.bean.ObjIntBean;
import com.mywaytec.myway.model.bean.ObjStringBean;
import com.mywaytec.myway.model.bean.OtherMsgBean;
import com.mywaytec.myway.model.bean.PraiseBean;
import com.mywaytec.myway.model.bean.PublicRoutePathsBean;
import com.mywaytec.myway.model.bean.PublishBean;
import com.mywaytec.myway.model.bean.RouteDetailBean;
import com.mywaytec.myway.model.bean.RouteListBean;
import com.mywaytec.myway.model.bean.RoutePathsListBean;
import com.mywaytec.myway.model.bean.SearchCarBean;
import com.mywaytec.myway.model.bean.UploadRouteBean;
import com.mywaytec.myway.model.bean.UsedCarBean;
import com.mywaytec.myway.model.bean.UserInfo;
import com.mywaytec.myway.model.bean.VersionBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 */

public interface MywayApis {

//    本地服务器
//    String HOST = "http://192.168.1.165:8080";
    //曼威服务器
//    String HOST_IP = "http://120.77.249.52";
    String HOST_IP = "http://www.mywaytec.cn";
    //外网服务器
    String HOST = HOST_IP+":8090";

    /**
     * 注册
     */
    @POST("/user/register")
    Observable<BaseInfo> getRegisterInfo(@Query("phoneNumber") String phoneNumber,
                                         @Query("countryCode") String countryCode,
                                         @Query("password") String password,
                                         @Query("vCode") String vCode);

    /**
     * 登录
     */
    @POST("/user/login")
    Observable<LoginInfo> getLoginInfo(@Query("phoneNumber") String phoneNumber,
                                       @Query("password") String password);

    /**
     * Token登录
     */
    @POST("/user/tokenLogin")
    Observable<LoginInfo> tokenLogin(@Query("uid") String uid,
                                     @Query("token") String token);

    /**
     * 第三方登录
     */
    @FormUrlEncoded
    @POST("/user/oauth/login")
    Observable<LoginInfo> oauthLogin(@Query("oauth_id") String oauth_id,
                                     @Field("name") String name,
                                     @Query("iconurl") String iconurl,
                                     @Field("platform") String platform);

    /**
     * 找回密码
     */
    @POST("/user/findPassword")
    Observable<BaseInfo> findPassword(@Query("phoneNumber") String phoneNumber,
                                      @Query("countryCode") String countryCode,
                                      @Query("vCode") String vCode,
                                      @Query("newPassword") String newPassword);

    /**
     * 修改密码
     */
    @POST("/user/modifyPassword")
    Observable<BaseInfo> changePassword(@Query("phoneNumber") String phoneNumber,
                                        @Query("password") String password,
                                        @Query("newPassword") String newPassword);

    /**
     *获取动态列表
     */
    @POST("/dynamic/list")
    Observable<DynamicListBean> getDynamicList(@Query("uid") String uid,
                                               @Query("token") String token,
                                               @Query("currentPage") int currentPage);

    /**
     *我的动态列表
     */
    @POST("/dynamic/my")
    Observable<DynamicListBean> getMyDynamicList(@Query("uid") String uid,
                                                 @Query("token") String token,
                                                 @Query("currentPage") int currentPage);

    /**
     *别人的动态列表
     */
    @POST("/dynamic/other")
    Observable<DynamicListBean> getOtherDynamicList(@Query("uid") String uid,
                                                    @Query("token") String token,
                                                    @Query("other_uid") String other_uid,
                                                    @Query("currentPage") int currentPage);

    /**
     *我的黑名单
     */
    @POST("/dynamic/shield/my")
    Observable<BlacklistBean> blackList(@Query("uid") String uid,
                                        @Query("token") String token );

    /**
     *删除动态黑名单
     */
    @POST("/dynamic/shield/cancel")
    Observable<BaseInfo> deleteBlackList(@Query("uid") String uid,
                                         @Query("token") String token,
                                         @Query("shield_uid") String shield_uid);

    /**
     *获取动态详情
     */
    @POST("/dynamic/detail")
    Observable<DynamicDetailBean> getDynamicDetail(@Query("sh_id") String sh_id);

    /**
     *点赞或取消点赞
     */
    @POST("/dynamic/like")
    Observable<PraiseBean> like(@Query("uid") String uid,
                                @Query("sh_id") String sh_id);

    /**
     *获取动态的所有评论
     */
    @POST("/dynamic/comment/list")
    Observable<CommentListBean> getCommentList(@Query("sh_id") String sh_id);

    /**
     *获取动态的点赞人
     */
    @POST("/dynamic/like/list")
    Observable<LikeListBean> getLikeList(@Query("sh_id") String sh_id);

    /**
     *发表动态
     */
    @FormUrlEncoded
    @POST("/dynamic/publish")
    Observable<PublishBean> publishDynamic(@Query("uid") String uid,
                                           @Query("token") String token,
                                           @Query("include_img") boolean include_img,
                                           @Field("content") String content,
                                           @Field("latitude") String latitude,
                                           @Field("longitude") String longitude,
                                           @Field("country") String country,
                                           @Field("province") String province,
                                           @Field("city") String city,
                                           @Field("district") String district,
                                           @Field("street") String street,
                                           @Field("streetnum") String streetnum,
                                           @Field("citycode") String citycode,
                                           @Field("adcode") String adcode,
                                           @Field("aoiname") String aoiname);

    /**
     *上传动态的图片（多个图片）
     */
    @Multipart
    @POST("/dynamic/picture/uploads")
    Observable<BaseInfo> upload(@PartMap Map<String, RequestBody> bodyMap,
                                @Part("sh_id") int sh_id);


    /**
     *上传动态的视频（只能一个）
     */
    @Multipart
    @POST("/dynamic/video/upload")
    Observable<BaseInfo> uploadVideo(@PartMap Map<String, RequestBody> bodyMap,
                                     @Part("sh_id") int sh_id);

    /**
     *发表一级评论
     */
    @FormUrlEncoded
    @POST("/dynamic/comment/first/publish")
    Observable<BaseInfo> publishComment(@Query("uid") String uid,
                                        @Query("sh_id") String sh_id,
                                        @Field("content") String content);

    /**
     *发表二级评论
     */
    @FormUrlEncoded
    @POST("/dynamic/comment/second/publish")
    Observable<BaseInfo> publishSecondComment(@Query("from_uid") String uid,
                                              @Field("content") String sh_id,
                                              @Query("to_uid") String content,
                                              @Query("parent_id") String parent_id);

    /**
     *获取用户信息
     */
    @POST("/user/detail")
    Observable<UserInfo> getUserInfo(@Query("uid") String uid);

    /**
     *更新用户头像
     */
    @Multipart
    @POST("/user/avatar/upload")
    Observable<ChangeHeadImgBean> changeUserHeadportrait(@PartMap Map<String, RequestBody> bodyMap);

    /**
     *查询积分和金币
     */
    @POST("/user/query/integral/gold")
    Observable<GoldInfoBean> queryIntegralGold(@Query("uid") String uid);

    /**
     *更新手机用户信息
     */
    @FormUrlEncoded
    @POST("/user/phone/update")
    Observable<BaseInfo> changeUserInfo(@Query("uid") String uid,
                                        @Field("email") String email,
                                        @Field("signature") String signature,
                                        @Field("nickname") String nickname,
                                        @Query("gender") boolean gender,
                                        @Field("username") String username,
                                        @Field("profession") String profession,
                                        @Field("birthday") String birthday,
                                        @Field("address") String address);

    /**
     *更新第三方登录用户信息
     */
    @FormUrlEncoded
    @POST("/user/oauth/update")
    Observable<BaseInfo> changeOauthUserInfo(@Query("uid") String uid,
                                             @Field("phoneNumber") String phoneNumber,
                                             @Field("email") String email,
                                             @Field("signature") String signature,
                                             @Field("nickname") String nickname,
                                             @Query("gender") boolean gender,
                                             @Field("username") String username,
                                             @Field("profession") String profession,
                                             @Field("birthday") String birthday,
                                             @Field("address") String address);

    /**
     *每日签到
     */
    @POST("/user/sign/in")
    Observable<BaseInfo> userSign(@Query("uid") String uid,
                                  @Query("token") String token);

    /**
     *屏蔽某人的动态
     */
    @POST("/dynamic/shield")
    Observable<BaseInfo> shield(@Query("uid") String uid,
                                @Query("shield_uid") String shield_uid );

    /**
     *举报动态
     */
    @POST("/dynamic/report")
    Observable<BaseInfo> report(@Query("uid") String uid,
                                @Query("token") String token,
                                @Query("sh_id") String sh_id,
                                @Query("reason") String reason);

    /**
     *删除动态
     */
    @POST("/dynamic/delete")
    Observable<BaseInfo> deleteDynamic(@Query("uid") String uid,
                                       @Query("token") String token,
                                       @Query("sh_id") String sh_id);

    /**
     *未读消息的列表
     */
    @POST("/message/list/unread")
    Observable<MessageBean> getUnreadMessageList(@Query("uid") String uid);

    /**
     * 获取所有消息列表
     */
    @POST("/message/list/all")
    Observable<MessageBean> getAllMessageList(@Query("uid") String uid);

    /**
     *获取未读消息数目
     */
    @POST("/message/count/unread")
    Observable<ObjIntBean> getUnreadCount(@Query("uid") String uid);

    /**
     *阅读消息
     */
    @POST("/message/read")
    Observable<BaseInfo> readMessage(@Query("uid") String uid,
                                     @Query("mid") String mid);

    /**
     *路线列表
     */
    @POST("/route/list")
    Observable<RouteListBean> getRouteList(@Query("uid") String uid,
                                           @Query("token") String token,
                                           @Query("currentPage") int currentPage);

    /**
     *路线详情
     */
    @POST("/route/detail")
    Observable<RouteDetailBean> getRouteDetail(@Query("uid") String uid,
                                               @Query("token") String token,
                                               @Query("route_id") int route_id);

    /**
     *分享路线和轨迹
     */
    @Multipart
    @POST("/route/paths/share")
    Observable<UploadRouteBean> shareRoutePath(@PartMap Map<String, RequestBody> bodyMap,
                                               @Part("sceneryStar") int scenery_star,
                                               @Part("difficultyStar") int difficulty_star,
                                               @Part("legend") int legend,
                                               @Part("enduranceClaim") int endurance_claim);

    /**
     *上传路线的轨迹(线)
     */
    @Multipart
    @POST("/route/path/uploads")
    Observable<BaseInfo> uploadRoutePath(@PartMap Map<String, RequestBody> bodyMap,
                                         @Part("route_id") int route_id);

    /**
     *上传路线的封面图片
     */
    @Multipart
    @POST("/route/cover/update")
    Observable<BaseInfo> uploadCoverImg(@PartMap Map<String, RequestBody> bodyMap,
                                        @Part("route_id") int route_id);

    /**
     *上传路线的图片
     */
    @Multipart
    @POST("/route/images/upload")
    Observable<BaseInfo> uploadRouteImg(@PartMap Map<String, RequestBody> bodyMap,
                                        @Part("route_id") int route_id);

    /**
     *路线点赞
     */
    @POST("/route/like")
    Observable<BaseInfo> routeLike(@Query("uid") String uid,
                                   @Query("route_id") String route_id);

    /**
     *我的路线
     */
    @POST("/route/my")
    Observable<RouteListBean> myRoute(@Query("uid") String uid,
                                      @Query("token") String token,
                                      @Query("currentPage") int currentPage);

    /**
     * 未发布的轨迹列表
     */
    @POST("/route/paths/list")
    Observable<RoutePathsListBean> routePathsList(@Query("uid") String uid,
                                                  @Query("token") String token);

    /**
     * 保存轨迹到服务端
     */
    @Multipart
    @POST("/route/paths/save")
    Observable<BaseInfo> saveRoutePaths(@PartMap Map<String, RequestBody> bodyMap,
                                        @Part("legend") int legend,
                                        @Part("duration") int duration);

    /**
     *保存路线
     */
    @Multipart
    @POST("/route/save")
    Observable<UploadRouteBean> saveRoute(@PartMap Map<String, RequestBody> bodyMap,
                                          @Part("sceneryStar") int scenery_star,
                                          @Part("difficultyStar") int difficulty_star,
                                          @Part("legend") int legend,
                                          @Part("enduranceClaim") int endurance_claim,
                                          @Part("score") int score,
                                          @Part("route_id") int route_id);

    /**
     * 发布轨迹
     */
    @Multipart
    @POST("/route/paths/public")
    Observable<PublicRoutePathsBean> publicRoutePaths(@PartMap Map<String, RequestBody> bodyMap,
                                                      @Part("sceneryStar") int scenery_star,
                                                      @Part("difficultyStar") int difficulty_star,
                                                      @Part("legend") int legend,
                                                      @Part("enduranceClaim") int endurance_claim,
                                                      @Part("score") int score,
                                                      @Part("route_id") int route_id);

    /**
     *获取Android最新版本
     */
    @POST("/app/Android/info")
    Observable<VersionBean> getAppInfo();

    /**
     *获取最新的固件
     */
    @POST("/firmware/new")
    Observable<FirmwareInfo> getFirmware(@Query("vehicle_version") String vehicle_version);

    /**
     *侧边栏信息
     */
    @POST("/user/other/msg")
    Observable<OtherMsgBean> getOtherMsg(@Query("uid") String uid,
                                         @Query("token") String token);

    /**
     *意见反馈
     */
    @FormUrlEncoded
    @POST("/opinion/commit")
    Observable<BaseInfo> feedback(@Query("uid") String uid,
                                  @Query("token") String token,
                                  @Field("content") String content,
                                  @Query("isAndroid") boolean isAndroid);

    /**
     *使用车辆
     */
    @FormUrlEncoded
    @POST("/vehicle/employ")
    Observable<BaseInfo> useCar(@Query("uid") String uid,
                                @Query("token") String token,
                                @Field("sn_code") String sn_code,
                                @Query("mileage") int mileage);

    /**
     *使用过的车辆列表
     */
    @POST("/vehicle/list")
    Observable<UsedCarBean> usedCarList(@Query("uid") String uid,
                                        @Query("token") String token );

    /**
     *搜索车辆
     */
    @POST("/vehicle/search")
    Observable<SearchCarBean> searchCar(@Query("uid") String uid,
                                        @Query("token") String token,
                                        @Query("sn_code") String sn_code);

    /**
     *绑定车辆
     */
    @POST("/vehicle/binding")
    Observable<BaseInfo> bindingCar(@Query("uid") String uid,
                                    @Query("token") String token,
                                    @Query("sn_code") String sn_code);

    /**
     *查询所有绑定的车辆
     */
    @POST("/vehicle/all/my")
    Observable<AllBindingCarBean> allBindingCar(@Query("uid") String uid,
                                                @Query("token") String token );


    /**
     *日榜
     */
    @POST("/vehicle/ranking/day")
    Observable<DayRankingBean> dayRanking(@Query("uid") String uid );

    /**
     *月榜
     */
    @POST("/vehicle/ranking/month")
    Observable<DayRankingBean> monthRanking(@Query("uid") String uid );

    /**
     *总榜
     */
    @POST("/vehicle/ranking/total")
    Observable<DayRankingBean> totalRanking(@Query("uid") String uid );

    /**
     *上传位置信息
     */
    @FormUrlEncoded
    @POST("/nearby/upload")
    Observable<BaseInfo> nearbyUpload(@Query("uid") String uid,
                                      @Query("token") String token,
                                      @Query("latitude") double latitude,
                                      @Query("longitude") double longitude,
                                      @Field("country") String country,
                                      @Field("province") String province,
                                      @Field("city") String city,
                                      @Field("district") String district,
                                      @Field("street") String street,
                                      @Field("streetnum") String streetnum,
                                      @Field("citycode") String citycode,
                                      @Field("adcode") String adcode,
                                      @Field("aoiname") String aoiname );

    /**
     *查看附近的人
     */
    @POST("/nearby/list")
    Observable<NearbyBean> nearbyList(@Query("uid") String uid,
                                      @Query("token") String token);

    /**
     *发起活动
     */
    @Multipart
    @POST("/activity/launch")
    Observable<BaseInfo> launchActivity(@PartMap Map<String, RequestBody> bodyMap,
                                        @Part("latitude") double latitude,
                                        @Part("longitude") double longitude);

    /**
     *参加活动
     */
    @POST("/activity/join")
    Observable<BaseInfo> joinActivity(@Query("uid") String uid,
                                      @Query("token") String token,
                                      @Query("aid") String aid);

    /**
     *附近活动
     */
    @POST("/activity/nearby")
    Observable<NearbyActivityBean> nearbyActivity(@Query("uid") String uid,
                                                  @Query("token") String token,
                                                  @Query("latitude") double latitude,
                                                  @Query("longitude") double longitude);

    /**
     *我的活动
     */
    @POST("/activity/my")
    Observable<NearbyActivityBean> myActivity(@Query("uid") String uid,
                                              @Query("token") String token);

    /**
     *活动的所有参与人
     */
    @POST("/activity/all/people")
    Observable<AllPeopleBean> allPeopleActivity(@Query("aid") String aid);

    /**
     *退出活动
     */
    @POST("/activity/exit")
    Observable<BaseInfo> exitActivity(@Query("uid") String uid,
                                           @Query("token") String token,
                                           @Query("aid") String aid);

    /**
     *活动签到
     */
    @POST("/activity/sign/in")
    Observable<BaseInfo> signinActivity(@Query("uid") String uid,
                                        @Query("token") String token,
                                        @Query("aid") String aid);

    /**
     *判断车主
     */
    @POST("/vehicle/user/owner")
    Observable<ObjStringBean> vehicleUserOwner(@Query("uid") String uid,
                                               @Query("sn_code") String sn_code);

    /**
     *验证用户是否已经注册过
     */
    @POST("/user/repeatability/verify")
    Observable<ObjBooleanBean> userRepeatabilityVerify(@Query("phoneNumber") String phoneNumber);

    /**
     *一键阅读所有消息
     */
    @POST("/message/readAll")
    Observable<BaseInfo> readAllMessage(@Query("uid") String uid,
                                        @Query("token") String token);
}
