package com.hehelabs.wegoo1;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Amiri on 2/17/16.
 */
public class WegooInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this,com.hehelabs.wegoo1.WegooGcmListenerService.class);
        startService(intent);
    }
}
