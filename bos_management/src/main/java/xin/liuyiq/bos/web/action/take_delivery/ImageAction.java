package xin.liuyiq.bos.web.action.take_delivery;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.take_delivery.Promotion;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class ImageAction extends BaseAction<Promotion> {

	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
	private final Map<String, Object> result = new HashMap<String, Object>();
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	@Action(value = "image_upload", results = { @Result(name = "success", type = "json") })
	public String image_upload() throws Exception {
		// 保存路径
		// 这个是upload文件夹的磁盘路径
		// savePath =
		// D:/develope/sts-bundle/workspace_bos2.0/bos2.0/bos_management/src/main/webapp/upload

		String savePath = ServletActionContext.getServletContext().getRealPath("upload");
		// 这个是upload文件夹的相对于项目路径
		// saveUrl = /bos_management/upload/
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

		// 生成图片的名称
		UUID uuid = UUID.randomUUID();
		// 文件的后缀名
		String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));

		String randomFileName = uuid + ext;
		// 保存图片的文件 路径是绝对路径
		// D:/develope/sts-bundle/workspace_bos2.0/bos2.0/bos_management/src/main/webapp/upload/91881982-3229-478a-95ac-4a7795b0e44a.txt
		File destFile = new File(savePath + "/" + randomFileName);
		FileUtils.copyFile(imgFile, destFile);

		// 保存成功后返回结果
		
		this.result.put("error", 0);
		this.result.put("url", saveUrl + randomFileName);
		pushObjectToValueStack(this.result);
		return SUCCESS;
	}

	// image_manager
	@Action(value = "image_manager", results = { @Result(name = "success", type = "json") })
	public String manager() throws Exception {

		// 根目录路径，可以指定绝对路径，
		String rootPath = ServletActionContext.getServletContext().getRealPath("/upload/");
		// 根目录URL，可以指定绝对路径，
		String rootUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		File rootPathFile = new File(rootPath);

		// 遍历目录取的文件信息
		List<HashMap> fileList = new ArrayList<HashMap>();
		for (File file : rootPathFile.listFiles()) {
			HashMap<String, Object> hash = new HashMap<String, Object>();
			String fileName = file.getName();
			if (file.isDirectory()) {
				hash.put("is_dir", true);
				hash.put("has_file", (file.listFiles() != null));
				hash.put("filesize", 0L);
				hash.put("is_photo", false);
				hash.put("filetype", "");
			} else if (file.isFile()) {
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				hash.put("is_dir", false);
				hash.put("has_file", false);
				hash.put("filesize", file.length());
				hash.put("is_photo", Arrays.<String> asList(fileTypes).contains(fileExt));
				hash.put("filetype", fileExt);
			}
			hash.put("filename", fileName);
			hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
			fileList.add(hash);
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		pushObjectToValueStack(result);
		return SUCCESS;
	}

}
