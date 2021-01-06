package connection;

import java.sql.Connection;

public class DbConnection {

	private static DbConnection single_instance = null;
	
	public static DbConnection getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection();
 
        return single_instance;
    }
	
	public  Connection getConnection(){
    	
   	 Connection conn=null;
   	try {
   			conn = C3P0DataSource.getInstance()
               .getConnection() ;
   	       
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }
	
}
