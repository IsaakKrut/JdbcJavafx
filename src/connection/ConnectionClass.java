/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Isaak
 */
public class ConnectionClass {
    
    public Connection getConnection(){
        Connection con = null;
        try{
                 
         Class.forName("oracle.jdbc.driver.OracleDriver");  

		con=DriverManager.getConnection(  
"jdbc:oracle:thin:@localhost:1521:xe","system","1234"); 
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return con;
    }

}
