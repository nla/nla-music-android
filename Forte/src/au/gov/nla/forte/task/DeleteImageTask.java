package au.gov.nla.forte.task;

import java.io.File;

import android.os.AsyncTask;

public class DeleteImageTask extends AsyncTask<String, Void, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
		String fileName = params[0];
		
		try {               	
        	File file = new File(fileName);
        	return file.delete();          
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return false;
	}

}
