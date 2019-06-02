import http_request.Request;
import http_response.Response;
import java.io.BufferedWriter;
import java.io.File;
import static java.io.File.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * "/Users/jrob/workspace/server/logs/log.txt"
 * 
 * COMMON LOG FORMAT (CLF):
 * 127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.1" 200 2326
 * 
 * @author Alberto Fernandez Saucedo
 */
public class Logger {
    private String filename;
    private String p = separator;
    private String crfl = "\r\n";
    private String clientIPaddress;
    private String identd = "-";
    private String userid = "-";
    private String time;
    private String requestLine;
    private int statusCode;  
    public Logger(String fileName){
        this.filename = fileName;
    }
    
    public Logger(){}
    public void write(Request request, Response response){
        
        clientIPaddress = request.getInetAddress().getHostAddress();
        requestLine = request.getVerb() + " " + request.getURI() + " \"" + request.getHttpVersion().trim() + "\"";
        statusCode = request.getStatusCode();
     
        time = getDateTime();
        
        String logData = 
                clientIPaddress + " " + identd + " " + userid + 
                " [" + time + "] " + " " + requestLine + " " + statusCode + " " + response.getBodySize() + crfl;
                
        log(logData);
    }
    
    private String getDateTime(){
        Date date = new Date(); 
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss");
        return sdf.format(date);
    }
    
    private void log(String logData){
        BufferedWriter bWriter; 
        try {
            FileWriter fWriter = new FileWriter(filename, true);
            bWriter = new BufferedWriter(fWriter);
            bWriter.write(logData);
            bWriter.flush();
            bWriter.close();
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}