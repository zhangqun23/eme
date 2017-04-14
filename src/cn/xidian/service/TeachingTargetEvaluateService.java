package cn.xidian.service;

import java.util.List;

import cn.xidian.entity.AverTeachingTargetEvaluate;
import cn.xidian.entity.TeachingTargetEvaluate;

public interface TeachingTargetEvaluateService {

	List<TeachingTargetEvaluate> selectByCursAndClazz(String cursName,
			String claName);

	List<AverTeachingTargetEvaluate> selectByGradeAndClazz(String cursName, String gradeName);
}
