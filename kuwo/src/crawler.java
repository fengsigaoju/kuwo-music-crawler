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

/*爬虫类先爬取某一个歌手的全部音乐,如果是第一页，则爬取类似歌手的,如果该歌手没有被访问过,则将其压入队列中,注意跳转到音乐栏目
 *爬取当前页面所有音乐,如果该音乐没有在set中,则将其和url放入map中。 
 *
 * 
 */
public class crawler {
  Set<String>set=new HashSet<String>();
  DB db=DB.GetSingle();
  public void  GetContent(String path) {//传入url返回content
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
		    connection.setConnectTimeout(5000);//设置连接超时
		    connection.setRequestMethod("GET");//设置获取方式
		    connection.setRequestProperty("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");//设置浏览器
			connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//设置获取语言
			if (connection.getResponseCode()==200){//状态返回码是200,代表成功
				is=connection.getInputStream();//获取输入流
				br=new BufferedReader(new InputStreamReader(is,"utf-8"));//转成字符流并添加缓冲区
				String line=null;
				while((line=br.readLine())!=null){
					content=content+line+"\r\n";
				}
				pw=new PrintWriter("F://1.txt");
				pw.write(content);
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
		   if (br!=null)
		   br.close();
		   if (pw!=null)
		   pw.close(); 
			} catch (IOException e) {
			    System.out.println("关闭输出流失败");
				e.printStackTrace();
			}
	}
	  String temp[]=content.split("<div class=\"name\">");
	  for (int i=2;i<=temp.length-1;i++){
		  String temp2[]=temp[i].split("target|</a>");
		  String new_url=temp2[0].replace("<a href=\"","").replace("\"","").trim();//获取歌曲的url
		  String song_name=temp2[1].replace("=\"_blank\">","").trim();//获取歌曲的
			 if (!set.contains(new_url)){
				 set.add(new_url);
				 String true_url=GetSong(new_url);//根据url获取正确的url
				 db.Update("insert into my_song(url,name)values(\""+true_url+"\",\""+song_name+"\")");//传入url和歌名,新增数据
			 }
			 }
}
  public String GetSong(String path){//根据url获取真实路径
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
		    connection.setConnectTimeout(5000);//设置连接超时
		    connection.setRequestMethod("GET");//设置获取方式
		    connection.setRequestProperty("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");//设置浏览器
			connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//设置获取语言
			if (connection.getResponseCode()==200){//状态返回码是200,代表成功
				is=connection.getInputStream();//获取输入流
				br=new BufferedReader(new InputStreamReader(is,"utf-8"));//转成字符流并添加缓冲区
				String line=null;
				while((line=br.readLine())!=null){
					content=content+line+"\r\n";
				}
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
		   if (br!=null)
		   br.close();
		   } catch (IOException e) {
			    System.out.println("关闭输出流失败");
				e.printStackTrace();
			}
	}
	  return  content;
  }
  public static void main(String[] args) {
	new crawler().GetContent("http://www.kuwo.cn/playlist/index?pid=1082656711");
	
}
}
