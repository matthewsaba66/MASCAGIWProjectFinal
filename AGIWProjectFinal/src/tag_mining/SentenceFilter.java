package tag_mining;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;


public class SentenceFilter {
	private static final int LOWER_BOUND = 3;
	private static final int UPPER_BOUND = 40;
	private static InputStream is;




	public static List<String> cut(String[] sentences) {
		List<String> sentences_cut = new ArrayList<String>();
		for (int i = 0; i < sentences.length; i++){
			String[] tokens = sentences[i].split(" ");
			if(tokens.length > 3 && tokens.length < 40){
				sentences_cut.add(sentences[i]);
			}
		}
		return sentences_cut;
	}


	public static String[] getSentences(String text, String language) throws InvalidFormatException, IOException{
		if (language.contains("en")){
			is = new FileInputStream("bin/en-sent.bin");		
		}
		else if (language.contains("de")){
			is = new FileInputStream("bin/de-sent.bin");			
		}
		else if (language.contains("nl")){
			is = new FileInputStream("bin/nl-sent.bin");			
		}
		else if (language.contains("pt")){
			is = new FileInputStream("bin/pt-sent.bin");		
		}
		else {
			is = new FileInputStream("bin/en-sent.bin");	
		}
		SentenceModel model = new SentenceModel(is);
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		String sentences[] = sdetector.sentDetect(text);
		return sentences;
	}



}
