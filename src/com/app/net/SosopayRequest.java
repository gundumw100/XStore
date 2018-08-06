package com.app.net;

import com.sosopay.SosopayConstants;

public class SosopayRequest {

	public static final String BUSI_CODE="0010000001";//商户编号由快收分配
	public static final String PRIVATE_KEY="98SDKFUO432HNODS098EEE07U9DUOIUE";//商户秘钥
//	public static final String SERVICE_URL = "https://www.sssyin.cn:7000/openGateway/openService/";//正式环境
//	public static final String SERVICE_URL = "http://test.sssyin.cn:7500/openGateway/openService/";//测试环境
	public static final String SERVICE_URL = "http://uat.sssyin.cn:9000/openGateway/openService/";//UAT环境
	public static final String ENCRY_TYPE = SosopayConstants.ENCRY_TYPE_3DES;//报文加密
	
	
//	/**退款状态-0：待付款*/
//	public static final int WAIT_PAY = 0;
//	/**退款状态-1：已付款*/
//	public static final int PAIED = 1;
//	/**退款状态-2：已撤单*/
//	public static final int CANCELED = 2;
//	/**退款状态-3：申请撤单*/
//	public static final int APPLY_REFUND = 3;
//	/**退款状态-4：部分退款*/
//	public static final int PART_REFOUNDED = 4;
//	/**退款状态-5：已退款*/
//	public static final int REFOUNDED = 5;
//	/**退款状态-6：交易关闭*/
//	public static final int TRADE_CLOSED = 6;
//	/**退款状态-7：查询时预留，同时查询已退款和已撤单*/
//	public static final int QUERY_BACK = 7;
//	/**退款状态-8：退款中*/
//	public static final int REFUND_ING = 8;
//	/**退款状态-9：退款失败*/
//	public static final int REFOUNDED_FAIL = 9;
//	/**退款状态-10：待退款*/
//	public static final int START_REFOUNDED_APPLY = 10;
//	/**退款状态-11：退款成功*/
//	public static final int START_REFOUNDED_SUCCESS = 11;
//	/**退款状态-12：退款失败*/
//	public static final int START_REFOUNDED_FAIL = 12;
//	/**退款状态-13：退款取消*/
//	public static final int START_REFOUNDED_CANCELED = 13;
//	/**退款状态-14：撤单成功*/
//	public static final int START_CANCELED_SUCCESS = 14;
	
}
