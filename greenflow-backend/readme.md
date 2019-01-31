GREENFLOW-BACKEND

Prerequisitions

-Install Wildfly 15 Final
-Modify the standalone.xml as the following
    modify the DATASOURCES from ExampleDS to greenflowDS
    set the connection url to: "dbc:h2:tcp://localhost/~/greenflow" This will prevent data loss on service restart
    IMPORTANT NOTE: From this moment, you have to manually start the H2 database. In Wildfly under the modules, find the right jar, and start with the command:
    'java -jar h2-1.4.193.jar  -webAllowOthers -tcpAllowOthers'
    
-mvn [clean] install : builds the .war file
-put the .war file (located under /target) into the $WILDFLY_HOME/standalone/deployments directory
-run the $WILDFLY_HOME/bin/standalone command (.sh when Linux, .bat when Windows)
-the server set-up, it listens on localhost:8080/greenflow/rest/{API_CALL}