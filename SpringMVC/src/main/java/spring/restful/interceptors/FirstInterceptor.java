package spring.restful.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class FirstInterceptor implements HandlerInterceptor{

	/**
	 * �÷�����Ŀ�귽��֮ǰ������.
	 * ������ֵΪ true, �������ú������������Ŀ�귽��. 
	 * ������ֵΪ false, �򲻻��ٵ��ú������������Ŀ�귽��. 
	 * 
	 * ���Կ�����Ȩ��. ��־, �����. 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("[FirstInterceptor] preHandle");
		return true;
	}

	/**
	 * ����Ŀ�귽��֮��, ����Ⱦ��ͼ֮ǰ. 
	 * ���Զ��������е����Ի���ͼ�����޸�. 
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("[FirstInterceptor] postHandle");
	}

	/**
	 * ��Ⱦ��ͼ֮�󱻵���. �ͷ���Դ
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("[FirstInterceptor] afterCompletion");
	}

}
