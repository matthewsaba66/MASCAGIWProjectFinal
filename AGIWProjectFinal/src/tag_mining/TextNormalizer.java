package tag_mining;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Tab1;
import model.Tab2;
import model.Tab3;


public class TextNormalizer {

	private static List<Tab1> tab1List = new ArrayList<Tab1>();
	private static List<Tab2> tab2List = new ArrayList<Tab2>();
	private static List<Tab3> tab3List = new ArrayList<Tab3>();
	

	
	static void visualizeTables() throws IOException {
		writeToFile1(tab1List, "output/table1.tsv");
		writeToFile2(tab2List, "output/table2.tsv");
		writeToFile3(tab3List, "output/table3.tsv");
//		for (Tab1 t : tab1List){
//			System.out.println(t.toString());
//			//writeToFile(tab1List, "/home/matteo/Scrivania/output/table1");
//		}
//		for (Tab2 s : tab2List){
//			System.out.println(s.toString());
//		}
//		for (Tab3 s : tab3List){
//			System.out.println(s.toString());
//		}

	}
	
	
	
	
	private static void writeToFile1(List<Tab1> tab1List, String path) throws IOException {
		File file = new File(path);
		FileOutputStream fop = new FileOutputStream(file);
		if (!file.exists()) {
			file.createNewFile();
		}
		String str = "TREC-ID\tSTRINGA\tTAG\n";
		for (Tab1 t : tab1List ){
			str = str.concat(t.toFile() + "\n");
		}
		// get the content in bytes
		byte[] contentInBytes = str.getBytes();

		fop.write(contentInBytes);
		fop.flush();
		fop.close();
	}
	
	private static void writeToFile2(List<Tab2> tab1List, String path) throws IOException {
		File file = new File(path);
		FileOutputStream fop = new FileOutputStream(file);
		if (!file.exists()) {
			file.createNewFile();
		}
		String str = "TREC-ID\tFRASE-ESTRATTA-SENZA-TAG\n";
		for (Tab2 t : tab2List ){
			str = str.concat(t.toFile() + "\n");
		}
		// get the content in bytes
		byte[] contentInBytes = str.getBytes();

		fop.write(contentInBytes);
		fop.flush();
		fop.close();
	}
	
	private static void writeToFile3(List<Tab3> tab1List, String path) throws IOException {
		File file = new File(path);
		FileOutputStream fop = new FileOutputStream(file);
		if (!file.exists()) {
			file.createNewFile();
		}
		String str = "TREC-ID\tFRASE-ESTRATTA-CON-TAG\n";
		for (Tab3 t : tab3List ){
			str = str.concat(t.toFile() + "\n");
		}
		// get the content in bytes
		byte[] contentInBytes = str.getBytes();

		fop.write(contentInBytes);
		fop.flush();
		fop.close();
	}




	public static void getURLs(List<String> sentences, String TREC){
		String regex = "( www\\.([a-z]|[A-Z]|[0-9]|[/.]|[~])+[/.]([a-z]|[A-Z]|[0-9]|[/.]|[~])+)";
		String tag = "#URL";
		fillTables(sentences, TREC,regex, tag);
		String regex2 = "(?<http>(https?:[/][/])([a-z]|[A-Z]|[0-9]|[/.]|[~])*(www)?[\\.]([a-z]|[A-Z]|[0-9]|[/.]|[~])+[\\.]([a-z]|[A-Z]|[0-9]|[/.]|[~])+)";
		fillTables(sentences, TREC,regex2, tag);

	}
	
