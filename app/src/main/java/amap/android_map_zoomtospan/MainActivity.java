package amap.android_map_zoomtospan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMap.OnMapClickListener, AMap.OnMapLoadedListener, View.OnClickListener, AMap.OnMapLongClickListener {
    private AMap aMap;
    private MapView mapView;
    private LatLng center = new LatLng(39.993167, 116.473274);// 中心点
    private Button btncenter;
    private Button btnzoom;
    private MarkerOverlay markerOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMapClickListener(this);// 地图点击监听
        aMap.setOnMapLoadedListener(this); //地图加载完成监听
        aMap.setOnMapLongClickListener(this); // 地图长按监听
        btncenter = (Button) findViewById(R.id.btn_center);
        btnzoom = (Button)findViewById(R.id.btn_zoom);
        btncenter.setOnClickListener(this);
        btnzoom.setOnClickListener(this);

    }

    private List<LatLng> getPointList() {
        List<LatLng> pointList = new ArrayList<LatLng>();
        pointList.add(new LatLng(39.993755, 116.467987));
        pointList.add(new LatLng(39.985589, 116.469306));
        pointList.add(new LatLng(39.990946, 116.48439));
        pointList.add(new LatLng(40.000466, 116.463384));
        pointList.add(new LatLng(39.975426, 116.490079));
        pointList.add(new LatLng(40.016392, 116.464343));
        pointList.add(new LatLng(39.959215, 116.464882));
        pointList.add(new LatLng(39.962136, 116.495418));
        pointList.add(new LatLng(39.994012, 116.426363));
        pointList.add(new LatLng(39.960666, 116.444798));
        pointList.add(new LatLng(39.972976, 116.424517));
        pointList.add(new LatLng(39.951329, 116.455913));
        return pointList;
    }

    /**
     * 地图单击回调
     * @param latLng 单击点经纬度
     */
    @Override
    public void onMapClick(LatLng latLng) {
        //单击添加marker
        markerOverlay.addPoint(latLng);
    }

    /**
     * 地图加载完成回调
     */
    @Override
    public void onMapLoaded() {
        //添加MarkerOnerlay
        markerOverlay = new MarkerOverlay(aMap, getPointList(),center);
        markerOverlay.addToMap();
        markerOverlay.zoomToSpanWithCenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_center:
                zoomToSpanWithCenter();
                break;
            case R.id.btn_zoom:
                zoomToSpan();
            default:
                break;
        }
    }

    private void zoomToSpan() {
        markerOverlay.zoomToSpan();
    }

    private void zoomToSpanWithCenter() {
        markerOverlay.zoomToSpanWithCenter();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        markerOverlay.removeFromMap();
        mapView.onDestroy();
    }

    /**
     * 地图长按回调
     * @param latLng
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        //改变中心点位置
        center = latLng;
        markerOverlay.setCenterPoint(center);
    }

}
