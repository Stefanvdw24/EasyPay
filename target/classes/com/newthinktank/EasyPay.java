package main.resources.com.newthinktank;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/EasyPay")
public class EasyPay extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EasyPay() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		 PrintWriter out = response.getWriter();
		 try {
			 out.println("<h3>ha</h3>");
		 }
		 finally {
			 out.close();
		 }
	}

}
