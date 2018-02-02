package com.mywaytec.myway.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.mywaytec.myway.AppManager;
import com.mywaytec.myway.R;
import com.mywaytec.myway.base.Constant;
import com.mywaytec.myway.model.bean.CityModel;
import com.mywaytec.myway.model.bean.CountryModel;
import com.mywaytec.myway.model.bean.DistrictModel;
import com.mywaytec.myway.model.bean.ProvinceModel;
import com.mywaytec.myway.ui.login.LoginActivity;
import com.mywaytec.myway.utils.data.BleInfo;
import com.mywaytec.myway.utils.data.IsLogin;
import com.mywaytec.myway.view.OneButtonDialog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import io.rong.imkit.RongIM;

/**
 *
 */

public class DialogUtils {

    public static AlertDialog confirmDialog;

    /** 是否确认dialog */
    public static void confirmDialog(Context context, String hint, View.OnClickListener mOnClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_confirm, null);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint_text);
        tvHint.setText(hint);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(mOnClickListener);
        builder.setView(view);
        confirmDialog = builder.show();
    }


    private static AlertDialog hintDialog;
    /** 提示信息dialog */
    public static void hintDialog(Context context, int imgRes, String hint){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_hint, null);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint);
        tvHint.setText(hint);
        ImageView imgCancel = (ImageView) view.findViewById(R.id.img_cancel);
        ImageView imgHint = (ImageView) view.findViewById(R.id.img_hint);
        if (imgRes > 0){
            imgHint.setImageResource(imgRes);
        }else if (imgRes == 0){
            imgHint.setVisibility(View.GONE);
        }
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintDialog.dismiss();
            }
        });
        builder.setView(view);
        hintDialog = builder.show();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int systemWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int wight = systemWidth * 3 / 4;
        WindowManager.LayoutParams params1 =
                hintDialog.getWindow().getAttributes();
        params1.width = wight;
        params1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        hintDialog.getWindow().setAttributes(params1);
    }


    //重新登录dialog
    public static void reLoginDialog(final Context context){
        final OneButtonDialog oneButtonDialog = new OneButtonDialog(context);
        oneButtonDialog.setText(R.string.用户验证失败);
        oneButtonDialog.setOnConfirmClickListener(new OneButtonDialog.OnConfirmClickListener() {
            @Override
            public void confirmClickListener() {
                RongIM.getInstance().logout();
                BleInfo.clearBleInfo();//清除蓝牙缓存信息
                CleanMessageUtil.clearAllCache(context);//清除缓存
                context.startActivity(new Intent(context, LoginActivity.class));
                IsLogin.saveDynamicData(false);
                AppManager.getAppManager().finishAllActivity();
            }
        });
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int systemWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int wight = systemWidth * 3 / 4;
        WindowManager.LayoutParams params1 =
                oneButtonDialog.getWindow().getAttributes();
        params1.width = wight;
        params1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        oneButtonDialog.getWindow().setAttributes(params1);
        oneButtonDialog.show();
    }

    /**
     * 选择城市
     */
    public static void selectCity(Context context, final OnCitySelectListener onCitySelectListener) {
        final ArrayList<String> item1 = new ArrayList<>();
        final ArrayList<ArrayList<String>> item2 = new ArrayList<>();
        final ArrayList<ArrayList<ArrayList<String>>> item3 = new ArrayList<>();
        try {
            AssetManager asset = context.getAssets();
            InputStream input = asset.open("locList.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler xmlParserHandler = new XmlParserHandler();
            parser.parse(input, xmlParserHandler);
            input.close();

            final List<CountryModel> countryList = xmlParserHandler.getDataList();

            for (int i = 0; i < countryList.size(); i++) {
                item1.add(countryList.get(i).getName());
                List<ProvinceModel> provinceList = countryList.get(i).getProvinceList();

                ArrayList<String> provinces = new ArrayList<>();
                ArrayList<ArrayList<String>> cityLists = new ArrayList<>();
                for (int j = 0; j < provinceList.size(); j++) {
                    provinces.add(provinceList.get(j).getName());
                    List<CityModel> cityList = provinceList.get(j).getCityList();

                    ArrayList<String> citys = new ArrayList<>();
                    for (int k = 0; k < cityList.size(); k++) {
                        citys.add(cityList.get(k).getName());
                    }
                    cityLists.add(citys);
                }
                item2.add(provinces);
                item3.add(cityLists);
            }

            OptionsPickerView pickerView = new OptionsPickerView(new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    onCitySelectListener.citySelect(item1.get(options1), item2.get(options1).get(options2),
                            item3.get(options1).get(options2).get(options3));
                }
            }));
            pickerView.setPicker(item1, item2, item3);
            pickerView.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public interface OnCitySelectListener{
        void citySelect(String s1, String s2, String s3);
    }
}
