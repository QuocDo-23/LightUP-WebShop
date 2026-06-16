function toggleNode(toggleEl) {
    const node     = toggleEl.closest('.cat-node');
    const children = node.querySelector(':scope > .cat-children');
    if (!children) return;
    const open = children.style.display !== 'none';
    children.style.display = open ? 'none' : 'block';
    toggleEl.classList.toggle('open', !open);
}

function expandAll() {
    document.querySelectorAll('.cat-children').forEach(el => el.style.display = 'block');
    document.querySelectorAll('.cat-toggle:not(.leaf)').forEach(el => el.classList.add('open'));
}

function collapseAll() {
    document.querySelectorAll('.cat-children').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.cat-toggle:not(.leaf)').forEach(el => el.classList.remove('open'));
}


function filterCategories(keyword) {
    const kw   = keyword.trim().toLowerCase();
    const rows = document.querySelectorAll('#catTree .cat-row');

    if (!kw) {
        rows.forEach(r => r.classList.remove('hidden', 'matched'));
        collapseAll();
        return;
    }

    rows.forEach(r => {
        const name  = r.dataset.name || '';
        const match = name.includes(kw);
        r.classList.toggle('matched', match);
        r.classList.toggle('hidden', !match);
    });


    document.querySelectorAll('.cat-row.matched').forEach(row => {
        let parent = row.parentElement; // li.cat-node
        while (parent) {
            if (parent.classList.contains('cat-children')) {
                parent.style.display = 'block';
                const toggle = parent.previousElementSibling
                    ?.querySelector('.cat-toggle');
                if (toggle) toggle.classList.add('open');
            }
            parent = parent.parentElement;
        }
    });
}



function btnAddSub(btn) {
    openAddForm(btn.dataset.id, btn.dataset.name);
}

function btnEdit(btn) {
    openEditForm(
        btn.dataset.id,
        btn.dataset.name,
        btn.dataset.parent || '',
        btn.dataset.sort || 0
    );
}

function btnDelete(btn) {
    confirmDelete(btn.dataset.id, btn.dataset.name);
}


function fillName(text) {
    const input = document.getElementById('categoryName');
    input.value = text;
    input.focus();
}

function resetForm() {
    document.getElementById('catForm').reset();
    document.getElementById('formMode').value        = 'add';
    document.getElementById('editCategoryId').value  = '';
    document.getElementById('categoryName').value    = '';
    document.getElementById('parentId').value        = '';
    document.getElementById('sortOrder').value       = '0';
    document.getElementById('categoryImage').value   = '';
    document.getElementById('imagePreview').style.display = 'none';

    document.getElementById('formTitle').innerHTML =
        '<i class="bi bi-plus-circle" style="color:#059669;"></i> Thêm Danh Mục '
        + '<span class="form-badge badge-add" id="formBadge">Mới</span>';

    document.getElementById('submitLabel').textContent = 'Thêm Danh Mục';
    hideFormAlert();
}

function openAddForm(parentId, parentName) {
    resetForm();
    if (parentId) {
        document.getElementById('parentId').value = parentId;
        document.getElementById('formTitle').innerHTML =
            '<i class="bi bi-plus-circle" style="color:#059669;"></i> Thêm Danh Mục Con '
            + '<span class="form-badge badge-add">Mới</span>';
    }
    document.getElementById('catFormPanel').scrollIntoView({ behavior: 'smooth' });
    document.getElementById('categoryName').focus();
}

function openEditForm(id, name, parentId, sortOrder) {
    document.getElementById('formMode').value       = 'edit';
    document.getElementById('editCategoryId').value = id;
    document.getElementById('categoryName').value   = name;
    document.getElementById('parentId').value       = parentId || '';
    document.getElementById('sortOrder').value      = sortOrder || 0;
    document.getElementById('categoryImage').value  = '';
    document.getElementById('imagePreview').style.display = 'none';

    document.getElementById('formTitle').innerHTML =
        '<i class="bi bi-pencil-square" style="color:#1d4ed8;"></i> Sửa Danh Mục '
        + '<span class="form-badge badge-edit" id="formBadge">Chỉnh sửa</span>';

    document.getElementById('submitLabel').textContent = 'Cập Nhật';
    hideFormAlert();

    document.getElementById('catFormPanel').scrollIntoView({ behavior: 'smooth' });
    document.getElementById('categoryName').focus();
}


