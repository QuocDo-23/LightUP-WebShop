<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <title>Danh Mục - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/categories.css">

</head>
<body>
<div class="container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">
        <div class="cat-page">

            <%-- PAGE HEADER --%>
            <div class="page-head">
                <div class="page-head-left">
                    <h1>Quản Lý Danh Mục</h1>
                    <p>Cấu trúc phân cấp sản phẩm — tối đa 3 cấp</p>
                </div>
                <div class="page-head-right">
                    <div class="avatar-chip">
                        <div class="avatar-circle">A</div>
                        <span>Admin</span>
                    </div>
                </div>
            </div>

            <%-- TOAST ALERTS --%>
            <c:if test="${not empty successMessage}">
                <div class="toast-alert success">
                    <i class="bi bi-check-circle-fill"></i>
                    <span>${successMessage}</span>
                </div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="toast-alert error">
                    <i class="bi bi-x-circle-fill"></i>
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <%-- STATS BAR --%>
            <c:set var="totalChildCount" value="0"/>
            <c:forEach var="entry" items="${childrenMap}">
                <c:set var="totalChildCount" value="${totalChildCount + fn:length(entry.value)}"/>
            </c:forEach>
            <c:set var="totalAll" value="${fn:length(rootCategories) + totalChildCount}"/>

            <div class="stats-bar">
                <div class="stat-card">
                    <div class="stat-icon amber"><i class="bi bi-grid-3x3-gap-fill"></i></div>
                    <div class="stat-info">
                        <h3>${totalAll}</h3>
                        <p>Tổng danh mục</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon amber"><i class="bi bi-lightning-charge-fill"></i></div>
                    <div class="stat-info">
                        <h3>${fn:length(rootCategories)}</h3>
                        <p>Cấp 1 (gốc)</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon blue"><i class="bi bi-diagram-2-fill"></i></div>
                    <div class="stat-info">
                        <h3>${totalChildCount}</h3>
                        <p>Cấp 2 / Cấp 3</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon green"><i class="bi bi-lamp-fill"></i></div>
                    <div class="stat-info">
                        <h3 id="statWithProducts">–</h3>
                        <p>Có sản phẩm</p>
                    </div>
                </div>
            </div>

            <%-- TWO COLUMN --%>
            <div class="cat-layout">

                <%-- ═══ LEFT: TREE PANEL ═══ --%>
                <div class="panel">
                    <div class="panel-header">
                        <h2><i class="bi bi-diagram-3" style="color:#f59e0b;margin-right:8px;"></i>Cây Danh Mục</h2>
                        <span class="meta">${totalAll} mục</span>
                    </div>
                    <div class="panel-body">

                        <div class="tree-toolbar">
                            <div class="search-wrap">
                                <i class="bi bi-search"></i>
                                <input type="text" id="catSearch" placeholder="Tìm tên danh mục…"
                                       oninput="filterCategories(this.value)">
                            </div>
                            <button class="btn-ghost" onclick="expandAll()">
                                <i class="bi bi-arrows-expand"></i> Mở rộng
                            </button>
                            <button class="btn-ghost" onclick="collapseAll()">
                                <i class="bi bi-arrows-collapse"></i> Thu gọn
                            </button>
                        </div>

                        <c:choose>
                            <c:when test="${empty rootCategories}">
                                <div class="empty-tree">
                                    <i class="bi bi-folder-x"></i>
                                    <p>Chưa có danh mục nào.<br>Hãy thêm danh mục đầu tiên!</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <ul class="cat-tree" id="catTree">
                                    <c:forEach var="root" items="${rootCategories}">
                                        <c:set var="rootChildren" value="${childrenMap[root.id]}"/>
                                        <c:set var="hasRootCh" value="${not empty rootChildren}"/>

                                        <li class="cat-node" data-id="${root.id}" data-level="root">
                                            <div class="cat-row" data-name="${fn:toLowerCase(root.name)}">
                                                <span class="cat-toggle ${hasRootCh ? '' : 'leaf'}"
                                                      onclick="${hasRootCh ? 'toggleNode(this)' : ''}">
                                                    <c:choose>
                                                        <c:when test="${hasRootCh}"><i class="bi bi-chevron-right"></i></c:when>
                                                        <c:otherwise><i class="bi bi-dot"></i></c:otherwise>
                                                    </c:choose>
                                                </span>
                                                <span class="cat-level-line"></span>
                                                <div class="cat-name-wrap">
                                                    <div class="cat-name">${root.name}
                                                        <c:if test="${root.sortOrder > 0}">
                                                            <span class="sort-chip">#${root.sortOrder}</span>
                                                        </c:if>
                                                    </div>
                                                    <c:if test="${hasRootCh}">
                                                        <div class="cat-sub-info">${fn:length(rootChildren)} danh mục con</div>
                                                    </c:if>
                                                </div>
                                                <span class="lvl-badge lvl-1">Cấp 1</span>
                                                <div class="cat-actions">
                                                    <button class="act-btn add" title="Thêm con"
                                                            data-id="${root.id}" data-name="${root.name}"
                                                            onclick="btnAddSub(this)">
                                                        <i class="bi bi-plus-lg"></i>
                                                    </button>
                                                    <button class="act-btn edit" title="Sửa"
                                                            data-id="${root.id}" data-name="${root.name}"
                                                            data-parent="${root.parentId != null ? root.parentId : ''}"
                                                            data-sort="${root.sortOrder}"
                                                            onclick="btnEdit(this)">
                                                        <i class="bi bi-pencil"></i>
                                                    </button>
                                                    <button class="act-btn del" title="Xóa"
                                                            data-id="${root.id}" data-name="${root.name}"
                                                            onclick="btnDelete(this)">
                                                        <i class="bi bi-trash3"></i>
                                                    </button>
                                                </div>
                                            </div>

                                            <c:if test="${hasRootCh}">
                                                <ul class="cat-children">
                                                    <c:forEach var="child" items="${rootChildren}">
                                                        <c:set var="grandChildren" value="${childrenMap[child.id]}"/>
                                                        <c:set var="hasGrandCh" value="${not empty grandChildren}"/>

                                                        <li class="cat-node" data-id="${child.id}" data-level="child">
                                                            <div class="cat-row" data-name="${fn:toLowerCase(child.name)}">
                                                                <span class="cat-toggle ${hasGrandCh ? '' : 'leaf'}"
                                                                      onclick="${hasGrandCh ? 'toggleNode(this)' : ''}">
                                                                    <c:choose>
                                                                        <c:when test="${hasGrandCh}"><i class="bi bi-chevron-right"></i></c:when>
                                                                        <c:otherwise><i class="bi bi-dot"></i></c:otherwise>
                                                                    </c:choose>
                                                                </span>
                                                                <span class="cat-level-line"></span>
                                                                <div class="cat-name-wrap">
                                                                    <div class="cat-name">${child.name}
                                                                        <c:if test="${child.sortOrder > 0}">
                                                                            <span class="sort-chip">#${child.sortOrder}</span>
                                                                        </c:if>
                                                                    </div>
                                                                    <c:if test="${hasGrandCh}">
                                                                        <div class="cat-sub-info">${fn:length(grandChildren)} danh mục con</div>
                                                                    </c:if>
                                                                </div>
                                                                <span class="lvl-badge lvl-2">Cấp 2</span>
                                                                <div class="cat-actions">
                                                                    <button class="act-btn add" title="Thêm con"
                                                                            data-id="${child.id}" data-name="${child.name}"
                                                                            onclick="btnAddSub(this)">
                                                                        <i class="bi bi-plus-lg"></i>
                                                                    </button>
                                                                    <button class="act-btn edit" title="Sửa"
                                                                            data-id="${child.id}" data-name="${child.name}"
                                                                            data-parent="${child.parentId != null ? child.parentId : ''}"
                                                                            data-sort="${child.sortOrder}"
                                                                            onclick="btnEdit(this)">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </button>
                                                                    <button class="act-btn del" title="Xóa"
                                                                            data-id="${child.id}" data-name="${child.name}"
                                                                            onclick="btnDelete(this)">
                                                                        <i class="bi bi-trash3"></i>
                                                                    </button>
                                                                </div>
                                                            </div>

                                                            <c:if test="${hasGrandCh}">
                                                                <ul class="cat-children">
                                                                    <c:forEach var="grand" items="${grandChildren}">
                                                                        <li class="cat-node" data-id="${grand.id}" data-level="grand">
                                                                            <div class="cat-row" data-name="${fn:toLowerCase(grand.name)}">
                                                                                <span class="cat-toggle leaf">
                                                                                    <i class="bi bi-dot"></i>
                                                                                </span>
                                                                                <span class="cat-level-line"></span>
                                                                                <div class="cat-name-wrap">
                                                                                    <div class="cat-name">${grand.name}
                                                                                        <c:if test="${grand.sortOrder > 0}">
                                                                                            <span class="sort-chip">#${grand.sortOrder}</span>
                                                                                        </c:if>
                                                                                    </div>
                                                                                </div>
                                                                                <span class="lvl-badge lvl-3">Cấp 3</span>
                                                                                <div class="cat-actions">
                                                                                    <button class="act-btn edit" title="Sửa"
                                                                                            data-id="${grand.id}" data-name="${grand.name}"
                                                                                            data-parent="${grand.parentId != null ? grand.parentId : ''}"
                                                                                            data-sort="${grand.sortOrder}"
                                                                                            onclick="btnEdit(this)">
                                                                                        <i class="bi bi-pencil"></i>
                                                                                    </button>
                                                                                    <button class="act-btn del" title="Xóa"
                                                                                            data-id="${grand.id}" data-name="${grand.name}"
                                                                                            onclick="btnDelete(this)">
                                                                                        <i class="bi bi-trash3"></i>
                                                                                    </button>
                                                                                </div>
                                                                            </div>
                                                                        </li>
                                                                    </c:forEach>
                                                                </ul>
                                                            </c:if>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </c:if>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>

                <%-- ═══ RIGHT: FORM PANEL ═══ --%>
                <div class="panel" id="catFormPanel">
                    <div class="form-panel-header">
                        <div class="form-mode-icon add" id="formModeIcon">
                            <i class="bi bi-plus-lg" id="formModeIco"></i>
                        </div>
                        <div>
                            <div class="form-mode-title" id="formTitle">Thêm Danh Mục</div>
                            <div class="form-mode-sub" id="formSub">Tạo danh mục mới</div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <form id="catForm" onsubmit="submitCatForm(event)">
                            <input type="hidden" id="formMode" value="add">
                            <input type="hidden" id="editCategoryId" value="">

                            <%-- Image upload --%>
                            <div class="f-group">
                                <label class="f-label">Hình Ảnh</label>
                                <div class="upload-area" id="uploadBox"
                                     ondrop="handleDrop(event)"
                                     ondragover="handleDragOver(event)"
                                     ondragleave="handleDragLeave(event)"
                                     onclick="document.getElementById('categoryImage').click()">
                                    <input type="file" id="categoryImage" name="categoryImage"
                                           accept="image/*" onchange="previewImage(event)">
                                    <i class="bi bi-cloud-arrow-up"></i>
                                    <span><strong>Chọn file</strong> hoặc kéo thả vào đây<br>
                                        <small>JPG, PNG, WEBP — tối đa 2MB</small>
                                    </span>
                                </div>
                                <div id="imagePreview" class="img-preview">
                                    <img id="previewImg" alt="Preview">
                                    <div><button type="button" class="rm-btn" onclick="removeImage()">
                                        <i class="bi bi-x"></i> Xóa hình
                                    </button></div>
                                </div>
                            </div>

                            <%-- Name --%>
                            <div class="f-group">
                                <label class="f-label" for="categoryName">
                                    Tên Danh Mục <span class="req">*</span>
                                </label>
                                <input type="text" id="categoryName" name="categoryName"
                                       class="f-input" placeholder="VD: Đèn LED, Đèn Thông Minh…"
                                       maxlength="100" required>
                                <div class="f-hint">Tối đa 100 ký tự</div>
                            </div>

                            <%-- Parent --%>
                            <div class="f-group">
                                <label class="f-label" for="parentId">Danh Mục Cha</label>
                                <div class="select-wrap">
                                    <select id="parentId" name="parentId" class="f-select">
                                        <option value="">— Cấp 1 (không có cha) —</option>
                                        <c:forEach var="root" items="${rootCategories}">
                                            <option value="${root.id}">[Cấp 1] ${root.name}</option>
                                            <c:set var="rootCh" value="${childrenMap[root.id]}"/>
                                            <c:if test="${not empty rootCh}">
                                                <c:forEach var="ch" items="${rootCh}">
                                                    <option value="${ch.id}">&nbsp;&nbsp;&nbsp;↳ [Cấp 2] ${ch.name}</option>
                                                </c:forEach>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="f-hint">Chọn cha → danh mục mới sẽ là cấp con của nó</div>
                            </div>

                            <%-- Sort order --%>
                            <div class="f-group">
                                <label class="f-label" for="sortOrder">Thứ Tự Hiển Thị</label>
                                <input type="number" id="sortOrder" name="sortOrder"
                                       class="f-input" placeholder="0" min="0" max="9999" value="0">
                                <div class="f-hint">Số nhỏ hơn hiển thị trước</div>
                            </div>

                            <div id="formAlert" class="f-alert"></div>

                            <div class="f-actions">
                                <button type="submit" class="btn-primary" id="submitBtn">
                                    <i class="bi bi-check-lg"></i>
                                    <span id="submitLabel">Thêm Danh Mục</span>
                                </button>
                                <button type="button" class="btn-secondary-sm" onclick="resetForm()">
                                    <i class="bi bi-arrow-counterclockwise"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

            </div><%-- /cat-layout --%>
        </div><%-- /cat-page --%>
    </div><%-- /main-content --%>
