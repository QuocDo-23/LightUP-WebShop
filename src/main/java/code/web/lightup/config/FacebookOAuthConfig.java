package code.web.lightup.config;

import jakarta.servlet.http.HttpServletRequest;

public class FacebookOAuthConfig {

    public static final String APP_ID     = System.getenv("FACEBOOK_APP_ID");
    public static final String APP_SECRET = System.getenv("FACEBOOK_APP_SECRET");

    public static final String REDIRECT_URI_PATH = "/facebook-callback";

    public static final String AUTH_URL  = "https://www.facebook.com/v19.0/dialog/oauth";
    public static final String TOKEN_URL = "https://graph.facebook.com/v19.0/oauth/access_token";
    public static final String GRAPH_URL =
            "https://graph.facebook.com/v19.0/me?fields=id,name,email,picture.type(large)";

    public static final String SCOPE = "public_profile,email";

    public static String getRedirectUri(HttpServletRequest request) {
        String scheme      = request.getScheme();
        String serverName  = request.getServerName();
        int    serverPort  = request.getServerPort();
        String contextPath = request.getContextPath();

        String portPart = "";
        if ((scheme.equals("http")  && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            portPart = ":" + serverPort;
        }

        return scheme + "://" + serverName + portPart + contextPath + REDIRECT_URI_PATH;
    }
}