package com.app.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.Listener;
import com.app.model.response.WeatherResponse;
import com.app.xstore.BaseActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.base.net.VolleyHelper;

/**
 * 获得当前城市的天气情况
 * 需要两个步骤：
 * 
 * 1，通过百度定位当前城市；（需要百度定位包）
 * 2，通过百度天气查询当前城市的天气;（需要百度天气地址，比如：http://api.map.baidu.com/telematics/v3/weather?location=%E4%B8%8A%E6%B5%B7%E5%B8%82&output=json&ak=D6804d8703d8e4578e525111771929ba）
 * 
 * 
 * 详见：
 * http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/getloc
 * 
 * @author Ni Guijun
 *
 */
public class WeatherUtil {

	private Context context;
	// 城市，多个城市用“|”隔开，中文需要转成application/x-www-form-urlencoded字符串
	private static String location;
	//输出格式
	private static final String output="json";
	//百度Key,需要申请，同<meta-data android:name="com.baidu.lbsapi.API_KEY" android:value="D6804d8703d8e4578e525111771929ba" />
	private static final String ak="D6804d8703d8e4578e525111771929ba";
	
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean isLocationSuccess=false;
	
	public WeatherUtil(Context context){
		this.context=context;
		initLocation();
	}
	
	private void initLocation() {
		mLocationClient = new LocationClient(context);     //声明LocationClient类
		mLocationClient.registerLocationListener( myListener );    //注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
//		int span = 1000;
//		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(false);// 可选，默认false,设置是否使用gps
//		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
//		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
		
	}	
	public void start(){
		if(mLocationClient!=null){
			mLocationClient.start();
		}
	}
	public void stop(){
		if(mLocationClient!=null){
			mLocationClient.stop();
		}
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			sb.append("\nProvince : ");
//			sb.append(location.getProvince());
//			sb.append("\nCity : ");
//			sb.append(location.getCity());
//			sb.append("\nDistrict : ");
//			sb.append(location.getDistrict());
//			sb.append("\nStreet : ");
//			sb.append(location.getStreet());
//			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());// 单位：公里每小时
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//				sb.append("\nheight : ");
//				sb.append(location.getAltitude());// 单位：米
//				sb.append("\ndirection : ");
//				sb.append(location.getDirection());// 单位度
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				sb.append("\ndescribe : ");
//				sb.append("gps定位成功");
//
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				// 运营商信息
//				sb.append("\noperationers : ");
//				sb.append(location.getOperators());
//				sb.append("\ndescribe : ");
//				sb.append("网络定位成功");
//			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//				sb.append("\ndescribe : ");
//				sb.append("离线定位成功，离线定位结果也是有效的");
//			} else if (location.getLocType() == BDLocation.TypeServerError) {
//				sb.append("\ndescribe : ");
//				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//				sb.append("\ndescribe : ");
//				sb.append("网络不同导致定位失败，请检查网络是否通畅");
//			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//				sb.append("\ndescribe : ");
//				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//			}
//			sb.append("\nlocationdescribe : ");
//			sb.append(location.getLocationDescribe());// 位置语义化信息
//			List<Poi> list = location.getPoiList();// POI数据
//			if (list != null) {
//				sb.append("\npoilist size = : ");
//				sb.append(list.size());
//				for (Poi p : list) {
//					sb.append("\npoi= : ");
//					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//				}
//			}
//			Log.i("tag", sb.toString());
			
			if(!isLocationSuccess){//只要定位成功一次就可以了
				isLocationSuccess=true;
				doCommandWeather(context, location.getCity(), new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						WeatherResponse obj=((BaseActivity)context).mapperToObject(response, WeatherResponse.class);
						if(obj!=null&&obj.getError()==0){
							if(onWeatherListener!=null){
								onWeatherListener.onWeather(obj);
							}
						}
					}
					
				} , false);
			}
			
		}
	}
	
	
	/**
	 * 通过百度获得天气情况
	 * @param context
	 * @param city
	 * @param onSuccessListener
	 * @param showProgress
	 */
	private static void doCommandWeather(Context context, String city,
			Listener<JSONObject> onSuccessListener,boolean showProgress) {
		location=toEncode(city);
		String url="http://api.map.baidu.com/telematics/v3/weather?location="+location+"&output="+output+"&ak="+ak+"";
		Map<String, String> map = new HashMap<String, String>();
		VolleyHelper.execPostRequest(context, null,url, map, onSuccessListener, showProgress);
	}
	
	/**
	 * 将普通字符串转换成application/x-www-form-urlencoded字符串
	 * 比如： 上海市就是%E4%B8%8A%E6%B5%B7%E5%B8%82
	 * @param text
	 * @return
	 */
	private static String toEncode(String text){
		String encode=null;
		try {
			encode=java.net.URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encode;
	}
	
	
	private OnWeatherListener onWeatherListener;

	public void setOnWeatherListener(OnWeatherListener onWeatherListener){
		this.onWeatherListener=onWeatherListener;
	}
	
	public interface OnWeatherListener {
		public void onWeather(WeatherResponse obj);
	}
	
	
}
