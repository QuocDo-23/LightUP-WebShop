package code.web.lightup.controller.User;


import code.web.lightup.dao.ProductDAO;
import code.web.lightup.model.Address;
import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.Cart.CartItem;
import code.web.lightup.model.OrderItem;
import code.web.lightup.model.Payment;
import code.web.lightup.model.User;
import code.web.lightup.service.AddressService;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import code.web.lightup.model.Order;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private OrderService orderService;
    private PaymentService paymentService;
    private AddressService addressService;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderService = new OrderService();
        paymentService = new PaymentService();
        addressService = new AddressService();
        productDAO = new ProductDAO();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Cart cart = (Cart) session.getAttribute("checkoutCart");
        if (cart == null) {
            cart = (Cart) session.getAttribute("cart");
        }

        if (cart == null || cart.getTotalItems() == 0) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        request.setAttribute("cart", cart);


        User user = (User) session.getAttribute("user");
        if (user != null) {

            List<Address> savedAddresses = addressService.getAddressByUserId(user.getId());
            request.setAttribute("savedAddresses", savedAddresses);


            String selectedAddressId = request.getParameter("selectedAddressId");

            if (selectedAddressId != null && !selectedAddressId.equals("new")) {

                try {
                    int addressId = Integer.parseInt(selectedAddressId);
                    Address selectedAddress = null;

                    for (Address addr : savedAddresses) {
                        if (addr.getId() == addressId) {
                            selectedAddress = addr;
                            break;
                        }
                    }

                    if (selectedAddress != null) {
                        request.setAttribute("selectedAddress", selectedAddress);
                    }
                } catch (NumberFormatException e) {

                }
            } else if (selectedAddressId != null) {
                request.setAttribute("isNewAddress", true);
            } else {
                for (Address addr : savedAddresses) {
                    if (addr.isDefault()) {
                        request.setAttribute("selectedAddress", addr);
                        break;
                    }
                }
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login?redirect=payment");
            return;
        }

        request.getRequestDispatcher("/views/user/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        try {
            Cart cart = (Cart) session.getAttribute("checkoutCart");
            if (cart == null) {
                cart = (Cart) session.getAttribute("cart");
            }
            if (cart == null || cart.getTotalItems() == 0) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            User user = (User) session.getAttribute("user");
            Integer userId = user != null ? user.getId() : null;


            String checkoutType = request.getParameter("checkoutType");
            String recipientName = request.getParameter("recipientName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String houseNumber = request.getParameter("houseNumber");
            String commune = request.getParameter("commune");
            String district = request.getParameter("district");
            String addressDetail = request.getParameter("addressDetail");
            if (addressDetail != null && addressDetail.trim().isEmpty()) {
                addressDetail = null;
            }


            String shippingMethod = request.getParameter("shippingMethod");
            String paymentMethod = request.getParameter("paymentMethod");

            boolean saveAddress = "true".equals(request.getParameter("saveAddress"));


            double shippingFee = "express".equals(shippingMethod) ? 30000 : 0;
            double totalAmount = cart.getTotalPrice() + shippingFee;


            Order order = new Order();
            order.setUserId(userId);
            order.setRecipientName(recipientName);
            order.setRecipientPhone(phone);
            order.setRecipientEmail(email);
            order.setShippingHouseNumber(houseNumber);
            order.setShippingCommune(commune);
            order.setShippingDistrict(district);
            order.setShippingAddressDetail(addressDetail);
            order.setOrderDate(LocalDateTime.now());
            order.setTotal(totalAmount);
            order.setStatus("pending");


            int orderId = orderService.insertOrder(order);

            if (orderId > 0) {

                for (CartItem item : cart.getListItem()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(orderId);
                    orderItem.setProductId(item.getProduct().getId());
                    orderItem.setProductName(item.getProduct().getName());
                    orderItem.setProductMaterial(item.getProduct().getMaterial());
                    orderItem.setPrice(item.getProduct().getDiscountedPrice());
                    orderItem.setImg(item.getProduct().getMainImage());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setSubtotal(item.getQuantity() * item.getProduct().getDiscountedPrice());
                    orderService.insertOrderItem(orderItem);


                    productDAO.decreaseProductQuantity(item.getProduct().getId(), item.getQuantity());

                }


                Payment payment = new Payment();
                payment.setOrderId(orderId);
                payment.setPaymentMethod(paymentMethod);
                payment.setAmount(totalAmount);
                payment.setStatus("pending");
                payment.setPayDate(LocalDate.now());

                paymentService.insertPayment(payment);


                if (user != null && saveAddress) {
                    Address newAddress = new Address(
                            phone, userId, recipientName,
                            houseNumber, commune, district, addressDetail
                    );
                    newAddress.setEmail(email);
                    addressService.insertAddress(newAddress);
                }


                Cart fullCart = (Cart) session.getAttribute("cart");
                if (fullCart != null) {
                    for (CartItem item : cart.getListItem()) {
                        fullCart.removeItem(item.getProduct().getId());
                    }
                    session.setAttribute("cart", fullCart);
                }
                // Xoá checkoutCart tạm
                session.removeAttribute("checkoutCart");
                session.removeAttribute("checkoutType");

                session.setAttribute("orderId", orderId);
                response.sendRedirect(request.getContextPath() + "/order-success");

            } else {

                request.setAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.");
                request.getRequestDispatcher("/views/user/payment.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/views/user/payment.jsp").forward(request, response);
        }
    }
}