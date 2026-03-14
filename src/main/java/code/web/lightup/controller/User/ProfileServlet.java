package code.web.lightup.controller.User;

import code.web.lightup.model.Address;
import code.web.lightup.model.User;
import code.web.lightup.service.AddressService;
import code.web.lightup.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/profile")

public class ProfileServlet extends HttpServlet {
    private UserService userService;
    private AddressService addressService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        addressService = new AddressService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User updatedUser = userService.getUserById(user.getId()).orElse(user);
        session.setAttribute("user", updatedUser);

        List<Address> addresses = addressService.getAddressByUserId(user.getId());
        request.setAttribute("addresses", addresses);

        Address defaultAddress = addressService.getDefaultAddress(user.getId());
        request.setAttribute("defaultAddress", defaultAddress);
        request.setAttribute("activeTab", "profile");

        request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("updateProfile".equals(action)) {
            updateUserProfile(request, response, user, session);
        } else if ("addAddress".equals(action)) {
            addAddress(request, response, user);
        } else if ("updateAddress".equals(action)) {
            updateAddress(request, response, user);
        } else if ("deleteAddress".equals(action)) {
            deleteAddress(request, response);
        } else if ("setDefaultAddress".equals(action)) {
            setDefaultAddress(request, response, user);
        } else {
            updateUserProfile(request, response, user, session);
        }
    }

    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response,
                                   User user, HttpSession session) throws IOException, ServletException {
        try {
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String dob = request.getParameter("dob");

            user.setName(fullName);
            user.setPhone(phone);
            user.setGender(gender);
            if (dob != null && !dob.isEmpty()) {
                user.setDateOfBirth(LocalDate.parse(dob));
            }

            boolean success = userService.updateUser(user);

            if (success) {
                User updatedUser = userService.getUserById(user.getId()).orElse(user);
                session.setAttribute("user", updatedUser);

                request.setAttribute("message", "Cập nhật thông tin thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Cập nhật thông tin thất bại!");
                request.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        doGet(request, response);
    }

    private void addAddress(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException, ServletException {
        try {
            String recipientName = request.getParameter("recipientName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String houseNumber = request.getParameter("houseNumber");
            String commune = request.getParameter("commune");
            String district = request.getParameter("district");
            String addressDetail = request.getParameter("addressDetail");
            boolean isDefault = "true".equals(request.getParameter("isDefault"));

            Address address = new Address();
            address.setUserId(user.getId());
            address.setRecipientName(recipientName);
            address.setPhone(phone);
            address.setEmail(email);
            address.setHouse_number(houseNumber);
            address.setCommune(commune);
            address.setDistrict(district);
            address.setAddressDetail(addressDetail);
            address.setDefault(isDefault);

            if (isDefault) {
                addressService.unsetAllDefault(user.getId());
            }

            boolean success = addressService.insertAddress(address);

            if (success) {
                request.setAttribute("message", "Thêm địa chỉ thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Thêm địa chỉ thất bại!");
                request.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        doGet(request, response);
    }

    private void updateAddress(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException, ServletException {
        try {
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            String recipientName = request.getParameter("recipientName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String houseNumber = request.getParameter("houseNumber");
            String commune = request.getParameter("commune");
            String district = request.getParameter("district");
            String addressDetail = request.getParameter("addressDetail");
            boolean isDefault = "true".equals(request.getParameter("isDefault"));

            Address address = new Address();
            address.setId(addressId);
            address.setUserId(user.getId());
            address.setRecipientName(recipientName);
            address.setPhone(phone);
            address.setEmail(email);
            address.setHouse_number(houseNumber);
            address.setCommune(commune);
            address.setDistrict(district);
            address.setAddressDetail(addressDetail);
            address.setDefault(isDefault);

            if (isDefault) {
                addressService.unsetAllDefault(user.getId());
            }

            boolean success = addressService.updateAddress(address);

            if (success) {
                request.setAttribute("message", "Cập nhật địa chỉ thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Cập nhật địa chỉ thất bại!");
                request.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        doGet(request, response);
    }

    private void deleteAddress(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            boolean success = addressService.deleteAddress(addressId);

            if (success) {
                request.setAttribute("message", "Xóa địa chỉ thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Xóa địa chỉ thất bại!");
                request.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        doGet(request, response);
    }

    private void setDefaultAddress(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException, ServletException {
        try {
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            boolean success = addressService.setAsDefault(addressId, user.getId());

            if (success) {
                request.setAttribute("message", "Đặt địa chỉ mặc định thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Đặt địa chỉ mặc định thất bại!");
                request.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        doGet(request, response);
    }
}