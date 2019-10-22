/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import model.DisplayScreen;
import model.DBConnection;
import dao.ConnectionDAO;
/**
 *
 * @author The Bright
 */
public class ConfigController {
    private DisplayScreen screen;
    private ConnectionDAO dao;
    private DBConnection con=new DBConnection();
    
    public ConfigController(){
        dao=new ConnectionDAO();
    }
    
    public void setDisplay(DisplayScreen screen){
        this.screen=screen;
        dao.setDisplay(screen);
    }
    public boolean connect(String username,String pass, String port, String host,String dbName){
         boolean ans=false;
         con.setUsername(username);
         con.setPassword(pass);
         con.setMysqlPort(port);
         con.setHostName(host);
         con.setDatabase(dbName);
         if(dao.loadDriver()){
             if(dao.connect(con)){
                 screen.updateStatus("Connection successfull");
                 ans=true;
             }
              
         }
          
         return ans;
         
    }
     public void closeAllResources(){
        dao.closeConnections();
        
    }
     public void clearQueue(){
         dao.removeLock();
     }
    
}
