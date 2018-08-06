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
import com.sosopay.request.SosopayTradeQueryRequest;
import com.sosopay.response.SosopayTradeQueryResponse;

/**
 * 查询详情接口
 * @author Ni Guijun
 *
 */
public class SosopayTradeQueryRequestTask extends AsyncTask<Integer, Integer, SosopayTradeQueryResponse> {

	private Context context;
	private String chargeCode;
	
	public SosopayTradeQueryRequestTask(Context context,String chargeCode,OnResponseListener onResponseListener){
		super();
		this.context=context;
		this.chargeCode=chargeCode;
		this.onResponseListener=onResponseListener;
	}
	
	@Override
	protected SosopayTradeQueryResponse doInBackground(Integer... params) {
		SosopayTradeQueryRequest request = new SosopayTradeQueryRequest();
		request.setChargeCode(chargeCode);
		
		SosopayClient client = SosopayAPIClientFactory.getSosopayClient(
				SosopayRequest.BUSI_CODE, SosopayRequest.PRIVATE_KEY, SosopayRequest.SERVICE_URL, SosopayRequest.ENCRY_TYPE);

		// 报文不加密
		// SosopayClient client = SosopayAPIClientFactory.getAlipayClient(busiCode,privateKye,serviceUrl);
		SosopayTradeQueryResponse response=null;
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
    protected void onPostExecute(SosopayTradeQueryResponse response) {
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
