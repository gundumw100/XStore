package com.app.net;

import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.xstore.BaseActivity;
import com.base.util.T;
import com.sosopay.SosopayClient;
import com.sosopay.SosopayResponse;
import com.sosopay.excption.SosopayApiException;
import com.sosopay.factory.SosopayAPIClientFactory;
import com.sosopay.request.SosopayTradeBatchQueryRequest;
import com.sosopay.response.SosopayTradeBatchQueryResponse;

/**
 * 查询交易列表接口
 * 
 * @author Ni Guijun
 *
 */
public class SosopayTradeBatchQueryRequestTask extends AsyncTask<Integer, Integer, SosopayTradeBatchQueryResponse> {

	private Context context;
	private String userId;
	private int state;
	private Date beginDate,endDate;
	private int pageNum,pageSize;
	
	public SosopayTradeBatchQueryRequestTask(Context context,String userId,int state,Date beginDate,Date endDate,int pageNum,int pageSize,OnResponseListener onResponseListener){
		super();
		this.context=context;
		this.userId=userId;
		this.state=state;
		this.beginDate=beginDate;
		this.endDate=endDate;
		this.pageNum=pageNum;
		this.pageSize=pageSize;
		this.onResponseListener=onResponseListener;
	}
	
	@Override
	protected SosopayTradeBatchQueryResponse doInBackground(Integer... params) {
		SosopayTradeBatchQueryRequest request = new SosopayTradeBatchQueryRequest();
		request.setuserId(userId);
		if(state>=0){
			request.setState(state);//订单状态
		}
		request.setBeginDate(beginDate);
		request.setEndDate(endDate);
		request.setPageNum(pageNum);
		request.setPageSize(pageSize);
//		request.setBusiCode(value);//商户编号
//		request.setChargeCode(chargeCode);//商户流水号
//		request.setChannelType(channelType);//交易渠道
		
//		SosopayClient client = SosopayAPIClientFactory.getSosopayClient(
//				SosopayRequest.BUSI_CODE, SosopayRequest.PRIVATE_KEY, SosopayRequest.SERVICE_URL, SosopayRequest.ENCRY_TYPE);

		// 报文不加密
		SosopayClient client = SosopayAPIClientFactory.getSosopayClient(SosopayRequest.BUSI_CODE, SosopayRequest.PRIVATE_KEY, SosopayRequest.SERVICE_URL);
		SosopayTradeBatchQueryResponse response=null;
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
    protected void onPostExecute(SosopayTradeBatchQueryResponse response) {
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
