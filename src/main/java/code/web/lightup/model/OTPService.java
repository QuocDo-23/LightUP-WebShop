package code.web.lightup.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OTPService {

    private static final Map<String, OTPData> otpStorage = new ConcurrentHashMap<>();
    private static final int OTP_MINUTES = 5;


    public void saveOTP(String email, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_MINUTES);
        otpStorage.put(email, new OTPData(otp, expiryTime));
    }


    public boolean verifyOTP(String email, String otp) {
        OTPData otpData = otpStorage.get(email);

        if (otpData == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpStorage.remove(email);
            return false;
        }


        if (otpData.getOtp().equals(otp)) {
            return true;
        }

        return false;
    }


    public void removeOTP(String email) {
        otpStorage.remove(email);
    }


    public boolean hasOTP(String email) {
        return otpStorage.containsKey(email);
    }


    private static class OTPData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OTPData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}