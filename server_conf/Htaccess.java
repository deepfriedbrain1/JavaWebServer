package server_conf;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alberto Fernandez Saucedo
 */
public class Htaccess extends ConfigurationReader{
    private Htpassword authUserFile;
    private String authType;
    private String authName;
    private String require;
    private String filename;

    public Htaccess(String filename) throws IOException {
        super(filename);
        this.filename = filename;     
        
    }
    
    @Override
    public void load(){
        
        try {
            super.load();
            while(super.hasMoreLines()){
                String line = nextLine();
                String[] tokens = line.split(" ");
                String name = tokens[0];
                String value = tokens[1].replace("\"", "").trim();
                if(name.equals("AuthUserFile")){
                    try {
                        authUserFile = new Htpassword(value);
                        authUserFile.load();
                    } catch (IOException ex) {
                        
                        Logger.getLogger(Htaccess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(name.equals("AuthType"))
                    authType = value;
                
                if(name.equals("AuthName"))
                    authName = value;
                
                if(name.equals("Require"))
                    require = value;
            }
        } catch (IOException ex) {
            Logger.getLogger(Htaccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public boolean isAuthorized(String authInfo){
        return authUserFile.isAuthorized(authInfo);     
    }
    
    public String getAuthType(){
        return authType;
    }
    
    public String getAuthName(){
        return authName;
    }
    
}