function previewImage(event) {
    const file = event.target.files[0];
    if (!file) return;

    if (!file.type.startsWith('image/')) {
        showFormAlert('error', 'Vui lòng chọn file hình ảnh!');
        event.target.value = '';
        return;
    }

    if (file.size > 2 * 1024 * 1024) {
        showFormAlert('error', 'File quá lớn! Tối đa 2MB');
        event.target.value = '';
        return;
    }

    const reader = new FileReader();
    reader.onload = function(e) {
        const preview = document.getElementById('imagePreview');
        const img = document.getElementById('previewImg');
        img.src = e.target.result;
        preview.style.display = 'block';
    };
    reader.readAsDataURL(file);
}

function removeImage() {
    document.getElementById('categoryImage').value = '';
    document.getElementById('imagePreview').style.display = 'none';
}

function handleDragOver(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById('uploadBox').classList.add('dragover');
}

function handleDragLeave(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById('uploadBox').classList.remove('dragover');
}

function handleDrop(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById('uploadBox').classList.remove('dragover');

    const files = event.dataTransfer.files;
    if (files.length > 0) {
        document.getElementById('categoryImage').files = files;
        // Kích hoạt event change để preview
        const fileInput = document.getElementById('categoryImage');
        const event2 = new Event('change', { bubbles: true });
        fileInput.dispatchEvent(event2);
    }
}


async function submitCatForm(e) {
    e.preventDefault();

    const mode      = document.getElementById('formMode').value;
    const name      = document.getElementById('categoryName').value.trim();
    const parentId  = document.getElementById('parentId').value;
    const sortOrder = document.getElementById('sortOrder').value;
    const imageFile = document.getElementById('categoryImage').files[0];

    if (!name) {
        showFormAlert('error', 'Vui lòng nhập tên danh mục.');
        return;
    }

    const btn = document.getElementById('submitBtn');
    btn.disabled  = true;
    btn.innerHTML = '<span class="spinner"></span> Đang xử lý...';

    const url  = mode === 'edit'
        ? CTX + '/admin/categories/edit'
        : CTX + '/admin/categories/add';

    const formData = new FormData();
    formData.append('ajax', 'true');
    formData.append('categoryName', name);
    if (parentId) formData.append('parentId', parentId);
    formData.append('sortOrder', sortOrder);
    if (imageFile) formData.append('categoryImage', imageFile);
    if (mode === 'edit') {
        formData.append('categoryId', document.getElementById('editCategoryId').value);
    }

    try {
        const res  = await fetch(url, {
            method:  'POST',
            body:    formData  // ✅ Gửi FormData thay vì URLSearchParams
        });

        if (!res.ok) {
            throw new Error('HTTP error, status = ' + res.status);
        }

        const data = await res.json();

        if (data.success) {
            showFormAlert('success',
                mode === 'edit'
                    ? 'Cập nhật danh mục thành công!'
                    : 'Thêm danh mục thành công!');
            setTimeout(() => location.reload(), 900);
        } else {
            showFormAlert('error', data.message || 'Có lỗi xảy ra.');
        }
    } catch (err) {
        console.error('Error:', err);
        showFormAlert('error', 'Lỗi kết nối máy chủ: ' + err.message);
    } finally {
        btn.disabled  = false;
        btn.innerHTML = '<i class="bi bi-check-lg"></i> <span id="submitLabel">'
            + (mode === 'edit' ? 'Cập Nhật' : 'Thêm Danh Mục') + '</span>';
    }
}

function showFormAlert(type, msg) {
    const el      = document.getElementById('formAlert');
    el.className  = 'alert ' + (type === 'success' ? 'alert-success' : 'alert-error');
    el.innerHTML  = (type === 'success'
        ? '<i class="bi bi-check-circle-fill"></i>'
        : '<i class="bi bi-x-circle-fill"></i>') + ' ' + msg;
    el.style.display = 'flex';
}

function hideFormAlert() {
    document.getElementById('formAlert').style.display = 'none';
}


let _deleteId   = null;
let _deleteName = '';

