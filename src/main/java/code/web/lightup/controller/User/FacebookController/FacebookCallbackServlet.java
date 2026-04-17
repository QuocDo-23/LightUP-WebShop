package code.web.lightup.controller.User.FacebookController;

import code.web.lightup.config.FacebookOAuthConfig;
import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.User;
import code.web.lightup.service.CartService;
import code.web.lightup.service.UserService;
import code.web.lightup.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/facebook-callback")
public class FacebookCallbackServlet extends HttpServlet {

    private UserService userService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String error = request.getParameter("error");
        if (error != null) {
            response.sendRedirect(request.getContextPath() + "/login?error=facebook_denied");
            return;
        }

        String state        = request.getParameter("state");
        String sessionState = (String) request.getSession().getAttribute("fb_oauth_state");
        request.getSession().removeAttribute("fb_oauth_state");

        if (state == null || !state.equals(sessionState)) {
            response.sendRedirect(request.getContextPath() + "/login?error=invalid_state");
            return;
        }

        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login?error=facebook_error");
            return;
        }

        try {
            String redirectUri = FacebookOAuthConfig.getRedirectUri(request);
            String accessToken = exchangeCodeForToken(code, redirectUri);
            if (accessToken == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=facebook_token");
                return;
            }

            JSONObject fbUser = fetchFacebookProfile(accessToken);
            if (fbUser == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=facebook_profile");
                return;
            }

            String fbId    = fbUser.optString("id");
            String name    = fbUser.optString("name");
            String email   = fbUser.optString("email", "");
            String picture = "";
            if (fbUser.optJSONObject("picture") != null
                    && fbUser.getJSONObject("picture").optJSONObject("data") != null) {
                picture = fbUser.getJSONObject("picture")
                        .getJSONObject("data")
                        .optString("url", "");
            }

            if (email.isEmpty()) {
                email = fbId + "@facebook.com";
            }

            Optional<User> existingUser = userService.getUserByEmail(email);
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();

                if ("locked".equalsIgnoreCase(user.getStatus())) {
                    response.sendRedirect(request.getContextPath() + "/login?error=account_locked");
                    return;
                }

                String provider = user.getAuthProvider();
                if (provider == null
                        || (!provider.equalsIgnoreCase("facebook")
                        &&  !provider.equalsIgnoreCase("google"))) {
                    response.sendRedirect(request.getContextPath() + "/login?error=email_exists");
                    return;
                }

            } else {
                User newUser = new User();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setAvatarImg(picture);
                newUser.setAuthProvider("facebook");

                boolean created = userService.registerFacebookUser(newUser);
                if (!created) {
                    response.sendRedirect(request.getContextPath() + "/login?error=facebook_register");
                    return;
                }
                user = userService.getUserByEmail(email).orElseThrow();
            }

            Cart guestCart  = (Cart) request.getSession().getAttribute("cart");
            SessionUtil.setUserSession(request, user);
            Cart mergedCart = cartService.mergeOnLogin(user.getId(), guestCart);
            request.getSession().setAttribute("cart", mergedCart);

            String redirectUrl = SessionUtil.getFacebookPostLoginRedirect(request );
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=facebook_error");
        }
    }

    private String exchangeCodeForToken(String code, String redirectUri) throws IOException {
        String params = "client_id="     + URLEncoder.encode(FacebookOAuthConfig.APP_ID,     StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(FacebookOAuthConfig.APP_SECRET, StandardCharsets.UTF_8)
                + "&redirect_uri="  + URLEncoder.encode(redirectUri,                    StandardCharsets.UTF_8)
                + "&code="          + URLEncoder.encode(code,                           StandardCharsets.UTF_8);

        URL url = new URL(FacebookOAuthConfig.TOKEN_URL + "?" + params);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) return null;

        String body = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        return json.optString("access_token", null);
    }

    private JSONObject fetchFacebookProfile(String accessToken) throws IOException {
        URL url = new URL(FacebookOAuthConfig.GRAPH_URL
                + "&access_token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) return null;

        String body = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return new JSONObject(body);
    }
}