package com.mywaytec.myway.base;

import android.os.Environment;

import com.mywaytec.myway.APP;

import java.io.File;

/**
 * Created by shemh on 2017/3/3.
 */

public class Constant {

    //文件根目录
    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory()+"/myway";

    /**
     * 是否是车主
     */
    public static final String IS_CHEZHU = "IS_CHEZHU";

    public static class BLE{

        public static final String WRITE_SERVICE_UUID = "0000ffe5-0000-1000-8000-00805f9b34fb";
        public static final String NOTIFY_SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
        public static final String WRITE_CHARACTERISTIC_UUID = "0000ffe9-0000-1000-8000-00805f9b34fb";
        public static final String NOTIFY_CHARACTERISTIC_UUID = "0000ffe4-0000-1000-8000-00805f9b34fb";
        public static final String READ_SERVICE_UUID = "0000180a-0000-1000-8000-00805f9b34fb";
        public static final String READ_CHARACTERISTIC_UUID = "00002a23-0000-1000-8000-00805f9b34fb";

        public static final String TAIDOU_WRITE_SERVICE_UUID = "00001000-0000-1000-8000-00805f9b34fb";
        public static final String TAIDOU_NOTIFY_SERVICE_UUID = "00001000-0000-1000-8000-00805f9b34fb";
        public static final String TAIDOU_WRITE_CHARACTERISTIC_UUID = "00001001-0000-1000-8000-00805f9b34fb";
        public static final String TAIDOU_NOTIFY_CHARACTERISTIC_UUID = "00001002-0000-1000-8000-00805f9b34fb";
        public static final String TAIDOU_READ_SERVICE_UUID = "0000180a-0000-1000-8000-00805f9b34fb";
        public static final String TAIDOU_READ_CHARACTERISTIC_UUID = "00002a23-0000-1000-8000-00805f9b34fb";

        /** 速度+电量+本次里程+总里程 */
        public static byte[] SPEED_AND_POWER = new byte[] {0x4d, 0x57, 0x01, 0x01, 0x09};

        /** 整车数据 */
        public static byte[] ALL_CAR_DATA = new byte[] {0x4d, 0x57, 0x01, 0x0A, 0x11, (byte)0xAC, (byte)0Xe6};

        /** 车辆状态 */
        public static byte[] CAR_STATE = new byte[] {0x4d, 0x57, 0x01, 0x02, 0x04, (byte)0xa3, 0x20};

        /** 车辆信息 */
        public static byte[] CAR_INFO = new byte[] {0x4d, 0x57, 0x01, 0x03, 0x30, (byte)0xe4, 0x20};

        /** 车辆SN码 */
        public static byte[] SN_CODE = new byte[] {0x4d, 0x57, 0x01, 0x03, 0x0c, (byte)0xf5, 0x20};

        /** 软件版本号 */
        public static byte[] SOFTWARE_VERSION_CODE = new byte[] {0x4d, 0x57, 0x01, 0x05, 0x08, (byte)0x96, 0x22};

        /** 硬件版本号 */
        public static byte[] FIRMWARE_VERSION_CODE = new byte[] {0x4d, 0x57, 0x01, 0x06, 0x08, (byte)0x66, 0x22};

        /** 程序运行位置 */
        public static byte[] PROGRAM_LOCATION = new byte[] {0x4d, 0x57, 0x01, 0x07, 0x01, (byte)0xf0, (byte)0xe3};

        /** 车辆实际限速值 */
        public static byte[]  PRACTICAL_LIMIT_VALUES = new byte[] {0x4d, 0x57, 0x01, 0x04, 0x01, (byte)0x00, (byte)0xe3};

        /** 车辆最高/低限速值 */
        public static byte[] SPEED_LIMIT_VALUES = new byte[] {0x4d, 0x57, 0x01, 0x08, 0x02, (byte)0x01, (byte)0xa6};

        /** 开灯(前灯) */       //, (byte)0x0c, (byte)0x90
        public static byte[] OPEN_LIGHT_FRONT = new byte[] {0x4d, 0x57, 0x02, 0x01, 0x01, 0x01, (byte)0x0c, (byte)0x90};

