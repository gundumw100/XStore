package com.app.net;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.xstore.BaseActivity;
import com.base.util.T;
import com.sosopay.SosopayClient;
import com.sosopay.SosopayResponse;
import com.sosopay.excption.SosopayApiException;
import com.sosopay.factory.SosopayAPIClientFactory;
import com.sosopay.request.SosopayTradePrecreateRequest;
import com.sosopay.response.SosopayTradePrecreateResponse;
import com.sosopay.vo.GoodsInfo;

/**
 * 主扫支付交易发起（客户扫商家）
 * @author Ni Guijun
 *
 */
public class SosopayTradePrecreateTask extends AsyncTask<Integer, Integer, SosopayTradePrecreateResponse> {

	private Context context;
	private String busiCode;
	private String operid;
	private String devid;
	private String storeid;
	private double amt;
	private int channelType;
	private String chargeCode;
	private String notifyUrl;
	private String paySubject;
	private ArrayList<GoodsInfo> goodsInfos;
	
	public SosopayTradePrecreateTask(Context context,String busiCode,String operid,String devid,String storeid,double amt,int channelType,String chargeCode,String notifyUrl,String paySubject,ArrayList<GoodsInfo> goodsInfos,OnResponseListener onResponseListener){
		super();
		this.context=context;
		this.busiCode=busiCode;
		this.operid=operid;
		this.devid=devid;
		this.storeid=storeid;
		this.amt=amt;
		this.channelType=channelType;
		this.chargeCode=chargeCode;
		this.notifyUrl=notifyUrl;
		this.paySubject=paySubject;
		this.goodsInfos=goodsInfos;
		this.onResponseListener=onResponseListener;
	}
	
	@Override
	protected SosopayTradePrecreateResponse doInBackground(Integer... params) {
		SosopayTradePrecreateRequest request=new SosopayTradePrecreateRequest();
		request.setBusiCode(busiCode);//商户编号由快收分配
		request.setOperid(operid);//操作员编号
		request.setDevid(devid);//设备编号
//		request.setMerchantPara("");//用户参数，采用 base64编码，接口返回原样返回
		//请求失败!受理机构必须传入sub_mch_id
//		request.setStoreid(storeid);//门店编号 REGISTER_ID？？？
		request.setAmt(amt);//交易金额，元
		request.setChannelType(channelType);//请参见附录 2 支付渠道数据字典 1支付宝 2微信 3招商银行...
		request.setChargeCode(chargeCode);//交易流水号
		request.setNotifyUrl(notifyUrl);//异步通知地址,商户通知接口地址?_type=json&encrypt=3DES&sign={sign}
		request.setPaySubject(paySubject);//支付信息描述
		request.setGoodsInfos(goodsInfos);//商品明细
//		request.setGoods(goods1);//商品明细
		
		SosopayClient client = SosopayAPIClientFactory.getSosopayClient(
				SosopayRequest.BUSI_CODE, SosopayRequest.PRIVATE_KEY, SosopayRequest.SERVICE_URL, SosopayRequest.ENCRY_TYPE);

		// 报文不加密
		// SosopayClient client = SosopayAPIClientFactory.getAlipayClient(busiCode,privateKye,serviceUrl);
		SosopayTradePrecreateResponse response=null;
		try {
			response = client.execute(request);
		} catch (SosopayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	 /**  
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）  
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置  
     */  
    @Override  
    protected void onPostExecute(SosopayTradePrecreateResponse response) {
    	((BaseActivity)context).closeProgress();
    	if(response==null){
    		return;
    	}
		if("SUCCESS".equals(response.getResult().getCode())){
			if(onResponseListener!=null){
				onResponseListener.onResponse(response);
			}
		}else{
			T.showToast(context, response.getResult().getInfo());
		}
		
    }  
  
  
    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置  
    @Override  
    protected void onPreExecute() {  
    	((BaseActivity)context).showProgress();
    }  
  
  
    /**  
     * 这里的Intege参数对应AsyncTask中的第二个参数  
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行  
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作  
     */  
    @Override  
    protected void onProgressUpdate(Integer... values) {  
//        int vlaue = values[0];
//        Log.i("tag", "vlaue-->"+vlaue);
    }  
    
    private OnResponseListener onResponseListener;
    
    public void setOnResponseListener(OnResponseListener onResponseListener) {
		this.onResponseListener = onResponseListener;
	}

	public interface OnResponseListener{
    	public void onResponse(SosopayResponse res);
    }
}
