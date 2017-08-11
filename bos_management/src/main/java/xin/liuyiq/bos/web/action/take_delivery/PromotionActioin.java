package xin.liuyiq.bos.web.action.take_delivery;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.take_delivery.Promotion;
import xin.liuyiq.bos.service.take_delivery.PromotionService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionActioin extends BaseAction<Promotion> {

	private File titleImgFile;
	private String titleImgFileFileName;


	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	// promotion_save
	@Autowired
	private PromotionService promotionService;

	@Action(value = "promotion_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws Exception {
		// 在这将文件上传保存进来
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
		/**
		 * 保存一个图片  
		 * 		处理这个图片的名字防止重复名字
		 * 		把接收到的图片存在绝对路径中
		 * 		拷贝文件
		 */
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		
		String randomFileName = uuid + ext;
		 	
		// 拷贝到绝对路径
		FileUtils.copyFile(titleImgFile, new File(savePath +"/"+ randomFileName));
		
		// 然后封装到promotion对象中
		model.setTitleImg(saveUrl+randomFileName); 
		
		model.setStatus("1");
		promotionService.save(model);
		
		return SUCCESS;
	}

	// promotion_pageQuery.action
	@Action(value = "promotion_pageQuery", results = {@Result(name = "success", type = "json")})
	public String pageQuery() {
		Pageable pageable = new PageRequest(page-1,rows);

		Page page = promotionService.findAll(pageable);

		responseJsonDataToPage(page);

		return SUCCESS;
	}
}