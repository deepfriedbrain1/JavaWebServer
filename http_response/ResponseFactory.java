package http_response;

import http_request.Request;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import server_conf.Htaccess;


/**
 *
 * @author Alberto Fernandez Saucedo, Zac Henney
 */
public class ResponseFactory {
    
    private int responseCode;
    private Path resourcePath;
    private String requestVerb;
    //private Response response;
    private Request request;
    private Resource resource;
    
    final int OK = 200;
    final int CREATED = 201;
    final int NO_CONTENT = 204;
    final int NOT_MODIFIED = 304;
    final int BAD_REQUEST = 400;
    final int UNAUTHORIZED = 401;
    final int FORBIDDEN = 403;
    final int NOT_FOUND = 404;
    final int INTERNAL_SERVER_ERROR = 500;
    //create final ints for server codes
    
    public Response getResponse(Request request, Resource resource) throws IOException{
        this.request = request;
        this.resource = resource;
        requestVerb = request.getVerb();
        resourcePath = Paths.get(resource.absolutePath());
        
        if(resource.isProtected()){
            responseCode = verifyAccess(resource, request);
        }

        else if(!fileExists(resource.absolutePath()) && !requestVerb.equals("PUT")){
            responseCode = NOT_FOUND;
        }
        
        /*
        if(resource.isScript()){
            if(executeScript(resourcePath)){
                //response = new OkResponse(resource);
                //response.setVerb(requestVerb);
                responseCode = OK;
            }
            else
                return new InternalServerErrorResponse(resource);
        }
        */
        else{  
            responseCode = processRequestVerb(requestVerb);       
        }

        return createResponse(responseCode);        
    }
    
    private int processRequestVerb(String requestVerb) throws IOException{
        if(requestVerb.equals("PUT")){
            return put(resourcePath);
        }
        else if(requestVerb.equals("DELETE")){
            return delete(resourcePath);
        }
        else if(requestVerb.equals("POST")){
            return post(resourcePath);
        }
        else if(requestVerb.equals("GET")){
            return get(resourcePath, true);
        }
        else if(requestVerb.equals("HEAD")){
            return get(resourcePath, false);
        }
        return INTERNAL_SERVER_ERROR;
    }
    
    private Response createResponse(int responseCode) throws IOException{
        Response response = null;
        switch (responseCode) {
            case OK:
                
                response = new OkResponse(resource);
                response.setVerb(requestVerb);
                response.setBody(resourcePath);
                response.initializeHeaders();                     
                break;
                
            case CREATED:
                response = new CreatedResponse(resource);
                String createdLocation = request.getURI();
                response.setHeader("Location", createdLocation);
                break;
                
            case NO_CONTENT:  
                response = new NoContentResponse(resource);
                break;
             /*   
            case NOT_MODIFIED:
                response = new NotModifiedResponse(resource);
                break;
                */
            
            case BAD_REQUEST:
                response = new BadRequestResponse(resource);
                break;
            
            case UNAUTHORIZED:
                Htaccess htaccess = new Htaccess(resource.getAccessFilePath());
                response = new UnauthorizedResponse(resource);
                String headerKey = "WWW-Authenticate";
                //String headerValue = htaccess.getAuthType() + "realm=\"" + htaccess.getAuthName() + "\"";
                String headerValue = "Basic realm=\"I Challenge You\"";
                response.setHeader(headerKey, headerValue);
                break;
                        
            case FORBIDDEN:
                response = new ForbiddenResponse(resource);
                break;
            
            case NOT_FOUND:
                response = new NotFoundResponse(resource);
                break;
                
            case INTERNAL_SERVER_ERROR:
                response = new InternalServerErrorResponse(resource);           
                break;
            
            default:
                response = new InternalServerErrorResponse(resource);
                break;
        }
        
        return response;
        
    }
    
    private int put(Path file){
        try {
            byte[] bodyToBytes = request.getBody().getBytes();
            
            if(!Files.exists(file)){
                
                Files.createFile(file);
                Files.write(file, bodyToBytes);
            }
            else
                Files.write(file, bodyToBytes);
            
            return responseCode = CREATED;
        } catch (IOException e){
            return responseCode = NOT_FOUND;
        }
    }
    
    private int delete(Path file){
        try {
            Files.delete(file);
            return responseCode = NO_CONTENT;
        } catch (IOException ex) {
            return responseCode = NOT_FOUND;
        }
    }
    
    private int post(Path file) throws IOException{
        try {
            byte[] bodyToBytes = request.getBody().getBytes();
            Files.write(file, bodyToBytes);
            return responseCode = OK;
        } catch (Exception e){
            return responseCode = NOT_FOUND;
        }      
    }
    
    private int get(Path file, boolean returnBody){
        //if(request.getHeader("If-Modified-Since") != null){
           /* 
            if(isModified(file)){
                return responseCode = OK;
            }
            
            else{ 
                return responseCode = NOT_MODIFIED;
            }
        }
        */
        //else{
            return responseCode = OK;        
        //}
    }
    
 /*
    private boolean isModified(Path file){
        //get last modified date of file
        //get 
        
        LocalDateTime modifiedTime = Files.getLastModifiedTime(file).toMillis();
    }
*/

    private boolean fileExists(String path){
        return new File(path).exists();
    }
    
    private int verifyAccess(Resource resource, Request request) throws IOException{
        
        Htaccess htaccess = new Htaccess(resource.getAccessFilePath());
        htaccess.load();
        String credentials = request.getHeader("Authorization");
        String authInfo;
        
        if(credentials.equals("")){
            responseCode = UNAUTHORIZED;
        }
        else{
            authInfo = credentials.split("\\s")[2];
            if(!credentials.equals("") && htaccess.isAuthorized(authInfo)){
                responseCode = OK; 
            }
            else
            {
                responseCode = FORBIDDEN;
            }
        }
        return responseCode; 
    }
}
