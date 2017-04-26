package cn.xidian.interceptor;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import cn.xidian.entity.User;

public class MethodInterceptor extends MethodFilterInterceptor {
	private static final long serialVersionUID = -5407269431454126006L;

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {

		ActionContext ctx = invocation.getInvocationContext();
		Map<String, Object> session = ctx.getSession();
		User user = (User) session.get("tUser");
		if (user == null || user.getSchNum().equals("1111")|| user.getSchNum().equals("0000")) {
			HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext()
					.get(ServletActionContext.HTTP_REQUEST);
			request.setAttribute("Message", "您没有此操作权限!");
			return "ts";
		}
		return invocation.invoke();
	}
}