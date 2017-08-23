package com.baixinping.framework.servlet;

import com.baixinping.framework.common.entity.Data;
import com.baixinping.framework.common.entity.Handler;
import com.baixinping.framework.common.entity.Param;
import com.baixinping.framework.common.entity.View;
import com.baixinping.framework.common.helper.BeanHelper;
import com.baixinping.framework.common.helper.ConfigHelper;
import com.baixinping.framework.common.helper.ControllerHelper;
import com.baixinping.framework.common.loader.HelperLoader;
import com.baixinping.framework.common.utils.CodeUtil;
import com.baixinping.framework.common.utils.JSONUtil;
import com.baixinping.framework.common.utils.ReflectUtil;
import com.baixinping.framework.common.utils.StreamUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*" , loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化helper
        HelperLoader.init();
        ServletContext servletContext = servletConfig.getServletContext();
        //注册jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的Servlet
        ServletRegistration staticServlet = servletContext.getServletRegistration("default");
        staticServlet.addMapping(ConfigHelper.getAppStaticPath());
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方法和请求路径
        String method = req.getMethod().toLowerCase();
        String path = req.getPathInfo();
        //获取处理器
        Handler handler = ControllerHelper.getHandler(method, path);
        if (handler != null){
            //获取Controller
            Object controllerBean = BeanHelper.getBean(handler.getControllerClazz());
            //创建请求参数对象
            Map<String, Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()){
                String name = parameterNames.nextElement();
                String value = req.getParameter(name);
                paramMap.put(name, value);
            }

            String body = CodeUtil.decodeUrl(StreamUtil.getString(req.getInputStream()));
            if (!StringUtils.isEmpty(body)){
                String[] params = body.split("&");
                for (String param : params){
                    String[] kv = param.split("=");
                    if (kv.length == 2){
                        paramMap.put(kv[0], kv[1]);
                    }
                }
            }
            Param param = new Param(paramMap);

            //调用RequestMapping方法
            Method mappingMethod = handler.getMethod();
            Object result = ReflectUtil.invokeMethod(controllerBean, mappingMethod, param);

            //处理Mapping方法的返回值
            if (result instanceof View){
                //jsp
                View view = (View) result;
                String jspPath = view.getPath();
                if (!StringUtils.isEmpty(jspPath)){
                    if(jspPath.startsWith("/")){
                        resp.sendRedirect(req.getContextPath()+jspPath);
                    }else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()){
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + jspPath).forward(req,resp);
                    }
                }
            }else if (result instanceof Data){
                //json
                Data data = (Data) result;
                Object model = data.getModel();
                if (model != null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    writer.write(JSONUtil.toJson(data));
                    writer.flush();;
                    writer.close();
                }

            }
        }
    }
}
