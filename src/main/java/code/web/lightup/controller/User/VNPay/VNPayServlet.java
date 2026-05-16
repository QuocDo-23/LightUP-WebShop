package code.web.lightup.controller.User.VNPay;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/vnpay-payment")
public class VNPayServlet extends HttpServlet {

    private static final String VNP_TMN_CODE    = "DEMOV210";
    private static final String VNP_HASH_SECRET = "RAOEXHYVSDDIIENYWSLDIIZTANXUXZFJ";
    private static final String VNP_URL         = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String VNP_RETURN_URL  = "https://salutary-salon-styling.ngrok-free.dev/LightUp_war/vnpay-return";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== VNPayServlet doPost called ===");

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            String amount  = request.getParameter("amount");
            String orderId = request.getParameter("orderId");

            System.out.println("amount=" + amount + ", orderId=" + orderId);

            long amountLong = Long.parseLong(amount.trim()) * 100;

            String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            Map<String, String> params = new TreeMap<>();
            params.put("vnp_Version",   "2.1.0");
            params.put("vnp_Command",   "pay");
            params.put("vnp_TmnCode",   VNP_TMN_CODE);
            params.put("vnp_Amount",    String.valueOf(amountLong));
            params.put("vnp_CurrCode",  "VND");
            params.put("vnp_TxnRef",    orderId);
            params.put("vnp_OrderInfo", "ThanhToanDonHang" + orderId);
            params.put("vnp_OrderType", "other");
            params.put("vnp_Locale",    "vn");
            params.put("vnp_ReturnUrl", VNP_RETURN_URL);
            params.put("vnp_IpAddr",    "127.0.0.1");
            params.put("vnp_CreateDate",createDate);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query    = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!first) { hashData.append('&'); query.append('&'); }

                hashData.append(entry.getKey()).append('=').append(entry.getValue());

                query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
                first = false;
            }

            String secureHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());
            String payUrl = VNP_URL + "?" + query + "&vnp_SecureHash=" + secureHash;

            System.out.println("hashData: " + hashData.toString());

            System.out.println("VNPay payUrl created: " + payUrl.substring(0, 80) + "...");

            response.getWriter().write("{\"payUrl\":\"" + payUrl + "\"}");

        } catch (Exception e) {
            System.out.println("VNPay error: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}