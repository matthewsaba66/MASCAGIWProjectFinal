package warc_parser;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;


public class ReadWarcSample {

	public static void main(String[] args) throws IOException {
		String inputWarcFile="/home/matteo/Scrivania/ClueWeb09/00.warc.gz";
		// open our gzip input stream
		GZIPInputStream gzInputStream=new GZIPInputStream(new FileInputStream(inputWarcFile));

		// cast to a data input stream
		DataInputStream inStream=new DataInputStream(gzInputStream);

		// iterate through our stream
		WarcRecord thisWarcRecord;
		int con = 0;
		String regex = "HTTP.*\n(.*\n)*Content-Length.*\n";
		while ((thisWarcRecord=WarcRecord.readNextWarcRecord(inStream))!=null) {
			// see if it's a response record
			if (thisWarcRecord.getHeaderRecordType().equals("response")) {
				
				File file = new File("index/" + con + "-extracted");

				// it is - create a WarcHTML record
				WarcHTMLResponseRecord htmlRecord=new WarcHTMLResponseRecord(thisWarcRecord);
				// get our TREC ID and target URI
				String thisTRECID=htmlRecord.getTargetTrecID();
				String thisTargetURI=htmlRecord.getTargetURI();
				String data = htmlRecord.getRawRecord().getContentUTF8();
				String text = thisTRECID + "\n" + data;
				text = text.replaceFirst(regex, "");

				// print our data
				//System.out.println(thisTRECID + " : " + thisTargetURI);
				//System.out.println(data);
				System.out.println("File numero: "+ con);
				
				FileOutputStream fop = new FileOutputStream(file);
				if (!file.exists()) {
					file.createNewFile();
				}
	 
				// get the content in bytes
				byte[] contentInBytes = text.getBytes();
	 
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
	 
				
				con++;


			}
		}

		inStream.close();
	}
}