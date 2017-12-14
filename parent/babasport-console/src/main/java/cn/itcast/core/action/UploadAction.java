package cn.itcast.core.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import cn.itcast.core.dictionary.Constants;
import cn.itcast.core.tools.FastDFSTool;

/**
 * 上传文件控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class UploadAction {

	// 上传单个文件
	@RequestMapping(value = "/uploadFile.do")
	@ResponseBody
	public Map<String, String> uploadFile(MultipartFile mpf) throws Exception {
		// 打印文件的原始名称
		System.out.println(mpf.getOriginalFilename());

		// 上传文件到分布式文件系统中
		String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(),
				mpf.getOriginalFilename());

		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("path", uploadFile);

		return hashMap;
	}

	// 上传多个个文件
	@RequestMapping(value = "/uploadFiles.do")
	@ResponseBody
	public List<String> uploadFiles(@RequestParam MultipartFile[] mpfs)
			throws FileNotFoundException, IOException, Exception {

		List<String> al = new ArrayList<String>();

		for (MultipartFile mpf : mpfs) {
			// 上传文件到分布式文件系统中
			String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(),
					mpf.getOriginalFilename());
			al.add(uploadFile);
		}

		return al;
	}

	// 上传富文本编辑器的文件
	// 接收富文本编辑器传递的图片(无敌版：不考虑文件的name，强行接收)
	@RequestMapping(value = "/uploadFck.do")
	@ResponseBody
	public Map<String, Object> uploadFck(HttpServletRequest request)
			throws Exception {

		// 将request强转为spring提供的MultipartRequest
		MultipartRequest mr = (MultipartRequest) request;

		// 获得MultipartRequest里面的所有文件
		Set<Entry<String, MultipartFile>> entrySet = mr.getFileMap().entrySet();

		Map<String, Object> hashMap = new HashMap<String, Object>();
		
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile mpf = entry.getValue();

			// 上传文件到分布式文件系统中
			String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(),
					mpf.getOriginalFilename());
			
			// error和url名字都是固定死的
			hashMap.put("error", 0);
			hashMap.put("url", uploadFile);
			
		}
		return hashMap;
	}

}
