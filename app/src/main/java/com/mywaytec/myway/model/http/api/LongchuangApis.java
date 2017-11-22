package com.mywaytec.myway.model.http.api;

import com.mywaytec.myway.model.BaseInfo;
import com.mywaytec.myway.model.bean.DianziweilanBean;
import com.mywaytec.myway.model.bean.FenceWarningBean;
import com.mywaytec.myway.model.bean.ObjStringBean;
import com.mywaytec.myway.model.bean.RoutePathsListBean;
import com.mywaytec.myway.model.bean.VehicleLastLocationBean;
import com.mywaytec.myway.model.bean.VehicleTrackBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by shemh on 2017/9/4.
 */

public interface LongchuangApis {

    //龙创服务器
    String HOST_IP = "http://116.62.193.58";
//    String HOST_IP = "http://192.168.1.165";
    String HOST = HOST_IP+":8081";
    String projectName = "/myway_iot";

    /**
     * 获取车辆轨迹
     */
    @POST(projectName+"/vehicle/track/list")
    Observable<VehicleTrackBean> getVehicleTrackList(@Query("deviceId") String deviceId,
                                                     @Query("start") String start,
                                                     @Query("end") String end);

    /**
     * 添加电子围栏
     */
    @FormUrlEncoded
    @POST(projectName+"/vehicle/fence/add")
    Observable<BaseInfo> addVehicleFence(@FieldMap Map<String, String> bodyMap,
                                         @Query("fenceType") int fenceType,
                                         @Query("radius") int radius);

    /**
     * 查询电子围栏
     */
    @POST(projectName+"/vehicle/fence/list")
    Observable<DianziweilanBean> getVehicleFenceList(@Query("deviceId") String deviceId);

    /**
     * 修改电子围栏的名称
     */
    @FormUrlEncoded
    @POST(projectName+"/vehicle/fence/update")
    Observable<BaseInfo> updateVehicleFence(@Query("objectId") String objectId,
                                            @Field("newName") String newName);

    /**
     * 打开电子围栏
     */
    @POST(projectName+"/vehicle/fence/open")
    Observable<BaseInfo> openVehicleFence(@Query("objectId") String objectId);

    /**
     * 打开电子围栏
     */
    @POST(projectName+"/vehicle/fence/close")
    Observable<BaseInfo> closeVehicleFence(@Query("objectId") String objectId);

    /**
     * 删除电子围栏
     */
    @POST(projectName+"/vehicle/fence/delete")
    Observable<BaseInfo> deleteVehicleFence(@Query("objectId") String objectId);

    /**
     * 获取车辆最后的位置
     */
    @POST(projectName+"/vehicle/new/location")
    Observable<VehicleLastLocationBean> vehicleLastLocation(@Query("deviceId") String deviceId);

    /**
     * 获取设备所有警报
     */
    @POST(projectName+"/vehicle/fence/warning/list")
    Observable<FenceWarningBean> getVehicleFenceWarningList(@Query("deviceId") String deviceId);

    /**
     * 删除警报
     */
    @POST(projectName+"/vehicle/fence/warning/delete")
    Observable<BaseInfo> deleteVehicleFenceWarning(@Query("deviceId") String deviceId,
                                                   @Query("objectId") String objectId);

}
