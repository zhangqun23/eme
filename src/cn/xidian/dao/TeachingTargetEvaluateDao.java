package cn.xidian.dao;

import java.util.List;

import cn.xidian.entity.TeachingTargetEvaluate;

public interface TeachingTargetEvaluateDao {

	boolean addTchingTargetEvaValue(TeachingTargetEvaluate targetEva);

	boolean updateTchingTargetEvaValue(TeachingTargetEvaluate targetEva);

	TeachingTargetEvaluate selectByClazzIdAndTargetId(String clazzId,
			Integer targetId);

	List<TeachingTargetEvaluate> selectByCursNameAndGrade(String cursName,String grade);

	List<TeachingTargetEvaluate> selectByCursAndClazz(String cursName, String claName);
}
