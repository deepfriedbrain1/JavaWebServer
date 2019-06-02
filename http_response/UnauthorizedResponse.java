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

/**
 *
 * @author zachenney
 */
public class UnauthorizedResponse extends Response {

    public UnauthorizedResponse(Resource resource) {
        super(resource);
        this.code = 401;
        this.reasonPhrase = "Unauthorized";
        this.initializeHeaders();
        initializeEntityHeaders();
    }

    @Override
    public void send(OutputStream out) throws IOException {
        
        
        this.body = getErrorPage().getBytes();
        
        Writer output = new OutputStreamWriter(out);
        
            try {

                output.write(this.statusLine);
                output.write(this.requiredHeaders);
                output.write(this.entityHeaders);
                output.write("\n");
                output.flush();
                
                out.write(this.body);

                output.flush();
                output.close();
                

            } catch (IOException ex) {
                System.out.println("bummer");
            }
            System.out.print(super.statusLine);
            System.out.print(super.requiredHeaders);
            System.out.print(super.entityHeaders);
            System.out.print(getErrorPage());
            
    }
    
    @Override
    protected void initializeEntityHeaders() {
        this.entityHeaders += "Content-Type: text/html\n";
        this.entityHeaders += "Content-Length: " + getErrorPage().length() +"\n";
    }
    
    private String getErrorPage(){
        String errorPage = "<html>"
                         + "<head><title>401 Unauthorized</title></head><body>"
                         + "<br><h1>401 - Not Found</h1>"
                         + "</body></html>";
        
        return errorPage;
    }
}