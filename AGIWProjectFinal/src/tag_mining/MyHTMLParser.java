package tag_mining;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MyHTMLParser {
	
	public static String parse(String html){
		Document doc = Jsoup.parse(html);

		if (doc.select("table")!=null)	{
			String row = doc.select("td").text();
			String[] rows = row.split("(\\. |\\? |\\... |\\! |\\n)");
			if (rows.length < 12)	{
				doc.select("table").remove();
			}
		}
		
		if (doc.select("title")!=null)	{			
			doc.select("title").remove();
		}	
		
		
		if (doc.select("script")!=null)	{			
			doc.select("script").remove();
		}	
		
		if (doc.select("head")!=null)	{			
			doc.select("head").remove();
		}	

		if (doc.select("ul")!=null)	{			
			doc.select("ul").remove();
		}			

		if (doc.select("ol")!=null)	{			
			doc.select("ol").remove();
		}				
		
		if (doc.select("div.header")!=null)	{			
			doc.select("div.header").remove();
		}			
		
		if (doc.getElementById("(\\d+)*header(\\d+)*")!=null)	{			
			doc.getElementById("(\\d+)*header(\\d+)*").remove();
		}
	
		if (doc.getElementsByClass("(\\d+)*meta(\\d+)*")!=null)	{	
			doc.getElementsByClass("(\\d+)*meta(\\d+)*").remove();
		}
		if (doc.getElementsByClass("(\\d+)*nav(\\d+)*")!=null)	{	
			doc.getElementsByClass("(\\d+)*nav(\\d+)*").remove();
		}
		if (doc.getElementsByClass("(\\d+)*header(\\d+)*")!=null)	{	
			doc.getElementsByClass("(\\d+)*header(\\d+)*").remove();
		}			
		if (doc.getElementsByClass("(\\d+)*footer(\\d+)*")!=null)	{	
			doc.getElementsByClass("(\\d+)*footer(\\d+)*").remove();
		}
		if (doc.getElementsByClass("(\\d+)*menu(\\d+)*")!=null)	{	
			doc.getElementsByClass("(\\d+)*menu(\\d+)*").remove();
		}
		if (doc.getElementsByClass("(\\d+)*legal(\\d+)*")!=null)	{	
			doc.getElementsByClass("(\\d+)*legal(\\d+)*").remove();
		}
		if (doc.getElementsByClass("(\\d+)*side-?bar(\\d+)*")!=null)	{	
			doc.getElementsByClass("(\\d+)*side-?bar(\\d+)*").remove();
		}			
		if (doc.getElementById("(\\d+)*nav(\\d+)*")!=null)	{	
			doc.getElementById("(\\d+)*nav(\\d+)*").remove();
		}
		
		if (doc.getElementById("(\\d+)*footer(\\d+)*")!=null)	{	
			doc.getElementById("(\\d+)*footer(\\d+)*").remove();
		}
		if (doc.getElementById("(\\d+)*side-?bar(\\d+)*")!=null)	{	
			doc.getElementById("(\\d+)*side-?bar(\\d+)*").remove();
		}
		if (doc.getElementById("(\\d+)*legal(\\d+)*")!=null)	{	
			doc.getElementById("(\\d+)*legal(\\d+)*").remove();
		}
		if (doc.getElementById("(\\d+)*menu(\\d+)*")!=null)	{	
			doc.getElementById("(\\d+)*menu(\\d+)*").remove();
		}				
		if (doc.select("img")!=null)	{	
			doc.select("img").remove();
		}
		if (doc.select("meta")!=null)	{	
			doc.select("meta").remove();
		}
		if (doc.select("link")!=null)	{	
			doc.select("link").remove();
		}			
		if (doc.select("noscript")!=null)	{	
			doc.select("noscript").remove();
		}
		if (doc.select("object")!=null)	{	
			doc.select("object").remove();
		}
		
		//doc.select("li").remove();		

		html = doc.select("body").html();
		String text = doc.select("body").text();
		//String body_clean2 = Jsoup.parse(body_clean).select("table").html();
		//String output1 = body_clean.replaceAll("</?table[^>]*>|</?tr[^>]*>|</?td[^>]*>|</?thead[^>]*>|</?tbody[^>]*>|</?font[^>]*>|</?img[^>]*>|</?!--[^>]*|</?form[^>]*|</?script[^>]*|</?input[^>]*|</?span[^>]*|</?iframe[^>]*|</?tfoot[^>]*>|</?.*meta[^>]*>|<ul[^>]*(.*\n)*</ul>", "");
		//String output2 = body_clean.replaceAll(".*<table[^>]*>([^<]*)</table>.*", "");
		
		return text;
	}
	
	public static String getTREC(String html){
		String TREC = html.substring(20, 25);
		return TREC;
	}
	
	
	public static String deleteTREC(String html){
		String deleted =html.substring(25, html.length());
		return deleted;
	}

	public static String getPageLanguage(String html) {
		Document doc = Jsoup.parse(html);
		
		String lang1 = doc.select("html").attr("lang");
		if (lang1 != null)
			return lang1;
		
		String lang2 = doc.select("meta").attr("lang");
		if (lang2 != null)
			return lang2;
		
		String lang3 = doc.select("meta").attr("language");
		if (lang3 != null)
			return lang3;
		
		return "";
	}

}

