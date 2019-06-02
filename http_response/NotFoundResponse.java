
package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zachenney
 */
public class NotFoundResponse extends Response {
    
    public NotFoundResponse(Resource resource) {
        super(resource);
        this.code = 404;
        this.reasonPhrase = "Not Found";       
    }

    @Override
    public void send(OutputStream out) throws IOException {
        
        this.initializeHeaders();
        initializeEntityHeaders();
        
        Writer output = new OutputStreamWriter(out);
        this.body = getErrorPage().getBytes();

        output.write(this.statusLine);
        output.write(this.requiredHeaders);
        output.write(this.entityHeaders);
        output.flush();
        out.write(this.body);

        output.flush();
        output.close();
        out.close();

    }

    @Override
    protected void initializeEntityHeaders() {
        this.entityHeaders = "Content-Type: text/html\n";
        this.entityHeaders += "Content-Length: " + this.getErrorPage().length() +"\n\n";
    }
    
    private String getErrorPage(){
        String errorPage = "<html>"
                         + "<head><title>404 Not Found</title></head><body align=\"center\">" 
                         + "<h1>404 - Not Found</h1>"
                         + "<h2>Little lost are we?</h2>"
                         + "</body></html>";
        
        return errorPage;
    }
    
    
    
}
