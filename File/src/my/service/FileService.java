package my.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;

public class FileService {
	private Context context;
	public FileService(Context context) {
		
		this.context = context;
	}
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param filecontent 文件内容
	 * @throws FileNotFoundException 
	 * 
	 */public void saveToSDCard(String filename, String content)throws Exception {
			File file = new File(Environment.getExternalStorageDirectory(), filename);
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(content.getBytes());
			outStream.close();
		}
	public void save(String filename, String filecontent) throws Exception{
		//获取输出流对象
		/*
		 * Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，
		 * 如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
		Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
		Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
		MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
		如果希望文件被其他应用读和写，可以传入： 
		openFileOutput("itcast.txt", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		 */
		FileOutputStream outStream=context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(filecontent.getBytes());//将文件内容（先编码成字节数组）写入到输出流中
		outStream.close();//将输出流中的数据刷到文件中，并关闭输出流,保存的路径：data/data/my.file/files/filename
	}
	public String read(String filename)throws Exception{
		FileInputStream inStream=context.openFileInput(filename);
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		while((len=inStream.read(buffer))!=-1){
			outStream.write(buffer,0,len);//将字节数组中的数据写入到输出流中
		}
		byte[] data=outStream.toByteArray();//将输出流中的字节数据存入到字节数组中
		return new String(data);//将字节数组转换成字符串
	}
}
