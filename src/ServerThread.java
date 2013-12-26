import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
public class ServerThread extends Thread{
    InetAddress yourAddress;
    Socket socket=null;
    DataOutputStream out=null;
    DataInputStream  in=null;
    Connection con=null;
    Statement  stmt=null;//封装SQL语句 ；
    ResultSet  rs;
    int number;
    ServerThread(Socket t){
        socket=t;
        try{  con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root");
              stmt=con.createStatement();
        }
        catch(Exception e){
              e.printStackTrace();
        }
        try{ in=new DataInputStream(socket.getInputStream());//获取输入流；
             
            //应用程序可以使用数据输出流写入稍后由数据输入流读取的数据。 
             out=new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e){}
    }  
    public void run(){ //要重写run方法；覆盖父类run方法
        String s="";
        int N=0;       
        while(true){
           try{
                s=in.readUTF();
                System.out.println(s);
                if(s.startsWith("字段个数:")){
                   String number=s.substring(s.lastIndexOf(":")+1);//返回指定子字符串在此字符串中最右边出现处的索引。
                   N=Integer.parseInt(number);
                }
                else{
                   String sqlCondition=null;
                   String 表名="",查询内容="",字段="",查询方式="";
                   StringTokenizer fenxi=new StringTokenizer(s,":");
                   if(fenxi.hasMoreTokens())
                      表名=fenxi.nextToken(); 
                   if(fenxi.hasMoreTokens())
                      查询内容=fenxi.nextToken(); 
                   if(fenxi.hasMoreTokens())
                      字段=fenxi.nextToken();
                   if(fenxi.hasMoreTokens())
                      查询方式=fenxi.nextToken(); 
                   if(查询方式.equals("完全一致")){
                     sqlCondition=
                    "SELECT * FROM "+表名+" WHERE "+字段+" LIKE "+"'"+查询内容+"' ";
                   }
                   else if(查询方式.equals("前方一致")){
                     sqlCondition=
                    "SELECT * FROM "+表名+" WHERE "+字段+" LIKE "+"'"+查询内容+"%' ";
                   } 
                   else if(查询方式.equals("后方一致")){
                     sqlCondition=
                    "SELECT * FROM "+表名+ " WHERE "+字段+" LIKE "+"'%"+查询内容+"' ";
                   } 
                   else if(查询方式.equals("中间包含")){
                     sqlCondition=
                    "SELECT * FROM "+表名+" WHERE "+字段+" LIKE "+"'%"+查询内容+"%' ";
                   }
                   try{  rs=stmt.executeQuery(sqlCondition);//获取结果集合；
                         number=0;
                         while(rs.next()){//使用next方法:多条记录吗？
                          number++;
                          StringBuffer buff=new StringBuffer();
                          for(int k=1;k<=N;k++){
                             buff.append(rs.getString(k)+"  ");
                          }
                          out.writeUTF("\n"+new String(buff));
                        }
                        if(number==0)
                          out.writeUTF("\n没有查询到任何记录\n");
                   }
                   catch(SQLException ee) {
                        out.writeUTF(""+ee);
                   }
                }
           }
           catch (IOException e){ 
               try{ socket.close();
                    con.close();
               }                    
               catch(Exception eee){}
               System.out.println("客户离开了");
               break;                                  
           }
       
       }
   }
}

