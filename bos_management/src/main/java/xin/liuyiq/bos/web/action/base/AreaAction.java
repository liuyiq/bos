package xin.liuyiq.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.base.Area;
import xin.liuyiq.bos.service.base.AreaService;
import xin.liuyiq.bos.utils.PinYin4jUtils;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area> {

	
	@Autowired
	private AreaService areaService;
	
	private File file ;
	
	public void setFile(File file) {
		this.file = file;
	}
	
	// area_batchImport
	@Action(value="area_batchImport")
	public String batchImport() throws IOException{
		List<Area> areas = new ArrayList<Area>(); 
		// 获取到每一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		// 或缺到每一个sheet
		HSSFSheet sheet = workbook.getSheetAt(0);
		// 遍历sheet 得到row
		for (Row row : sheet) {
			// 获取到每一列 如果有空的就跳过,而且还要跳过第一行
			// 第一行跳过 
			if(row.getRowNum() == 0){
				continue;
			}
			// 空行跳过
			if(row.getCell(0)==null || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			// 基于pinyin4j添加城市编码和简码
			
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province=province.substring(0, province.length()-1);
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			
			// 简码
			String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
			StringBuffer sb = new StringBuffer();
			for (String str : headByString) {
				sb.append(str);
			}
			area.setShortcode(sb.toString());
			
			// 城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);
			areas.add(area);
		}
		areaService.batchImport(areas);
		return NONE;
	}

	@Action(value="area_pageQuery",results = {@Result(name = SUCCESS,type = "json")})
	public String pageQuery(){
		
		// 构建分页对象 在baseAction 中已经存在
		Pageable pageable = new PageRequest(page-1,rows);
		// 构建查询条件对象
		Specification specification = new Specification() {
			
			List<Predicate> predicateList = new ArrayList<Predicate>();
			
			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
				// 判断条件
				if (StringUtils.isNotBlank(model.getProvince())){

                    Predicate p1 = cb.like(root.get("province").as(String.class), "%"+model.getProvince()+"%");
                    predicateList.add(p1);
                }

                if (StringUtils.isNotBlank(model.getCity())){
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%");
                    predicateList.add(p2);
                }

                if (StringUtils.isNotBlank(model.getDistrict())){
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    predicateList.add(p3);
                }
				return cb.and(predicateList.toArray(new Predicate[0]));
			}
		};

		Page<Area> pageBean = areaService.pageQuery(specification,pageable);

		responseJsonDataToPage(pageBean);
        return SUCCESS;
	}
	
	// area_export
	@Action(value = "area_export")
	public void exprot() throws Exception{
		// 查询到含有area的list 集合
		List<Area> areas = areaService.findAll();
		// 生成一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成sheet
		HSSFSheet sheet = workbook.createSheet("sheetName");
		// 往第一行里面填数据
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("区域编码");
		row.createCell(1).setCellValue("省份");
		row.createCell(2).setCellValue("市");
		row.createCell(3).setCellValue("县");
		row.createCell(4).setCellValue("邮政编码");
		row.createCell(5).setCellValue("城市编码");
		row.createCell(6).setCellValue("简码");
		// 往后面的内容填数据
		for(int i = 1 ; i <= areas.size() ; i++){
			Area area = areas.get(i-1);
			HSSFRow rows = sheet.createRow(i);
			rows.createCell(0).setCellValue(area.getId());
			rows.createCell(1).setCellValue(area.getProvince());
			rows.createCell(2).setCellValue(area.getCity());
			rows.createCell(3).setCellValue(area.getDistrict());
			rows.createCell(4).setCellValue(area.getPostcode());
			rows.createCell(5).setCellValue(area.getCitycode());
			rows.createCell(6).setCellValue(area.getShortcode());
		}
		// 把工作簿传到前台页面下载
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		// 文件名字
		String filename = "区域信息.xls";
		filename = new String(filename.getBytes(),"iso-8859-1");
		// 找到文件的MIME类型
		String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
		// 设置这个头ContentType
		ServletActionContext.getResponse().setContentType(mimeType);
		// 设置另外一个头 Header
		ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename="+filename);
		
		workbook.write(outputStream);
	}

	// area_findAll
	@Action(value="area_findAll",results = {@Result(name = SUCCESS,type = "json")})
	public String findAll(){
        List<Area> areaList = areaService.findAll();
        pushObjectToValueStack(areaList);
        return SUCCESS;
	}
}
