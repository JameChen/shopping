package com.nahuo.service.autoupdate.internal;

import com.nahuo.service.autoupdate.ResponseParser;
import com.nahuo.service.autoupdate.Version;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SimpleJSONParser implements ResponseParser {

    @Override
    public Version parser(String response) {
        try {
            JSONTokener jsonParser = new JSONTokener(response);
            JSONObject json = (JSONObject) jsonParser.nextValue();
            boolean success = json.getBoolean("Result");
            Version version = null;
            if (success) {
                JSONObject dataField = json.getJSONObject("Data");
                int code = dataField.getInt("Code");
                String name = dataField.getString("Version");
                String feature = dataField.getString("Feature");
                String targetUrl = dataField.getString("Url");
                String destroyVersion = dataField.getString("DestroyVersion");
                version = new Version(code, name, feature, targetUrl, destroyVersion);
            }
            return version;
        } catch (JSONException exp) {
            exp.printStackTrace();
            return null;
        }
    }

}
