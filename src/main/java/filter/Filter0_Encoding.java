package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//过滤器的名称为Filter 0，且该过滤器对所有的请求有效
@WebFilter(filterName = "Filter 0", urlPatterns = {"/*"})

public class Filter0_Encoding implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
    //重写Filter中的声明方法
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 0 - encoding begins");
        //强制转换
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        //获取资源名称
        String path= request.getRequestURI();
        String method=request.getMethod();
        if (path.contains("/login")){
            System.out.println("未设置字符编码格式");
        }else {
            System.out.println(method);
            response.setContentType("text/html;charset=UTF-8");
            System.out.println("设置响应对象字符编码格式为utf8");
            if (method=="POST"||method=="PUT"){
                System.out.println(method);
                request.setCharacterEncoding("UTF-8");
                System.out.println("设置请求对象字符编码格式为utf8");
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 0 - encoding ends");
    }
}
