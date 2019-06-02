
package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author zachenney
 */
public class OkResponse extends Response {
    
    public OkResponse(Resource resource) throws IOException {
        super(resource);
        this.code = 200;
        this.reasonPhrase = "OK";
        
    }
    
    @Override
    public void send(OutputStream out) {
        
        Writer output = new OutputStreamWriter(out);
        
        if(resource.isScript()){
            //script stuff
        }
        else{
            try {
                initializeEntityHeaders();
                
                output.write(this.statusLine);
                output.write(this.requiredHeaders);
                output.write(this.entityHeaders);
                output.flush();
                
                out.write(this.body);
                out.flush();
                output.flush();
                output.close();
                out.close();
                //out.close();
  
            } catch (IOException ex) {
                System.out.println("bummer");
            }
        }           
    }
    
    protected void initializeEntityHeaders(){
        
        this.entityHeaders = "Content-Type: " + this.resource.getMimeType() + "\n";
        this.entityHeaders += "Content-Length: " + this.body.length +"\n\n";
    }
 
}
    
    
