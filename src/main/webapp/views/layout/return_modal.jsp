<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="returnModal" class="modal">
    <div class="modal-content return-modal-content">
        <div class="modal-header">
            <h2>Yêu cầu trả hàng</h2>
            <span class="close-modal" onclick="closeReturnModal()">&times;</span>
        </div>

        <form id="returnForm" action="return-order" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="orderId" id="returnOrderId">

            <div class="form-group">
                <label for="returnReason" class="form-label">
                    Lý do trả hàng <span class="required">*</span>
                </label>
                <select id="returnReason" name="reason" class="form-control" required>
                    <option value="">-- Chọn lý do --</option>
                    <option value="defective">Sản phẩm lỗi</option>
                    <option value="wrong_item">Sai sản phẩm</option>
                    <option value="damaged">Hàng bị hư hỏng</option>
                    <option value="not_as_described">Không đúng mô tả</option>
                    <option value="changed_mind">Thay đổi ý định</option>
                    <option value="other">Lý do khác</option>
                </select>
            </div>

            <div class="form-group">
                <label for="returnDescription" class="form-label">Mô tả chi tiết</label>
                <textarea id="returnDescription" name="description" class="form-control" 
                          rows="4" placeholder="Vui lòng mô tả chi tiết lý do trả hàng..."></textarea>
                <small class="form-text">Tối thiểu 10 ký tự, tối đa 500 ký tự</small>
            </div>

            <div class="form-group">
                <label class="form-label">
                    Đính kèm ảnh minh chứng <span class="required">*</span>
                </label>
                <div class="image-upload-area">
                    <div class="upload-box" onclick="document.getElementById('imageInput').click()">
                        <i class="bi bi-cloud-arrow-up"></i>
                        <p>Nhấp để chọn ảnh hoặc kéo thả ảnh vào đây</p>
                        <small>Chỉ hỗ trợ: JPG, PNG, GIF, WebP (Tối đa 5MB/ảnh)</small>
                    </div>
                    <input type="file" id="imageInput" name="evidence_image" accept="image/*" 
                           style="display:none" multiple onchange="handleImageSelect()">
                </div>

                <!-- Preview ảnh đã chọn -->
                <div id="imagePreviewContainer" class="image-preview-container"></div>
                <div id="imageErrorMessage" class="error-message" style="display:none;"></div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeReturnModal()">
                    Hủy
                </button>
                <button type="submit" class="btn btn-primary" id="submitReturnBtn">
                    <i class="bi bi-send"></i> Gửi yêu cầu
                </button>
            </div>
        </form>
    </div>
</div>

