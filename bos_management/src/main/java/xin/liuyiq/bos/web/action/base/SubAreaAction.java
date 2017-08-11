package xin.liuyiq.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import xin.liuyiq.bos.domain.base.FixedArea;
import xin.liuyiq.bos.domain.base.SubArea;
import xin.liuyiq.bos.service.base.SubAreaService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SubAreaAction extends BaseAction<SubArea> {
	
	@Autowired
	private SubAreaService subAreaService;
	// subArea_pageQuery

	@Action(value = "subArea_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构建分页
		Pageable pageable = new PageRequest(page-1, rows);
		// 构建条件
		Specification<SubArea> specification = new Specification<SubArea>() {
			List<Predicate> predicateList = new ArrayList<Predicate>();
			@Override
			public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 添加区域查询条件
			    Join<Object, Object> join = root.join("area", JoinType.INNER);
				if(model.getArea() != null && StringUtils.isNotBlank(model.getArea().getProvince())){
					Predicate p1 = cb.like(join.get("province").as(String.class), "%"+model.getArea().getProvince()+"%");
					predicateList.add(p1);
				}

				if(model.getArea() != null && StringUtils.isNotBlank(model.getArea().getCity())){
                    Predicate city = cb.like(join.get("city").as(String.class), "%"+model.getArea().getCity()+"%");
                    predicateList.add(city);
                }

                if(model.getArea() != null && StringUtils.isNotBlank(model.getArea().getDistrict())){
                    Predicate district = cb.like(join.get("district").as(String.class), "%"+model.getArea().getDistrict()+"%");
                    predicateList.add(district);
                }

                // 添加定区编码查询条件
                Join<Object, Object> fixedAreaJoin = root.join("fixedArea", JoinType.INNER);
                if (model.getFixedArea() != null && StringUtils.isNotBlank(model.getFixedArea().getId())){
                    Predicate id = cb.like(fixedAreaJoin.get("id").as(String.class), model.getFixedArea().getId());
                    predicateList.add(id);
                }

                // 添加关键字查询条件 keyWords
                if(StringUtils.isNotBlank(model.getKeyWords())){
                    Predicate keyWords = cb.like(root.get("keyWords").as(String.class), "%"+model.getKeyWords()+"%");
                    predicateList.add(keyWords);
                }
                return cb.and(predicateList.toArray(new Predicate[0]));
			}
		};
		Page<SubArea> page = subAreaService.pageQuery(specification,pageable);
		
		responseJsonDataToPage(page);
		return SUCCESS;
	};

	// subArea_save
	@Action(value = "subArea_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/sub_area.html") })
	public String save() {
		subAreaService.save(model);
		return SUCCESS;
	};

	private File file;

    public void setFile(File file) {
        this.file = file;
    }

    // subArea_batchImport
    @Action(value = "subArea_batchImport")
    public String batchImport() throws Exception {
        List<SubArea> subAreas = new ArrayList<SubArea>();
        // 导入excel文件的操作
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        // 获取一个工作簿
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        // 遍历sheet 得到row 行
        for(Row row : sheet){
            // 跳过空行
            // 第一列取到了空 或者每一列的值是空
            if(row.getRowNum() == 0){
                continue;
            }

            if(row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
                continue;
            }

            // 有值的行
            SubArea subArea = new SubArea();
            subArea.setId(row.getCell(0).getStringCellValue());

            FixedArea fixedArea = new FixedArea();
            fixedArea.setId(row.getCell(1).getStringCellValue());
            subArea.setFixedArea(fixedArea);


            Area area = new Area();
            area.setId(row.getCell(2).getStringCellValue());
            subArea.setArea(area);

            subArea.setKeyWords(row.getCell(3).getStringCellValue());
            subArea.setStartNum(row.getCell(4).getStringCellValue());
            subArea.setEndNum(row.getCell(5).getStringCellValue());

            subArea.setSingle(row.getCell(6).getStringCellValue().charAt(0));

            subArea.setAssistKeyWords(row.getCell(7).getStringCellValue());

            subAreas.add(subArea);
        }
        subAreaService.batchImport(subAreas);

        return NONE;
    };
}
