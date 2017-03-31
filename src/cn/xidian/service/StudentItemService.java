package cn.xidian.service;

import java.util.Date;
import java.util.List;

import cn.xidian.entity.ItemEvaluatePoint;
import cn.xidian.entity.ItemEvaluateScore;
import cn.xidian.entity.ItemEvaluateType;
import cn.xidian.entity.ItemFile;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StudentItem;

public interface StudentItemService {

	boolean deleteItemById(Integer id);

	boolean deleteFileById(Integer id);

	boolean add(StudentItem item);

	PageBean<StudentItem> selectByStuNum(String stuNum,Integer page);

	boolean saveAttachment(ItemFile itemFile);

	StudentItem selectItemInfo(Integer itemId);

	List<ItemFile> selectItemFile(Integer itemId);

	List<ItemEvaluateType> selectItemEvaTypes();

	List<ItemEvaluatePoint> selectItemEvaPoints(Integer id);

	List<ItemEvaluateScore> selectItemEvaScoresByPointId(Integer id);

	ItemEvaluateType selectItemEvaType(Integer id);

	ItemEvaluatePoint selectItemEvaPoint(Integer id);

	ItemEvaluateScore selectItemEvaScore(Integer id);

	List<StudentItem> selectItemByLimitTime(String stuNum, Date startTime, Date endTime);
	
	List<StudentItem> selectItemByLimitTimes(Integer id,String stuNum, Date startTime, Date endTime);
}
