package com.guohe.corecenter.constant;

public class UrlConst {

    public static final String DOMAIN = "http://192.168.0.199:9090/";//"http://39.100.41.44/";

    public static final String PICTURE_DOMAIN = "http://img.guoheai.com/";

    public static final String LOGIN_URL = DOMAIN + "account/mobile/mobLogin.do";
    public static final String CHECK_TOKEN_URL = DOMAIN + "account/mobile/checkToken.do";

    public static final String USER_UPDATE_URL = DOMAIN + "account/mobile/update.do";

    public static final String MOMENT_LIST_URL = DOMAIN + "social/moment/queryList.do";
    public static final String PUBLISH_MOMENT_URL = DOMAIN + "social/moment/publish.do";

    public static final String GET_QINIU_TOKEN = DOMAIN + "social/qiniu/getToken.do?type=";

}
