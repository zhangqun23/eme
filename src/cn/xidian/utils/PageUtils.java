package cn.xidian.utils;


import cn.xidian.entity.PageBean;

public class PageUtils {

	public static <T>  PageBean<T> page(Integer page,Integer allCount,Integer limitNum){
		PageBean<T> pageBean = new PageBean<T>();
		pageBean.setPage(page);
		//设置每页显示的记录数
		int limit =limitNum;
		pageBean.setLimit(limit);
		//设置总记录数
		int totalCount=0;
		totalCount=allCount;
		pageBean.setTotalCount(totalCount);
		//设置总页数
		int totalPage=0;
		if(totalCount % limit ==0){
			totalPage=totalCount/limit;
			
		}else {
			totalPage= totalCount/limit +1;
		}
		pageBean.setTotalPage(totalPage);
		//每页显示的数据集合
		//从哪开始
		int begin=(page-1)*limit;
		pageBean.setBegin(begin);
		return pageBean;
	}
}
