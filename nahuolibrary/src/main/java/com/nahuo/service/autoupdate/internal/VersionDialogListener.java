package com.nahuo.service.autoupdate.internal;

public interface VersionDialogListener {
	void doUpdate(boolean laterOnWifi);
	void doIgnore();
}
