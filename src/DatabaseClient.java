import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class DatabaseClient extends JFrame implements Runnable,ActionListener
{
	
	String formName;      //数据库中的表名
	    JTextField 输入查询内容,表名;                             
	    Choice choice;                                 
	    Checkbox  完全一致,前方一致,后方一致,中间包含;      
	    CheckboxGroup group=null;
	    JButton 查询;
	    JTextArea 显示查询结果;//显示 查询结果；
	    JLabel 提示条;
	    Socket socket=null;
	    DataInputStream in=null;
	    DataOutputStream out=null;
	    Thread thread;
	    boolean ok=false;
	    int N=0;                //字段个数 
	    String []ziduanName;    //存放字段名字的数组
	    String []ziduanExplain; //存放字段解释的数组
	public DatabaseClient ()
	{
	
	    super("标准查询系统(客户端)");
		thread=new Thread(this);
        输入查询内容=new JTextField(10);
        查询=new JButton("查询");
        提示条=new JLabel("正在连接到服务器,请稍等...",JLabel.CENTER);
        choice=new Choice();
 
        File f=new File("1.txt");
		   try {
				FileReader fr = new FileReader(f);//从定中读取数据的    f   文件的情况下创建一个新 FileReader
				BufferedReader bf = new BufferedReader(fr);
				String str = bf.readLine();
				
				N = Integer.parseInt(str);//读字段个数
				
				formName= bf.readLine();//读表名
				ziduanName= new String[N];
				ziduanExplain= new String[N];
				String s1="";
				for( int i=0;(s1=bf.readLine())!=null;i++) 
				{
					String s = s1;
					//读字段名以及字段解释
					ziduanName[i] = s.substring(0, s.indexOf(":")).trim();//取 数据库中的字段名；（ISBN，price，date）
					ziduanExplain[i] = s.substring(s.indexOf(":") + 1).trim();//取 出版日期，出版社
				}
				 for(int k=0;k<N;k++)
			      {
			        choice.add(ziduanExplain[k]);//添加 选项 ：ISBN，作者，价格。。。。。
			      }
				bf.close();//关闭输入流
				fr.close(); 
			} 
		   catch (Exception e) 
			{
				e.printStackTrace();
			}
      choice.select(0);//默认是选择 ISBN  ：也就 第一项 ；
      
      
      group=new CheckboxGroup();
      完全一致=new Checkbox("完全一致",true,group);
      前方一致=new Checkbox("前方一致",false,group);
      后方一致=new Checkbox("后方一致",false,group);
      中间包含=new Checkbox("中间包含",false,group);
      显示查询结果=new JTextArea(8,43);
      提示条.setForeground(Color.red);
      提示条.setFont(new Font("TimesRoman",Font.BOLD,24));
      
      Panel box1=new Panel();  //上
      box1.add(new Label("输入查询内容:",Label.CENTER));
      box1.add(输入查询内容); 
      box1.add(choice); 
      box1.add(查询);
      
      Panel box2=new Panel();   //中              
      box2.add(new Label("选择查询条件:",Label.CENTER));
      box2.add(完全一致);
      box2.add(前方一致);
      box2.add(后方一致);
      box2.add(中间包含);
      
      Panel box3=new Panel();  //下 ；          
      box3.add(new Label("查询结果:",Label.CENTER));
      box3.add(显示查询结果);
     
      Panel box=new Panel(); //面板的默认布局管理器都是 FlowLayout 类。
      box.add(box1,BorderLayout.NORTH);
      box.add(box2,BorderLayout.CENTER);
      box.add(box3,BorderLayout.SOUTH);
      add(提示条,BorderLayout.NORTH);
      add(box,BorderLayout.CENTER); 
      setBounds(100,100,600,350);
      查询.addActionListener(this);
      setBackground(Color.cyan);
      this.setVisible(true);
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
     setResizable(false);
     start();
   }
	
       
       
       
   public void start()
   {
      ok=true;
      if(socket!=null&&in!=null&&out!=null)
      {
         try
         { socket.close();
              in.close(); 
              out.close();
         }
          catch(Exception ee){}
      }
      //socket获得班底的ip地址和端口
      try
      { socket=new Socket("127.0.0.1", 6666);
           in=new DataInputStream(socket.getInputStream());
           out=new DataOutputStream(socket.getOutputStream());
      }
      catch (IOException ee)
      {
           提示条.setText("连接失败");
      }
      if(socket!=null)
      {
           InetAddress address=socket.getInetAddress();
           提示条.setText("连接:"+address+"成功");
      } 
      if(!(thread.isAlive()))
      {
           thread=new Thread(this);
           thread.start();
      }
   }
   
   
   

   
   
   
   public void run()
   {
      String s=null;
      while(true)
      {
        try
        { s=in.readUTF();
        }
        catch (IOException e)
        {
            提示条.setText("与服务器已断开");
            break;
        }
        显示查询结果.append(s);//显示结果；
        if(ok==false)
          break;
     }
  }

   
   
   
  public void actionPerformed(ActionEvent e)
  {
     if(e.getSource()==查询)
     {
        显示查询结果.setText(null);//先清空  显示查询结果的 JTextArea域；
        String 查询内容="";
        查询内容=输入查询内容.getText();// 取JTextField的内容 ，也就是用户查询的内容； 
        int index=choice.getSelectedIndex();
        String 字段=ziduanName[index];       
        String 查询方式=group.getSelectedCheckbox().getLabel();
        if(查询内容.length()>0)
        {
          try
          {  out.writeUTF("字段个数:"+N);
                out.writeUTF(formName+":"+查询内容+":"+字段+":"+查询方式);
              }
          catch(IOException e1)
             {
                提示条.setText("与服务器已断开");
             } 
        } 
        else
          输入查询内容.setText("请输入内容"); 
     }
  }
	

  


	public static void main(String []args)
	{
		
		new DatabaseClient();
	}
}



