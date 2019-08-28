package com.nahuo.service.autoupdate.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.nahuo.service.autoupdate.ResponseParser;
import com.nahuo.service.autoupdate.Version;

public class VerifyTask extends AsyncTask<String, Integer, Version> {

	private ResponseParser parser;
	private ResponseCallback callback;
	private Context context;
	
	public VerifyTask(Context context,ResponseParser parser, ResponseCallback callback){
		this.parser = parser;
		this.context = context;
		this.callback = callback;
	}
	
	@Override
	protected Version doInBackground(String... args) {
		String url = args[0];
		Version latestVersion = null;
		try {
			URL targetUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
			InputStream is = connection.getInputStream();
			latestVersion = parser.parser(toStringBuffer(is).toString());
			is.close();
			connection.disconnect();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return latestVersion;
	}
	
	@Override  
    protected void onPostExecute(Version latestVersion) {  
        super.onPostExecute(latestVersion);
        if(comparedWithCurrentPackage(latestVersion)){
			callback.onFoundLatestVersion(latestVersion);
		}else{
			callback.onCurrentIsLatest();
		}
    } 
	
	boolean comparedWithCurrentPackage(Version version){
		if(version == null) return false;
		int currentVersionCode = 0;
		try {
			PackageInfo pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			currentVersionCode = pkg.versionCode;
		} catch (NameNotFoundException exp) {
			exp.printStackTrace();
		}
		return version.code > currentVersionCode;
	}
	
	StringBuffer toStringBuffer(InputStream is) throws IOException{
	    if( null == is) return null;
	    BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = null;
		while ((line = in.readLine()) != null){
		      buffer.append(line).append("\n");
		}
		is.close();
		return buffer;
	}

}
