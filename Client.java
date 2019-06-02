
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Alberto Fernandez Saucedo
 */
public class Client {
    
    Client(String host, int port) throws IOException{
        
        Socket socket = new Socket(host, port);
        
        // Outgoing communication to server
        OutputStream ostream = socket.getOutputStream();
        DataOutputStream do_stream = new DataOutputStream(ostream);
        do_stream.writeUTF("POST /cgi-bin/process.cgi HTTP/1.1\r\n" +
                "User-Agent: Mozilla/4.0\r\n" +
                "Host: www.tutorialspoint.com\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: length\r\n" +
                "Accept-Language: en-us\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: Keep-Alive\r\n" +
                "\r\n" +
                "licenseID=string&content=string&/paramsXML=string\r\n" +
                "body body body bodybodybodybodybodybodybody\r\n"+
                "body body body bodybodybodybodybodybody body\r\n");
        
        
        // Incoming communication from server
        InputStream istream = socket.getInputStream();
        DataInputStream di_stream = new DataInputStream(istream);
        System.out.println("Server responsed: " + di_stream.readUTF());
        
    }
    
    public static void main(String[] args) throws IOException {
      
            new Client("127.0.0.1", 8080);
        
       
    }
}
