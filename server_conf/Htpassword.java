package server_conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alberto Fernandez Saucedo
 */
public class Htpassword extends ConfigurationReader {
    private HashMap<String, String> users;

    public Htpassword(String filename) throws IOException {
        super(filename);   
        this.users = new HashMap<>();       
    }
    
    protected void parseLine(String line){
        String[] tokens = line.split(":");
        if(tokens.length == 2){
            users.put(tokens[0], tokens[1].replace("{SHA}", "").trim());
        }
    }
    
    @Override
    public void load() throws FileNotFoundException{
        super.load();
        try {
            while(this.hasMoreLines()){
                parseLine(super.nextLine());
            }
        } catch (IOException ex) {
            Logger.getLogger(Htpassword.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isAuthorized(String authInfo){       
        String credentials = new String(Base64.getDecoder().decode(authInfo), Charset.forName("UTF-8"));   
        String[] tokens = credentials.split(":");
        boolean isAuthorized = false;
        String username, password;
        if(tokens.length == 2){
            username = tokens[0];
            password = tokens[1];
            String encryptedPassword = encryptClearPassword(password);
            if(verifyPassword(username, encryptedPassword)){
                isAuthorized = true;
            }
        }
        
        return isAuthorized;
    }
    
    private boolean verifyPassword(String username, String password){
        boolean verified = false;
        if(users.containsKey(username)){
            String pwd = users.get(username);
            if(password.equals(pwd)){
                verified = true;
            }        
        }
        
       return verified;      
    }
    
    private String encryptClearPassword( String password ) {

        try {
          MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
          byte[] result = mDigest.digest( password.getBytes() );
         return Base64.getEncoder().encodeToString( result );
        } catch( NoSuchAlgorithmException e ) {
          return "";
        }
    }
    
    
    
}
