package cn.xidian.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.xidian.dao.StudentItemDao;
import cn.xidian.entity.ItemEvaluatePoint;
import cn.xidian.entity.ItemEvaluateScore;
import cn.xidian.entity.ItemEvaluateType;
import cn.xidian.entity.ItemFile;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StudentItem;
import cn.xidian.service.StudentItemService;
import cn.xidian.utils.PageUtils;

@Component("studentItemServiceImpl")
public class StudentItemServiceImpl implements StudentItemService {

	private StudentItemDao studentItemDao;

	@Resource(name = "studentItemDaoImpl")
	public void setStudentItemDao(StudentItemDao studentItemDao) {
		this.studentItemDao = studentItemDao;
	}

	@Override
	public boolean deleteItemById(Integer id) {
		return studentItemDao.deleteItemById(id);
	}

	@Override
	public boolean add(StudentItem item) {
		return studentItemDao.add(item);
	}

	@Override
	public PageBean<StudentItem> selectByStuNum(String stuNum,Integer page) {
		PageBean<StudentItem> siPageBean=new PageBean<StudentItem>();
		List<StudentItem> studentItems=studentItemDao.selectByStuNum(stuNum);
		siPageBean=PageUtils.page(page, studentItems.size(),10);
		List<StudentItem> items=studentItemDao.findByStuNum(stuNum,siPageBean.getBegin(),siPageBean.getLimit());
		siPageBean.setList(items);
		return siPageBean;
	}

	@Override
	public boolean saveAttachment(ItemFile itemFile) {
		// TODO Auto-generated method stub

		return studentItemDao.saveAttachment(itemFile);
	}

	@Override
	public StudentItem selectItemInfo(Integer itemId) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemInfo(itemId);
	}

	@Override
	public List<ItemFile> selectItemFile(Integer itemId) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemFile(itemId);
	}

	@Override
	public boolean deleteFileById(Integer id) {
		// TODO Auto-generated method stub
		return studentItemDao.deleteFileById(id);
	}

	@Override
	public List<ItemEvaluateType> selectItemEvaTypes() {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemEvaTypes();
	}

	@Override
	public List<ItemEvaluatePoint> selectItemEvaPoints(Integer id) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemEvaPoints(id);
	}

	@Override
	public List<ItemEvaluateScore> selectItemEvaScoresByPointId(Integer id) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemEvaScoresByPointId(id);
	}

	@Override
	public ItemEvaluateType selectItemEvaType(Integer id) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemEvaType(id);
	}

	@Override
	public ItemEvaluatePoint selectItemEvaPoint(Integer id) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemEvaPoint(id);
	}

	@Override
	public ItemEvaluateScore selectItemEvaScore(Integer id) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemEvaScore(id);
	}

	@Override
	public List<StudentItem> selectItemByLimitTime(String stuNum, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemByLimitTime(stuNum,startTime,endTime);
	}

	@Override
	public List<StudentItem> selectItemByLimitTimes(Integer id, String stuNum, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return studentItemDao.selectItemByLimitTimes(id,stuNum,startTime,endTime);
	}

}
