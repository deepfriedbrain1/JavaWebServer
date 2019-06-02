/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

/**
 *
 * @author zachenney
 */
public class NoContentResponse extends Response {

    public NoContentResponse(Resource resource) {
        super(resource);
        this.code = 204;
        this.reasonPhrase = "No Content";
    }

    @Override
    public void send(OutputStream out) throws IOException {
        
        this.initializeHeaders();
        //initializeEntityHeaders();
        Writer output = new OutputStreamWriter(out);
        
            try {

                output.write(this.statusLine);
                output.flush();
                output.write(this.requiredHeaders);
                output.write("\n");
                //output.write(this.entityHeaders);
                
                //out.flush();
                output.flush();
                output.close();
                //out.close();

            } catch (IOException ex) {
                System.out.println("bummer");
            }
            
            System.out.print(super.statusLine);
            System.out.print(super.requiredHeaders);
            //System.out.print(super.entityHeaders);
            //System.out.print(Arrays.toString(super.body));
    }
    
    @Override
    protected void initializeEntityHeaders() {
        this.entityHeaders += "Content-Type: " + this.resource.getMimeType() + "\n";
        //this.entityHeaders += "Content-Length: " + this.body.length +"\n\n";
    }
}
