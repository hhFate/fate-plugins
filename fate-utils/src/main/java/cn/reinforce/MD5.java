package cn.reinforce;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

public class MD5 {
	
	private static Logger LOG = Logger.getLogger(MD5.class);
	
	private MD5() {}
	
	public static String getMd5ByFile(InputStream is, long skipBytes, long len)
			throws FileNotFoundException {
		String value = null;
		FileInputStream in;
		if (is instanceof FileInputStream){
			in = (FileInputStream) is;
		}else{
			return null;
		}
			
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(
					FileChannel.MapMode.READ_ONLY, skipBytes, len);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
			while(value.length()<32)
				value="0"+value;
		} catch (Exception e) {
			LOG.error(e);
		}
		return value==null?null:value.toUpperCase();
	}
	
	/**
     * 求一个字符串的md5值
     * @param target 字符串
     * @return md5 value
     */
    public static String md5(String target) {
        return DigestUtils.md5Hex(target);
    }
	
}