        /** 关灯(前灯) */
        public static byte[] CLOSE_LIGHT_FRONT = new byte[] {0x4d, 0x57, 0x02, 0x01, 0x01, 0x00, (byte)0xcc, 0x51};

        /** 开灯(后灯) */
        public static byte[] OPEN_LIGHT_BACK = new byte[] {0x4d, 0x57, 0x02, 0x09, 0x01, 0x00, 0x0e, (byte)0xd0};

        /** 关灯(后灯) */
        public static byte[] CLOSE_LIGHT_BACK = new byte[] {0x4d, 0x57, 0x02, 0x09, 0x01, 0x01, (byte)0xce, 0x11};

        /** 锁车 */
        public static byte[] LOCK_CAR = new byte[] {0x4d, 0x57, 0x02, 0x02, 0x01, 0x01, (byte)0x0c, (byte)0x60};

        /** 解锁 */
        public static byte[] DEBLOCKING = new byte[] {0x4d, 0x57, 0x02, 0x02, 0x01, 0x00, (byte)0xcc, (byte)0xa1};

        /** 限速设置 */
        public static byte[] LIMIT_SPEED = new byte[] {0x4d, 0x57, 0x02, 0x03, 0x01};

        /** 滑行启动(打开) */
        public static byte[] OPEN_TAXI_START = new byte[] {0x4d, 0x57, 0x02, 0x04, 0x01, 0x01, 0x0d, (byte)0x80};

        /** 滑行启动(关闭) */
        public static byte[] CLOSE_TAXI_START = new byte[] {0x4d, 0x57, 0x02, 0x04, 0x01, 0x00, (byte)0xcd, 0x41};

        /** 模式切换(运动模式) */
        public static byte[] MODE_SWITCH_SPORTS = new byte[] {0x4d, 0x57, 0x02, 0x05, 0x01, 0x01, (byte)0xcd, (byte)0xd1};

        /** 模式切换(节能模式) */
        public static byte[] MODE_SWITCH_ENERGY_SAVING = new byte[] {0x4d, 0x57, 0x02, 0x05, 0x01, 0x00, 0x0d, 0x10};

        /** 定速巡航(打开) */
        public static byte[] OPEN_CONSTANT_SPEED = new byte[] {0x4d, 0x57, 0x02, 0x06, 0x01, 0x01, (byte)0xcd, 0x21};

        /** 定速巡航(关闭) */
        public static byte[] CLOSE_CONSTANT_SPEED = new byte[] {0x4d, 0x57, 0x02, 0x06, 0x01, 0x00, 0x0d, (byte)0xe0};

        /** 蓝牙密码修改 */
        public static byte[] CHANGE_PASSWORD = new byte[] {0x4d, 0x57, 0x0e, 0x01, 0x00};

        //硬件暂时没有此功能
        /** 蓝牙名称修改 */
        public static byte[] CHANGE_NAME = new byte[] {0x4d, 0x57, 0x0f, 0x01, 0x00};

        /** 故障检测 */
        public static byte[] FAULT_DETECT = new byte[] {0x4d, 0x57, 0x08, 0x01, 0x00};

        /** 固件升级 */
        public static byte[] FIRMWARE_UPDATE = new byte[] {0x4d, 0x57, 0x03, 0x03, 0x41, 0x41};

        /** 车身校准 */
        public static byte[] BODY_CALIBRATION = new byte[] {0x4d, 0x57, 0x0d, 0x01, 0x00};

        /** 固件升级确认 */
        public static byte[] FIRMWARE_UPDATE_CONFIRM = new byte[] {0x4d, 0x57, 0x03, 0x07, (byte)0x82, 0x40};

        /** 水平较准 */
        public static byte[] HORIZONTAL_ALIGNMENT = new byte[] {0x4d, 0x57, 0x03, 0x01, (byte)0x80, (byte)0xc0};

