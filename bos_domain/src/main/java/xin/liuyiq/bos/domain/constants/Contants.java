package xin.liuyiq.bos.domain.constants;

/**
 * 常量工具类
 * @author liuyiq
 *
 */
public class Contants {
	
	public static final String  BOS_MANAGEMENT_HOST = "http://localhost:9001";
	public static final String  CRM_MANAGEMENT_HOST = "http://localhost:9002";
	public static final String  BOS_FORE_HOST = "http://localhost:9003";
	public static final String  BOS_SMS_HOST = "http://localhost:9004";
	
	public static final String  BOS_MANAGEMENT_CONTEXT = "/bos_management";
	public static final String  CRM_MANAGEMENT_CONTEXT = "/crm_management";
	public static final String  BOS_FORE_CONTEXT = "/bos_fore";
	public static final String  BOS_SMS_CONTEXT = "/bos_sms";
	
	public static final String  BOS_MANAGEMENT_URL = BOS_MANAGEMENT_HOST+BOS_MANAGEMENT_CONTEXT;
	public static final String  CRM_MANAGEMENT_URL = CRM_MANAGEMENT_HOST+CRM_MANAGEMENT_CONTEXT;
	public static final String  BOS_FORE_URL = BOS_FORE_HOST+BOS_FORE_CONTEXT;
	public static final String  BOS_SMS_URL = BOS_SMS_HOST+BOS_SMS_CONTEXT;
	
	
	
	public static final String SUCCESS = "success";
	public static final String MSG = "msg";
	public static final String WAYBILL_DATA = "wayBillData";
	public static final String ORDER_DATA = "orderData";
}
