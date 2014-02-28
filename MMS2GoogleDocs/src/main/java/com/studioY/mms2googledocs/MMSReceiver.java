package com.studioY.mms2googledocs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MMSReceiver extends BroadcastReceiver{
    private static final String ACTION_MMS_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    private static final String MMS_DATA_TYPE = "application/vnd.wap.mms-message";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        
        if(action.equals(ACTION_MMS_RECEIVED) && type.equals(MMS_DATA_TYPE)){
            Log.d("yskang", "mms received");
        }
    }
}
