package http_response;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import server_conf.HttpdConf;

/**
 *
 * @author Alberto Fernandez Saucedo, Zac Henney
 */
public class Resource {
    private HttpdConf configuration;
    private String uri;
    private boolean isScript;
    private boolean isProtected = false;
    private String absolutePath;
    private String accessFilePath;
    private String mimeType;
    
    
    public Resource(String uri, HttpdConf config, String mimeType){
        this.configuration = config;
        this.uri = uri;
        this.mimeType = mimeType;
        this.verifyAccessFilePath();      
    }
    
    public String absolutePath(){
        String tempPath = "";
        String path = "";
        StringTokenizer tokens = new StringTokenizer(uri,"/");
        
        if (tokens.hasMoreTokens()){
            path +=   tokens.nextToken();
        }

        tempPath = configuration.getRoot() + path;
        path = tempPath;
        
        while(tokens.hasMoreTokens()){
            path += "/" + tokens.nextToken();
        }
        
        if(uri.endsWith("/")){
            if(isAlias(uri)){
                path = configuration.getAlias(uri);
                path += configuration.getDirectoryIndex();
            }
            else if(isScriptAlias(uri)){
                path = configuration.getScriptAlias(uri);
                isScript = true;
            }
            else{
               path += "/" + configuration.getDirectoryIndex(); 
            }
        }
        
        return absolutePath = path;   
    }
    
    public boolean isScript(){
        return isScript;
    }
    
    public boolean isProtected(){
        return isProtected;
    }
    
    private boolean isAlias(String uri){
        return configuration.getAlias(uri) != null;
    }
    
    private boolean isScriptAlias(String path){
        return configuration.getScriptAlias(path) != null;
    }
    
    public String getMimeType(){
        return mimeType;
    }
    
    public String toString(){
        return absolutePath;
    }
    
    public String getAccessFilePath(){
        return accessFilePath;
    }
    
    private void verifyAccessFilePath(){
        Path potentialAccessFilePath = Paths.get(this.absolutePath());
        String accessFileName = configuration.getAccessFileName();
        int count = potentialAccessFilePath.getNameCount();
        
        for(int i = count; i > 0; i--){
            if(potentialAccessFilePath.resolve(accessFileName).toFile().exists()){
                isProtected = true;
                accessFilePath = potentialAccessFilePath.toString() + "/" + accessFileName;
                break;
            }
            else{
                Path tempPath = potentialAccessFilePath.getParent();;
                potentialAccessFilePath = tempPath;        
            }
        }       
    }
}
