package com.studioY.mms2googledocs;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class MMSReceiver extends BroadcastReceiver{
    private static final String ACTION_MMS_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    private static final String MMS_DATA_TYPE = "application/vnd.wap.mms-message";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String intentType = intent.getType();
        
        if(action.equals(ACTION_MMS_RECEIVED) && intentType.equals(MMS_DATA_TYPE)){


            Log.d("yskang", "mms received");
            Log.d("yskang", intent.toString());
            Log.d("yskang", intent.getExtras().toString());


            ContentResolver contentResolver = context.getContentResolver();
            final String[] projection = new String[]{"_id", "ct_t"};
            Uri uri = Uri.parse("content://mms-sms/conversations/");
            Cursor query = contentResolver.query(uri, projection, null, null, null);
            if (query.moveToFirst()) {
                do {
                    String string = query.getString(query.getColumnIndex("ct_t"));
                    if ("application/vnd.wap.multipart.related".equals(string)) {
                        // it's MMS
                        Log.d("yskang", query.getString(query.getColumnIndex("_id")));

                    } else {
                        // it's SMS
                    }
                } while (query.moveToNext());
            }

            String selectionPart = "mid=" + "117";
            uri = Uri.parse("content://mms/part");
            Cursor cPart = context.getContentResolver().query(uri, null, selectionPart, null, null);

            if(cPart.moveToFirst()){
                do{
                    String partId = cPart.getString(cPart.getColumnIndex("_id"));
                    String type = cPart.getString(cPart.getColumnIndex("ct"));
                    if("image/jpeg".equals(type) || "image/bmp".equals(type) || "image/png".equals(type)){
                        Bitmap bitmap = getMMSImage(partId, context);
                        Log.d("yskang", "image found");
                    }
                }while (cPart.moveToNext());
            }
        }
    }

    private Bitmap getMMSImage(String _id, Context context) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = context.getContentResolver().openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return bitmap;
    }
}
