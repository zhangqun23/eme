package cn.xidian.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

public class CollectionUtil<T> {
	
	/**
	 * 根据实体按指定字段升/降排序
	 * 
	 * @param list
	 * @param filedName
	 * @param ascFlag
	 *            true升序,false降序
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sort(List<?> list, String fieldName, boolean ascFlag) {
		if (list.size() == 0 || fieldName.equals("")) {
			return;
		}
		Comparator<?> cmp = ComparableComparator.getInstance();

		if (ascFlag) {// 升序
			cmp = ComparatorUtils.nullLowComparator(cmp);
		} else {// 降序
			cmp = ComparatorUtils.reversedComparator(cmp);
		}
		Collections.sort(list, new BeanComparator(fieldName, cmp));
	}
}
