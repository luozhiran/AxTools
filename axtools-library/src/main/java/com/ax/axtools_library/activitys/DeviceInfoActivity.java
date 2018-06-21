package com.ax.axtools_library.activitys;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ax.axtools_library.R;
import com.ax.axtools_library.utils.AxDeviceTool;
import com.ax.axtools_library.utils.AxPermissionsTool;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DeviceInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeviceAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        recyclerView = findViewById(R.id.recycler);
        AxPermissionsTool.
                with(this).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.CALL_PHONE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                addPermission(Manifest.permission.ACCESS_WIFI_STATE).
                initPermission();

        mAdapter = new DeviceAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setList(createStringList());
    }

    private List<String> createStringList(){
        ArrayList<String> list = new ArrayList<>();
        if (AxDeviceTool.isPhone(this)) {
            list.add("是否为手机：是" );
        } else {
            list.add("是否为手机：否");
        }
        list.add("手机类型:"+AxDeviceTool.getPhoneType(this));
        list.add("设备密度:"+AxDeviceTool.getScreenDensity(this));
        list.add("手机唯一标识序列号:"+AxDeviceTool.getUniqueSerialNumber());
        list.add("屏幕的宽:"+AxDeviceTool.getScreenWidth(this));
        list.add("屏幕的高:"+AxDeviceTool.getScreenHeight(this));
        list.add("App版本名称:"+AxDeviceTool.getAppVersionName(this));
        list.add("App版本号:"+AxDeviceTool.getAppVersionNo(this));
        list.add("设备的IMEI:"+AxDeviceTool.getDeviceIdIMEI(this));
        list.add("设备的IMSI:"+AxDeviceTool.getIMSI(this));
        list.add("设备的软件版本号:"+AxDeviceTool.getDeviceSoftwareVersion(this));
        list.add("设备MAC地址:"+AxDeviceTool.getMacAddress(this));
        list.add("MCC + MNC:"+AxDeviceTool.getNetworkOperator(this));
        list.add("网络注册名称:"+AxDeviceTool.getNetworkOperatorName(this));
        list.add("SIM国家码:"+AxDeviceTool.getNetworkCountryIso(this));
        list.add("SIM网络码:"+AxDeviceTool.getSimOperator(this));
        list.add("SIM序列号:"+AxDeviceTool.getSimSerialNumber(this));
        list.add("SIM状态:"+AxDeviceTool.getSimState(this));
        list.add("服务商名称:"+AxDeviceTool.getSimOperatorName(this));
        list.add("唯一的用户ID:"+AxDeviceTool.getSubscriberId(this));
        list.add("语音邮件号码:"+AxDeviceTool.getVoiceMailNumber(this));
        list.add("ANDROID ID:"+AxDeviceTool.getAndroidId(this));
        list.add("设备型号:"+AxDeviceTool.getBuildBrandModel());
        list.add("设备厂商:"+AxDeviceTool.getBuildMANUFACTURER());
        list.add("设备品牌:"+AxDeviceTool.getBuildBrand());
        list.add("序列号:"+AxDeviceTool.getSerialNumber());
        list.add("国际长途区号:"+AxDeviceTool.getNetworkCountryIso(this));
        list.add("手机号:"+AxDeviceTool.getLine1Number(this));
        return list;
    }



    private static class DeviceAdapter extends RecyclerView.Adapter<DeviceHolder>{

        private Context context;
        private List<String> list;


        public void setList(List<String> list) {
            this.list.addAll(list);
        }

        public DeviceAdapter(Context context) {
            this.context = context;
            list = new ArrayList<>();
        }

        @NonNull
        @Override
        public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DeviceHolder(LayoutInflater.from(context).inflate(R.layout.item_device,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {

            holder.content.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    private static class DeviceHolder extends RecyclerView.ViewHolder {

        TextView content;
        public DeviceHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
