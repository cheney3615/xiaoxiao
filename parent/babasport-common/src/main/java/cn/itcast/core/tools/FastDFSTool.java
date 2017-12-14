package cn.itcast.core.tools;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import cn.itcast.core.dictionary.Constants;

/**
 * 分布式文件系统工具类
 * 
 * @author Administrator
 *
 */
public class FastDFSTool {

	/**
	 * 上传文件到分布式文件系统中
	 * 
	 * @param bs
	 *            文件的字节数组
	 * @param filename
	 *            文件名
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String uploadFile(byte[] bs, String filename)
			throws FileNotFoundException, IOException, Exception {

		// 将该文件存入fastdfs中，并取得存储的路径，并返回给客户端

		// 获得classpath下文件的绝对路径
		ClassPathResource classPathResource = new ClassPathResource(
				"fdfs_client.conf");
		String path = classPathResource.getClassLoader()
				.getResource("fdfs_client.conf").getPath();

		// 客户端全局初始化，参数：关于fastdfs客户端配置文件的地址
		ClientGlobal.init(path);

		// 创建老大客户端
		TrackerClient trackerClient = new TrackerClient();

		// 通过老大客户端取得连接获得老大服务器端
		TrackerServer connection = trackerClient.getConnection();

		// 小弟客户端
		StorageClient1 storageClient1 = new StorageClient1(connection, null);

		// 取得file文件的原始扩展名
		String extension = FilenameUtils.getExtension(filename);

		// 上传文件到分布式文件系统中
		String upload_file1 = storageClient1.upload_file1(bs, extension, null);

		System.out.println(upload_file1);

		String filePath = Constants.FDFS_SERVER + upload_file1;

		return filePath;
	}

}
