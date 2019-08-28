package com.nahuo.service.autoupdate.internal;

import com.nahuo.service.autoupdate.Version;


public interface ResponseCallback {
	void onFoundLatestVersion(Version version);
	void onCurrentIsLatest();
}
