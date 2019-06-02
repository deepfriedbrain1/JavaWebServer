
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import server_conf.HttpdConf;
import server_conf.MimeTypes;


/**
 *
 * @author Alberto Fernandez Saucedo
 */
public class WebServer {
    public HttpdConf configuration;
    public MimeTypes mimeTypes;
    private static ServerSocket socketServer;
    private static ExecutorService threadPool;
    private Map<String,String> accessFiles;
    private final static int PORT = 8080;
    
    public WebServer() throws FileNotFoundException{
        configuration = new HttpdConf("build/classes/conf/httpd.conf");
        configuration.load();
        mimeTypes = new MimeTypes("build/classes/conf/MIME.types");
        mimeTypes.load();
    }
   
    public void start() throws IOException{
      
        try{

            threadPool = Executors.newFixedThreadPool(10);
            socketServer = new ServerSocket(PORT);
            
            while(true){               
                Socket socket = socketServer.accept();
                System.out.println("New Request: Thread Created");
                threadPool.execute(new Worker(socket, configuration, mimeTypes));      
            } 
            
        }catch(IOException ioe){
            System.out.println("IOException: " + ioe.getMessage());
            System.out.println("Server shutting down...");
            threadPool.shutdown();
        } 
    }

    public static void main(String[] args) throws IOException{
        WebServer webserver = new WebServer();
        webserver.start();

    }
}
