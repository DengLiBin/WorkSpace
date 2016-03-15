package com.bin.Tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 *获取输入流中的数据 
 */
public class StreamTool {
	public static byte[] read(InputStream inStream)throws Exception {
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		while((len=inStream.read(buffer))!=-1){
			outStream.write(buffer, 0, len);//强字节数组中的数据 写入到内存中
		}
		inStream.close();
		return outStream.toByteArray();
		
	}
}
