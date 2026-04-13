package code.web.lightup.controller.User.FacebookController;

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
    private static final String APP_ID       = "key_ID";
    private static final String APP_SECRET   = "key_Secret";
    private static final String REDIRECT_URI = "http://localhost:8080/LightUp_war/facebook-callback";

    private static final String TOKEN_URL =
            "https://graph.facebook.com/v19.0/oauth/access_token";
    private static final String GRAPH_URL =
            "https://graph.facebook.com/v19.0/me?fields=id,name,email,picture.type(large)";

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

        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login?error=facebook_error");
            return;
        }

        try {

            String accessToken = exchangeCodeForToken(code);
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
            String picture = fbUser
                    .optJSONObject("picture") != null
                    ? fbUser.getJSONObject("picture")
                    .optJSONObject("data") != null
                    ? fbUser.getJSONObject("picture").getJSONObject("data").optString("url", "")
                    : ""
                    : "";


            if (email.isEmpty()) {
                email = fbId + "@facebook.com";
            }

            Optional<User> existingUser = userService.getUserByEmail(email);
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();

                if (user.getAuthProvider() == null
                        || (!user.getAuthProvider().equalsIgnoreCase("facebook")
                        && !user.getAuthProvider().equalsIgnoreCase("google"))) {
                    response.sendRedirect(request.getContextPath()
                            + "/login?error=email_exists");
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

            if ("locked".equalsIgnoreCase(user.getStatus())) {
                response.sendRedirect(request.getContextPath() + "/login?error=account_locked");
                return;
            }

            Cart guestCart = (Cart) request.getSession().getAttribute("cart");
            SessionUtil.setUserSession(request, user);
            Cart mergedCart = cartService.mergeOnLogin(user.getId(), guestCart);
            request.getSession().setAttribute("cart", mergedCart);


            String contextPath = request.getContextPath();
            String fbRedirect  = (String) request.getSession().getAttribute("fbRedirect");
            request.getSession().removeAttribute("fbRedirect");

            String redirectUrl;
            if ("payment".equals(fbRedirect)) {
                redirectUrl = contextPath + "/payment";
            } else if ("cart".equals(fbRedirect)) {
                redirectUrl = contextPath + "/cart";
            } else if (SessionUtil.isAdmin(request)) {
                redirectUrl = contextPath + "/admin/dashboard";
            } else {
                redirectUrl = contextPath + "/";
            }
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=facebook_error");
        }
    }

    private String exchangeCodeForToken(String code) throws IOException {
        String params = "client_id="     + URLEncoder.encode(APP_ID,       StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(APP_SECRET,   StandardCharsets.UTF_8)
                + "&redirect_uri="  + URLEncoder.encode(REDIRECT_URI,  StandardCharsets.UTF_8)
                + "&code="          + URLEncoder.encode(code,          StandardCharsets.UTF_8);

        URL url = new URL(TOKEN_URL + "?" + params);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) return null;

        String body = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        return json.optString("access_token", null);
    }

    private JSONObject fetchFacebookProfile(String accessToken) throws IOException {
        URL url = new URL(GRAPH_URL + "&access_token="
                + URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) return null;

        String body = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return new JSONObject(body);
    }
}