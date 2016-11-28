# android-map-zoomtospan
地图内容全览

本工程为基于高德地图Android SDK进行封装，实现通过缩放保证地图上自定义内容（模拟为点）均展现在视野范围内, 并且地图中心点不被改变。。
## 前述 ##
- [高德官网申请Key](http://lbs.amap.com/dev/#/).
- 阅读[参考手册](http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html).
- 工程基于3D地图SDK实现

##效果展示##
![Screenshot]( https://github.com/amap-demo/android-map-zoomtospan/raw/master/apk/Screenshot.png )

## 扫一扫安装##
![Screenshot]( https://github.com/amap-demo/android-map-zoomtospan/raw/master/apk/1480320135.png )

## 使用方法##
###1:配置搭建AndroidSDK工程###
- [Android Studio工程搭建方法](http://lbs.amap.com/api/android-sdk/guide/creat-project/android-studio-creat-project/#add-jars).
- [通过maven库引入SDK方法](http://lbsbbs.amap.com/forum.php?mod=viewthread&tid=18786).

## 核心类/接口 ##
| 类    | 接口  | 说明   |
| -----|:-----:|:-----:|
| CameraUpdateFactory | newLatLngBounds(LatLngBounds bounds, int padding) | 返回CameraUpdate对象，这个对象包含一个经纬度限制的区域，并且是最大可能的缩放级别。你可以设置一个边距数值来控制插入区域与view的边框之间的空白距离。方法必须在地图初始化完成之后使用。 |
| LatLngBounds | 	including(LatLng point) | 返回一个新的矩形区域。新区域是根据传入的经纬度对原有区域进行最小的扩展。多次调用此方法即可。 这个方法将选择向东或向西方向扩展，期间扩展面积相对较小一个区域。如果相同，则优先向东方向扩展。 |

## 核心难点 ##
```
mTimerTask = new circleTask(circle, 1000);
mTimer.schedule(mTimerTask, 0, 30);
/* 外圈动画. */
private  class circleTask extends TimerTask {
        private double r;
        private Circle circle;
        private long duration = 1000;

        public circleTask(Circle circle, long rate){
            this.circle = circle;
            this.r = circle.getRadius();
            if (rate > 0 ) {
                this.duration = rate;
            }
        }
        @Override
        public void run() {
            try {
                long elapsed = SystemClock.uptimeMillis() - start;
                float input = (float)elapsed / duration;
//                外圈放大后消失
                float t = interpolator1.getInterpolation(input);
                double r1 = (t + 1) * r;
                circle.setRadius(r1);
                if (input > 2){
                    start = SystemClock.uptimeMillis();
                }
            } catch (Throwable e) {
               e.printStackTrace();
            }
        }
    }
}
```