async function confirmDelete(id, name) {
    _deleteId   = id;
    _deleteName = name;

    setDeleteState('loading');
    document.getElementById('deleteModal').classList.add('show');

    try {
        const res  = await fetch(CTX + '/admin/categories/delete?id=' + id + '&ajax=true');

        if (!res.ok) {
            throw new Error('HTTP error, status = ' + res.status);
        }

        const data = await res.json();

        if (!data.success) {
            console.error('Error response:', data);
            showPageAlert('error', 'Lỗi: ' + (data.message || 'Không xác định'));
            closeDeleteModal();
            return;
        }

        const canDelete = data.canDelete || 'error';

        switch (canDelete) {
            case 'can_delete':
                document.getElementById('deleteSimpleName').textContent = '"' + name + '"';
                setDeleteState('simple');
                break;

            case 'has_products':
                document.getElementById('deleteHasProdName').textContent = '"' + name + '"';
                const sel = document.getElementById('targetCategorySelect');
                Array.from(sel.options).forEach(opt => {
                    opt.disabled = (opt.value == id);
                });
                sel.value = '';
                setDeleteState('has_products');
                break;

            case 'has_children':
                document.getElementById('deleteHasChildName').textContent = '"' + name + '"';
                setDeleteState('has_children');
                break;

            default:
                console.error('Unknown canDelete status:', canDelete);
                showPageAlert('error', 'Lỗi không xác định');
                closeDeleteModal();
        }
    } catch (err) {
        console.error('Delete check error:', err);
        showPageAlert('error', 'Lỗi kết nối máy chủ: ' + err.message);
        closeDeleteModal();
    }
}

function setDeleteState(state) {
    document.getElementById('deleteSimple').style.display      = state === 'simple'       ? '' : 'none';
    document.getElementById('deleteHasProducts').style.display = state === 'has_products' ? '' : 'none';
    document.getElementById('deleteHasChildren').style.display = state === 'has_children' ? '' : 'none';
    document.getElementById('deleteLoading').style.display     = state === 'loading'       ? '' : 'none';
}


async function doDelete() {
    setDeleteState('loading');

    const body = new URLSearchParams();
    body.append('ajax',       'true');
    body.append('categoryId', _deleteId);

    try {
        const res  = await fetch(CTX + '/admin/categories/delete', {
            method:  'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body:    body.toString()
        });

        if (!res.ok) {
            throw new Error('HTTP error, status = ' + res.status);
        }

        const data = await res.json();
        closeDeleteModal();

        if (data.success) {
            showPageAlert('success', '✅ Đã xóa danh mục "' + _deleteName + '" thành công!');
            setTimeout(() => location.reload(), 900);
        } else {
            showPageAlert('error', data.message || 'Xóa thất bại.');
        }
    } catch (err) {
        console.error('Delete error:', err);
        closeDeleteModal();
        showPageAlert('error', 'Lỗi kết nối máy chủ: ' + err.message);
    }
}


async function doDeleteMoveProducts() {
    const target = document.getElementById('targetCategorySelect').value;

    // ✅ Kiểm tra target category
    if (!target || target.trim() === '') {
        showPageAlert('error', 'Vui lòng chọn danh mục đích.');
        return;
    }

    if (target == _deleteId) {
        showPageAlert('error', 'Danh mục đích phải khác danh mục đang xóa.');
        return;
    }

    setDeleteState('loading');

    const body = new URLSearchParams();
    body.append('ajax',             'true');
    body.append('categoryId',       _deleteId);
    body.append('action',           'move_products');
    body.append('targetCategoryId', target);

    try {
        const res  = await fetch(CTX + '/admin/categories/delete', {
            method:  'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body:    body.toString()
        });

        if (!res.ok) {
            throw new Error('HTTP error, status = ' + res.status);
        }

        const data = await res.json();
        closeDeleteModal();

        if (data.success) {
            showPageAlert('success', '✅ Đã chuyển sản phẩm và xóa danh mục "' + _deleteName + '"!');
            setTimeout(() => location.reload(), 1200);
        } else {
            showPageAlert('error', data.message || 'Xóa thất bại.');
        }
    } catch (err) {
        console.error('Delete with move error:', err);
        closeDeleteModal();
        showPageAlert('error', 'Lỗi kết nối: ' + err.message);
    }
}

function closeDeleteModal() {
    document.getElementById('deleteModal').classList.remove('show');
    _deleteId = null;
}


document.addEventListener('DOMContentLoaded', function () {
    // Stat card "Có sản phẩm" – placeholder (server chưa trả về)
    document.getElementById('statActiveCount').textContent = '–';

    document.getElementById('deleteModal').addEventListener('click', function (e) {
        if (e.target === this) closeDeleteModal();
    });
});


function showPageAlert(type, msg) {
    document.querySelectorAll('.alert').forEach(a => a.remove());

    const div     = document.createElement('div');
    div.className = 'alert ' + (type === 'success' ? 'alert-success' : 'alert-error');
    div.innerHTML = (type === 'success'
        ? '<i class="bi bi-check-circle-fill"></i>'
        : '<i class="bi bi-x-circle-fill"></i>') + ' ' + msg;

    document.querySelector('.header').insertAdjacentElement('afterend', div);
    div.scrollIntoView({ behavior: 'smooth' });
}