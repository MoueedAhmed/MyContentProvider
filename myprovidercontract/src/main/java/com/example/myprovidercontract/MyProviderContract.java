package com.example.myprovidercontract;

import android.content.ContentResolver;
import android.net.Uri;

public class MyProviderContract {

    public static final String AUTHORITY = "com.example.mycontentprovider";

    public static final String BASE_PATH = "employees";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/employees";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/employee";
}
