package code.web.lightup.controller.User.GoogleLoginController;


import code.web.lightup.config.GoogleOAuthConfig;
import code.web.lightup.model.User;
import code.web.lightup.service.UserService;
import code.web.lightup.util.SessionUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/google-callback")
public class GoogleCallbackServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String error = request.getParameter("error");

        if (error != null) {
            response.sendRedirect(request.getContextPath() + "/login?error=google_denied");
            return;
        }

        String sessionState = (String) request.getSession().getAttribute("oauth_state");
        if (state == null || !state.equals(sessionState)) {
            response.sendRedirect(request.getContextPath() + "/login?error=invalid_state");
            return;
        }

        try {
            String redirectUri = GoogleOAuthConfig.getRedirectUri(request);

            String accessToken = getAccessToken(code, redirectUri);

            JsonObject userInfo = getUserInfo(accessToken);

            String email = userInfo.get("email").getAsString();
            String name = userInfo.get("name").getAsString();
            String picture = userInfo.has("picture") ? userInfo.get("picture").getAsString() : null;

            Optional<User> existingUser = userService.getUserByEmail(email);

            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();

                if ("locked".equals(user.getStatus())) {
                    response.sendRedirect(request.getContextPath() + "/login?error=account_locked");
                    return;
                }
                if ("local".equalsIgnoreCase(user.getAuthProvider())) {
                    response.sendRedirect(request.getContextPath() + "/login?error=email_exists");
                    return;
                }
            } else {
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setAvatarImg(picture);
                user.setPassword("");

                boolean registered = userService.registerGoogleUser(user);
                if (!registered) {
                    response.sendRedirect(request.getContextPath() + "/login?error=registration_failed");
                    return;
                }
                user = userService.getUserByEmail(email).orElseThrow();
            }


            SessionUtil.setUserSession(request, user);


            String redirectUrl = getRedirectUrl(request);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=google_auth_failed");
        }
    }

    private String getAccessToken(String code, String redirectUri) throws IOException {
        URL url = new URL(GoogleOAuthConfig.TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(GoogleOAuthConfig.CLIENT_ID, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(GoogleOAuthConfig.CLIENT_SECRET, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&grant_type=authorization_code";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseStr.append(line);
            }

            JsonObject jsonResponse = JsonParser.parseString(responseStr.toString()).getAsJsonObject();
            return jsonResponse.get("access_token").getAsString();
        }
    }

    private JsonObject getUserInfo(String accessToken) throws IOException {
        URL url = new URL(GoogleOAuthConfig.USER_INFO_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseStr.append(line);
            }

            return JsonParser.parseString(responseStr.toString()).getAsJsonObject();
        }
    }

    private String getRedirectUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String redirect = (String) request.getSession().getAttribute("oauth_redirect");

        if ("payment".equals(redirect)) {
            return contextPath + "/payment";
        } else if ("cart".equals(redirect)) {
            return contextPath + "/cart";
        } else {
            return contextPath + "/";
        }
    }
}