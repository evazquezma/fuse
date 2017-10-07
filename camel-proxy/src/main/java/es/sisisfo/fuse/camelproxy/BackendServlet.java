package es.sisisfo.fuse.camelproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name="backendServlet", urlPatterns = {"/backend/*"})
public class BackendServlet extends HttpServlet {
	private static final long serialVersionUID = -2182366270115387826L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BackendServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		printRequestInfo(req);
			
		resp.getWriter().write("<html><body>OK por GET</body></html>");
		resp.getWriter().close();
	}		

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		printRequestInfo(req);
		
		resp.getWriter().write("<html><body>OK por POST</body></html>");
		resp.getWriter().close();
	}
	
	
	
	private void printRequestInfo(HttpServletRequest req) throws IOException {
		LOGGER.info("Request URL: {}" + req.getRequestURL());
		
		final Enumeration<String> headers = req.getHeaderNames();
		while (headers.hasMoreElements()) {
			final String headerName = (String) headers.nextElement();
			LOGGER.info("Param {}: {}", headerName, req.getHeader(headerName));
		}
		
		final Enumeration<String> parameters = req.getParameterNames();
		while(parameters.hasMoreElements()) {
			final String paramName = (String) parameters.nextElement();
			LOGGER.info("Param {}: {}", paramName, req.getParameter(paramName));
		}
		
		
		
	    final StringBuilder buffer = new StringBuilder();
	    final BufferedReader reader = req.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	    }
		LOGGER.info("Body: {}", buffer.toString());
	}
}
