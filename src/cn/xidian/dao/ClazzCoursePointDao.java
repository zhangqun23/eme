package cn.xidian.dao;

import java.util.List;

import cn.xidian.entity.AverClazzCoursePoint;
import cn.xidian.entity.ClazzCoursePoint;

public interface ClazzCoursePointDao {

	boolean addByCursNum(ClazzCoursePoint point);
	
	List<ClazzCoursePoint> selectByCursNum(String cursNum);
	
	boolean add(ClazzCoursePoint point);
	
	boolean add(AverClazzCoursePoint point);
	
	List<ClazzCoursePoint> selectByCursId(Integer cursId);
	
	List<ClazzCoursePoint> selectByCursAndClazzId(Integer cursId,String clazId);
	
	List<ClazzCoursePoint> selectByCursAndClazzId(Integer cursId,Integer clazId);
	
	List<AverClazzCoursePoint> selectByCursAndGrade(Integer cursId,String grade);
	
	boolean deleteById(ClazzCoursePoint coursePoint);
	
	boolean deleteByAverId(AverClazzCoursePoint coursePoint);
	
	List<ClazzCoursePoint> findByCursNameAndTerm(String cursName);
	
	boolean deleteByCursId(Integer cursId);
	
	List<ClazzCoursePoint> findByCursAndClazz(String cursName,String claName);
	
	List<AverClazzCoursePoint> findByCursAndGrade(String cursName,String gradeName);

	List<AverClazzCoursePoint> findByCursNameAndGrade(String cursName);
	
}
