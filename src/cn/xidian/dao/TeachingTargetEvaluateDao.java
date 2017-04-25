package cn.xidian.dao;

import java.util.List;

import cn.xidian.entity.AverTeachingTargetEvaluate;
import cn.xidian.entity.TeachingTargetEvaluate;

public interface TeachingTargetEvaluateDao {

	boolean addTchingTargetEvaValue(TeachingTargetEvaluate targetEva);

	boolean updateTchingTargetEvaValue(TeachingTargetEvaluate targetEva);

	TeachingTargetEvaluate selectByClazzIdAndTargetId(Integer clazzId,
			Integer targetId);

	List<TeachingTargetEvaluate> selectByCursNameAndGrade(String cursName,String grade);

	List<TeachingTargetEvaluate> selectByCursAndClazz(String cursName, String claName);

	AverTeachingTargetEvaluate selectByGradeAndTargetId(String gradeName, Integer targetId);

	boolean addTchingTargetEvaValue(AverTeachingTargetEvaluate targetEva);//储存年级平均

	boolean updateTchingTargetEvaValue(AverTeachingTargetEvaluate targetEva);//更新年级平均

	List<AverTeachingTargetEvaluate> selectByGradeAndClazz(String cursName, String gradeName);

	List<AverTeachingTargetEvaluate> selectByCursNameAndGradeName(String cursName, String grade);
}
