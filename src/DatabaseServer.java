
import java.net.*;
import java.util.*;
import java.io.*;
public class DatabaseServer{
   ServerSocket server;
   Socket you;
   InetAddress yourAddress;
   public DatabaseServer(){
      try{ Class.forName("com.mysql.jdbc.Driver");//���� JDBC_ODBC�Ž������� ��
      }
      catch(ClassNotFoundException e){
           System.out.println(e);
      }
      
      System.out.println("���Ƿ������˳���,�������û�����������"); 
   } 
   public void startServer(int port){
      while(true){ 
          try{
               server=new ServerSocket(port);//��ͨ �ö˿ڣ���server��Ϊ�÷�����ʹ�ã�
          }
          catch(IOException e1){ 
               System.out.println("���ڼ���:");
          } 
          try{ System.out.println("�ȴ��û�����.");
               you=server.accept();//���÷��������������û��ģ��ͻ��ˣ�������          
               yourAddress=you.getInetAddress();
               System.out.println("�ͻ���IP:"+yourAddress);
                     
          }
          catch (IOException e){}
          if(you!=null) {
              new ServerThread(you).start();//����ServerThread�Ķ���
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
