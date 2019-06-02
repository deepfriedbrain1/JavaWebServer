package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zachenney
 */
public class BadRequestResponse extends Response {

    public BadRequestResponse(Resource resource) {
        super(resource);
        this.code = 400;
        this.reasonPhrase = "Bad Request";
        this.body = getErrorPage().getBytes();
    }

    @Override
    public void send(OutputStream out) throws IOException {
  
        Writer output = new OutputStreamWriter(out);
        
            try {
                initializeEntityHeaders();

                output.write(this.statusLine);
                output.write(this.requiredHeaders);
                output.write(this.entityHeaders);
                out.write(this.body);
                output.flush();
                output.close();

            } catch (IOException ex) {
                System.out.println("bummer");
            }
    }

    @Override
    protected void initializeEntityHeaders() {
        this.entityHeaders = "Content-Type: texxt/html\n";
        this.entityHeaders += "Content-Length: " + this.body.length +"\n";
        this.entityHeaders += "Connection: closed\n\n";
    }
    
    private String getErrorPage(){
        String errorPage = "<html>"
                         + "<head><title>400 Bad Request</title></head><body>" 
                         + "<h1>400 - Bad Request :(</h1></body></html>";
        
        return errorPage;
    }
    
}
