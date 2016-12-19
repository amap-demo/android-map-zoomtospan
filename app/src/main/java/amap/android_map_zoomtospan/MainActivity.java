package amap.android_map_zoomtospan;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMap.OnMapClickListener, AMap.OnMapLoadedListener, View.OnClickListener, AMap.OnMapLongClickListener {
    private AMap aMap;
    private MapView mapView;
    private Marker centerMarker;
    private LatLng center = new LatLng(39.993167, 116.473274);// 中心点
    private List<LatLng> pointList = new ArrayList<LatLng>();
    private Button btn;

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
        poiListinit();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMapClickListener(this);
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapLongClickListener(this);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    private void poiListinit() {
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
    }

    @Override
    public void onMapClick(LatLng latLng) {
        aMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        pointList.add(latLng);
    }

    @Override
    public void onMapLoaded() {
        //添加中心点Marker，具体应用中可以是定位点
        centerMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(), R.drawable.pointnew)))
                .position(center)
                .title("中心点"));
        centerMarker.showInfoWindow();
        //添加点
        addToMap();
        zoomToSpan(center, pointList);
    }

    /**
     * 添加Marker到地图中。
     */
    public void addToMap() {
        for (int i = 0; i < pointList.size(); i++) {
            aMap.addMarker(new MarkerOptions()
                    .position(pointList.get(i))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    //缩放移动地图，保证所有自定义marker在可视范围中，且地图中心点不变。
    public void zoomToSpan(LatLng center, List<LatLng> pointList) {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds(center, pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds(LatLng center, List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < pointList.size(); i++) {
            LatLng p = pointList.get(i);
            LatLng p1 = new LatLng((center.latitude * 2) - p.latitude, (center.longitude * 2) - p.longitude);
            b.include(p);
            b.include(p1);
        }
        return b.build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                zoomToSpan(center, pointList);
                break;
            default:
                break;
        }
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
        mapView.onDestroy();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        center = latLng;
        centerMarker.setPosition(center);
    }

}
