package cn.xidian.web.service;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import cn.xidian.entity.AverClazzCoursePoint;
import cn.xidian.entity.AverTeachingTargetEvaluate;
import cn.xidian.entity.ClazzCoursePoint;
import cn.xidian.entity.TeachingTarget;
import cn.xidian.entity.TeachingTargetEvaluate;
import cn.xidian.utils.StringUtil;
import cn.xidian.web.bean.WordCourseEvaluate;
import cn.xidian.web.bean.WordCourseTarget;

public class WordCourseTargetService {
	public List<WordCourseTarget> getB1(List<TeachingTarget> targets, List<TeachingTargetEvaluate> targetValues) {
		DecimalFormat df = new DecimalFormat("#0.000");// 用于格式化Double类型数据
		List<WordCourseTarget> claCursB1 = new LinkedList<WordCourseTarget>();
		for (int i = 0; i < targets.size(); i++) {
			TeachingTarget target = targets.get(i);
			TeachingTargetEvaluate targetValue = targetValues.get(i);
			WordCourseTarget wordCourseTarget = new WordCourseTarget();
			wordCourseTarget.setTarget("目标" + (i + 1));
			wordCourseTarget.setTargetOneValue(target.getTchtargetClassTargetValue().toString());
			wordCourseTarget.setTargetTwoValue(target.getTchtargetHomeworkTargetValue().toString());
			wordCourseTarget.setTargetThreeValue(target.getTchtargetExpTargetValue().toString());
			wordCourseTarget.setTargetFourValue(target.getTchtargetMidTargetValue().toString());
			wordCourseTarget.setTargetFiveValue(target.getTchtargetFinTargetValue().toString());
			String sww = StringUtil.add(target.getTchtargetClassTargetValue().toString(),
					target.getTchtargetHomeworkTargetValue().toString());
			sww = StringUtil.add(sww, target.getTchtargetExpTargetValue().toString());
			sww = StringUtil.add(sww, target.getTchtargetMidTargetValue().toString());
			sww = StringUtil.add(sww, target.getTchtargetFinTargetValue().toString());
			wordCourseTarget.setTargetValue(sww);

			wordCourseTarget.setEvaluateOneValue(df.format(targetValue.getTchtargetClassEvaValue()));
			wordCourseTarget.setEvaluateTwoValue(df.format(targetValue.getTchtargetWorkEvaValue()));
			wordCourseTarget.setEvaluateThreeValue(df.format(targetValue.getTchtargetExpEvaValue()));
			wordCourseTarget.setEvaluateFourValue(df.format(targetValue.getTchtargetMidEvaValue()));
			wordCourseTarget.setEvaluateFiveValue(df.format(targetValue.getTchtargetFinEvaValue()));

			wordCourseTarget.setA1(df.format(targetValue.getA1()));
			wordCourseTarget.setB1(df.format(targetValue.getB1()));
			claCursB1.add(wordCourseTarget);
		}
		return claCursB1;
	}
	public List<WordCourseTarget> getAB1(List<TeachingTarget> targets, List<AverTeachingTargetEvaluate> targetValues) {
		DecimalFormat df = new DecimalFormat("#0.000");// 用于格式化Double类型数据
		List<WordCourseTarget> claCursB1 = new LinkedList<WordCourseTarget>();
		for (int i = 0; i < targets.size(); i++) {
			TeachingTarget target = targets.get(i);
			AverTeachingTargetEvaluate targetValue = targetValues.get(i);
			WordCourseTarget wordCourseTarget = new WordCourseTarget();
			wordCourseTarget.setTarget("目标" + (i + 1));
			wordCourseTarget.setTargetOneValue(target.getTchtargetClassTargetValue().toString());
			wordCourseTarget.setTargetTwoValue(target.getTchtargetHomeworkTargetValue().toString());
			wordCourseTarget.setTargetThreeValue(target.getTchtargetExpTargetValue().toString());
			wordCourseTarget.setTargetFourValue(target.getTchtargetMidTargetValue().toString());
			wordCourseTarget.setTargetFiveValue(target.getTchtargetFinTargetValue().toString());
			String sww = StringUtil.add(target.getTchtargetClassTargetValue().toString(),
					target.getTchtargetHomeworkTargetValue().toString());
			sww = StringUtil.add(sww, target.getTchtargetExpTargetValue().toString());
			sww = StringUtil.add(sww, target.getTchtargetMidTargetValue().toString());
			sww = StringUtil.add(sww, target.getTchtargetFinTargetValue().toString());
			wordCourseTarget.setTargetValue(sww);

			wordCourseTarget.setEvaluateOneValue(df.format(targetValue.getTchtargetClassEvaValue()));
			wordCourseTarget.setEvaluateTwoValue(df.format(targetValue.getTchtargetWorkEvaValue()));
			wordCourseTarget.setEvaluateThreeValue(df.format(targetValue.getTchtargetExpEvaValue()));
			wordCourseTarget.setEvaluateFourValue(df.format(targetValue.getTchtargetMidEvaValue()));
			wordCourseTarget.setEvaluateFiveValue(df.format(targetValue.getTchtargetFinEvaValue()));

			wordCourseTarget.setA1(df.format(targetValue.getA1()));
			wordCourseTarget.setB1(df.format(targetValue.getB1()));
			claCursB1.add(wordCourseTarget);
		}
		return claCursB1;
	}

	public List<WordCourseEvaluate> getB2(List<ClazzCoursePoint> ccPoints) {
		DecimalFormat df = new DecimalFormat("#0.000");// 用于格式化Double类型数据
		List<WordCourseEvaluate> claCursB2s = new LinkedList<WordCourseEvaluate>();
		for (int i = 0; i < ccPoints.size(); i++) {
			ClazzCoursePoint ccPoint = ccPoints.get(i);
			WordCourseEvaluate bValue2 = new WordCourseEvaluate();
			bValue2.setPoint(ccPoint.getIndPoint().getIndPointNum() + ccPoint.getIndPoint().getIndPointContent());
			bValue2.setTargetTarValue(df.format(ccPoint.getTargetTarValue()));
			bValue2.setA2(df.format(ccPoint.getA2()));
			bValue2.setB2(df.format(ccPoint.getB2()));
			claCursB2s.add(bValue2);
		}
		return claCursB2s;
	}
	public List<WordCourseEvaluate> getAB2(List<AverClazzCoursePoint> ccPoints) {
		DecimalFormat df = new DecimalFormat("#0.000");// 用于格式化Double类型数据
		List<WordCourseEvaluate> claCursB2s = new LinkedList<WordCourseEvaluate>();
		for (int i = 0; i < ccPoints.size(); i++) {
			AverClazzCoursePoint ccPoint = ccPoints.get(i);
			WordCourseEvaluate bValue2 = new WordCourseEvaluate();
			bValue2.setPoint(ccPoint.getIndPoint().getIndPointNum() + ccPoint.getIndPoint().getIndPointContent());
			bValue2.setTargetTarValue(df.format(ccPoint.getTargetTarValue()));
			bValue2.setA2(df.format(ccPoint.getA2()));
			bValue2.setB2(df.format(ccPoint.getB2()));
			claCursB2s.add(bValue2);
		}
		return claCursB2s;
	}

}
