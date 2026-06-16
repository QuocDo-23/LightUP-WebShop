package code.web.lightup.util;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.crypto.Mac; import javax.crypto.spec.SecretKeySpec;

public class VnpayConstants {

    // === CẤU HÌNH VNPAY (SANDBOX) ===
    public static String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String VNP_RETURN_URL = "http://localhost:8080/LightUp_war/vnpay-return";
    public static String VNP_TMN_CODE = "1N3OPOMK";
    public static String VNP_HASH_SECRET = "7N2CMCKL126LQ2MVJ2034ERINFMI1DHT";

    public static String VNP_VERSION = "2.1.0";
    public static String VNP_COMMAND = "pay";
    public static String VNP_ORDER_TYPE = "other";

    // Hàm tạo chữ ký HMAC-SHA512
    public static String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : hmacBytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký VNPAY", e);
        }
    }

    public static String generatePaymentUrl(int orderId, double amount, String orderInfo, HttpServletRequest request) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", VNP_VERSION);
        vnpParams.put("vnp_Command", VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", VNP_TMN_CODE);
        vnpParams.put("vnp_Amount", String.valueOf((long) (amount * 100)));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", String.valueOf(orderId));
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", VNP_ORDER_TYPE);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VNP_RETURN_URL);
        vnpParams.put("vnp_IpAddr", getIpAddress(request));
        vnpParams.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnpParams.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encodedName = java.net.URLEncoder.encode(fieldName, StandardCharsets.UTF_8);
                String encodedValue = java.net.URLEncoder.encode(fieldValue, StandardCharsets.UTF_8);

                if (hashData.length() > 0) {
                    hashData.append("&");
                    query.append("&");
                }

                hashData.append(encodedName).append("=").append(encodedValue);
                query.append(encodedName).append("=").append(encodedValue);
            }
        }

        String vnpSecureHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());

        return VNP_PAY_URL + "?" + query + "&vnp_SecureHash=" + vnpSecureHash;
    }

    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    // Kiểm tra chữ ký khi VNPAY trả về
    public static boolean validateSignature(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);

            if (fieldValue != null
                    && !fieldValue.isEmpty()
                    && !fieldName.equals("vnp_SecureHash")
                    && !fieldName.equals("vnp_SecureHashType")) {
                fields.put(fieldName, fieldValue);
            }
        }

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = fields.get(fieldName);

            if (hashData.length() > 0) {
                hashData.append("&");
            }

            hashData.append(java.net.URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
            hashData.append("=");
            hashData.append(java.net.URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
        }

        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        String calculatedHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());

        return calculatedHash.equalsIgnoreCase(vnpSecureHash);
    }

}