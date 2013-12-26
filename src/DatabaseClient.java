import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class DatabaseClient extends JFrame implements Runnable,ActionListener
{
	
	String formName;      //���ݿ��еı���
	    JTextField �����ѯ����,����;                             
	    Choice choice;                                 
	    Checkbox  ��ȫһ��,ǰ��һ��,��һ��,�м����;      
	    CheckboxGroup group=null;
	    JButton ��ѯ;
	    JTextArea ��ʾ��ѯ���;//��ʾ ��ѯ�����
	    JLabel ��ʾ��;
	    Socket socket=null;
	    DataInputStream in=null;
	    DataOutputStream out=null;
	    Thread thread;
	    boolean ok=false;
	    int N=0;                //�ֶθ��� 
	    String []ziduanName;    //����ֶ����ֵ�����
	    String []ziduanExplain; //����ֶν��͵�����
	public DatabaseClient ()
	{
	
	    super("��׼��ѯϵͳ(�ͻ���)");
		thread=new Thread(this);
        �����ѯ����=new JTextField(10);
        ��ѯ=new JButton("��ѯ");
        ��ʾ��=new JLabel("�������ӵ�������,���Ե�...",JLabel.CENTER);
        choice=new Choice();
 
        File f=new File("1.txt");
		   try {
				FileReader fr = new FileReader(f);//�Ӷ��ж�ȡ���ݵ�    f   �ļ�������´���һ���� FileReader
				BufferedReader bf = new BufferedReader(fr);
				String str = bf.readLine();
				
				N = Integer.parseInt(str);//���ֶθ���
				
				formName= bf.readLine();//������
				ziduanName= new String[N];
				ziduanExplain= new String[N];
				String s1="";
				for( int i=0;(s1=bf.readLine())!=null;i++) 
				{
					String s = s1;
					//���ֶ����Լ��ֶν���
					ziduanName[i] = s.substring(0, s.indexOf(":")).trim();//ȡ ���ݿ��е��ֶ�������ISBN��price��date��
					ziduanExplain[i] = s.substring(s.indexOf(":") + 1).trim();//ȡ �������ڣ�������
				}
				 for(int k=0;k<N;k++)
			      {
			        choice.add(ziduanExplain[k]);//��� ѡ�� ��ISBN�����ߣ��۸񡣡�������
			      }
				bf.close();//�ر�������
				fr.close(); 
			} 
		   catch (Exception e) 
			{
				e.printStackTrace();
			}
      choice.select(0);//Ĭ����ѡ�� ISBN  ��Ҳ�� ��һ�� ��
      
      
      group=new CheckboxGroup();
      ��ȫһ��=new Checkbox("��ȫһ��",true,group);
      ǰ��һ��=new Checkbox("ǰ��һ��",false,group);
      ��һ��=new Checkbox("��һ��",false,group);
      �м����=new Checkbox("�м����",false,group);
      ��ʾ��ѯ���=new JTextArea(8,43);
      ��ʾ��.setForeground(Color.red);
      ��ʾ��.setFont(new Font("TimesRoman",Font.BOLD,24));
      
      Panel box1=new Panel();  //��
      box1.add(new Label("�����ѯ����:",Label.CENTER));
      box1.add(�����ѯ����); 
      box1.add(choice); 
      box1.add(��ѯ);
      
      Panel box2=new Panel();   //��              
      box2.add(new Label("ѡ���ѯ����:",Label.CENTER));
      box2.add(��ȫһ��);
      box2.add(ǰ��һ��);
      box2.add(��һ��);
      box2.add(�м����);
      
      Panel box3=new Panel();  //�� ��          
      box3.add(new Label("��ѯ���:",Label.CENTER));
      box3.add(��ʾ��ѯ���);
     
      Panel box=new Panel(); //����Ĭ�ϲ��ֹ��������� FlowLayout �ࡣ
      box.add(box1,BorderLayout.NORTH);
      box.add(box2,BorderLayout.CENTER);
      box.add(box3,BorderLayout.SOUTH);
      add(��ʾ��,BorderLayout.NORTH);
      add(box,BorderLayout.CENTER); 
      setBounds(100,100,600,350);
      ��ѯ.addActionListener(this);
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
      //socket��ð�׵�ip��ַ�Ͷ˿�
      try
      { socket=new Socket("127.0.0.1", 6666);
           in=new DataInputStream(socket.getInputStream());
           out=new DataOutputStream(socket.getOutputStream());
      }
      catch (IOException ee)
      {
           ��ʾ��.setText("����ʧ��");
      }
      if(socket!=null)
      {
           InetAddress address=socket.getInetAddress();
           ��ʾ��.setText("����:"+address+"�ɹ�");
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
            ��ʾ��.setText("��������ѶϿ�");
            break;
        }
        ��ʾ��ѯ���.append(s);//��ʾ�����
        if(ok==false)
          break;
     }
  }

   
   
   
  public void actionPerformed(ActionEvent e)
  {
     if(e.getSource()==��ѯ)
     {
        ��ʾ��ѯ���.setText(null);//�����  ��ʾ��ѯ����� JTextArea��
        String ��ѯ����="";
        ��ѯ����=�����ѯ����.getText();// ȡJTextField������ ��Ҳ�����û���ѯ�����ݣ� 
        int index=choice.getSelectedIndex();
        String �ֶ�=ziduanName[index];       
        String ��ѯ��ʽ=group.getSelectedCheckbox().getLabel();
        if(��ѯ����.length()>0)
        {
          try
          {  out.writeUTF("�ֶθ���:"+N);
                out.writeUTF(formName+":"+��ѯ����+":"+�ֶ�+":"+��ѯ��ʽ);
              }
          catch(IOException e1)
             {
                ��ʾ��.setText("��������ѶϿ�");
             } 
        } 
        else
          �����ѯ����.setText("����������"); 
     }
  }
	

  


	public static void main(String []args)
	{
		
		new DatabaseClient();
	}
}



