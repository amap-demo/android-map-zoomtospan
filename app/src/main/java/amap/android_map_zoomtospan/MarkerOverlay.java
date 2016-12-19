package amap.android_map_zoomtospan;

import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/12/19.
 */

public class MarkerOverlay {
    private List<LatLng> pointList = new ArrayList<LatLng>();
    private AMap aMap;
    private LatLng centerPoint;
    private Marker centerMarker;
    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();

    public MarkerOverlay(AMap amap, List<LatLng> points, LatLng centerpoint) {
        this.aMap = amap;
        this.centerPoint = centerpoint;
        initPointList(points);
        initCenterMarker();
    }

    //初始化list
    private void initPointList(List<LatLng> points) {
        if (points != null && points.size() > 0){
            for (LatLng point : points){
                pointList.add(point);
            }
        }
    }

    //初始化中心点Marker
    private void initCenterMarker() {
        this.centerMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.point6))
                .position(centerPoint)
                .title("中心点"));
        centerMarker.showInfoWindow();
    }

    /**
     * 设置改变中心点经纬度
     * @param centerpoint 中心点经纬度
     */
    public void setCenterPoint(LatLng centerpoint) {
        this.centerPoint = centerpoint;
        if(centerMarker == null)
            initCenterMarker();
        this.centerMarker.setPosition(centerpoint);
        centerMarker.setVisible(true);
        centerMarker.showInfoWindow();
    }
    /**
     * 添加Marker到地图中。
     */
    public void addToMap() {
        try{
            for (int i = 0; i < pointList.size(); i++) {
                Marker marker = aMap.addMarker(new MarkerOptions()
                        .position(pointList.get(i))
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                marker.setObject(i);
                mMarkers.add(marker);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 去掉MarkerOverlay上所有的Marker。
     */
    public void removeFromMap() {
        for (Marker mark : mMarkers) {
            mark.remove();
        }
        centerMarker.remove();
    }

    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中，且地图中心点不变。
     */
    public void zoomToSpanWithCenter() {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            centerMarker.setVisible(true);
            centerMarker.showInfoWindow();
            LatLngBounds bounds = getLatLngBounds(centerPoint, pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds(LatLng centerpoint, List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (centerpoint != null){
            for (int i = 0; i < pointList.size(); i++) {
                LatLng p = pointList.get(i);
                LatLng p1 = new LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    /**
     *  缩放移动地图，保证所有自定义marker在可视范围中。
     */
    public void zoomToSpan() {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            centerMarker.setVisible(false);
            LatLngBounds bounds = getLatLngBounds(pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }
    /**
     * 根据自定义内容获取缩放bounds
     */
    private LatLngBounds getLatLngBounds( List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < pointList.size(); i++) {
             LatLng p = pointList.get(i);
             b.include(p);
         }
        return b.build();
    }

    /**
     * 添加一个Marker点
     * @param latLng 经纬度
     */
    public void addPoint(LatLng latLng) {
        pointList.add(latLng);
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        marker.setObject(pointList.size()-1);
        mMarkers.add(marker);
    }
}