</div><%-- /container --%>


<%-- ═══════════════ DELETE MODAL ═══════════════ --%>
<div class="modal-overlay" id="deleteModal">
    <div class="modal-box">

        <%-- Simple delete --%>
        <div id="deleteSimple">
            <div class="modal-icon danger"><i class="bi bi-trash3-fill"></i></div>
            <h3>Xóa danh mục?</h3>
            <p>Bạn sắp xóa danh mục <strong id="deleteSimpleName"></strong>.<br>
                Hành động này <strong>không thể hoàn tác</strong>.</p>
            <div class="modal-actions">
                <button class="btn-modal-secondary" onclick="closeDeleteModal()">Huỷ</button>
                <button class="btn-modal-danger" onclick="doDelete()">
                    <i class="bi bi-trash3"></i> Xóa ngay
                </button>
            </div>
        </div>

        <%-- Has products --%>
        <div id="deleteHasProducts" style="display:none;">
            <div class="modal-icon warning"><i class="bi bi-exclamation-triangle-fill"></i></div>
            <h3>Danh mục có sản phẩm</h3>
            <p>Danh mục <strong id="deleteHasProdName"></strong> đang chứa sản phẩm.<br>
                Chọn danh mục đích để chuyển toàn bộ trước khi xóa:</p>
            <div class="modal-select-wrap">
                <select id="targetCategorySelect">
                    <option value="">— Chọn danh mục đích —</option>
                    <c:forEach var="root" items="${rootCategories}">
                        <option value="${root.id}">[Cấp 1] ${root.name}</option>
                        <c:set var="rCh" value="${childrenMap[root.id]}"/>
                        <c:if test="${not empty rCh}">
                            <c:forEach var="ch" items="${rCh}">
                                <option value="${ch.id}">&nbsp;&nbsp;&nbsp;↳ [Cấp 2] ${ch.name}</option>
                            </c:forEach>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <div class="modal-actions">
                <button class="btn-modal-secondary" onclick="closeDeleteModal()">Huỷ</button>
                <button class="btn-modal-danger" onclick="doDeleteMoveProducts()">
                    <i class="bi bi-arrow-right-circle"></i> Chuyển & Xóa
                </button>
            </div>
        </div>

        <%-- Has children --%>
        <div id="deleteHasChildren" style="display:none;">
            <div class="modal-icon block"><i class="bi bi-diagram-3-fill"></i></div>
            <h3>Không thể xóa</h3>
            <p>Danh mục <strong id="deleteHasChildName"></strong> đang có danh mục con.<br>
                Vui lòng xóa hoặc di chuyển các danh mục con trước.</p>
            <div class="modal-actions">
                <button class="btn-modal-secondary" style="flex:1;" onclick="closeDeleteModal()">
                    <i class="bi bi-x-lg"></i> Đóng
                </button>
            </div>
        </div>

        <%-- Loading --%>
        <div id="deleteLoading" style="display:none;">
            <div class="modal-spinner">
                <div class="spinner-ring"></div>
                <p>Đang xử lý…</p>
            </div>
        </div>

    </div>
</div>


<script>
    const CTX = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/views/JS/admin_categories.js" defer></script>

</body>
</html>
