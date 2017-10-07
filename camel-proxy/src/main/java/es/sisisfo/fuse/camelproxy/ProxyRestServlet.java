package es.sisisfo.fuse.camelproxy;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "proxyServlet", urlPatterns = { "/camel/*" }, loadOnStartup = 1)
public class ProxyRestServlet extends org.apache.camel.component.servlet.CamelHttpTransportServlet {	
	private static final long serialVersionUID = 2886685336873526067L;
	
}
