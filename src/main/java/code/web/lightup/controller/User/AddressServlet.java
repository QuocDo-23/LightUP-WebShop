package code.web.lightup.controller.User;

import code.web.lightup.model.Address;
import code.web.lightup.model.User;
import code.web.lightup.service.AddressService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/address")
public class AddressServlet extends HttpServlet {
    private AddressService addressService;

    @Override
    public void init() throws ServletException {
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

        List<Address> addresses = addressService.getAddressByUserId(user.getId());
        request.setAttribute("addresses", addresses);

        request.getRequestDispatcher("/views/user/address.jsp").forward(request, response);
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

        if ("addAddress".equals(action)) {
            addAddress(request, response, user);
        } else if ("updateAddress".equals(action)) {
            updateAddress(request, response, user);
        } else if ("deleteAddress".equals(action)) {
            deleteAddress(request, response);
        } else if ("setDefaultAddress".equals(action)) {
            setDefaultAddress(request, response, user);
        } else {
            doGet(request, response);
        }
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