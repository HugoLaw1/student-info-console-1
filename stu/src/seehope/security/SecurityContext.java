package seehope.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import seehope.model.UserModel;
import seehope.util.MD5;

public class SecurityContext {

    /**
     * 线程副本存储，可以缓存当前线程的数据，线程与线程之间的数据不存在任何的影响
     */
    public static ThreadLocal<SecurityContext> thread = new ThreadLocal<SecurityContext>();

    private SecurityProvider securityProvider;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public void setThreadLocal(SecurityContext context) {
        thread.set(context);
    }

    public static SecurityContext getContext() {
        return thread.get();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public SecurityContext() {
        super();
    }

    public SecurityContext(HttpServletRequest request, HttpServletResponse response, SecurityProvider securityProvider) {
        super();
        this.request = request;
        this.response = response;
        this.securityProvider = securityProvider;
    }
    
    public static String encryptPassword(String str){
    	return MD5.encrypt(str);
    }

    /**
     * 获取当前的用户数据
     *
     * @return
     */
    public static UserModel getCurrentUser() {
        SecurityContext context = thread.get();
//        if(context != null) {
//        return context.getSecurityProvider().getCurrentUser(context.request);
//        }
        return new UserModel();
    }
    
    public static String getCurrentContext() {
        SecurityContext context = thread.get();
        return context.getSecurityProvider().getCurrentContext(context.request);
    }

    public static void setCurrentUser(UserModel user){
        SecurityContext context = thread.get();
        context.getSecurityProvider().setCurrentUser(user,context.request);
    }

    /**
     * 获取安全提供器，用来获取与用户相关的数据
     *
     * @return
     */
    public SecurityProvider getSecurityProvider() {
        return securityProvider;
    }

    /**
     * 设置安全提供器
     *
     * @return
     */
    public void setSecurityProvider(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

}
