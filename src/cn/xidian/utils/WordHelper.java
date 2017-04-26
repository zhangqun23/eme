package cn.xidian.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.TableView.TableRow;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

public class WordHelper<T> {

	/**
	 * 导出2007版word（模板）（不合并列项单元格）
	 * 
	 * @param path
	 *            模板路径
	 * @param listMap
	 *            表格内容
	 * @param contentMap
	 *            特定字符串替换
	 * @param rowNum
	 *            表头行数
	 * @param out
	 *            输出
	 * @param mergeColumn在这个方法中没用，合并减下个重载的方法
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void export2007Word(String path, Map<String, Object> listMap, Map<String, Object> contentMap, Integer rowNum,
			OutputStream out, Integer mergeColumn) {
		// 读取模板
		FileInputStream in = null;
		XWPFDocument doc = null;
		try {
			in = new FileInputStream(new File(path));
			doc = new XWPFDocument(in);

			if (contentMap != null) {
				// 替换模版中的变量(包含添加图片)
				generateWord(doc, contentMap);
			}

			// 解析map中的多个list，并根据表头动态生成word表格
			if (listMap != null) {
				Iterator<Entry<String, Object>> it = listMap.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> entry = it.next();
					Integer tableOrder = Integer.valueOf(entry.getKey());
					Object val = entry.getValue();
					Collection<T> list = (Collection<T>) val;
					// 根据表头动态生成word表格(tableOrder:word模版中的第tableOrder张表格)
					dynamicWord(doc, list, tableOrder, rowNum);

				}
			}
			write2007Out(doc, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * export2007Word方法重载，针对传递不同的参数，对word表格的指定列项合并单元格（合并列项单元格）
	 * 
	 * @param path
	 *            模板路径
	 * @param listMap
	 *            表格内容
	 * @param contentMap
	 *            特定字符串替换
	 * @param rowNum
	 *            表头行数
	 * @param out
	 *            输出
	 * @param mergeColumn
	 *            需要合并的列都有哪些的集合
	 * 
	 * @param baseContent
	 *            根据对象的哪一个字段进行合并
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void export2007Word(String path, Map<String, Object> listMap, Map<String, Object> contentMap, Integer rowNum,
			OutputStream out, List<Integer> mergeColumn, String baseContent) {
		// 读取模板
		FileInputStream in = null;
		XWPFDocument doc = null;
		try {
			in = new FileInputStream(new File(path));
			doc = new XWPFDocument(in);

			if (contentMap != null) {
				// 替换模版中的变量(包含添加图片)
				generateWord(doc, contentMap);
			}

			// 解析map中的多个list，并根据表头动态生成word表格
			if (listMap != null) {
				Iterator<Entry<String, Object>> it = listMap.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> entry = it.next();
					Integer tableOrder = Integer.valueOf(entry.getKey());
					Object val = entry.getValue();
					Collection<T> list = (Collection<T>) val;
					// 根据表头动态生成word表格(tableOrder:word模版中的第tableOrder张表格)
					dynamicWord(doc, list, tableOrder, rowNum, mergeColumn, baseContent);

				}
			}
			write2007Out(doc, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据表头动态生成word表格（不合并列项单元格）
	 * 
	 * @param doc
	 * @param list
	 * @param tableOrder
	 * @param rowNum:表头行数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void dynamicWord(XWPFDocument doc, Collection<T> list, Integer tableOrder, Integer rowNum) {
		List<XWPFTable> tables;
		XWPFTable table = null;
		try {
			tables = doc.getTables();
			table = tables.get(tableOrder);// 变量
			XWPFTableRow row0 = table.getRow(0);// 表头第一行
			XWPFTableRow row = table.getRow(rowNum - 1);// 表头最后一行
			List<BigInteger> widthList = new ArrayList<BigInteger>(); // 记录表格标题宽度
			List<XWPFTableCell> cells0 = row0.getTableCells();// 表头第一行
			List<XWPFTableCell> cells = row.getTableCells();// 表头最后一行
			XWPFTableCell cell = null;
			CTTcPr cellPr = null;
			int colNum0 = cells0.size();
			int colNum = cells.size();
			int Dvalue = colNum - colNum0;// 多行表头时，列的差值

			for (int i = 0; i < colNum; i++) {
				cell = cells.get(i);
				cellPr = cell.getCTTc().getTcPr();
				BigInteger width = cellPr.getTcW().getW();// 获取单元格宽度
				widthList.add(width);
			}

			Iterator<T> it = list.iterator();
			while (it.hasNext()) {
				row = table.createRow();// 默认按第一行的列数创建行
				if (Dvalue > 0) {// 差值>0：创建行时，追加单元格
					for (int m = 0; m < Dvalue; m++) {
						row.createCell();
					}
				}
				T t = (T) it.next();
				//Boolean flag = tranFieldToPer(t);// 需要处理%列
				Field[] fields = t.getClass().getDeclaredFields();
				cells = row.getTableCells();
				for (int i = 0; i < colNum; i++) {
					Field field = fields[i];
					String fieldName = field.getName();
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					cell = cells.get(i);
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if (value != null) {
//						if (flag && judgeField(fieldName)) {
//							cell.setText(StringUtil.strFloatToPer(String.valueOf(value)));// 转换成%
//						} else {
//							cell.setText(String.valueOf(value));// 写入单元格内容
//						}
						cell.setText(String.valueOf(value));// 写入单元格内容
					}
					cellPr = cell.getCTTc().addNewTcPr();// 获取单元格样式
					cellPr.addNewTcW().setW(widthList.get(i));// 设置单元格宽度
					cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);// 表格内容垂直居中
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据表头动态生成word表格,dynamicWord方法重载，针对传递不同的参数,当需要合并列项单元格使用（合并列项单元格）
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void dynamicWord(XWPFDocument doc, Collection<T> list, Integer tableOrder, Integer rowNum,
			List<Integer> mergeColumn, String baseContent) {
		List<XWPFTable> tables;
		XWPFTable table = null;
		try {
			tables = doc.getTables();
			table = tables.get(tableOrder);// 变量
			XWPFTableRow row0 = table.getRow(0);// 表头第一行
			XWPFTableRow row = table.getRow(rowNum - 1);// 表头最后一行
			List<BigInteger> widthList = new ArrayList<BigInteger>(); // 记录表格标题宽度
			List<XWPFTableCell> cells0 = row0.getTableCells();// 表头第一行
			List<XWPFTableCell> cells = row.getTableCells();// 表头最后一行
			XWPFTableCell cell = null;
			CTTcPr cellPr = null;
			int colNum0 = cells0.size();
			int colNum = cells.size();
			int Dvalue = colNum - colNum0;// 多行表头时，列的差值

			for (int i = 0; i < colNum; i++) {
				cell = cells.get(i);
				cellPr = cell.getCTTc().getTcPr();
				BigInteger width = cellPr.getTcW().getW();// 获取单元格宽度
				widthList.add(width);
			}

			Iterator<T> it = list.iterator();
			while (it.hasNext()) {
				row = table.createRow();// 默认按第一行的列数创建行
				if (Dvalue > 0) {// 差值>0：创建行时，追加单元格
					for (int m = 0; m < Dvalue; m++) {
						row.createCell();
					}
				}
				T t = (T) it.next();
				//Boolean flag = tranFieldToPer(t);// 需要处理%列
				Field[] fields = t.getClass().getDeclaredFields();
				cells = row.getTableCells();
				for (int i = 0; i < colNum; i++) {
					Field field = fields[i];
					String fieldName = field.getName();
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					cell = cells.get(i);
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if (value != null) {
//						if (flag && judgeField(fieldName)) {
//							cell.setText(StringUtil.strFloatToPer(String.valueOf(value)));// 转换成%
//						} else {
//							cell.setText(String.valueOf(value));// 写入单元格内容
//						}
						cell.setText(String.valueOf(value));// 写入单元格内容
					}
					cellPr = cell.getCTTc().addNewTcPr();// 获取单元格样式
					cellPr.addNewTcW().setW(widthList.get(i));// 设置单元格宽度
					cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);// 表格内容垂直居中
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer mm = table.getNumberOfRows();
		// 第mergeColumn列相同数据合并单元格
		if (mergeColumn != null) {
			addMergedRegion0(table, mergeColumn, rowNum, mm, list, baseContent);// 就是合并第一列的所有相同单元格
		}

	}

	// merge方法纵向合并单元格（合并列项单元格）
	private static void merge(XWPFTable table, List<Integer> col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			for (int i = 0; i < col.size(); i++) {
				XWPFTableCell cell = table.getRow(rowIndex).getCell(col.get(i));
				if (rowIndex == fromRow) {
					getCellCTTcPr(cell).addNewVMerge().setVal(STMerge.RESTART);
				} else {
					getCellCTTcPr(cell).addNewVMerge().setVal(STMerge.CONTINUE);
				}
			}

		}
	}

	private static CTTcPrBase getCellCTTcPr(XWPFTableCell cell) {
		CTTc cttc = cell.getCTTc();
		CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
		return tcPr;
	}

	/*
	 * 当word有合并单元格要求时实现合并
	 * 
	 * table:要合并的表格 cellLine：要合并的列的集合（整型） startRow：起始合并行 endRow：表格总行数
	 * list：要处理的数据集合 baseContent：根据那个字段进行合并单元格操作
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addMergedRegion0(XWPFTable table, List<Integer> cellLine, int startRow, int endRow, Collection<T> list,
			String baseContent) {

		Iterator<T> it = list.iterator();
		String preContent = null;
		int j = 0;
		int merge_start_row = startRow;
		while (it.hasNext()) {
			T t = (T) it.next();
			String getMethodName = "get" + baseContent;
			Class tCls = t.getClass();
			Method getMethod;

			try {
				getMethod = tCls.getMethod(getMethodName, new Class[] {});
				Object value;
				value = getMethod.invoke(t, new Object[] {});
				String nowContent = String.valueOf(value);
				if (!nowContent.equals(preContent)) {
					if (j != startRow) {
						merge(table, cellLine, merge_start_row, j);
						merge_start_row = j + 1;
					}

				}
				preContent = nowContent;
				j++;
				if (j == endRow - 1) {
					merge(table, cellLine, merge_start_row, j);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings("unused")
	private static String getTableCellContent(XWPFTableCell cell) {
		StringBuffer sb = new StringBuffer();
		List<XWPFParagraph> cellPList = cell.getParagraphs();
		if (cellPList != null && cellPList.size() > 0) {
			for (XWPFParagraph xwpfPr : cellPList) {
				List<XWPFRun> runs = xwpfPr.getRuns();
				if (runs != null && runs.size() > 0) {
					for (XWPFRun xwpfRun : runs) {
						sb.append(xwpfRun.getText(0));
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 根据指定的参数值、模板，生成 word 文档
	 * 
	 * @param param
	 *            需要替换的变量
	 * @param template
	 *            模板
	 */
	private XWPFDocument generateWord(XWPFDocument doc, Map<String, Object> param) {
		try {
			if (param != null && param.size() > 0) {

				// 处理段落
				List<XWPFParagraph> paragraphList = doc.getParagraphs();
				processParagraphs(paragraphList, param, doc);
 
				// 处理表格
				Iterator<XWPFTable> it = doc.getTablesIterator();
				while (it.hasNext()) {
					XWPFTable table = it.next();
					List<XWPFTableRow> rows = table.getRows();
					for (XWPFTableRow row : rows) {
						List<XWPFTableCell> cells = row.getTableCells();
						for (XWPFTableCell cell : cells) {
							List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
							processParagraphs(paragraphListTable, param, doc);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 处理段落（多个图片）
	 * 
	 * @param paragraphList
	 */
	@SuppressWarnings("unchecked")
	private void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, Object> param, XWPFDocument doc) {
		if (paragraphList != null && paragraphList.size() > 0) {
			for (int m = 0; m < paragraphList.size(); m++) {
				XWPFParagraph paragraph = paragraphList.get(m);
				List<XWPFRun> runs = paragraph.getRuns();
				for (int i = 0; i < runs.size(); i++) {
					XWPFRun run = runs.get(i);
					String text = run.getText(0);
					if (text != null) {
						boolean isSetText = false;
						for (Entry<String, Object> entry : param.entrySet()) {
							String key = entry.getKey();
							if (text.indexOf(key) != -1) {
								isSetText = true;
								Object value = entry.getValue();
								if (value instanceof String) {// 文本替换
									text = text.replace(key, value.toString());
								} else if (value instanceof Map) {// 图片替换
									text = text.replace(key, "");
									Map<String, Object> pic = (Map<String, Object>) value;
									int width = Integer.parseInt(pic.get("width").toString());
									int height = Integer.parseInt(pic.get("height").toString());
									int picType = getPictureType(pic.get("type").toString());
									byte[] byteArray = (byte[]) pic.get("content");
									ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
									try {
										String blipId = doc.addPictureData(byteInputStream, picType);
										createPicture(doc, blipId, width, height, paragraph);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
						if (isSetText) {
							run.setText(text, 0);
						}
					}
				}
			}
		}
	}

	/**
	 * 根据图片类型，取得对应的图片类型代码
	 * 
	 * @param picType
	 * @return int
	 */
	private int getPictureType(String picType) {
		int res = XWPFDocument.PICTURE_TYPE_PICT;
		if (picType != null) {
			if (picType.equalsIgnoreCase("png")) {
				res = XWPFDocument.PICTURE_TYPE_PNG;
			} else if (picType.equalsIgnoreCase("dib")) {
				res = XWPFDocument.PICTURE_TYPE_DIB;
			} else if (picType.equalsIgnoreCase("emf")) {
				res = XWPFDocument.PICTURE_TYPE_EMF;
			} else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
				res = XWPFDocument.PICTURE_TYPE_JPEG;
			} else if (picType.equalsIgnoreCase("wmf")) {
				res = XWPFDocument.PICTURE_TYPE_WMF;
			}
		}
		return res;
	}

	/**
	 * word图片配置
	 * 
	 * @param doc
	 * @param blipId
	 * @param width
	 * @param height
	 * @param paragraph
	 */
	private void createPicture(XWPFDocument doc, String blipId, int width, int height, XWPFParagraph paragraph) {
		int id = doc.getAllPictures().size() - 1;
		final int EMU = 9525;
		width *= EMU;
		height *= EMU;
		CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();
		String picXml = "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
				+ "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>"
				+ "            <pic:cNvPicPr/>" + "         </pic:nvPicPr>" + "         <pic:blipFill>"
				+ "            <a:blip r:embed=\"" + blipId
				+ "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
				+ "            <a:stretch>" + "               <a:fillRect/>" + "            </a:stretch>"
				+ "         </pic:blipFill>" + "         <pic:spPr>" + "            <a:xfrm>"
				+ "               <a:off x=\"0\" y=\"0\"/>" + "               <a:ext cx=\"" + width + "\" cy=\""
				+ height + "\"/>" + "            </a:xfrm>" + "            <a:prstGeom prst=\"rect\">"
				+ "               <a:avLst/>" + "            </a:prstGeom>" + "         </pic:spPr>"
				+ "      </pic:pic>" + "   </a:graphicData>" + "</a:graphic>";

		inline.addNewGraphic().addNewGraphicData();
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXml);
		} catch (XmlException xe) {
			xe.printStackTrace();
		}
		inline.set(xmlToken);

		inline.setDistT(0);
		inline.setDistB(0);
		inline.setDistL(0);
		inline.setDistR(0);

		CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx(width);
		extent.setCy(height);

		CTNonVisualDrawingProps docPr = inline.addNewDocPr();
		docPr.setId(id);
		docPr.setName("图片" + id);
		docPr.setDescr("");
	}

	/**
	 * 将输出流写入word(2007版)
	 * 
	 * @param doc
	 * @param out
	 */
	private void write2007Out(XWPFDocument doc, OutputStream out) {
		try {
			doc.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 需要转化%列的类
	 * 
	 * @param t
	 * @return
	 */
	/*private Boolean tranFieldToPer(T t) {
		Boolean flag = false;
		Class<? extends Object> cla = t.getClass();
		List<Object> list = new ArrayList<Object>();
		list.add(HoCustomerService.class);
		list.add(HouseCustomerServiceLoad.class);
		list.add(HouseCustomerServiceType.class);
		list.add(WorkEfficiency.class);
		list.add(WorkReject.class);
		list.add(RobEfficiency.class);
		list.add(CheckOutEfficiency.class);
		list.add(CheckHouse.class);
		if (list.contains(cla)) {
			flag = true;
		}
		return flag;
	}*/

	/**
	 * 判断需要转换的字段属性
	 * 
	 * @param fieldName
	 * @return
	 */
	private Boolean judgeField(String fieldName) {
		Boolean flag = false;
		List<String> list = new ArrayList<String>();
		list.add("timeOutRate");
		list.add("house_eff");
		list.add("house_serv_eff");
		list.add("reject_dust_eff");
		list.add("reject_night_eff");
		list.add("reject_leave_eff");
		list.add("workEffeciencyAvg");
		list.add("efficiency");

		if (list.contains(fieldName)) {
			flag = true;
		}
		return flag;
	}

}
