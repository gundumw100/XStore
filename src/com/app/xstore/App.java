package com.app.xstore;

import java.util.ArrayList;
import java.util.List;

import amobile.android.barcodesdk.api.IWrapperCallBack;
import android.os.Handler;
import android.os.Message;

import com.app.model.GuaDan;
import com.app.model.User;
import com.app.model.UserInfo;
import com.app.model.WXYTImageServerInfo;
import com.app.printer.GprinterUtil;
import com.app.util.ScanUtil;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.base.app.BaseApp;
import com.widget.crash.CrashHelper;
import com.widget.crash.CustomErrorActivity;
import com.widget.iconify.Iconify;
import com.widget.iconify.font.FontAwesomeModule;

public class App extends BaseApp implements IWrapperCallBack {

	// 是否打印请求返回后的解密数据，控制着Commands.java测试和正式地址，发布的时候必须false
	public static boolean isLog=true;//
	
	//QQ万象优图参数
	public static int APP_ID_V2 = 10009153;// 10000001
	public static String SECRET_ID_V2 = "AKIDfSSfovfua0dim2D6lbDo9uFHOQ29q1cO";// AKIDNZwDVhbRtdGkMZQfWgl2Gnn1dhXs95C0
	public static String SECRET_KEY_V2 = "leHXiJjxZeVYToYyYjKN2UXEKXqYkyfs";// ZDdyyRLCLv1TkeYOl5OCMLbyH4sJ40wp
	public static String BUCKET = "zstore"; // 空间名 testa
	public static final boolean NEED_CHECK_PORN = false; // 是否有必要检查porn图片
		
	public static User user = new User();
	public static ScanUtil scanUtil;

	public static final String EVENT_UPDATE_INVENTORY="updateInventory";
	public static final String EVENT_CLEAR_SHELF="clearShelf";
	public static final String EVENT_UPDATE_DOCUMENT_LIST="updateDocs";
	public static final String EVENT_UPDATE_PANDIAN_LIST="updatePanDian";
	public static final String EVENT_UPDATE_CUSTOM_PANDIANDAN_LIST="updateCustomPanDianDan";
	public static final String EVENT_FINISH="finish";
	public static final String EVENT_CLEAR="clear";
	public static final String EVENT_REFRESH="refresh";
	public static final String EVENT_UPDATE_DOCUMENT_LIST_FROM_SERVER="updateDocumentListFromServer";
	public static final String EVENT_UPDATE_DOCUMENT_DETAIL="updateDocumentDetail";
	public static final String EVENT_SAVE_FITTING="saveFitting";
	public static final String EVENT_EXPORT_FITTING="exportFitting";
	public static final String EVENT_UPDATE_FITTING="updateFitting";
	public static final String EVENT_UPDATE_SPACE="updateSpace";
	public static final String EVENT_SELECT_PICTURE="selectPicture";
//	public static final String EVENT_UPDATE_BOX="updateBox";
	public static final String EVENT_UPDATE_BOX2="updateBox2";
	
	public static final String KEY_CHECK_SETTING="checksetting";//校验设置
	public static List<UserInfo> userInfos;
	public static ArrayList<GuaDan> dans=new ArrayList<GuaDan>();//挂单列表
	public static ArrayList<ProductDangAn> shoppingCartItems=new ArrayList<ProductDangAn>();//收银台预加载的商品
	
	public static WXYTImageServerInfo qqCloudInfo;//QQ云图相关信息
	public static String KEY_HAS_DATA="HAS_DATA";
	
	public static GprinterUtil printerUtil;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initCrash();
		initScan();
		initIconify();
		
		printerUtil=GprinterUtil.getInstance(this);
	}

	private void initIconify(){
		Iconify
        .with(new FontAwesomeModule());
//        .with(new EntypoModule())
//        .with(new TypiconsModule())
//        .with(new MaterialModule())
//        .with(new MeteoconsModule())
//        .with(new WeathericonsModule())
//        .with(new SimpleLineIconsModule())
//        .with(new IoniconsModule());
	}
	
	private void initScan() {
		//只有G0550和n5000,Android才具有扫描功能
		if(App.config.getModel().equals("M5")){
			App.user.setPhoneType(0);
		}
		else if(App.config.getModel().equals("G0550")){
			App.user.setPhoneType(1);
			scanUtil = new ScanUtil(this, false);
		}else if(App.config.getModel().equals("Android")){
			App.user.setPhoneType(2);
		}
		else{
			App.user.setPhoneType(-1);//不支持扫描
		}
	}

	private void initCrash(){
		//如果设置了错误捕获，需要在子工程的manifest.xml中添加:
		//<activity android:process=":error_activity" android:name="com.base.app.DefaultErrorActivity" android:screenOrientation="portrait"/>
        //<activity android:process=":error_activity" android:name="com.base.app.CustomErrorActivity" android:screenOrientation="portrait"/>
		//自定义错误显示界面，如果没有设置指定界面，则显示默认的DefaultErrorActivity界面
		CrashHelper.setErrorActivityClass(CustomErrorActivity.class);
		CrashHelper.install(this);
	}
	
	@Override
	public void onDataReady(String result) {
		// TODO Auto-generated method stub
		Handler handler = scanUtil.getHandler();
		if (handler == null) {
			return;
		}
		Message message = handler.obtainMessage();
		// message.obj = result;
		message.obj = result.replaceAll("true", "");
		handler.sendMessage(message);
	}

}
