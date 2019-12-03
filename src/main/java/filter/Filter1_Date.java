package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

//过滤器的名称为Filter 1，且该过滤器对所有的请求有效
@WebFilter(filterName = "Filter 1", urlPatterns = {"/*"})
public class Filter1_Date implements Filter {

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 1 - date begins");
        //强制转换
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        //获取资源名称
        String path=request.getRequestURI();
        //创建Date对象，声明date变量指向它
        Date date = new Date();
        System.out.println("请求资源名称：" + path + ",请求资源:" + date);
        //执行其他过滤器，若过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 1 - date ends");
    }
}
