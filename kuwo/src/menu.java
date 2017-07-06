import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class menu extends JFrame implements ActionListener{
	private JMenuBar menubar;
	private JMenu menu,menu2,menu3;
	JMenuItem menuitem,menuitem2,menuitem3,menuitem4,menuitem5,menuitem6,menuitem7,menuitem8;
	private static Set<String>set=new HashSet<String>();
	final JList<String>jlist;
	private static Vector<String>vector=new Vector<String>();
	private static DB db=DB.GetSingle();
	public menu(){
		init();
		setTitle("音乐播放器");
		setBounds(700,200,500,600);
		setLayout(null);
		Container container=getContentPane();
		menubar=new JMenuBar();
		setJMenuBar(menubar);//添加菜单栏对象  
		menu=new JMenu("开始");
		menubar.add(menu);
		menuitem=new JMenuItem("播放/暂停");
		menuitem.addActionListener(this);//添加监听事件
		menu.add(menuitem);
		menuitem2=new JMenuItem("上一首");
		menuitem2.addActionListener(this);//添加监听事件  
		menu.add(menuitem2);
		menuitem3=new JMenuItem("下一首");
		menuitem3.addActionListener(this);//添加监听事件  
		menu.add(menuitem3);
		menu2=new JMenu("模式");
		menubar.add(menu2);
		menuitem4=new JMenuItem("循环模式");
		menuitem4.addActionListener(this);//添加监听事件  
		menu2.add(menuitem4);
		menuitem5=new JMenuItem("列表模式");
		menuitem5.addActionListener(this);//添加监听事件  
		menu2.add(menuitem5);
		menuitem6=new JMenuItem("随机模式");
		menuitem6.addActionListener(this);//添加监听事件
		menu2.add(menuitem6);
		menu3=new JMenu("检索");
		menubar.add(menu3);
		menuitem7=new JMenuItem("搜索");
	    menuitem7.addActionListener(this);//添加监听事件  
		menu3.add(menuitem7);
		menuitem8=new JMenuItem("退出");
		menuitem8.addActionListener(this);
		menu3.add(menuitem8);
		JScrollPane scrollpane=new JScrollPane();
		scrollpane.setBounds(45,50,400,450);
		jlist = new JList<String>(vector);
		jlist.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
            if (!jlist.getValueIsAdjusting()) //设置只有释放鼠标时才触发  	
            { 
              System.out.println(jlist.getSelectedValue());
              play(jlist.getSelectedValue());
            }
             }
        });
		scrollpane.setViewportView(jlist);  
		container.add(scrollpane);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==menuitem){
			System.out.println("1");
			
		}
		if (e.getSource()==menuitem2){
			System.out.println("2");
		}
		if (e.getSource()==menuitem3){
			System.out.println("3");
		}
		if (e.getSource()==menuitem4){
			System.out.println("4");
		}
		if (e.getSource()==menuitem5){
			System.out.println("5");
		}
		if (e.getSource()==menuitem6){
			System.out.println("6");
		}
		if (e.getSource()==menuitem7){
			System.out.println("7");
		    String temp=(String)JOptionPane.showInputDialog(null,"请输入歌曲名\n","搜索",JOptionPane.PLAIN_MESSAGE,null,null,"在这输入");
		    if (vector.contains(temp)){
		    	vector.remove(temp);
		    	vector.add(0,temp);
		    	jlist.setSelectedIndex(0);
		    }
		}
		if (e.getSource()==menuitem8){
			System.out.println("8");
			System.exit(1);
		}
		
	}
	public void play(String name){
		   ResultSet rs=db.Query("select url from my_song where name=\""+name+"\";");
		   String path=null;
		   BufferedInputStream buffer=null;
		   BufferedOutputStream fps=null;
		   try {
			 rs.next();
			 path=rs.getString("url");
		} catch (SQLException e1) {
			System.out.println("获取url路径出错");
			e1.printStackTrace();
		}
		   System.out.println(path);
		 URL url=null;
		   HttpURLConnection connection=null;
		   InputStream is=null;
		   try {
				url=new URL(path);
				try {
					connection=(HttpURLConnection) url.openConnection();
				    connection.setConnectTimeout(5000);//设置连接超时
				    connection.setRequestMethod("GET");//设置获取方式
				    connection.setRequestProperty("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");//设置浏览器
					connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//设置获取语言
					if (connection.getResponseCode()==200){//状态返回码是200,代表成功
						is=connection.getInputStream();//获取输入流
						fps=new BufferedOutputStream(new FileOutputStream("F:/third.mp3"));
						byte[] b=new byte[512];
						int len=-1;
						while((len=is.read(b))!=-1)
						fps.write(b,0,len);
						buffer = new BufferedInputStream(new FileInputStream("F:/third.mp3"));
						String file=path;
				        Runtime.getRuntime().exec("cmd /c start "   +   file.replaceAll(" ", "\" \""));
					}
			} catch (IOException e) {
					System.out.println("获取输入流有误,请检查");
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
			    System.out.println("url地址有误,请检查");
				e.printStackTrace();
			}finally{
				try {
						if (fps!=null)
						fps.close();
						if (buffer!=null)
							buffer.close();
					} catch (IOException e) {
						System.out.println("关闭输出流失败");
						e.printStackTrace();
					}
		}
		}
	public static void init(){//列表存储的是歌名
		ResultSet rs=db.Query("select name from my_song;");
		try {
			while(rs.next()){
				set.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			System.out.println("遍历列表失败");
			e.printStackTrace();
		}
		vector.addAll(set);//添加set
	}
	public static void main(String[] args) {
		   try {  
	            UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");  
	            new menu();  
	            //player.play();
	         }  
	        catch (Exception ex) {  
	            ex.printStackTrace();  
	        }  
	    } 
}