	public static void getMails(List<String> sentences, String TREC){
		String regex = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)*\\.\\w+";
		String tag = "#MAIL";
		fillTables(sentences, TREC,regex, tag);
	}
	
	
	
	public static void getDistances(List<String> sentences, String TREC){
		String regex = "(((km( |)|kilometer( |)| mi?(\\ |\\.|\\,|\\:|\\;|\\?|\\!)| ft( |)|yd( |))(\\d{1,3}(,|.| ))*\\d+"
					+ "(\\.\\d+)*)|((\\d{1,3}(,| ))* \\d+([\\.|\\,]\\d+)*(( |)km|( |)kilometers?|( |)mi?(\\ |\\.|\\,|\\:|\\;|\\?|\\!)|"
					+ "( |)ft|( |)yd|( |)metr(o|i)|( |)meters?)))";
		String tag = "#DIST";
		fillTables(sentences, TREC,regex, tag);
	}
	
	public static void getNumbers(List<String> sentences, String TREC){
		String regex = "([0-9])+ (//, ||//. |\\; |\\ )+";
		String tag = "#NUM";
		fillTables(sentences, TREC,regex, tag);
	}
	
	public static void getOrdinals(List<String> sentences, String TREC){
		String regex = "( |)[0-9]*(0th|1st|2nd|3rd|4th|5th|6th|7th|8th|9th)( |\\,|\\.|\\?|\\!|\\;|\\:)"; //( |\\,|\\.|\\?|\\!|\\;|\\:)
		String tag = "#ORD";
		fillTables(sentences, TREC,regex, tag);
	}
	
	public static void getDates(List<String> sentences, String TREC){
		String regex = "([0-9]*(0th|1st|2nd|3rd|4th|5th|6th|7th|8th|9th) ((j|J)an|(j|J)anuary|(F|f)eb|"
				+ "(F|f)ebruary|(M|m)ar|(M|m)arch|(a|A)pr|(a|A)pril|(M|m)ay|(j|J)une|(j|J)un|(j|J)uly|"
				+ "(j|J)ul|(a|A)ugust|(a|A)ug|(s|S)eptember|(s|S)ept|(o|O)ctober|(o|O)ct|(n|N)ovember|"
				+ "(n|N)ov|(d|D)ecember|(d|D)ec), ?[1-2][0-9][0-9][0-9]|[1-2][0-9][0-9][0-9]-[0-2][0-9]-"
				+ "[0-3][0-9]|([1-3][0-9]/([0][1-9]|[1][0-2])/[0-9]*)|[1-3][0-9] ((j|J)an|(j|J)anuary|"
				+ "(F|f)eb|(F|f)ebruary|(M|m)ar|(M|m)arch|(a|A)pr|(a|A)pril|(M|m)ay|(j|J)une|(j|J)un|"
				+ "(j|J)uly|(j|J)ul|(a|A)ugust|(a|A)ug|(s|S)eptember|(s|S)ept|(o|O)ctober|(o|O)ct|"
				+ "(n|N)ovember|(n|N)ov|(d|D)ecember|(d|D)ec) [0-9][0-9][0-9][0-9]|((j|J)an|(j|J)anuary|"
				+ "(F|f)eb|(F|f)ebruary|(M|m)ar|(M|m)arch|(a|A)pr|(a|A)pril|(M|m)ay|(j|J)une|(j|J)un|"
				+ "(j|J)uly|(j|J)ul|(a|A)ugust|(a|A)ug|(s|S)eptember|(s|S)ept|(o|O)ctober|(o|O)ct|"
				+ "(n|N)ovember|(n|N)ov|(d|D)ecember|(d|D)ec) (the )?[0-9]*(0th|1st|2nd|3rd|4th|5th|6th|7th"
				+ "|8th|9th),? [1-2][0-9][0-9][0-9])";
		String tag = "#DATE";
		fillTables(sentences, TREC,regex, tag);
	}
	public static void getMoney(List<String> sentences, String TREC){
		String regex = "(((\\$|\\€|\\£|\\¥)(\\d{1,3}[,| ])*\\d+(\\.\\d+)*)|((\\d{1,3}[,| ])*\\d+(\\.\\d+)*"
				+ "(\\$|\\€|\\£|\\¥|\\ pounds?|\\ euros?|\\ dollars?|\\ yen))|(((usd |euro |eur |btc |jpy |gbp |USD |EURO |EUR |BTC |JPY |GBP |bitcoin |BITCOIN )+(\\d+[\\ ])"
				+ "(million|mn|bn|billion)))|(\\d+[\\ ])(million|mn|bn|billion)( usd| euro| eur| btc| jpy| gbp| USD| EURO| EUR| BTC| JPY| GBP| BITCOIN| bitcoin))";
		String tag = "#MONEY";
		fillTables(sentences, TREC,regex, tag);
	}
	
	public static void getPercentages(List<String> sentences, String TREC){
		String regex = "\\d+((\\.|,)\\d*)?%(\\ ||\\.|\\,|\\!|\\?)";
		String tag = "#PERC";
		fillTables(sentences, TREC,regex, tag);
	}
	
	
	
	
	
	
	
	
	private static void fillTables(List<String> sentences, String TREC, String regex, String tag){
		for (String sentence_clean : sentences){
			String sentence_tag = sentence_clean;
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(sentence_tag); // get a matcher object
			while(m.find()) {
				Tab1 tab1 = new Tab1(TREC, m.group(),tag );
				tab1List.add(tab1);
			}
			sentence_tag = m.replaceAll(tag);
			if (sentence_tag.contains(tag)){
				Tab2 tab2 = new Tab2(TREC, sentence_clean);
				tab2List.add(tab2);
				Tab3 tab3 = new Tab3(TREC, sentence_tag);
				tab3List.add(tab3);
			}
		}
	}
}
