package com.ekumfi.juice.util;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmUtility {
    private static final int SCHEMA_V_PREV = 5;// previous schema version
    private static final int SCHEMA_V_NOW = 10;// change schema version if any change happened in schema


    public static int getSchemaVNow() {
        return SCHEMA_V_NOW;
    }


    public static RealmConfiguration getDefaultConfig(Context context) {


        RealmConfiguration config = new RealmConfiguration
                .Builder()
//                .directory(new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()))
//                .name("superFixRealmDataBase.realm")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .schemaVersion(SCHEMA_V_NOW)
                .deleteRealmIfMigrationNeeded()// if migration needed then this methoud will remove the existing database and will create new database
//                .encryptionKey(new byte[]{42, -17, -94, 54, 44, 122, -71, 110, -80, 23, 53, 6, 102, 67, -24, -63, -93, -45, 64, -25, 90, -6, 125, -64, 74, 53, -41, -81, -12, 7, -27, 34, 84, 86, 108, -46, -126, -6, 59, 64, -23, -56, 2, -89, 28, -47, -43, -70, 94, -1, -110, 126, -14, -31, 51, -23, -120, -50, -70, -104, -100, -39, 52, 77})
                .build();

        Realm.setDefaultConfiguration(config);
        return config;
    }
}
