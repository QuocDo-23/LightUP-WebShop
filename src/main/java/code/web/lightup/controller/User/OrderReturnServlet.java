package code.web.lightup.controller.User;

import code.web.lightup.dao.OrderDAO;
import code.web.lightup.model.Order;
import code.web.lightup.model.OrderReturn;
import code.web.lightup.model.User;
import code.web.lightup.service.OrderReturnService;
import code.web.lightup.util.FileUploadUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "OrderReturnServlet", urlPatterns = {"/return-order"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 20
)
public class OrderReturnServlet extends HttpServlet {

    private OrderReturnService orderReturnService;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderReturnService = new OrderReturnService();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String reason = request.getParameter("reason");
            String description = request.getParameter("description");

            if (reason == null || reason.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Vui lòng chọn lý do trả hàng!");
                response.sendRedirect("order_detail?id=" + orderId);
                return;
            }

            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                session.setAttribute("errorMessage", "Đơn hàng không tồn tại!");
                response.sendRedirect("orders");
                return;
            }

            if (order.getUserId() != user.getId()) {
                session.setAttribute("errorMessage", "Bạn không có quyền trả hàng này!");
                response.sendRedirect("orders");
                return;
            }

            if (!order.getStatus().equals("shipped") && !order.getStatus().equals("delivered")) {
                session.setAttribute("errorMessage", "Chỉ có thể trả hàng khi đơn hàng đang giao hoặc đã giao!");
                response.sendRedirect("order_detail?id=" + orderId);
                return;
            }

            if (orderReturnService.hasOrderReturn(orderId)) {
                session.setAttribute("errorMessage", "Đơn hàng này đã có yêu cầu trả hàng!");
                response.sendRedirect("order_detail?id=" + orderId);
                return;
            }

            OrderReturn orderReturn = new OrderReturn();
            orderReturn.setOrderId(orderId);
            orderReturn.setUserId(user.getId());
            orderReturn.setReason(reason);
            orderReturn.setDescription(description != null ? description : "");
            orderReturn.setStatus("pending");

            List<String> uploadedImages = new ArrayList<>();
            try {
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    if (part.getName().startsWith("evidence_image") && part.getSize() > 0) {
                        String uploadedPath = FileUploadUtil.uploadImage(part, getServletContext());
                        if (uploadedPath != null) {
                            uploadedImages.add(uploadedPath);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            orderReturn.setEvidenceImages(uploadedImages);

            int returnId = orderReturnService.createOrderReturn(orderReturn);

            if (returnId > 0) {
                orderDAO.updateOrderStatus(orderId, "returning");

                session.setAttribute("successMessage",
                        "Yêu cầu trả hàng đã được gửi thành công! " +
                                "Chúng tôi sẽ xem xét và liên hệ với bạn sớm nhất.");
                response.sendRedirect("order_detail?id=" + orderId);
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi gửi yêu cầu trả hàng!");
                response.sendRedirect("order_detail?id=" + orderId);
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Mã đơn hàng không hợp lệ!");
            response.sendRedirect("orders");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect("orders");
        }
    }
}