package xin.liuyiq.bos.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5Utils {
	
	public static String getPassword(String password) throws NoSuchAlgorithmException{
		
		MessageDigest instance = MessageDigest.getInstance("MD5");
		// 参数就是要加密的字符串对应的字节数组,返回值就是加密以后的字节数组
		byte[] digest = instance.digest(password.getBytes());
		
		// 将字节数组中的数据右移一位
		for(int x = 0 ; x < digest.length ; x++){
			digest[x] = (byte) (digest[x] >> 1);
		}
		
		// MD5加密的基础上行16进制的处理
		char[] encodeHex = Hex.encodeHex(digest);
		
		// 把处理后的字节数据转换成字符串
		String crtPassword = new String(encodeHex);
		return crtPassword;
	}
}
