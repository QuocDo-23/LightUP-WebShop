package code.web.lightup.controller.User.FacebookController;

import code.web.lightup.config.FacebookOAuthConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebServlet("/facebook-login")
public class FacebookLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute("fb_oauth_state", state);

        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            request.getSession().setAttribute("fbRedirect", redirect);
        }

        String redirectUri = FacebookOAuthConfig.getRedirectUri(request);

        String authUrl = FacebookOAuthConfig.AUTH_URL
                + "?client_id="    + URLEncoder.encode(FacebookOAuthConfig.APP_ID,   StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri,                  StandardCharsets.UTF_8)
                + "&scope="        + URLEncoder.encode(FacebookOAuthConfig.SCOPE,    StandardCharsets.UTF_8)
                + "&state="        + URLEncoder.encode(state,                        StandardCharsets.UTF_8)
                + "&response_type=code";

        response.sendRedirect(authUrl);
    }
}