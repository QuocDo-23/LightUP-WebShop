let selectedFiles = [];
const MAX_IMAGES = 5;
const MAX_FILE_SIZE = 5 * 1024 * 1024;

function openReturnModal(orderId) {
    document.getElementById('returnModal').classList.add('show');
    document.getElementById('returnOrderId').value = orderId;
    selectedFiles = [];
    document.getElementById('returnForm').reset();
    document.getElementById('imagePreviewContainer').innerHTML = '';
    document.getElementById('imageErrorMessage').style.display = 'none';
    document.body.style.overflow = 'hidden';
}

function closeReturnModal() {
    document.getElementById('returnModal').classList.remove('show');
    selectedFiles = [];
    document.getElementById('returnForm').reset();
    document.getElementById('imagePreviewContainer').innerHTML = '';
    document.body.style.overflow = 'auto';
}

function handleImageSelect() {
    const input = document.getElementById('imageInput');
    const files = Array.from(input.files);
    const errorMsg = document.getElementById('imageErrorMessage');

    errorMsg.style.display = 'none';
    errorMsg.innerHTML = '';

    if (selectedFiles.length + files.length > MAX_IMAGES) {
        errorMsg.textContent = `Tối đa ${MAX_IMAGES} ảnh. Bạn đã chọn ${selectedFiles.length} ảnh.`;
        errorMsg.style.display = 'block';
        input.value = '';
        return;
    }

    let hasError = false;
    const validFiles = [];

    files.forEach(file => {
        if (!file.type.startsWith('image/')) {
            errorMsg.textContent = `File ${file.name} không phải là ảnh.`;
            errorMsg.style.display = 'block';
            hasError = true;
            return;
        }

        if (file.size > MAX_FILE_SIZE) {
            errorMsg.textContent = `File ${file.name} vượt quá 5MB.`;
            errorMsg.style.display = 'block';
            hasError = true;
            return;
        }

        validFiles.push(file);
    });

    if (hasError) {
        input.value = '';
        return;
    }

    selectedFiles = [...selectedFiles, ...validFiles];
    updateImagePreview();
    updateFileInput();
    input.value = '';
}

function updateImagePreview() {
    const container = document.getElementById('imagePreviewContainer');
    container.innerHTML = '';

    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();

        reader.onload = (e) => {
            const previewItem = document.createElement('div');
            previewItem.className = 'image-preview-item';
            previewItem.innerHTML = `
                <img src="${e.target.result}" alt="Preview">
                <button type="button" class="image-remove-btn" onclick="removeImage(${index})" title="Xóa">
                    <i class="bi bi-x"></i>
                </button>
            `;
            container.appendChild(previewItem);
        };

        reader.readAsDataURL(file);
    });
}

function removeImage(index) {
    selectedFiles.splice(index, 1);
    updateImagePreview();
    updateFileInput();
}

function updateFileInput() {
    const dataTransfer = new DataTransfer();

    selectedFiles.forEach(file => {
        dataTransfer.items.add(file);
    });

    document.getElementById('imageInput').files = dataTransfer.files;
}

document.addEventListener('DOMContentLoaded', () => {

    const uploadBox = document.querySelector('.upload-box');

    if (uploadBox) {
        uploadBox.addEventListener('dragover', (e) => {
            e.preventDefault();
            uploadBox.style.borderColor = '#007bff';
            uploadBox.style.backgroundColor = '#e7f1ff';
        });

        uploadBox.addEventListener('dragleave', () => {
            uploadBox.style.borderColor = '#ddd';
            uploadBox.style.backgroundColor = '#f8f9fa';
        });

        uploadBox.addEventListener('drop', (e) => {
            e.preventDefault();

            uploadBox.style.borderColor = '#ddd';
            uploadBox.style.backgroundColor = '#f8f9fa';

            document.getElementById('imageInput').files = e.dataTransfer.files;
            handleImageSelect();
        });
    }

    const returnForm = document.getElementById('returnForm');

    if (returnForm) {
        returnForm.addEventListener('submit', function (e) {
            e.preventDefault();

            const reason = document.getElementById('returnReason').value;
            const description = document.getElementById('returnDescription').value;
            const errorMsg = document.getElementById('imageErrorMessage');

            errorMsg.style.display = 'none';

            if (!reason) {
                errorMsg.textContent = 'Vui lòng chọn lý do trả hàng!';
                errorMsg.style.display = 'block';
                return;
            }

            if (description && (description.length < 10 || description.length > 500)) {
                errorMsg.textContent = 'Mô tả phải từ 10-500 ký tự!';
                errorMsg.style.display = 'block';
                return;
            }

            if (selectedFiles.length === 0) {
                errorMsg.textContent = 'Vui lòng đính kèm ít nhất một ảnh minh chứng!';
                errorMsg.style.display = 'block';
                return;
            }

            const submitBtn = document.getElementById('submitReturnBtn');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="bi bi-hourglass"></i> Đang gửi...';

            this.submit();
        });
    }

    document.addEventListener('keydown', (e) => {
        if (
            e.key === 'Escape' &&
            document.getElementById('returnModal').classList.contains('show')
        ) {
            closeReturnModal();
        }
    });

    const modal = document.getElementById('returnModal');

    if (modal) {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                closeReturnModal();
            }
        });
    }
});