package seehope.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import seehope.dao.UserDao;
import seehope.model.UserModel;
import seehope.security.SecurityContext;
import seehope.security.SecurityProvider;
import seehope.service.impl.UserService;

public class SecurityFilter implements Filter {

    private String ignorePattern = "";
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse responce = (HttpServletResponse) resp;
        // 创建当前线程的安全上下文
        SecurityProvider provider = new SecurityProvider();
        SecurityContext securityContext = new SecurityContext(request, responce, provider);
        SecurityContext.thread.set(securityContext);

        if (isIgnore(request)) {
            chain.doFilter(req, resp);
        } else {
            UserModel user = SecurityContext.getCurrentUser();
            if (user == null) {
                responce.sendRedirect(request.getContextPath() + "/common/login.jsp");
            } else {
            	if(!islimit(request))
            		chain.doFilter(req, resp);
            	else
            		responce.sendRedirect(request.getContextPath() + "/module/404.jsp");
            }

        }
    }

    private boolean islimit(HttpServletRequest request) {
    	String uri = request.getServletPath();
    	UserModel user = SecurityContext.getCurrentUser();
    	String context = UserDao.getRoleContext(user);
    	if(uri.equals("/module/user/user-add.jsp")&&context.indexOf("5")<0)
    		return true;
    	if(uri.equals("/module/user/user-setRole.jsp")&&context.indexOf("2")<0)
    		return true;
    	if(uri.equals("/module/user/user-setStatus.jsp")&&context.indexOf("3")<0)
    		return true;
    	if(uri.startsWith("/module/role")&&context.indexOf("1")<0)
    		return true;
    	if(uri.startsWith("/module/sex")&&context.indexOf("6")<0)
    		return true;
    	if(uri.startsWith("/module/log")&&context.indexOf("7")<0)
    		return true;
    	return false;
	}

	private boolean isIgnore(HttpServletRequest request) {
        String uri = request.getServletPath();
        //System.out.println("iuuuu:"+uri);
        // if (!request.getContextPath().equals("")) {
        // uri = uri.replace("/" + request.getContextPath(), "");
        // }
        //System.out.println("puuuu:"+request.getRequestURI());
        //<param-value>*.ttf|*.woff|*.svg|*.eot|/common/*|*.css|*.js|*.png|*.jpg|/LoginController/*</param-value>

        String[] ignorePatterns = ignorePattern.split("\\|");
        for (String pattern : ignorePatterns) {
            if (pattern.indexOf("*") < 0) {
                if (uri.equals(pattern)) {
                    return true;
                }
            } else if (pattern.startsWith("*")) {
                pattern = pattern.replace("*", "");
                if (uri.endsWith(pattern)) {
                    return true;
                }
            } else if (pattern.endsWith("*")) {
                pattern = pattern.replace("*", "");
                if (uri.startsWith(pattern)) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        ignorePattern = config.getInitParameter("ignorePattern");
    }

}
