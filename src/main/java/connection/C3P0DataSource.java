package connection;

import java.beans.PropertyVetoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;



public class C3P0DataSource {
   private static C3P0DataSource dataSource;
   private ComboPooledDataSource comboPooledDataSource;

   private C3P0DataSource() {
     /* try {
         
      	  comboPooledDataSource = new ComboPooledDataSource();
         comboPooledDataSource
            .setDriverClass("com.mysql.jdbc.Driver");
       comboPooledDataSource
           .setJdbcUrl("jdbc:mysql://localhost:3306/society_parking_system");
      
         comboPooledDataSource.setUser("root");
         comboPooledDataSource.setPassword("");
         /*comboPooledDataSource
         .setJdbcUrl("jdbc:mysql://aa1jvbsmp8xehn4.cx3ixx7rbf8k.ap-south-1.rds.amazonaws.com:3306/society_parking_system");
      comboPooledDataSource.setUser("murari");
      comboPooledDataSource.setPassword("jaihanuman");*/
         
        
         
   /*   comboPooledDataSource.setMinPoolSize(2);
      comboPooledDataSource.setAcquireIncrement(5);
      comboPooledDataSource.setMaxPoolSize(60);
      comboPooledDataSource.setMaxStatements(150);
      comboPooledDataSource.setIdleConnectionTestPeriod(300);        
      comboPooledDataSource.setTestConnectionOnCheckout(true);
      comboPooledDataSource.setPreferredTestQuery("SELECT 1");     
        /* comboPooledDataSource.setMinPoolSize(2);
         comboPooledDataSource.setAcquireIncrement(5);
         comboPooledDataSource.setMaxPoolSize(20);
         comboPooledDataSource.setMaxStatements(50);
         comboPooledDataSource.setIdleConnectionTestPeriod(300);        
         comboPooledDataSource.setTestConnectionOnCheckout(true);
         comboPooledDataSource.setPreferredTestQuery("SELECT 1");   */    
               
         
     /* }
      catch (PropertyVetoException ex1) {
         ex1.printStackTrace();
      }*/
   }

   public static C3P0DataSource getInstance() {
      if (dataSource == null)
         dataSource = new C3P0DataSource();
      return dataSource;
   }

   public Connection getConnection() {
      Connection con = null;
      /*try {
         con = comboPooledDataSource.getConnection();
         System.out.println("connection count=>"+comboPooledDataSource.getNumConnections());
      } catch (SQLException e) {
         e.printStackTrace();
      }*/
      try {
       // Class.forName("com.mysql.jdbc.Driver");
          Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/camel_demo?autoReconnect=true", "root", "");
 //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatbot", "root", "root@#123");      
      } catch (Exception ex) {
          System.out.println("Exception in DBConnection java file of fun" + ex);
      }
      return con;
   }
}
