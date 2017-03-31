package cn.xidian.web.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.stereotype.Component;
import com.opensymphony.xwork2.ActionSupport;
import cn.xidian.web.bean.EvaluateResult;
import cn.xidian.entity.ItemEvaluateType;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.service.StudentItemService;
import cn.xidian.service.TeacherStudentService;
import cn.xidian.web.bean.AdminStuLimits;

@Component
@SuppressWarnings("serial")
public class ExportAction extends ActionSupport implements RequestAware {

	private InputStream excelFile;
	private EvaluateResult evaluateResult;
	private List<EvaluateResult> evaluateResults;
	private Integer claId;
	private String schoolYear;
	private String fileName;
	private List<ItemEvaluateType> itemEvaluateTypes;


	@Override
	public void setRequest(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

	TeacherStudentService teacherStudentService;

	@Resource(name = "teacherStudentServiceImpl")
	public void setTeacherStudentService(TeacherStudentService teacherStudentService) {
		this.teacherStudentService = teacherStudentService;
	}

	private StudentItemService studentItemService;

	@Resource(name = "studentItemServiceImpl")
	public void setStudentItemService(StudentItemService studentItemService) {
		this.studentItemService = studentItemService;
	}

	public String excelExport() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		// 文件名，以当前秒为文件名
		fileName = sdf.format(new Date()) + ".xls";
		itemEvaluateTypes = studentItemService.selectItemEvaTypes();
		//拼凑evaluateResults
		List<EvaluateResult> evaluateResults = new LinkedList<EvaluateResult>();
		for (int i = 0; i < itemEvaluateTypes.size(); i++) {
			List<StuEvaluateResult> stuEvaluateResults = teacherStudentService.findStuEvaByPageCid(itemEvaluateTypes.get(i).getItemEvaTypeId(), claId, schoolYear);
			switch (i) {
			case 0:
				for (int j = 0; j < stuEvaluateResults.size(); j++) {
					EvaluateResult e = new EvaluateResult();
					e.setStudent(stuEvaluateResults.get(j).getStudent());
					e.setClazz(stuEvaluateResults.get(j).getClazz());
					e.setM1(stuEvaluateResults.get(j).getmScore());
					evaluateResults.add(e);
				}
				break;
			case 1:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM2(stuEvaluateResults.get(k).getmScore());
				}
				break;
			case 2:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM3(stuEvaluateResults.get(k).getmScore());
				}
				break;
			case 3:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM4(stuEvaluateResults.get(k).getmScore());
				}
				break;
			case 4:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM5(stuEvaluateResults.get(k).getmScore());
				}
				break;
			}

		}
		/*evaluateResults = teacherStudentService.selectSummaryEvas(claId, schoolYear);*/
		
		int num = itemEvaluateTypes.size() + itemEvaluateTypes.size() + 5;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("schoolExcel");
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 4500);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4500);
		sheet.setColumnWidth(4, 4000);
		for (int i = 5; i < num; i++) {
			if (i % 2 == 0) {
				sheet.setColumnWidth(i, 3500);
			}
			if (i % 2 != 0) {
				sheet.setColumnWidth(i, 3000);
			}

		}
		// 设置大标题行开始
		HSSFFont font0 = (HSSFFont) workbook.createFont(); // 创建字体样式
		font0.setFontHeight((short) (350));
		font0.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); // 加粗
		CellStyle cellStyle0 = workbook.createCellStyle();
		cellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 左右居中
		cellStyle0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		cellStyle0.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		cellStyle0.setFont(font0);
		Row row0 = sheet.createRow(0);
		row0.setHeight((short) 500);
		HSSFCell cell0 = (HSSFCell) row0.createCell(0);// 第一列
		cell0.setCellStyle(cellStyle0);
		cell0.setCellValue("机电工程学院" + schoolYear + "学年综合测评详细表");
		// CellRangeAddress 起始行 结束行 起始列 结束列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, (short) 0, (short) 14));
		// 设置大标题行结束

		// 设置小标题行开始
		Row row1 = sheet.createRow(1);
		row1.setHeight((short) 400);
		Row row2 = sheet.createRow(2);
		row2.setHeight((short) 400);
		// 设置小标题样式开始
		HSSFFont font1 = (HSSFFont) workbook.createFont(); // 创建字体样式
		font1.setFontHeight((short) (250));
		CellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 左右居中
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		cellStyle1.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		cellStyle1.setFont(font1);
		// 设置小标题样式结束

		HSSFCell cell = (HSSFCell) row1.createCell(0);// 第一列
		cell.setCellStyle(cellStyle1);
		cell.setCellValue("姓名");
		HSSFCell cell1 = (HSSFCell) row1.createCell(1);// 第二列
		cell1.setCellStyle(cellStyle1);
		cell1.setCellValue("学号");
		HSSFCell cell2 = (HSSFCell) row1.createCell(2);// 第三列
		cell2.setCellStyle(cellStyle1);
		cell2.setCellValue("奖学金等级");
		HSSFCell cell3 = (HSSFCell) row1.createCell(3);// 第四列
		cell3.setCellStyle(cellStyle1);
		cell3.setCellValue("学院");
		HSSFCell cell4 = (HSSFCell) row1.createCell(4);// 第五列
		cell4.setCellStyle(cellStyle1);
		cell4.setCellValue("班级");
		//将前五列的第二行和第三行都合并同类项
		sheet.addMergedRegion(new CellRangeAddress(1, 2, (short) 0, (short) 0));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, (short) 1, (short) 1));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, (short) 2, (short) 2));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, (short) 3, (short) 3));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, (short) 4, (short) 4));
		int k = 0;
		for (int j = 5; j < num; j++) {
			//将第二行和第三行的第j和j+1列合并
			sheet.addMergedRegion(new CellRangeAddress(1, 1, (short) j, (short) j + 1));
			HSSFCell cell5 = (HSSFCell) row1.createCell(j);// 第5列
			cell5.setCellStyle(cellStyle1);
			cell5.setCellValue(
					itemEvaluateTypes.get(k).getItemEvaTypeName() + itemEvaluateTypes.get(k).getItemEvaTypeMark());
			HSSFCell cel5 = (HSSFCell) row2.createCell(j);// 第5列
			cel5.setCellStyle(cellStyle1);
			cel5.setCellValue("分值");
			HSSFCell cell6 = (HSSFCell) row2.createCell(j + 1);// 第6列
			cell6.setCellStyle(cellStyle1);
			cell6.setCellValue("等级");
			k++;
			j++;
		}
		// 设置小标题结束

		for (int i = 3; i <= evaluateResults.size(); i++) {
			k++;
			evaluateResult = evaluateResults.get(i - 3);
			Row row = sheet.createRow(i);
			row.createCell(0).setCellValue(evaluateResult.getStudent().getStuName());
			row.createCell(1).setCellValue(evaluateResult.getStudent().getStuSchNum());
			row.createCell(3).setCellValue("机电工程学院");
			row.createCell(4).setCellValue(evaluateResult.getClazz().getClaName());
			row.createCell(5).setCellValue(evaluateResult.getM1());
			row.createCell(7).setCellValue(evaluateResult.getM2());
			row.createCell(9).setCellValue(evaluateResult.getM3());
			row.createCell(11).setCellValue(evaluateResult.getM4());
			row.createCell(13).setCellValue(evaluateResult.getM5());
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		workbook.write(baos);
		excelFile = new ByteArrayInputStream(baos.toByteArray());
		baos.close();
		return "excel";
	}

	public InputStream getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(InputStream excelFile) {
		this.excelFile = excelFile;
	}

	public EvaluateResult getEvaluateResult() {
		return evaluateResult;
	}

	public void setEvaluateResult(EvaluateResult evaluateResult) {
		this.evaluateResult = evaluateResult;
	}

	public List<EvaluateResult> getEvaluateResults() {
		return evaluateResults;
	}

	public void setEvaluateResults(List<EvaluateResult> evaluateResults) {
		this.evaluateResults = evaluateResults;
	}

	public Integer getClaId() {
		return claId;
	}

	public void setClaId(Integer claId) {
		this.claId = claId;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<ItemEvaluateType> getItemEvaluateTypes() {
		return itemEvaluateTypes;
	}

	public void setItemEvaluateTypes(List<ItemEvaluateType> itemEvaluateTypes) {
		this.itemEvaluateTypes = itemEvaluateTypes;
	}

}
