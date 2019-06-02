
package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zachenney
 */
public class InternalServerErrorResponse extends Response {
    
    public InternalServerErrorResponse(Resource resource) {
        super(resource);
        super.code = 500;
        super.reasonPhrase = "Internal Server Error";
    }

    @Override
    public void send(OutputStream out) throws IOException {
        initializeEntityHeaders();
        Writer output = new OutputStreamWriter(out);

        output.write(this.statusLine);
        output.write(this.requiredHeaders);
        output.write(this.entityHeaders);
        out.write(this.body);

        output.flush();
        output.close();

    }

    @Override
    protected void initializeEntityHeaders() {
        this.entityHeaders = "Content-Type: texxt/html\n";
        this.entityHeaders += "Content-Length: " + this.getErrorPage().length() +"\n";
        this.entityHeaders += "Connection: closed\n\n";
    }
    
    private String getErrorPage(){
        String errorPage = "<html>"
                         + "<head><title>500 Internal Server Error</title></head><body>" 
                         + "<h1>500 - Internal Server Error</h1>"
                         + "<h2>Uh oh something is broken</h2>"
                         + "</body></html>";
        
        return errorPage;
    }
    
}
