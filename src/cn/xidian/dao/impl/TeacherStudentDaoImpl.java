package cn.xidian.dao.impl;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.swing.Spring;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import cn.xidian.dao.TeacherStudentDao;
import cn.xidian.entity.Clazz;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentCourse;
import cn.xidian.web.bean.AdminStuLimits;

@Component
public class TeacherStudentDaoImpl implements TeacherStudentDao {

	private SessionFactory sessionFactory;

	@Override
	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Clazz> selectChargeCla(Integer id) {
		// TODO Auto-generated method stub
		String sql = "from Clazz where tchrId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, id);
		List<Clazz> clazzs = query.list();
		return clazzs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> selectChargeStus(Integer id) {
		// TODO Auto-generated method stub
		String sql = "from Student where claId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, id);
		List<Student> students = query.list();
		return students;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Student> selectStuLimits(AdminStuLimits limits, List<Clazz> clazzs) {
		// TODO Auto-generated method stub
		if (limits == null) {
			return null;
		}
		Set<Student> students = new LinkedHashSet<Student>();
		
		Integer i = 0;
		StringBuffer sb = new StringBuffer();
		for (Clazz element : clazzs) {
			if (i < clazzs.size()) {
				if (i != clazzs.size() - 1) {
					sb.append(element.getClaId() + ",");
					i++;
				} else {
					sb.append(element.getClaId());
				}
			}
		}
		String clazzRange = sb.toString();
		String sql = "from Student where 1=1";
		if (limits.getStuClazz() != null) {
			sql += " and claId=" + limits.getStuClazz();
			if (!limits.getStuSchNum().equals("")) {
				sql += " and stuSchNum  like '%" + limits.getStuSchNum()+"%'";
				if (!limits.getStuName().equals("")) {
					sql += " and stuName like '%" + limits.getStuName() +"%'";
				}
			} else {
				if (!limits.getStuName().equals("")) {
					sql += " and stuName like '%" + limits.getStuName() + "%'";
				}
			}
		} else {
			sql += " and claId in (" + clazzRange + ")";
			if (!limits.getStuSchNum().equals("")) {
				sql += " and stuSchNum like '%" + limits.getStuSchNum()+"%'";
				if (!limits.getStuName().equals("")) {
					sql += " and stuName like '%" + limits.getStuName() + "%'";
				}
			} else {
				if (!limits.getStuName().equals("")) {
					sql += " and stuName like '%" + limits.getStuName() + "%'";
				}
			}
		}
		Query query = currentSession().createQuery(sql);
		students.addAll(query.list());
		return students;
	}

	@Override
	public Clazz selectClazzById(Integer id) {
		// TODO Auto-generated method stub
		String sql = "from Clazz where claId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, id);
		Clazz clazz = (Clazz) query.uniqueResult();
		return clazz;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<StudentCourse> selectStuGrades(Integer stuId, String schoolYear) {
		// TODO Auto-generated method stub
		String sql = "from StudentCourse where stuId=? and schoolYear=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, stuId);
		query.setString(1, schoolYear);
		List<StudentCourse> studentCourses = query.list();
		return studentCourses;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuEvaluateResult> selectMaxEva(String schoolYear, Integer i) {
		// TODO Auto-generated method stub
		String sql1 = "from StuEvaluateResult where schoolYear=? and itemEvaTypeId=?order by mScore desc";
		Query query = currentSession().createQuery(sql1);
		query.setString(0, schoolYear);
		query.setInteger(1, i);
		List<StuEvaluateResult> stuEvaluateResults = query.list();
		return stuEvaluateResults;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<StudentCourse> findStuGradesByPage(Integer stuId, String schoolYear, Integer begin, Integer limit) {
		// TODO Auto-generated method stub
		String sql = "from StudentCourse where stuId=? and schoolYear=?";
		Query query = currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, stuId);
		query.setString(1, schoolYear);
		List<StudentCourse> studentCourses = query.list();
		return studentCourses;
	}


	@Override
	public boolean addStuEvaScore(StuEvaluateResult stuEvaluateResult) {
		// TODO Auto-generated method stub
		currentSession().save(stuEvaluateResult);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuEvaluateResult> selectSummaryStuEvas(Integer claId, String schoolYear) {
		// TODO Auto-generated method stub
		String sql="from StuEvaluateResult where claId=? and schoolYear=?";
		Query query =currentSession().createQuery(sql);
		query.setInteger(0, claId);
		query.setString(1, schoolYear);
		List<StuEvaluateResult> stuEvaluateResults=query.list();
		return stuEvaluateResults;
	}

	@Override
	public boolean deleteStuEvas(Integer claId, String schoolYear) {
		// TODO Auto-generated method stub
		String sql = "delete from StuEvaluateResult  where claId=? and schoolYear=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, claId);
		query.setString(1, schoolYear);
		query.executeUpdate();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuEvaluateResult> findStuEvaByPageCid(Integer itemEvaTypeId,Integer claId, String schoolYear, Integer begin, Integer limit) {
		// TODO Auto-generated method stub
		String sql="from StuEvaluateResult where claId=? and schoolYear=? and itemEvaTypeId=?";
		Query query=currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, claId);
		query.setString(1, schoolYear);
		query.setInteger(2, itemEvaTypeId);
		List<StuEvaluateResult> stuEvaluateResults=query.list();
		return stuEvaluateResults;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuEvaluateResult> findStuEvas(Integer itemEvaTypeId, Integer claId, String schoolYear) {
		// TODO Auto-generated method stub
		String sql="from StuEvaluateResult where claId=? and schoolYear=? and itemEvaTypeId=?";
		Query query=currentSession().createQuery(sql);
		query.setInteger(0, claId);
		query.setString(1, schoolYear);
		query.setInteger(2, itemEvaTypeId);
		List<StuEvaluateResult> stuEvaluateResults=query.list();
		return stuEvaluateResults;
		
	}

}
