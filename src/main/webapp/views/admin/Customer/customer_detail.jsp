<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <title>Chi Tiết Khách Hàng - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
</head>

<body>
<div class="container">
    <%@ include file="/views/layout/siderbar_admin.jsp" %>

    <div class="main-content">
        <div class="header">
            <div style="display: flex; align-items: center; gap: 10px;">
                <a href="javascript:history.back()" style="text-decoration: none; color: #718096; font-size: 20px;">
                    <i class="bi bi-arrow-left"></i>
                </a>
                <h1>Chi Tiết Khách Hàng: ${customer.name}</h1>
            </div>

                <div class="user-info">
                    <div class="avatar">
                        ${sessionScope.user.name.charAt(0)}
                    </div>
                    <div>
                        <div style="font-weight: 600;">${sessionScope.user.name}</div>
                        <div style="font-size: 12px; color: #718096;">
                            <c:choose>
                                <c:when test="${sessionScope.user.roleId == 1}">Quản trị viên</c:when>
                                <c:otherwise>Nhân viên</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

            <c:if test="${not empty sessionScope.newPassword and sessionScope.passwordResetFor == customer.id}">
                <div class="password-reset-alert">
                    Mật khẩu mới đã được tạo: <strong>${sessionScope.newPassword}</strong>. Vui lòng sao chép và gửi cho khách hàng. Mật khẩu này sẽ biến mất sau khi tải lại trang.
                </div>

                <c:remove var="newPassword" scope="session"/>
                <c:remove var="passwordResetFor" scope="session"/>
            </c:if>
        </div>

            <div class="detail-container">
                <div class="left-column">
                    <div class="info-card" style="margin-bottom: 20px;">
                        <h3>Thông Tin Cá Nhân</h3>
                        <div style="text-align: center; margin-bottom: 20px;">
                            <img src="${customer.avatarImg != null ? customer.avatarImg : 'https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png'}"
                                 alt="Avatar"
                                 style="width: 80px; height: 80px; border-radius: 50%; object-fit: cover; border: 2px solid #e2e8f0;">
                        </div>
                        <div class="info-row">
                            <span class="info-label">ID:</span>
                            <span class="info-value">#${customer.id}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Họ tên:</span>
                            <span class="info-value">${customer.name}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Email:</span>
                            <span class="info-value">${customer.email}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Số điện thoại:</span>
                            <span class="info-value">${customer.phone}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Giới tính:</span>
                            <span class="info-value">
                                <c:choose>
                                    <c:when test="${customer.gender == 'male'}">Nam</c:when>
                                    <c:when test="${customer.gender == 'female'}">Nữ</c:when>
                                    <c:otherwise>Khác</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Ngày sinh:</span>
                            <span class="info-value">${customer.dateOfBirth}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Trạng thái:</span>
                            <span class="info-value">
                                <c:choose>
                                    <c:when test="${customer.status == 'locked' or customer.lockUntil != null}">
                                        <span style="color: #e53e3e; font-weight: bold;">Đã khóa</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #38a169; font-weight: bold;">Hoạt động</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Vai trò:</span>
                            <span class="info-value">${customer.roleName}</span>
                        </div>

                        <form action="${pageContext.request.contextPath}/admin/customer-detail" method="post" class="role-selector">
                            <input type="hidden" name="id" value="${customer.id}">
                            <input type="hidden" name="action" value="changeRole">
                            <label for="newRoleId" style="margin-right: 5px; color: #718096; font-weight: 500;">Đổi vai trò:</label>
                            <select name="newRoleId" id="newRoleId">
                                <option value="1" ${customer.roleId == 1 ? 'selected' : ''}>Admin</option>
                                <option value="2" ${customer.roleId == 2 ? 'selected' : ''}>Khách hàng</option>
                            </select>
                            <button type="submit" onclick="return confirm('Bạn có chắc muốn thay đổi vai trò của người dùng này?');">Cập nhật</button>
                        </form>

                        <div style="margin-top: 20px; display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 10px;">
                            <a href="mailto:${customer.email}" style="text-align: center; padding: 8px; background: #3182ce; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none;">
                                Gửi Email
                            </a>

                            <form action="${pageContext.request.contextPath}/admin/customer-detail" method="post">
                                <input type="hidden" name="id" value="${customer.id}">
                                <c:choose>
                                    <c:when test="${customer.status == 'locked' or customer.lockUntil != null}">
                                        <input type="hidden" name="action" value="unlock">
                                        <button type="submit" style="width: 100%; padding: 8px; background: #38a169; color: white; border: none; border-radius: 4px; cursor: pointer;" onclick="return confirm('Bạn có chắc muốn mở khóa tài khoản này?');">
                                            Mở Khóa
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="action" value="lock">
                                        <button type="submit" style="width: 100%; padding: 16px; background: #e53e3e; color: white; border: none; border-radius: 4px; cursor: pointer;" onclick="return confirm('Bạn có chắc muốn khóa tài khoản này?');">
                                            Khóa TK
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </form>

                            <form action="${pageContext.request.contextPath}/admin/customer-detail" method="post">
                                <input type="hidden" name="id" value="${customer.id}">
                                <input type="hidden" name="action" value="resetPassword">
                                <button type="submit" style="width: 100%; padding: 8px; background: #dd6b20; color: white; border: none; border-radius: 4px; cursor: pointer;" onclick="return confirm('Bạn có chắc muốn đặt lại mật khẩu cho người dùng này?');">
                                    Reset Mật Khẩu
                                </button>
                            </form>
                        </div>
                    </div>

                    <div class="info-card">
                        <h3>Địa Chỉ (${addresses.size()})</h3>
                        <c:choose>
                            <c:when test="${not empty addresses}">
                                <c:forEach items="${addresses}" var="addr">
                                    <div class="address-item ${addr.isDefault() ? 'default' : ''}">
                                        <div style="font-weight: 600; margin-bottom: 4px;">
                                            ${addr.recipientName}
                                            <c:if test="${addr.isDefault()}"><span style="color: #38a169; font-size: 11px;">(Mặc định)</span></c:if>
                                        </div>
                                        <div style="font-size: 13px; color: #4a5568;">${addr.phone}</div>
                                        <div style="font-size: 13px; color: #4a5568;">
                                            ${addr.house_number}, ${addr.commune}, ${addr.district}
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p style="color: #718096; font-size: 14px;">Chưa có địa chỉ nào.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="right-column">
                    <div class="info-card">
                        <h3>Lịch Sử Đơn Hàng (${orders.size()})</h3>

                        <div class="table-container" style="box-shadow: none; padding: 0;">
                            <table style="width: 100%;">
                                <thead>
                                    <tr>
                                        <th>Mã ĐH</th>
                                        <th>Ngày đặt</th>
                                        <th>Trạng thái</th>
                                        <th>Tổng tiền</th>
                                        <th>Chi tiết</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty orders}">
                                            <c:forEach items="${orders}" var="o">
                                                <tr>
                                                    <td>#${o.id}</td>
                                                    <td>
                                                        ${o.orderDateFormatted}
                                                    </td>
                                                    <td>
                                                        <span class="status-badge
                                                            <c:choose>
                                                                <c:when test="${o.status == 'Đã nhận'}">status-pending</c:when>
                                                                <c:when test="${o.status == 'Đang vận chuyển'}">status-shipping</c:when>
                                                                <c:when test="${o.status == 'Đã giao hàng'}">status-delivered</c:when>
                                                                <c:when test="${o.status == 'Hủy đơn'}">status-cancelled</c:when>
                                                            </c:choose>
                                                        ">
                                                            ${o.status}
                                                        </span>
                                                    </td>
                                                    <td style="font-weight: 600;">
                                                        <fmt:formatNumber value="${o.total}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/admin/orders/detail?id=${o.id}" style="color: #3182ce; text-decoration: none;">Xem</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5" style="text-align: center; padding: 20px; color: #718096;">Khách hàng chưa có đơn hàng nào.</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

    </div>
</body>

</html>