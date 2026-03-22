<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <title>Tin Tức - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin_news.css">
</head>

<body>
<div class="container">

    <%@ include file="/views/layout/siderbar_admin.jsp" %>

    <div class="main-content">
        <div class="header">
            <h1>Quản Lý Tin Tức</h1>
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
        </div>

        <div class="table-container">
            <div class="table-header">
                <h2>Danh Sách Tin Tức</h2>
                <div class="search-box">
                    <form action="${pageContext.request.contextPath}/admin/news" method="get" style="display: flex; gap: 10px;">
                        <input type="text" name="search" placeholder="Tìm kiếm..." value="${param.search}">
                        <button type="submit" class="btn btn-primary">Tìm</button>
                    </form>
                    <button class="btn btn-primary" onclick="openModal()">
                        + Thêm tin tức mới
                    </button>
                </div>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Hình Ảnh</th>
                    <th>Tiêu Đề</th>
                    <th>Loại</th>
                    <th>Ngày Đăng</th>
                    <th>Chức Năng</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${not empty newsList}">
                        <c:forEach items="${newsList}" var="article">
                            <tr>
                                <td class="product-img">
                                    <a href="${pageContext.request.contextPath}/admin/news-content?articleId=${article.id}">
                                        <img src="${article.mainImg}" alt="${article.title}">
                                    </a>
                                </td>

                                <td>
                                    <div class="product-info">
                                        <a href="${pageContext.request.contextPath}/admin/news-content?articleId=${article.id}" class="article-title-link">
                                                ${article.title}
                                        </a>
                                    </div>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${article.feature}">Nổi bật</c:when>
                                        <c:otherwise>Thường</c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <fmt:formatDate value="${article.dateOfPosting}" pattern="dd/MM/yyyy"/>
                                </td>

                                <td>
                                   <div class="product-actions">
                                        <button class="btn btn-sm btn-edit" onclick="editNews('${article.id}', '${article.title}', '${article.categoryId}', '${article.mainImg}', '${article.description}', '${article.feature}')">Sửa</button>
                                        <form action="${pageContext.request.contextPath}/views/admin/news" method="post" style="display: inline;" onsubmit="return confirm('Bạn có chắc muốn xóa tin tức này?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${article.id}">
                                            <button type="submit" class="btn btn-sm btn-delete">Xóa</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 20px;">Không có tin tức nào.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!--thêm/sửa tin tức -->
<div id="productModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 id="modalTitle">Thêm Tin Tức Mới</h3>
            <button class="close-btn" onclick="closeModal()">×</button>
        </div>

        <form id="newsForm" action="${pageContext.request.contextPath}/views/admin/news" method="post">
            <input type="hidden" name="action" id="formAction" value="add">
            <input type="hidden" name="id" id="newsId">

            <div class="form-group">
                <label>Link Hình Ảnh</label>
                <input type="url" name="imageLink" id="imageLink" required>
            </div>

            <div class="form-group">
                <label>Tiêu Đề</label>
                <input type="text" name="newsTitle" id="newsTitle" required>
            </div>

            <div class="form-group">
                <label>Loại tin tức</label>
                <select name="newsCategory" id="newsCategory" required>
                    <option value="1">Nổi bật</option>
                    <option value="2">Thường</option>
                </select>
            </div>

            <div class="form-group">
                <label>Nội Dung</label>
                <textarea name="newsContent" id="newsContent" rows="5"
                          style="width: 100%; padding: 10px 15px; border: 1px solid #e2e8f0; border-radius: 8px; font-size: 14px;"
                          required></textarea>
            </div>

            <div class="form-actions">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu Tin</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openModal() {
        document.getElementById('productModal').classList.add('active');
        document.getElementById('modalTitle').innerText = 'Thêm Tin Tức Mới';
        document.getElementById('formAction').value = 'add';
        document.getElementById('newsId').value = '';
        document.getElementById('newsForm').reset();
    }

    function closeModal() {
        document.getElementById('productModal').classList.remove('active');
    }

    function editNews(id, title, categoryId, image, content, isFeature) {
        document.getElementById('productModal').classList.add('active');
        document.getElementById('modalTitle').innerText = 'Cập Nhật Tin Tức';
        document.getElementById('formAction').value = 'update';
        document.getElementById('newsId').value = id;

        document.getElementById('newsTitle').value = title;
        document.getElementById('newsCategory').value = isFeature === 'true' ? '1' : '2';
        document.getElementById('imageLink').value = image;
        document.getElementById('newsContent').value = content;
    }

    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const message = urlParams.get('message');
        if (message) {
            alert(decodeURIComponent(message));
            window.history.replaceState({}, document.title, window.location.pathname + window.location.search.replace(/[\?&]message=[^&]+/, '').replace(/^&/, '?'));
        }
    };
</script>
</body>

</html>