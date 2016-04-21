package test11.mylocation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import overlayutil.BikingRouteOverlay;
import overlayutil.DrivingRouteOverlay;
import overlayutil.OverlayManager;
import overlayutil.TransitRouteOverlay;
import overlayutil.WalkingRouteOverlay;

/**
 * Created by 123 on 2016/4/3.
 */
public class SearchActivity extends Activity implements OnGetRoutePlanResultListener,BaiduMap.OnMapClickListener{

    // 浏览路线相关
    public OverlayManager routeOverlay = null;
    private boolean useDefaultIcon = false;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    // 搜索相关
    private RoutePlanSearch mSearch = null;
    private String cityname="北京";
    private final String SORRY="抱歉，未找到结果";
    private final String AMBIGUITY="地址有歧义，请重新输入";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        mMapView = (MapView) findViewById(R.id.search_map);
        mBaiduMap = mMapView.getMap();
        // 地图点击事件处理
        mBaiduMap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    /**
     * 发起路线规划搜索
     */
    public void searchButtonProcess(View v) {
        mBaiduMap.clear();
        // 处理搜索按钮响应
        EditText editSt = (EditText) findViewById(R.id.editText_start);
        EditText editEn = (EditText) findViewById(R.id.editText_end);
        EditText editCy = (EditText) findViewById(R.id.editText_city);
        // 设置起终点信息
        if(editCy.getText()!=null) {
            cityname =editCy.getText().toString();
        }
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(cityname, editSt.getText().toString());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(cityname, editEn.getText().toString());


        if (v.getId() == R.id.drive) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else if (v.getId() == R.id.transit) {
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode).city(cityname).to(enNode));
        } else if (v.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else if (v.getId() == R.id.bike) {
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
    }




    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            if (result!=null&&result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //地名有歧义
                Toast.makeText(SearchActivity.this,AMBIGUITY,Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(SearchActivity.this, SORRY, Toast.LENGTH_SHORT).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            if (result!=null&&result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //地名有歧义
                Toast.makeText(SearchActivity.this,AMBIGUITY,Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(SearchActivity.this, SORRY, Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            if (result!=null&&result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //地名有歧义
                Toast.makeText(SearchActivity.this,AMBIGUITY,Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(SearchActivity.this, SORRY, Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            if (result!=null&&result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //地名有歧义
                Toast.makeText(SearchActivity.this,AMBIGUITY,Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(SearchActivity.this,SORRY, Toast.LENGTH_SHORT).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public  MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }


    }

    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }
}
