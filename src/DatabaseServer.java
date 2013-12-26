
import java.net.*;
import java.util.*;
import java.io.*;
public class DatabaseServer{
   ServerSocket server;
   Socket you;
   InetAddress yourAddress;
   public DatabaseServer(){
      try{ Class.forName("com.mysql.jdbc.Driver");//加载 JDBC_ODBC桥接器驱动 ；
      }
      catch(ClassNotFoundException e){
           System.out.println(e);
      }
      
      System.out.println("我是服务器端程序,负责处理用户的连接请求"); 
   } 
   public void startServer(int port){
      while(true){ 
          try{
               server=new ServerSocket(port);//开通 该端口，到server，为该服务器使用；
          }
          catch(IOException e1){ 
               System.out.println("正在监听:");
          } 
          try{ System.out.println("等待用户呼叫.");
               you=server.accept();//调用方法，接受来自用户的（客户端）的请求；          
               yourAddress=you.getInetAddress();
               System.out.println("客户的IP:"+yourAddress);
                     
          }
          catch (IOException e){}
          if(you!=null) {
              new ServerThread(you).start();//调用ServerThread的对象；
          }  
          else 
              continue;
     }
   }
   public static void main(String args[]){
       DatabaseServer server=new DatabaseServer();
       server.startServer(6666);  
   }
}
