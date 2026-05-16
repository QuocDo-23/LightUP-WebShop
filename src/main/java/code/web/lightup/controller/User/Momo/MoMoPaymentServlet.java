package code.web.lightup.controller.User.Momo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebServlet("/momo-payment")
public class MoMoPaymentServlet extends HttpServlet {

    private static final String PARTNER_CODE = "MOMO";
    private static final String ACCESS_KEY   = "F8BBA842ECF85";
    private static final String SECRET_KEY   = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    private static final String ENDPOINT     = "https://test-payment.momo.vn/v2/gateway/api/create";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String amount    = request.getParameter("amount");
        String orderInfo = request.getParameter("orderInfo");
        String orderId   = request.getParameter("orderId");

        String requestId    = PARTNER_CODE + System.currentTimeMillis();

        // mỗi lần chạy ngrok phải thay url bang72 lệnh ngrok http 8080
        String redirectUrl = "https://salutary-salon-styling.ngrok-free.dev/LightUp_war/order-success";
        String ipnUrl      = "https://salutary-salon-styling.ngrok-free.dev/LightUp_war/momo-ipn";
        String requestType  = "captureWallet";
        String extraData    = "";

        String rawHash = "accessKey="   + ACCESS_KEY
                + "&amount="       + amount
                + "&extraData="    + extraData
                + "&ipnUrl="       + ipnUrl
                + "&orderId="      + orderId
                + "&orderInfo="    + orderInfo
                + "&partnerCode="  + PARTNER_CODE
                + "&redirectUrl="  + redirectUrl
                + "&requestId="    + requestId
                + "&requestType="  + requestType;

        String signature = hmacSHA256(rawHash, SECRET_KEY);

        String jsonBody = "{"
                + "\"partnerCode\":\""  + PARTNER_CODE  + "\","
                + "\"accessKey\":\""    + ACCESS_KEY     + "\","
                + "\"requestId\":\""    + requestId      + "\","
                + "\"amount\":\""       + amount         + "\","
                + "\"orderId\":\""      + orderId        + "\","
                + "\"orderInfo\":\""    + orderInfo      + "\","
                + "\"redirectUrl\":\""  + redirectUrl    + "\","
                + "\"ipnUrl\":\""       + ipnUrl         + "\","
                + "\"extraData\":\""    + extraData      + "\","
                + "\"requestType\":\""  + requestType    + "\","
                + "\"signature\":\""    + signature      + "\","
                + "\"lang\":\"vi\""
                + "}";

        URL url = new URL(ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) result.append(line);
        }

        response.getWriter().write(result.toString());
    }

    private String hmacSHA256(String data, String key) throws IOException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IOException("HMAC error: " + e.getMessage());
        }
    }
}