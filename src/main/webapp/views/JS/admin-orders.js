let currentOrderId = null;

// Lấy contextPath từ JSP
const contextPath = document.body.dataset.contextPath;

// Lọc theo trạng thái
function filterByStatus(status) {
    window.location.href =
        contextPath + '/admin/orders?status=' + status;
}

// Xem đánh giá
function viewReview(orderId, customerName) {
    fetch(contextPath + '/admin/orders/review?id=' + orderId)
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById('modalOrderId').textContent = data.orderId;
                document.getElementById('modalCustomer').textContent =
                    data.userName || customerName;

                let stars = '';
                for (let i = 0; i < 5; i++) {
                    stars += i < data.rating
                        ? '<i class="fas fa-star" style="color:#f39c12"></i>'
                        : '<i class="far fa-star" style="color:#cbd5e0"></i>';
                }

                document.getElementById('modalRating').innerHTML = stars;
                document.getElementById('modalNote').textContent =
                    data.content || 'Không có ghi chú';

                document.getElementById('reviewModal').style.display = 'block';
            } else {
                alert(data.message);
            }
        })
        .catch(() => alert('Lỗi tải đánh giá'));
}

function showUpdateStatusModal(orderId, currentStatus) {
    currentOrderId = orderId;
    document.getElementById('statusOrderId').textContent = '#DH' + String(orderId).padStart(3, '0');

    let statusText = '';
    switch(currentStatus) {
        case 'pending': statusText = 'Chờ xác nhận'; break;
        case 'processing': statusText = 'Đang xử lý'; break;
        case 'shipped': statusText = 'Đang giao'; break;
        case 'delivered': statusText = 'Đã giao'; break;
        case 'cancelled': statusText = 'Đã hủy'; break;
        default: statusText = currentStatus;
    }
    document.getElementById('statusCurrent').textContent = statusText;
    document.getElementById('statusSelect').value = '';
    document.getElementById('statusModal').style.display = 'block';
}

// Xác nhận cập nhật trạng thái
function confirmUpdateStatus() {
    const newStatus = document.getElementById('statusSelect').value;
    if (!newStatus) return alert('Vui lòng chọn trạng thái');

    fetch(contextPath + '/admin/orders/updateStatus', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body:
            'action=updateStatus&orderId=' +
            currentOrderId +
            '&status=' +
            newStatus
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert('Cập nhật thành công');
                location.reload();
            } else alert(data.message);
        })
        .catch(() => alert('Lỗi cập nhật'));
}

// Đóng modal
function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

window.onclick = function (e) {
    if (e.target.classList.contains('modal')) {
        e.target.style.display = 'none';
    }
};

// Select all
function toggleSelectAll(cb) {
    document.querySelectorAll('.order-checkbox')
        .forEach(c => c.checked = cb.checked);
    updateBulkActions();
}

// Update bulk bar
function updateBulkActions() {
    const checked =
        document.querySelectorAll('.order-checkbox:checked').length;
    document.getElementById('selectedCount').textContent = checked;

    document.getElementById('bulkActions')
        .classList.toggle('show', checked > 0);
}

// Clear chọn
function clearSelection() {
    document.getElementById('selectAll').checked = false;
    document.querySelectorAll('.order-checkbox')
        .forEach(c => c.checked = false);
    updateBulkActions();
}

// Bulk update
function bulkUpdateStatus() {
    const status = document.getElementById('bulkStatus').value;
    if (!status) return alert('Chọn trạng thái');

    const ids = Array.from(
        document.querySelectorAll('.order-checkbox:checked')
    ).map(cb => cb.value);

    if (!ids.length) return alert('Chưa chọn đơn');

    if (!confirm(`Cập nhật ${ids.length} đơn?`)) return;

    const params = new URLSearchParams();
    params.append('action', 'bulkUpdate');
    params.append('status', status);
    ids.forEach(id => params.append('orderIds[]', id));

    fetch(contextPath + '/admin/orders/bulkUpdate', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    })
        .then(res => res.json())
        .then(d => d.success ? location.reload() : alert(d.message))
        .catch(() => alert('Lỗi bulk update'));
}

// Export
function exportOrders() {
    const params = new URLSearchParams(location.search);
    params.set('action', 'export');
    location.href = contextPath + '/admin/orders?' + params;
}
