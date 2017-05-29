package com.pragbits.bitbucketserver;

public class PluginMetadata {

    public static String getPluginKey() {
        return "com.pragbits.stash.stash2chatwork";
    }

    public static String getCompleteModuleKey(String moduleKey) {
        return getPluginKey() + ":" + moduleKey;
    }
}
