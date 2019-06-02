
package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zachenney
 */
public class NotModifiedResponse extends Response {

    public NotModifiedResponse(Resource resource) {
        super(resource);
        this.code = 304;
        this.reasonPhrase = "Not Modified";
    }

    @Override
    public void send(OutputStream out) throws IOException {
        
        Writer output = new OutputStreamWriter(out);

        output.write(this.statusLine);
        output.write(this.requiredHeaders);
        
        output.flush();
        output.close();

    }

    @Override
    protected void initializeEntityHeaders() {
    }
    
}
