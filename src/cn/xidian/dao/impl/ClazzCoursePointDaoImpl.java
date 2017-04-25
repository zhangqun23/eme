package cn.xidian.dao.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import cn.xidian.dao.ClazzCoursePointDao;
import cn.xidian.entity.AverClazzCoursePoint;
import cn.xidian.entity.ClazzCoursePoint;

@Component("clazzCoursePointDaoImpl")
public class ClazzCoursePointDaoImpl implements ClazzCoursePointDao {

	private SessionFactory sessionFactory;

	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public boolean addByCursNum(ClazzCoursePoint point) {
		currentSession().save(point);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClazzCoursePoint> selectByCursNum(String cursNum) {
		List<ClazzCoursePoint> ips = new LinkedList<ClazzCoursePoint>();
		String sql = "from ClazzCoursePoint cp where cursId = (select cursId from Course as cursId where cursNum = ? and isDelete=1) order by clazzCursPointId asc";
		Query query = currentSession().createQuery(sql).setString(0, cursNum);
		ips.addAll(query.list());
		return ips;
	}

	@Override
	public boolean add(ClazzCoursePoint point) {
		currentSession().save(point);
		return true;
	}
	
	@Override
	public boolean add(AverClazzCoursePoint point) {
		currentSession().save(point);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClazzCoursePoint> selectByCursId(Integer cursId) {
		List<ClazzCoursePoint> ips = new LinkedList<ClazzCoursePoint>();
		String sql = "from ClazzCoursePoint cp where cursId = ? order by clazzCursPointId asc";
		Query query = currentSession().createQuery(sql).setInteger(0, cursId);
		ips.addAll(query.list());
		return ips;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClazzCoursePoint> selectByCursAndClazzId(Integer cursId,
			String clazId) {
		List<ClazzCoursePoint> ips = new LinkedList<ClazzCoursePoint>();
		String sql = "from ClazzCoursePoint cp where cursId = ? and claId = (?) order by clazzCursPointId asc";
		Query query = currentSession().createQuery(sql).setInteger(0, cursId)
				.setString(1, clazId);
		ips.addAll(query.list());
		return ips;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AverClazzCoursePoint> selectByCursAndGrade(Integer cursId,
			String grade) {
		List<AverClazzCoursePoint> ips = new LinkedList<AverClazzCoursePoint>();
		String sql = "from AverClazzCoursePoint acp where cursId = ? and grade = (?) order by clazzCursPointId asc";
		Query query = currentSession().createQuery(sql).setInteger(0, cursId)
				.setString(1, grade);
		ips.addAll(query.list());
		return ips;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClazzCoursePoint> selectByCursAndClazzId(Integer cursId,
			Integer clazId) {
		List<ClazzCoursePoint> ips = new LinkedList<ClazzCoursePoint>();
		String sql = "from ClazzCoursePoint cp where cursId = ? and claId = ? order by clazzCursPointId asc";
		Query query = currentSession().createQuery(sql).setInteger(0, cursId)
				.setInteger(1, clazId);
		ips.addAll(query.list());
		return ips;
	}

	@Override
	public boolean deleteById(ClazzCoursePoint coursePoint) {
		currentSession().delete(coursePoint);
		return true;
	}
	
	@Override
	public boolean deleteByAverId(AverClazzCoursePoint coursePoint) {
		currentSession().delete(coursePoint);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClazzCoursePoint> findByCursNameAndTerm(String cursName) {
		List<ClazzCoursePoint> cplist = new LinkedList<ClazzCoursePoint>();
		String sql = "from ClazzCoursePoint cp where cp.course.cursName=? and cp.course.isDelete=1";
		Query query = currentSession().createQuery(sql).setString(0, cursName);
		cplist.addAll(query.list());
		return cplist;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AverClazzCoursePoint> findByCursNameAndGrade(String cursName) {
		List<AverClazzCoursePoint> cplist = new LinkedList<AverClazzCoursePoint>();
		String sql = "from AverClazzCoursePoint acp where acp.course.cursName=? and acp.course.isDelete=1";
		Query query = currentSession().createQuery(sql).setString(0, cursName);
		cplist.addAll(query.list());
		return cplist;
	}

	@Override
	public boolean deleteByCursId(Integer cursId) {
		String sql = "delete ClazzCoursePoint cp where cp.course.cursId=?";
		Query query = currentSession().createQuery(sql).setInteger(0, cursId);
		query.executeUpdate();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClazzCoursePoint> findByCursAndClazz(String cursName,
			String claName) {
		List<ClazzCoursePoint> cplist = new LinkedList<ClazzCoursePoint>();
		String sql = "from ClazzCoursePoint cp where cp.course.cursName=? "
				+ "and cp.course.isDelete=1 and cp.clazz.claName in (?)";
		Query query = currentSession().createQuery(sql).setString(0, cursName)
				.setString(1, claName);
		cplist.addAll(query.list());
		return cplist;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AverClazzCoursePoint> findByCursAndGrade(String cursName,
			String gradeName) {
		List<AverClazzCoursePoint> cplist = new LinkedList<AverClazzCoursePoint>();
		String sql = "from AverClazzCoursePoint cp where cp.course.cursName=? "
				+ "and cp.course.isDelete=1 and cp.grade in (?)";
		Query query = currentSession().createQuery(sql).setString(0, cursName)
				.setString(1, gradeName);
		cplist.addAll(query.list());
		return cplist;
	}

}
