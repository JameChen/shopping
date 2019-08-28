package com.nahuo.service.autoupdate.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nahuo.service.autoupdate.Version;

import java.net.URLDecoder;

public class VersionPersistent {

    public static final String VERSION_PERSISTENT_NAME = "auto_update_version";

    public static final String VERSION_CODE = "code";
    public static final String VERSION_NAME = "name";
    public static final String VERSION_FEATURE = "feature";
    public static final String VERSION_URL = "url";
    public static final String VERSION_DESTROY_VERSION = "destroyVersion";


    private SharedPreferences shared;

    public VersionPersistent(Context context) {
        shared = context.getSharedPreferences(VERSION_PERSISTENT_NAME, Context.MODE_PRIVATE);
    }

    public void save(Version version) {
        if (version == null) return;
        Editor editor = shared.edit();
        editor.clear();
        editor.putInt(VERSION_CODE, version.code);
        editor.putString(VERSION_FEATURE, version.feature);
        editor.putString(VERSION_NAME, version.name);
        editor.putString(VERSION_URL, version.targetUrl);
        editor.putString(VERSION_DESTROY_VERSION, version.destroyVersions);
        editor.commit();
    }

    public void clear() {
        Editor editor = shared.edit();
        editor.clear();
        editor.commit();
    }

    public Version load() {
        if (shared.contains(VERSION_CODE)) {
            int code = shared.getInt(VERSION_CODE, 0);
            String name = URLDecoder.decode(shared.getString(VERSION_NAME, null));
            String feature = shared.getString(VERSION_FEATURE, null);
            String url = shared.getString(VERSION_URL, null);
            String destroyVersion = shared.getString(VERSION_DESTROY_VERSION, null);
            if (name == null || url == null) return null;
            return new Version(code, name, feature, url, destroyVersion);
        }
        return null;
    }

}
