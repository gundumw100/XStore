package com.app.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.xstore.BaseActivity;
import com.base.util.T;
import com.sosopay.SosopayClient;
import com.sosopay.SosopayResponse;
import com.sosopay.excption.SosopayApiException;
import com.sosopay.factory.SosopayAPIClientFactory;
import com.sosopay.request.SosopayTradeRefundRequest;
import com.sosopay.response.SosopayTradeRefundResponse;

/**
 * 退款接口
 * @author Ni Guijun
 *
 */
public class SosopayTradeRefundRequestTask extends AsyncTask<Integer, Integer, SosopayTradeRefundResponse> {

	private Context context;
	private String busiCode;
	private String operid;
	private String devid;
	private double refundFee;
	private String refundSubject;
	private String chargeCode;
	private String chargeDownCode;
	private String outRefundNo;
	
	public SosopayTradeRefundRequestTask(Context context,String busiCode,String operid,String devid,double refundFee,String refundSubject,String chargeCode,String chargeDownCode,String outRefundNo,OnResponseListener onResponseListener){
		super();
		this.context=context;
		this.busiCode=busiCode;
		this.operid=operid;
		this.devid=devid;
		this.refundFee=refundFee;
		this.refundSubject=refundSubject;
		this.chargeCode=chargeCode;
		this.chargeDownCode=chargeDownCode;
		this.outRefundNo=outRefundNo;
		this.onResponseListener=onResponseListener;
	}
	
	@Override
	protected SosopayTradeRefundResponse doInBackground(Integer... params) {
		SosopayTradeRefundRequest request = new SosopayTradeRefundRequest();
		request.setBusiCode(busiCode);//商户编号由快收分配
		request.setOperid(operid);//操作员编号
		request.setDevid(devid);//设备编号
		request.setRefundFee(refundFee);//退款金额
		request.setRefundSubject(refundSubject);//退款描述
		request.setChargeCode(chargeCode);//上行流水号（需要唯一）
		request.setChargeDownCode(chargeDownCode);//交易下行流水号
		request.setOutRefundNo(outRefundNo);//商户退款流水号（需要唯一）
		SosopayClient client = SosopayAPIClientFactory.getSosopayClient(
				SosopayRequest.BUSI_CODE, SosopayRequest.PRIVATE_KEY, SosopayRequest.SERVICE_URL, SosopayRequest.ENCRY_TYPE);

		// 报文不加密
		// SosopayClient client = SosopayAPIClientFactory.getAlipayClient(busiCode,privateKye,serviceUrl);
		SosopayTradeRefundResponse response=null;
		try {
			response = client.execute(request);
		} catch (SosopayApiException e) {
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
    protected void onPostExecute(SosopayTradeRefundResponse response) {
    	((BaseActivity)context).closeProgress();
    	if(response==null){
    		return;
    	}
		if("SUCCESS".equals(response.getResult().getCode())){
			if(onResponseListener!=null){
				onResponseListener.onResponse(response);
			}
		}else{
			Log.i("tag", response.getResult().getInfo());
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
    }  

    private OnResponseListener onResponseListener;
    
    public void setOnResponseListener(OnResponseListener onResponseListener) {
		this.onResponseListener = onResponseListener;
	}

	public interface OnResponseListener{
    	public void onResponse(SosopayResponse res);
    }

	
}
