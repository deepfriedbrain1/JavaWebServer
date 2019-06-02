package server_conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Alberto Fernandez Saucedo, Zac Henney
 */
public class HttpdConf extends ConfigurationReader{
    private HashMap<String, String> aliases;
    private HashMap<String, String> scriptAliases;
    private HashMap<String, String> options;

    
    public HttpdConf(String fileName){
        super(fileName);
        
        aliases = new HashMap();
        scriptAliases = new HashMap();
        options = new HashMap();
    }
    
    @Override
    public void load() throws FileNotFoundException{
        String nextLine;
        String delimiter = "\\s+";
        String[] tokens;
        String value;
        String key;
        super.load();
        try {
            while(super.hasMoreLines()){                
                nextLine = super.nextLine();
                if(nextLine.startsWith("Alias")){
                    tokens = nextLine.split(delimiter);
                    key = tokens[1];
                    value = tokens[2];
                    value = value.replaceAll("^\"|\"$", "");
                    aliases.put(key, value); 
                    
                }  
                else if(nextLine.startsWith("ScriptAlias")){                   
                    tokens = nextLine.split(delimiter);
                    key = tokens[1];
                    value = tokens[2];
                    value = value.replaceAll("^\"|\"$", "");
                    scriptAliases.put(key, value); 
                }
                else{
                   tokens = nextLine.split(delimiter);
                   key = tokens[0];
                   value = tokens[1];
                   value = value.replaceAll("^\"|\"$", "");
                   options.put(key, value);
                }
            }
            if(options.get("AccessFile") == null){
                options.put("AccessFile", ".htaccess");
            }
            if(options.get("DirectoryIndex") == null){
                options.put("DirectoryIndex", "index.html");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MimeTypes.class.getName()).log(Level.SEVERE, null, ex);           
        }
    } 
    
    public String getAlias(String path){
        return aliases.get(path);
    }
    
    public String getScriptAlias(String path){
        return scriptAliases.get(path);
    }
    
    public String getRoot(){
        return options.get("DocumentRoot");
    }
    public String getAccessFileName(){
        return options.get("AccessFile");
    }
    public String getDirectoryIndex(){
        return options.get("DirectoryIndex");
    }
    public String getLogFilePath(){
        return options.get("LogFile");
    }
}
