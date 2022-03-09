package Context;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/GetContext")
public class GetContext extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
         ServletContext对象获取：
           1. 通过request对象获取 request.getServletContext();
           2. 通过HttpServlet获取 this.getServletContext();
         */
        //1. 通过request对象获取
        ServletContext context1 = request.getServletContext();
        //2. 通过HttpServlet获取
        ServletContext context2 = this.getServletContext();
        System.out.println(context1);
        System.out.println(context2);
        System.out.println(context1 == context2);//true


        //MIME类型数据
        /*
          ServletContext功能：
            1. 获取MIME类型：
             MIME类型:在互联网通信过程中定义的一种文件数据类型
             格式： 大类型/小类型   text/html		image/jpeg
             获取：String getMimeType(String file)
           2. 域对象：共享数据
           3. 获取文件的真实(服务器)路径
         */

        //2. 通过HttpServlet获取
        ServletContext context = this.getServletContext();
        //3. 定义文件名称
        //image/jpeg
        String filename = "a.jpg";
        //4.获取MIME类型
        String mimeType = context.getMimeType(filename);
        System.out.println(mimeType);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}

