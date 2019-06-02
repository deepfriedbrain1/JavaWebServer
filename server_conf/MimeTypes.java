package server_conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alberto Fernandez Saucedo, Zac Henney
 */
public class MimeTypes extends ConfigurationReader{
    private HashMap<String, String> types;
    
    public MimeTypes(String fileName){
        super(fileName);
        types = new HashMap();
    }
    
    @Override
    public void load() throws FileNotFoundException{
        String nextLine;
        String delimiter = "\\s+";
        String[] tokens;
        String value;
        super.load();
        try {
            while(super.hasMoreLines()){                
                nextLine = super.nextLine();
                if(!nextLine.startsWith("#")){
                    tokens = nextLine.split(delimiter);
                    value = tokens[0];
                    if (tokens.length > 1){
                        for(int i = 1; i < tokens.length; i++){
                            types.put(tokens[i], value);
                        }
                    }    
                }                         
            }
        } catch (IOException ex) {
            Logger.getLogger(MimeTypes.class.getName()).log(Level.SEVERE, null, ex);           
        }
    }
    
    public String lookup(String extension){
        return types.get(extension);
    }
}
