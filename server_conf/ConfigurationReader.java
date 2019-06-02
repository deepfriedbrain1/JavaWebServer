package server_conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Alberto Fernandez Saucedo, Zac Henney
 */
public class ConfigurationReader {
    private File file;
    private Scanner scanner;
       
    public ConfigurationReader(String fileName){
        file = new File(fileName);
    }
    
    public boolean hasMoreLines() throws IOException{
        return scanner.hasNextLine();      
    }
    
    public String nextLine() throws IOException{
        String nextString;
        nextString = scanner.nextLine();       
        return nextString;
    }
    
    public void load() throws FileNotFoundException{
        scanner = new Scanner(file);       
    }   
}
