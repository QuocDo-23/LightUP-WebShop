    let currentOrderId = null;
const contextPath = document.body.dataset.contextPath;

function showUpdateStatusModal(orderId) {
    currentOrderId = orderId;
    document.getElementById('statusModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('statusModal').style.display = 'none';
}

function updateStatus() {
    const newStatus = document.getElementById('newStatus').value;
    if (!newStatus) {
        alert('Vui lòng chọn trạng thái');
        return;
    }

    fetch(contextPath + '/admin/orders/updateStatus', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body:
            'action=updateStatus&orderId=' +
            currentOrderId +
            '&status=' +
            newStatus
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert('Cập nhật thành công!');
                location.reload();
            } else {
                alert(data.message);
            }
        })
        .catch(() => alert('Có lỗi xảy ra'));
}

window.onclick = function (e) {
    if (e.target.classList.contains('modal')) {
        e.target.style.display = 'none';
    }
};
