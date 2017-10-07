Camel proxy
-----------------

Example of camel route being used as a HTTP Proxy. It is intended to handle Client Certificate Autentication.

Prerequisites
-------------

* JBoss Fuse 6.3 over JBoss EAP 6.4
* Apache mod_cluster

Running the example
-------------------
* Configure mod cluster with JBoss EAP 6.4
* Configure identity store and truststore keystores (just for test purpose)

Testing
-----------------
Mock backend:
http://localhost:8080/integracion-rolece/backend
https://desarr.local/integracion-rolece/backend (mod_cluster)


Http proxy:
http://localhost:8080/camel-proxy/camel/proxy/...


Https proxy:
http://localhost:8080/camel-proxy/camel/sproxy/...
