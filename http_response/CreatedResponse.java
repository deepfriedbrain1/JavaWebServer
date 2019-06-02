
package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zachenney
 */
public class CreatedResponse extends Response {
    
    public CreatedResponse(Resource resource) {
        super(resource);
        this.code = 201;
        this.reasonPhrase = "Created";
    }
    
    @Override
    public void send(OutputStream out) throws IOException {
  
        Writer output = new OutputStreamWriter(out);

            this.initializeHeaders();
            initializeEntityHeaders();

            output.write(this.statusLine);
            output.write(this.requiredHeaders);
            output.write(this.entityHeaders);
            
            output.flush();
            output.close();
    
        }

    @Override
    protected void initializeEntityHeaders() {
        this.entityHeaders += "Content-Type: " + this.resource.getMimeType() + "\n\n";
    }
}

    
    

