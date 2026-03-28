<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Sản Phẩm - ${product.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/products_setting.css" >
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin_products.css">

</head>
<body>
<div class="container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">

        <div class="form-container">
            <div class="form-header">
                <h1>👁️ Chi Tiết Sản Phẩm</h1>
                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/admin/products/edit?id=${product.id}"
                       class="btn btn-primary">✏️ Chỉnh sửa</a>
                    <a href="${pageContext.request.contextPath}/admin/products"
                       class="btn btn-secondary">← Quay lại</a>
                </div>
            </div>

            <div class="product-image-gallery">
                <c:forEach items="${product.images}" var="img">
                    <img src="${img}"
                         alt="${product.name}"
                         class="product-image"/>
                </c:forEach>
            </div>


            <div class="form-section">
                <div class="form-section-title">📋 Thông Tin Cơ Bản</div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Tên Sản Phẩm: </label>
                        <input type="text" name="productName" required
                               value="${product.name}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Danh Mục: </label>
                        <select name="categoryId" disabled>
                            <option value="">${product.categoryName}</option>
                        </select>
                    </div>
                </div>

                <div class="form-row triple">
                    <div class="form-group">
                        <label>Giá (VNĐ)</label>
                        <input type="number" name="price"
                               value="${product.price}" readonly>
                    </div>

                    <div class="form-group">
                        <label>Số Lượng Tồn Kho:</label>
                        <input type="number" name="stock"
                               value="${product.inventoryQuantity != null ? product.inventoryQuantity : 0}"
                               readonly>
                    </div>

                    <div class="form-group">
                        <label>Đánh Giá Trung Bình</label>
                        <input type="number" name="review"
                               value="${product.review}"
                               readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Giảm Giá</label>
                        <select name="discountId"  disabled>
                            <option value="">${product.discountRate}</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Trạng Thái </label>
                        <select name="status" disabled>
                            <option value="" >${product.status} </option>
                        </select>
                    </div>
                </div>
            </div>


            <div class="form-section">
                <div class="form-section-title">🔧 Chi Tiết Sản Phẩm</div>

                <div class="form-row full">
                    <div class="form-group">
                        <label>Mô Tả Sản Phẩm</label>
                        <textarea name="description" readonly>${product.description}</textarea>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Chất Liệu</label>
                        <input type="text" name="material" value="${product.material}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Điện Áp</label>
                        <input type="text" name="voltage" value="${product.voltage}" readonly>
                    </div>
                </div>

                <div class="form-row triple">
                    <div class="form-group">
                        <label>Kích Thước</label>
                        <input type="text" name="dimension" value="${product.dimensions}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Loại Đèn</label>
                        <input type="text" name="type" value="${product.type}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Bảo Hành (Tháng)</label>
                        <input type="text" name="warranty" value="${product.warranty}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Màu Sắc / Nhiệt Độ Màu</label>
                        <input type="text" name="color" value="${product.color}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Phong Cách Thiết Kế</label>
                        <input type="text" name="style" value="${product.style}" readonly>
                    </div>
                </div>
            </div>
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/products"
                   class="btn btn-secondary">Đóng</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>