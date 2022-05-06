
import java.util.logging.FileHandler;
import java.util.logging.Logger;

 
public class Log {
	
     
    public static void main(String[] args) throws Exception {  
       
    }
    
    public static void log(int count) throws Exception{
    	
    	
    	 
    
    	boolean append = true;
    	FileHandler handler = new FileHandler("unique_count.log", append);
    	 
    	Logger logger = Logger.getLogger("artiApi");
    	logger.addHandler(handler);
    	
    	         
    	logger.info("The application recieved " + count + " unique requests.");
    	 
    }
    	 
}
 
