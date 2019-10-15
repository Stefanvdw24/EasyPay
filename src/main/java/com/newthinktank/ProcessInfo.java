package main.java.com.newthinktank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/ProcessInfo")
@MultipartConfig
public class ProcessInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProcessInfo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/DisplayInfo.jsp";
		
		Part filePart = request.getPart("TheFile");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
	    InputStream fileContent = filePart.getInputStream();
	    ArrayList<String> records = new ArrayList<>();
	    StringBuilder stringBuilder = new StringBuilder();
	    String line = null;
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileContent, "US-ASCII"))) {	
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
				records.add(line);
			}
		}
		String fileContentString = stringBuilder.toString();
		String header = records.get(0);
		String[] trailers = new String[2];
		trailers[0] = records.get(records.size()-2);
		trailers[1] = records.get(records.size()-1);
		records.remove(0);
		records.remove(records.size()-1);
		records.remove(records.size()-1);
		
		String errorsMessage = "";
		
		//header checks
		if(!header.startsWith("01")) {
			errorsMessage += "The header does not have the right record identifier<br>";
		}
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            df.setLenient(false);
            df.parse(header.substring(2,14));
        } catch (ParseException e) {
        	errorsMessage += "The header has an invalid date/time<br>";
        }
		
		//transaction checks
		for(int x=0;x <= records.size()-1;x++) {
			if(!records.get(x).startsWith("10") && !records.get(x).startsWith("11") && !records.get(x).startsWith("50")) {
				errorsMessage += "Transaction " + (x+1) + " does not have a valid record identifier<br>";
			}
	        try {
	            DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
	            df.setLenient(false);
	            df.parse(records.get(x).substring(2,14));
	        } catch (ParseException e) {
	        	errorsMessage += "Transaction " + (x+1) + " has an invalid date/time<br>";
	        }
	        
	        int foo = 0;
	        for(int y = 0; y < 74; y++) {
	        	foo += ( ( (int) records.get(x).charAt(y) ) * (y+1) );
	        }
	        if( !records.get(x).endsWith(Integer.toString(foo)) ) {
	        	errorsMessage += "Transaction " + (x+1) + " byte check failed<br>";
	        }
	        if(records.get(x).startsWith("11")) {
	        	String b = records.get(x).substring(64,66);
	        	if(b.compareTo("00") != 0 && b.compareTo("01") != 0 && b.compareTo("08") != 0 && 
	        	   b.compareTo("09") != 0 && b.compareTo("04") != 0 && b.compareTo("05") != 0) {
	        		errorsMessage += "Transaction " + (x+1) + " has an invalid tender type<br>";
	        	}
	        }
		}
		
		//trailer checks
		for(int x=0; x<=1; x++) {
			if(!trailers[x].startsWith("88") && !trailers[x].startsWith("99")) {
				errorsMessage += "Trailer " + (x+1) + " does not have a valid record identifier<br>";
			}
		}
		int ByteCheckTotal = Integer.parseInt(header.substring(74,80));
		for(int x=0;x <= records.size()-1;x++) {
			ByteCheckTotal += Integer.parseInt(records.get(x).substring(74,80));
		}
		ByteCheckTotal += Integer.parseInt(trailers[0].substring(74,80));
		if ( !trailers[1].substring(62,74).endsWith(Integer.toString(ByteCheckTotal)) ) {
			errorsMessage += "The Byte Check in the last trailer is not equal to the sum of all the other byte checks<br>";
		}
		
		request.setAttribute("errorsMessage", errorsMessage);
		request.setAttribute("fileName", fileName);
		request.setAttribute("fileContent", fileContentString);
		getServletContext()
		.getRequestDispatcher(url)
		.forward(request, response);
	}

}
