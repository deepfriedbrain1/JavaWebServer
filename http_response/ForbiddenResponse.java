
package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zachenney
 */
public class ForbiddenResponse extends Response {
    
    public ForbiddenResponse(Resource resource) {
        super(resource);
        super.code = 403;
        super.reasonPhrase = "Forbidden";
        this.body = getErrorPage().getBytes();
    }

    @Override
    public void send(OutputStream out) throws IOException {
        
        this.initializeHeaders();
        initializeEntityHeaders();
        
        Writer output = new OutputStreamWriter(out);

                output.write(this.statusLine);
                output.write(this.requiredHeaders);
                output.write(this.entityHeaders);
                output.write("\n");
                
                output.flush();
                
                out.write(this.body);
                
                output.flush();
                output.close();

    }

    @Override
    protected void initializeEntityHeaders() {
        this.entityHeaders = "Content-Type: text/html\n";
        this.entityHeaders += "Content-Length: " + getErrorPage().length() +"\n";
    }
    
    private String getErrorPage(){
        String errorPage = "<html>"
                         + "</br>"
                         + "<head><title>403 Forbidden</title></head><body align=\"center\">" 
                         + "<h1>403 - Forbidden</h1>"
                         + "<h2>Ah ah ah you didnt say the magic word</h2>"
                         + "</body></html>";
        
        return errorPage;
    }
}
