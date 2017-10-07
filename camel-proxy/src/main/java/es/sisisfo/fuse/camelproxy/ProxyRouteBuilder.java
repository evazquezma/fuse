package es.sisisfo.fuse.camelproxy;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.util.jsse.KeyManagersParameters;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.wildfly.extension.camel.CamelAware;

@Startup
@CamelAware
@ApplicationScoped
public class ProxyRouteBuilder extends RouteBuilder {
		
    @Override
    public void configure() throws Exception {    	   
    	from("servlet:/proxy?matchOnUriPrefix=true&servletName=proxyServlet")
    	.routeId("Common Proxy")
    	.log(LoggingLevel.INFO, "Procesando common")
    	.to("http4://localhost:8080/camel-proxy/backend/?bridgeEndpoint=true");
    	

    	final Endpoint httpsEndpoint = setupSSLConext(getContext());
    	from("servlet:/sproxy?matchOnUriPrefix=true&servletName=proxyServlet")
    	.routeId("SSL Proxy")
    	.log(LoggingLevel.INFO, "Procesando SSL")
    	.to(httpsEndpoint);  	    	  
    }
    
      
  
    private Endpoint setupSSLConext(CamelContext camelContext) throws Exception {

        final KeyStoreParameters identityStore = new KeyStoreParameters();       
        identityStore.setResource("C:/Servers/jboss-eap-6.4-fuse-6.3/proyectos/rolece/conf/indentyStore-1234.jks");
        identityStore.setPassword("1234");
        
        final KeyStoreParameters trustStore = new KeyStoreParameters();       
        trustStore.setResource("C:/Servers/jboss-eap-6.4-fuse-6.3/proyectos/rolece/conf/trustStore-1234.jks");
        trustStore.setPassword("1234");
        
        

        final KeyManagersParameters identityKeyManagersParameters = new KeyManagersParameters();
        identityKeyManagersParameters.setKeyStore(identityStore);
        identityKeyManagersParameters.setKeyPassword("1234");
        
        final TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        trustManagersParameters.setKeyStore(trustStore);

        final SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setKeyManagers(identityKeyManagersParameters);
        sslContextParameters.setTrustManagers(trustManagersParameters);

        
        final X509HostnameVerifier noVerifier = new X509HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {			
				return true;
			}

			@Override
			public void verify(String host, SSLSocket ssl) throws IOException {
				// No hace nada				
			}

			@Override
			public void verify(String host, X509Certificate cert) throws SSLException {
				// No hace nada				
			}

			@Override
			public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
				// No hace nada				
			}        	
        };		
        
        final HttpComponent httpComponent = camelContext.getComponent("https4", HttpComponent.class);
        httpComponent.setSslContextParameters(sslContextParameters);      
        httpComponent.setX509HostnameVerifier(noVerifier );
        
        return httpComponent.createEndpoint("https4://desarr.local/camel-proxy/backend/?bridgeEndpoint=true");
    }
 
}