        /** 水平较准确认 */
        public static byte[] HORIZONTAL_ALIGNMENT_CONFIRM = new byte[] {0x4d, 0x57, 0x03, 0x05, 0x43, (byte)0xc1};

        /** 油门霍尔较准 */
        public static byte[] YOUMEN_ALIGNMENT = new byte[] {0x4d, 0x57, 0x03, 0x02, (byte)0x81, (byte)0x80};

        /** 油门霍尔较准确认 */
        public static byte[] YOUMEN_ALIGNMENT_CONFIRM = new byte[] {0x4d, 0x57, 0x03, 0x06, 0x42, (byte)0x81};

        /** 心跳包 */
        public static byte[] HEARTBEAT_PACKET = new byte[] {0x4d, 0x57, 0x03, 0x09, 0x46, (byte)0xc1};

        /** 关机 */
        public static byte[] CLOSE_CAR = new byte[] {0x4d, 0x57, 0x03, 0x04, (byte)0x83, 0x00};

        /** 强制关机 */
        public static byte[] FORCE_CLOSE_CAR = new byte[] {0x4d, 0x57, 0x03, 0x08, (byte)0x86, 0x00};

        /** 指纹录入 */
        public static byte[] ENTER_FINGER_WARK = new byte[] {0x4d, 0x57, 0x03, 0x0A, 0x47, (byte)0x81};

        /** 删除全部指纹 */
        public static byte[] DETELE_ALL_FINGER_WARK = new byte[] {0x4d, 0x57, 0x03, 0x0B, (byte)0x87, 0x40};

        /** 删除指纹 */
        public static byte[] DETELE_FINGER_WARK = new byte[] {0x4d, 0x57, 0x02, 0x0A, 0x01};

        /** 灯带模式 */
        public static byte[] DENGDAI_MODE = new byte[] {0x4d, 0x57, 0x02, 0x0B, 0x01};

        /** 关闭灯带模式 */
        public static byte[] CLOSE_DENGDAI_MODE = new byte[] {0x4d, 0x57, 0x02, 0x0B, 0x01, (byte)0xff, (byte)0x8e, 0x31};

        /** 氛围灯模式 */
        public static byte[] CURRENT_DENGDAI_MODE = new byte[] {0x4d, 0x57, 0x01, 0x09, 0x01, (byte)0x90, (byte)0xe7};

        /** 车辆密码验证 */
        public static byte[] VEHICLE_PASSWORD_VERIFICATION = new byte[] {0x4d, 0x57, 0x02, 0x0C, 0x06};

        /** 车辆密码设置 */
        public static byte[] VEHICLE_PASSWORD_SETTING = new byte[] {0x4d, 0x57, 0x02, 0x0D, 0x06};

        /** 车辆密码复位 */
        public static byte[] VEHICLE_PASSWORD_RESET = new byte[] {0x4d, 0x57, 0x03, 0x0C, 0x45, 0x01};

    }

    public static class MQTT{
        //MQTT服务器地址
        public static String MQTT_BROKER = "tcp://121.43.180.66:1883";
        //MQTT服务器账号
        public static String MQTT_ACCOUNT = "admin";
        //MQTT服务器账号密码
        public static String MQTT_PASSWORD = "public";
    }


    //================= PATH ====================

    public static final String PATH_DATA = APP.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    public static final String KEY_API = "52b7ec3471ac3bec6846577e79f20e4c";//需要APIKEY请去 http://www.tianapi.com/#wxnew 申请,复用会减少访问可用次数

    public static final String KEY_ALIPAY = "aex07566wvayrgxicnaraae";

    public static final String LEANCLOUD_ID = "mhke0kuv33myn4t4ghuid4oq2hjj12li374hvcif202y5bm6";

    public static final String LEANCLOUD_SIGN = "badc5461a25a46291054b902887a68eb,1480438132702";

    public static final String BUGLY_ID = "257700f3f8";

    public static final String DYNAMIC_SHARE_URL = "http://www.mywaytec.cn:8090/dynamic/activity.html?sh_id=";

}
