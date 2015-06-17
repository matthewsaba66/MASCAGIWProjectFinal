package tag_mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClass {




	public static void main(String[] args) throws FileNotFoundException, IOException {
		final File folder = new File("index/");
		List<String> names = listFilesForFolder(folder);
		int y = 0;
		for(String file : names){
			analyzeFile("index/"+file);
			System.out.println("analizzo file: " + file);
			System.out.println("numero: " + y);
			y++;

		}

		//	analyzeFile("/home/matteo/index200/130-extracted");
		TextNormalizer.visualizeTables();
	}



	private static void analyzeFile(String file) throws FileNotFoundException, IOException{
		//reads and opens a single file
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}

			String everything = sb.toString();
			//System.out.println(everything);

			//find TREC-ID
			String TREC = MyHTMLParser.getTREC(everything);

			//elimino la riga clueweb
			everything = MyHTMLParser.deleteTREC(everything);

			//individuo la lingua
			String language =MyHTMLParser.getPageLanguage(everything);

			String body_clean = MyHTMLParser.parse(everything);
			//System.out.println(body_clean);

			String sentences[] = SentenceFilter.getSentences(body_clean, language);

			//taglio frasi brevi e lunghe (3,40)
			List<String> sentences_cut = SentenceFilter.cut(sentences);

			//normalize text
			normalize(sentences_cut, TREC);

		}

	}




	private static void normalize(List<String> sentences_cut, String TREC) {

		TextNormalizer.getURLs(sentences_cut, TREC);				//ok
		TextNormalizer.getMails(sentences_cut, TREC);				//ok
		TextNormalizer.getDistances(sentences_cut, TREC);			//ok
		TextNormalizer.getNumbers(sentences_cut, TREC);			//ok
		TextNormalizer.getOrdinals(sentences_cut, TREC);			//ok
		TextNormalizer.getDates(sentences_cut, TREC);				//ok
		TextNormalizer.getMoney(sentences_cut, TREC);				//ok
		TextNormalizer.getPercentages(sentences_cut, TREC);		//ok

	}



	public static List<String> listFilesForFolder(final File folder) {
		List<String> list = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				list.add((fileEntry.getName()));
			}
		}
		return list;
	}


}
