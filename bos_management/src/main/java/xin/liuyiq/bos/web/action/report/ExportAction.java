package xin.liuyiq.bos.web.action.report;

import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.take_delivery.WayBill;
import xin.liuyiq.bos.service.take_delivery.WayBillService;
import xin.liuyiq.bos.utils.FileUtils;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class ExportAction extends BaseAction<WayBill>{

	@Autowired
	private WayBillService wayBillService;
	
	@Action(value="report_exportXls")
	public String exportXls() throws Exception{
		// 查询到数据
		List<WayBill> wayBills = wayBillService.findAll(model);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建一个工作簿
		HSSFSheet sheet = workbook.createSheet("运单数据");
		// 获得第一行
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("运单编号");
		headRow.createCell(1).setCellValue("寄送人");
		headRow.createCell(2).setCellValue("寄送人电话");
		headRow.createCell(3).setCellValue("寄送人地址");
		headRow.createCell(4).setCellValue("收件人");
		headRow.createCell(5).setCellValue("收件人电话");
		headRow.createCell(6).setCellValue("寄送人地址");
		
		for (WayBill wayBill : wayBills) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
			dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
			dataRow.createCell(1).setCellValue(wayBill.getSendName());
			dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
			dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
			dataRow.createCell(4).setCellValue(wayBill.getRecName());
			dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
			dataRow.createCell(6).setCellValue(wayBill.getRecAddress());
		};
		
		// 设置一个流 两个头    application/x-xls
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
		String filename = "运单数据.xls";
		
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment;filename="+filename);
		
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return NONE;
	}
}
