<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <title>Chỉnh Sửa Sản Phẩm - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/products_setting.css" >
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin_editProducts.css">



</head>
<body>
<div class="container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">
        <div class="header">
            <h1>Chỉnh Sửa Sản Phẩm</h1>
            <div class="user-info">
                <div class="avatar">Q</div>
                <div>
                    <div style="font-weight: 600;">Admin</div>
                    <div style="font-size: 12px; color: #718096;">Quản trị viên</div>
                </div>
            </div>
        </div>

        <div class="form-container">
            <div class="form-header">
                <div>
                    <h2> Chỉnh Sửa Sản Phẩm</h2>
                    <div class="product-info-badge">
                        ID: #${product.id} • <strong>${product.name}</strong>
                    </div>
                </div>
                <a href="javascript:history.back()" class="btn btn-secondary">
                    ← Quay lại
                </a>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                     ${error}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/admin/products/edit">
                <input type="hidden" name="productId" value="${product.id}">

                <div class="form-section">
                    <div class="form-section-title"><i class="bi bi-person-check"></i>Thông Tin Cơ Bản</div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Tên Sản Phẩm <span class="required">*</span></label>
                            <input type="text" name="productName" required
                                   placeholder="VD: Đèn LED Downlight 7W"
                                   value="<c:out value='${product.name}'/>">
                        </div>
                        <div class="form-group">
                            <label>Danh Mục <span class="required">*</span></label>
                            <select name="categoryId" required>
                                <option value="">Chọn danh mục</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.id}"
                                        ${product.categoryId == cat.id ? 'selected' : ''}>
                                            ${cat.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row triple">
                        <div class="form-group">
                            <label>Giá (VNĐ) <span class="required">*</span></label>
                            <input type="number" name="price" required min="0"
                                   placeholder="0" value="${product.price}">
                        </div>
                        <div class="form-group">
                            <label>Số Lượng Tồn Kho <span class="required">*</span></label>
                            <input type="number" name="stock" required min="0"
                                   value="${product.inventoryQuantity}">
                        </div>
                        <div class="form-group">
                            <label>Đánh Giá Trung Bình</label>
                            <input type="number" name="review" min="0" max="5" step="0.1"
                                   placeholder="0.0" value="${product.review}">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Giảm Giá</label>
                            <select name="discountId">
                                <option value="">Không giảm giá</option>
                                <option value="1" ${product.discountId == 1 ? 'selected' : ''}>Giảm 10%</option>
                                <option value="2" ${product.discountId == 2 ? 'selected' : ''}>Giảm 20%</option>
                                <option value="3" ${product.discountId == 3 ? 'selected' : ''}>Giảm 30%</option>
                                <option value="4" ${product.discountId == 4 ? 'selected' : ''}>Giảm 50%</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Trạng Thái <span class="required">*</span></label>
                            <select name="status" required>
                                <option value="active" ${product.status == 'active' ? 'selected' : ''}>Đang bán</option>
                                <option value="inactive" ${product.status == 'inactive' ? 'selected' : ''}>Ngừng bán</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- PHẦN 2: HÌNH ẢNH -->
                <div class="form-section">
                    <div class="form-section-title"><i class="bi bi-image"></i> Hình Ảnh Sản Phẩm</div>

                    <div class="form-row full">
                        <div class="form-group">
                            <label>Link Hình Ảnh Sản Phẩm</label>

                            <!-- Ảnh cũ -->
                            <div id="imageInputs">
                                <c:forEach items="${product.images}" var="img">
                                    <input type="url"
                                           name="imageLinks"
                                           class="image-input"
                                           value="<c:out value='${img}'/>"
                                           onchange="previewImages()">
                                </c:forEach>

                                <c:if test="${empty product.images}">
                                    <input type="url"
                                           name="imageLinks"
                                           class="image-input"
                                           placeholder="https://example.com/image.jpg"
                                           onchange="previewImages()">
                                </c:if>
                            </div>

                            <button type="button" onclick="addImageInput()"><i class="bi bi-plus-circle"></i> Thêm hình ảnh</button>

                            <div class="image-preview-box" id="imagePreviewBox">
                                <p style="color:#718096">Xem trước hình ảnh sẽ hiển thị ở đây</p>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="form-section">
                    <div class="form-section-title"><i class="bi bi-wrench"></i> Chi Tiết Sản Phẩm</div>

                    <div class="form-row full">
                        <div class="form-group">
                            <label>Mô Tả Sản Phẩm</label>
                            <textarea name="description"
                                      placeholder="Nhập mô tả chi tiết về sản phẩm, tính năng, ưu điểm..."><c:out value="${product.description}"/></textarea>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Chất Liệu</label>
                            <input type="text" name="material"
                                   placeholder="VD: Nhôm, Thép không gỉ, Nhựa PC..."
                                   value="<c:out value='${product.material}'/>">
                        </div>
                        <div class="form-group">
                            <label>Điện Áp</label>
                            <input type="text" name="voltage"
                                   placeholder="VD: 220V, 12V, USB 5V..."
                                   value="<c:out value='${product.voltage}'/>">
                        </div>
                    </div>

                    <div class="form-row triple">
                        <div class="form-group">
                            <label>Kích Thước</label>
                            <input type="text" name="dimension"
                                   placeholder="VD: 60x60cm, Ø12xH20cm"
                                   value="<c:out value='${product.dimensions}'/>">
                        </div>
                        <div class="form-group">
                            <label>Loại Đèn</label>
                            <input type="text" name="type"
                                   placeholder="VD: Ốp trần, Âm trần, Tuýp..."
                                   value="<c:out value='${product.type}'/>">
                        </div>
                        <div class="form-group">
                            <label>Bảo Hành (Tháng)</label>
                            <input type="number" name="warranty" min="0"
                                   placeholder="VD: 12, 24, 36"
                                   value="${product.warranty}">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Màu Sắc / Nhiệt Độ Màu</label>
                            <input type="text" name="color"
                                   placeholder="VD: Trắng 6500K, Vàng ấm 3000K, RGB"
                                   value="<c:out value='${product.color}'/>">
                        </div>
                        <div class="form-group">
                            <label>Phong Cách Thiết Kế</label>
                            <input type="text" name="style"
                                   placeholder="VD: Hiện đại, Cổ điển, Công nghiệp"
                                   value="<c:out value='${product.style}'/>">
                        </div>
                    </div>
                </div>

                <!-- BUTTONS -->
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/admin/products"
                       class="btn btn-secondary">Hủy</a>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-floppy-fill"></i> Cập Nhật Sản Phẩm
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function previewImages() {
        const inputs = document.querySelectorAll('.image-input');
        const previewBox = document.getElementById('imagePreviewBox');

        previewBox.innerHTML = '';
        let hasImage = false;

        inputs.forEach(input => {
            const url = input.value.trim();
            if (url) {
                hasImage = true;
                const img = document.createElement('img');
                img.src = url;
                img.className = 'image-preview show';

                img.onerror = function () {
                    this.remove();
                    const p = document.createElement('p');
                    p.textContent = '❌ Không thể tải hình ảnh';
                    p.style.color = '#ef4444';
                    previewBox.appendChild(p);
                };

                previewBox.appendChild(img);
            }
        });

        if (!hasImage) {
            previewBox.innerHTML =
                '<p style="color:#718096">Xem trước hình ảnh sẽ hiển thị ở đây</p>';
        }
    }

    // Load preview khi mở trang edit
    window.onload = previewImages;

    function addImageInput() {
        const input = document.createElement('input');
        input.type = 'url';
        input.name = 'imageLinks';
        input.className = 'image-input';
        input.placeholder = 'https://example.com/image.jpg';
        input.onchange = previewImages;

        document.getElementById('imageInputs').appendChild(input);
    }
</script>

</body>
</html>