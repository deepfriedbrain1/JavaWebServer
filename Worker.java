import http_request.Request;
import http_response.Resource;
import http_response.Response;
import http_response.ResponseFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Level;
import server_conf.HttpdConf;
import server_conf.MimeTypes;

/**
 *
 * @author Alberto Fernandez Saucedo
 */
public class Worker implements Runnable {
    private Socket client;
    private MimeTypes mimes;
    private HttpdConf config;
    private Request request;
    private Logger logger;
    private boolean running = true;
    
    public Worker(Socket socket, HttpdConf config, MimeTypes mimes){
        this.client = socket;
        this.mimes = mimes;
        this.config = config;
        
    }
    
    @Override
    public void run(){
 
        try{

            InputStream istream = client.getInputStream();
            int length = istream.available();

            Reader readIn = new InputStreamReader(new BufferedInputStream(istream), "US-ASCII");
            StringBuilder data = new StringBuilder();

            while(length != 0){
                int c = readIn.read();
                System.out.print(c + "");
                data.append((char)c);
                length--;
            }

            String str = data.toString();
            request = new Request(str);
            request.setInetAddress(client.getInetAddress());
            String requestURI = request.getURI();
            String requestExtension = request.getExtension();
            String requestMimeType = mimes.lookup(requestExtension);

            Resource resource = new Resource(requestURI, config, requestMimeType);

            ResponseFactory responseFactory = new ResponseFactory();
            Response response = responseFactory.getResponse(request, resource);

            OutputStream ostream = new BufferedOutputStream(client.getOutputStream());
            response.send(ostream);

            try {
                Thread.sleep(200);
                logger = new Logger(configuation.getLogFilePath());
                logger.write(request, response);

            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }


        }catch (Exception ex) {
            Response badRequest = new BadRequestResponse();
            badRequest.send(out);
        }

        finally{
            try {
                client.close();
            } catch (IOException ex) {
                Response serverError = new InternalServerErrorResponse();
                serverError.send(out);
                java.util.logging.Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }    
}
