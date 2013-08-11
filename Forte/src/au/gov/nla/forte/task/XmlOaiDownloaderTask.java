package au.gov.nla.forte.task;

import java.lang.ref.WeakReference;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.os.AsyncTask;
import android.util.Log;
import au.gov.nla.forte.model.ScoreMetadata;
 
public class XmlOaiDownloaderTask extends AsyncTask<String, Void, Document> {
    
	private final WeakReference scoreMetadataReference;
	 
    public XmlOaiDownloaderTask(ScoreMetadata scoreMetadata) {
    	scoreMetadataReference = new WeakReference(scoreMetadata);
    }
    
    @Override
    // Actual download method, run in the task thread
    protected Document doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        return getDocumentFromUrl(params[0]);
    }
 
    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Document document) {
        if (isCancelled()) {
            document = null;
        }
        
        if (document != null) {
        	ScoreMetadata scoreMetadata = (ScoreMetadata)scoreMetadataReference.get();
        	scoreMetadata.populateFromDocument(document);
        }
    }
    
    private Document getDocumentFromUrl(String myUrl) {
		try {
            URL url = new URL(myUrl);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));   
            return doc;
        } catch (Exception e) {
        	Log.w("Utils.getXmlFromUrl", "XML Pasing Excpetion = " + e);
        }
		return null;
	}
 
}
