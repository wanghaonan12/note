package Context;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/GetRealPath")
public class GetRealPath extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// 通过HttpServlet获取
        ServletContext context = this.getServletContext();
        // 获取文件的服务器路径
        //web目录下资源访问
        String b = context.getRealPath("/b.txt");
        System.out.println(b);
        // File file = new File(realPath);
        //WEB-INF目录下的资源访问,在/WEB-INF目录下会受到保护,客户端无法直接访问
        String c = context.getRealPath("/WEB-INF/c.txt");
        System.out.println(c);

        //src目录下的资源访问
        //注意注意注意注意注意注意注意注意注意注意注意注意注意注意
        //这些路径都是看target文件下的路径.绝对路径是/(以项目文件为出发地)开头的相对路径是./或../开头(以自身为出发地)
        //注意注意注意注意注意注意注意注意注意注意注意注意注意注意
        String a = context.getRealPath("/WEB-INF/classes/Context/a.txt");
        System.out.println(a);
    }
}
