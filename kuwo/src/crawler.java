import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/*����������ȡĳһ�����ֵ�ȫ������,����ǵ�һҳ������ȡ���Ƹ��ֵ�,����ø���û�б����ʹ�,����ѹ�������,ע����ת��������Ŀ
 *��ȡ��ǰҳ����������,���������û����set��,�����url����map�С� 
 *
 * 
 */
public class crawler {
  Set<String>set=new HashSet<String>();
  DB db=DB.GetSingle();
  public void  GetContent(String path) {//����url����content
	  URL url=null;
	  HttpURLConnection connection=null;
	  InputStream is=null;
	  BufferedReader br=null;
	  String content="";
	  PrintWriter pw=null;
	  try {
		url=new URL(path);
		try {
			connection=(HttpURLConnection) url.openConnection();
		    connection.setConnectTimeout(5000);//�������ӳ�ʱ
		    connection.setRequestMethod("GET");//���û�ȡ��ʽ
		    connection.setRequestProperty("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");//���������
			connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//���û�ȡ����
			if (connection.getResponseCode()==200){//״̬��������200,����ɹ�
				is=connection.getInputStream();//��ȡ������
				br=new BufferedReader(new InputStreamReader(is,"utf-8"));//ת���ַ�������ӻ�����
				String line=null;
				while((line=br.readLine())!=null){
					content=content+line+"\r\n";
				}
				pw=new PrintWriter("F://1.txt");
				pw.write(content);
			}
		} catch (IOException e) {
			System.out.println("��ȡ����������,����");
			e.printStackTrace();
		}
	} catch (MalformedURLException e) {
	    System.out.println("url��ַ����,����");
		e.printStackTrace();
	}finally{
		try {
		   if (br!=null)
		   br.close();
		   if (pw!=null)
		   pw.close(); 
			} catch (IOException e) {
			    System.out.println("�ر������ʧ��");
				e.printStackTrace();
			}
	}
	  String temp[]=content.split("<div class=\"name\">");
	  for (int i=2;i<=temp.length-1;i++){
		  String temp2[]=temp[i].split("target|</a>");
		  String new_url=temp2[0].replace("<a href=\"","").replace("\"","").trim();//��ȡ������url
		  String song_name=temp2[1].replace("=\"_blank\">","").trim();//��ȡ������
			 if (!set.contains(new_url)){
				 set.add(new_url);
				 String true_url=GetSong(new_url);//����url��ȡ��ȷ��url
				 db.Update("insert into my_song(url,name)values(\""+true_url+"\",\""+song_name+"\")");//����url�͸���,��������
			 }
			 }
}
  public String GetSong(String path){//����url��ȡ��ʵ·��
	   int n=path.indexOf("?");
	  String temp=path.substring(26,n);
	  StringBuilder sb=new StringBuilder("http://antiserver.kuwo.cn/anti.s?rid=MUSIC_");
	  sb.append(temp);
	  sb.append("&type=convert_url&response=url&format=aac|mp3");
	  URL url=null;
	  HttpURLConnection connection=null;
	  InputStream is=null;
	  BufferedReader br=null;
	  String content="";
	 try {
		url=new URL(sb.toString());
		try {
			connection=(HttpURLConnection) url.openConnection();
		    connection.setConnectTimeout(5000);//�������ӳ�ʱ
		    connection.setRequestMethod("GET");//���û�ȡ��ʽ
		    connection.setRequestProperty("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");//���������
			connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//���û�ȡ����
			if (connection.getResponseCode()==200){//״̬��������200,����ɹ�
				is=connection.getInputStream();//��ȡ������
				br=new BufferedReader(new InputStreamReader(is,"utf-8"));//ת���ַ�������ӻ�����
				String line=null;
				while((line=br.readLine())!=null){
					content=content+line+"\r\n";
				}
			}
		} catch (IOException e) {
			System.out.println("��ȡ����������,����");
			e.printStackTrace();
		}
	} catch (MalformedURLException e) {
	    System.out.println("url��ַ����,����");
		e.printStackTrace();
	}finally{
		try {
		   if (br!=null)
		   br.close();
		   } catch (IOException e) {
			    System.out.println("�ر������ʧ��");
				e.printStackTrace();
			}
	}
	  return  content;
  }
  public static void main(String[] args) {
	new crawler().GetContent("http://www.kuwo.cn/playlist/index?pid=1082656711");
	
}
}
