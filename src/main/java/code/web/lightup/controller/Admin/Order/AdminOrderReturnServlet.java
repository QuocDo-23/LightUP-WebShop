package code.web.lightup.controller.Admin.Order;

import code.web.lightup.model.OrderReturn;
import code.web.lightup.service.OrderReturnService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "AdminOrderReturnServlet", urlPatterns = {"/admin/order-returns"})
public class AdminOrderReturnServlet extends HttpServlet {

    private OrderReturnService orderReturnService;

    @Override
    public void init() throws ServletException {
        orderReturnService = new OrderReturnService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        try {
            String action = request.getParameter("action");

            if ("view".equals(action)) {
                viewReturnDetail(request, response);
            } else if ("approve".equals(action)) {
                approveReturn(request, response);
            } else if ("reject".equals(action)) {
                rejectReturn(request, response);
            } else if ("complete".equals(action)) {
                completeReturn(request, response);
            } else {
                listReturns(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect("admin/order-returns");
        }
    }

    /**
     * Lấy danh sách yêu cầu trả hàng
     */
    private void listReturns(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String statusFilter = request.getParameter("status");
        String searchKeyword = request.getParameter("search");

        List<OrderReturn> returns;

        if (statusFilter != null && !statusFilter.isEmpty()) {
            returns = orderReturnService.getOrderReturnsByStatus(statusFilter);
        } else {
            returns = orderReturnService.getAllOrderReturns();
        }

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            returns.removeIf(r ->
                    !String.valueOf(r.getOrderId()).contains(searchKeyword) &&
                            !String.valueOf(r.getUserId()).contains(searchKeyword)
            );
        }

        int pendingCount = orderReturnService.countOrderReturnsByStatus("pending");
        int approvedCount = orderReturnService.countOrderReturnsByStatus("approved");
        int rejectedCount = orderReturnService.countOrderReturnsByStatus("rejected");
        int completedCount = orderReturnService.countOrderReturnsByStatus("completed");

        request.setAttribute("returns", returns);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("approvedCount", approvedCount);
        request.setAttribute("rejectedCount", rejectedCount);
        request.setAttribute("completedCount", completedCount);

        request.getRequestDispatcher("/views/admin/Orders/order_returns_list.jsp")
                .forward(request, response);
    }

    /**
     * Xem chi tiết yêu cầu trả hàng
     */
    private void viewReturnDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int returnId = Integer.parseInt(request.getParameter("id"));
        OrderReturn orderReturn = orderReturnService.getOrderReturn(returnId);

        if (orderReturn == null) {
            request.getSession().setAttribute("errorMessage", "Yêu cầu trả hàng không tồn tại!");
            response.sendRedirect("order-returns");
            return;
        }

        request.setAttribute("orderReturn", orderReturn);
        request.getRequestDispatcher("/views/admin/Orders/order_returns_list.jsp")
                .forward(request, response);
    }

    /**
     * Duyệt yêu cầu trả hàng
     */
    private void approveReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int returnId = Integer.parseInt(request.getParameter("id"));

        String refundAmountStr = request.getParameter("refundAmount");

        if (refundAmountStr == null || refundAmountStr.isBlank()) {
            request.getSession().setAttribute(
                    "errorMessage",
                    "Thiếu số tiền hoàn trả!"
            );
            response.sendRedirect("order-returns?action=view&id=" + returnId);
            return;
        }

        double refundAmount = Double.parseDouble(refundAmountStr);

        String adminNotes = request.getParameter("adminNotes");

        boolean success = orderReturnService.approveOrderReturn(returnId, 0);

        if (success && adminNotes != null && !adminNotes.isEmpty()) {
            orderReturnService.updateAdminNotes(returnId, adminNotes);
        }

        if (success) {
            request.getSession().setAttribute("successMessage",
                    "Yêu cầu trả hàng đã được duyệt!");
        } else {
            request.getSession().setAttribute("errorMessage",
                    "Có lỗi xảy ra khi duyệt yêu cầu!");
        }

        response.sendRedirect("order-returns?action=view&id=" + returnId);
    }

    /**
     * Từ chối yêu cầu trả hàng
     */
    private void rejectReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int returnId = Integer.parseInt(request.getParameter("id"));
        String adminNotes = request.getParameter("adminNotes");

        if (adminNotes == null || adminNotes.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage",
                    "Vui lòng nhập lý do từ chối!");
            response.sendRedirect("order-returns?action=view&id=" + returnId);
            return;
        }

        boolean success = orderReturnService.rejectOrderReturn(returnId, adminNotes);

        if (success) {
            request.getSession().setAttribute("successMessage",
                    "Yêu cầu trả hàng đã được từ chối!");
        } else {
            request.getSession().setAttribute("errorMessage",
                    "Có lỗi xảy ra khi từ chối yêu cầu!");
        }

        response.sendRedirect("order-returns");
    }

    /**
     * Hoàn thành trả hàng (Hoàn tiền)
     */
    private void completeReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int returnId = Integer.parseInt(request.getParameter("id"));
        OrderReturn orderReturn = orderReturnService.getOrderReturn(returnId);

        if (orderReturn == null || !orderReturn.getStatus().equals("approved")) {
            request.getSession().setAttribute("errorMessage",
                    "Chỉ có thể hoàn thành yêu cầu đã được duyệt!");
            response.sendRedirect("order-returns");
            return;
        }

        boolean success = orderReturnService.completeOrderReturn(returnId, LocalDateTime.now());

        if (success) {
            request.getSession().setAttribute("successMessage",
                    "Yêu cầu trả hàng đã hoàn thành! Tiền đã hoàn lại cho khách hàng.");
        } else {
            request.getSession().setAttribute("errorMessage",
                    "Có lỗi xảy ra khi hoàn thành yêu cầu!");
        }

        response.sendRedirect("order-returns");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}