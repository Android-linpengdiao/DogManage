package com.dog.manage.app.area;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.base.utils.CommonUtil;
import com.dog.manage.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityManager {

    private static CityManager mInstance;
    private Context context;
    private Map<String, String> provinceMap = new HashMap<>();
    private Map<String, String> cityMap = new HashMap<>();


    private Map<String, String> cityIdMap = new HashMap<>();
    private Map<String, String> areaIdMap = new HashMap<>();

    private List<CityData> cityDataList;

    public synchronized static CityManager getInstance() {
        if (mInstance == null) {
            synchronized (CityManager.class) {
                if (mInstance == null) {
                    mInstance = new CityManager();
                }
            }
        }
        return mInstance;
    }

    private CityManager() {
        this.context = MyApplication.getInstance();
    }

    public List<CityData> getAreas() {
        List<CityData> list = new ArrayList<>();
        String area = getJson();
        try {
            JSONObject jsonObject = new JSONObject(area);
            for (int i = 1; i <= 46; i++) {
                int sizeID = 10 + i;
                JSONObject object = jsonObject.optJSONObject(String.valueOf(sizeID));
                if (!CommonUtil.isBlank(object)) {
                    list.add(CityData.parseJsonToBean(object.toString()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void setMapData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CityData> list = getAreas();
                if (list != null && list.size() > 0) {
                    cityDataList=list;
                    for (CityData cityData : list) {
                        provinceMap.put(cityData.getName(), cityData.getId());
                        List<CityData.FirstChildrenBean> firstChildrenBeans = cityData.getChildren();
                        if (firstChildrenBeans != null && firstChildrenBeans.size() > 0) {

                            for (CityData.FirstChildrenBean firstChildrenBean : firstChildrenBeans) {
                                cityMap.put(cityData.getName() + "_" + firstChildrenBean.getName(), firstChildrenBean.getId());
                                cityIdMap.put(firstChildrenBean.getId(), firstChildrenBean.getName());
                                for (CityData.FirstChildrenBean.SecondChildrenBean secondChildrenBean : firstChildrenBean.getChildren()) {
                                    areaIdMap.put(secondChildrenBean.getId(), secondChildrenBean.getName());
                                }

                            }

                        }
                    }
                }
            }
        }).start();
    }

    private String getJson() {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open("area.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取省份的ID
     *
     * @param provinceName 名字
     * @return id
     */
    public String getProvinceId(String provinceName) {

        if (!TextUtils.isEmpty(provinceName)) {
            if (provinceMap != null && provinceMap.get(provinceName) != null) {
                return provinceMap.get(provinceName);
            }
        }
        return "1";
    }

    public String getProvinceName(String value) {

        if (!TextUtils.isEmpty(value)) {
            if (provinceMap != null && provinceMap.size() > 0) {
                for (String key : provinceMap.keySet()) {
                    if (provinceMap.get(key).equals(value)) {
                        return key;
                    }
                }
            }
        }
        return "";
    }

    /**
     * 获取城市的ID
     *
     * @param cityName 名字
     * @return id
     */
    public String getCityId(String provinceName, String cityName) {

        if (!TextUtils.isEmpty(provinceName) && !TextUtils.isEmpty(cityName)) {
            if (cityMap != null && cityMap.get(provinceName + "_" + cityName) != null) {
                return cityMap.get(provinceName + "_" + cityName);
            }
        }
        return "1101";
    }

    public String getCityName(String id) {

        if (!TextUtils.isEmpty(id)) {
            if (cityIdMap != null && cityIdMap.size() > 0) {
                if (cityIdMap.get(id) != null) {
                    return cityIdMap.get(id);
                }
            }
        }
        return "";
    }

    public List<CityData> getCityDataList(){
        return cityDataList;
    }

    public String getAreaName(String id) {

        if (!TextUtils.isEmpty(id)) {
            if (areaIdMap != null && areaIdMap.size() > 0) {
                if (areaIdMap.get(id) != null) {
                    return areaIdMap.get(id);
                }
            }
        }
        return "";
    }


}
