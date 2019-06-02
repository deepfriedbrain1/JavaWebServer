package http_response;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import java.time.format.FormatStyle;
import java.time.temporal.UnsupportedTemporalTypeException;
/**
 *
 * @author Alberto Fernandez Saucedo, Zac Henney
 */
public abstract class Response {
    
    protected int code;
    protected String reasonPhrase;   
    protected Resource resource;
    protected String requestVerb;
    protected String entityHeaders = "";
    protected String statusLine;
    protected String requiredHeaders;
    protected byte[] body = null;
    protected String httpVersion = "HTTP/1.1";
    private String response;
    final String SERVER = "Super Awesome Server 3000";
    
    
    public Response(Resource resource){       
        this.resource = resource;   
    }

    public abstract void send(OutputStream out) throws IOException;
    
    protected void setHeader(String key, String value){
        entityHeaders += key + ": " + value + "\n";
        System.out.print(entityHeaders);
    }
    
    protected void setVerb(String requestVerb){
        this.requestVerb = requestVerb;
    }
    
    public int getBodySize(){
        return body.length;
    }
    
    protected void setBody( Path filePath ) throws IOException {
        body = Files.readAllBytes(filePath);
    }
    
    protected void initializeHeaders(){
        statusLine = httpVersion + " " + code + " " + reasonPhrase + "\n";
        requiredHeaders = "Server: " + SERVER + "\n";
        
        try{
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC);  
        String formattedDateTime = dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        requiredHeaders += "Date: " + formattedDateTime + "\n";
        } catch (UnsupportedTemporalTypeException e)
        {
            System.out.println("RFC_1123_DATE_TIME is not supported: " + e.getMessage());
        }
    }
    
    protected abstract void initializeEntityHeaders();
    
}

