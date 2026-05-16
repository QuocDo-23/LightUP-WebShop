document.addEventListener("DOMContentLoaded", function () {

    // ── Sắp xếp sản phẩm ──
    document.querySelectorAll('.sort-list li').forEach(item => {
        item.addEventListener('click', () => {
            const sortType = item.dataset.sort;
            const products = Array.from(document.querySelectorAll('.product-card'));
            const grid = document.querySelector('.product-grid');

            let sorted;

            if (sortType === 'price-asc') {
                sorted = products.sort((a, b) => a.dataset.price - b.dataset.price);
            }
            if (sortType === 'price-desc') {
                sorted = products.sort((a, b) => b.dataset.price - a.dataset.price);
            }
            if (sortType === 'popular') {
                sorted = products.sort((a, b) => b.dataset.rating - a.dataset.rating);
            }

            grid.innerHTML = '';
            sorted.forEach(p => grid.appendChild(p));
        });
    });

    // ── Toggle filter dropdown ──
    document.querySelectorAll('.filter-dropdown .filter-toggle')
        .forEach(toggle => {
            toggle.addEventListener('click', function (e) {
                e.stopPropagation();

                const list = this.parentElement.querySelector('.filter-list');
                if (!list) return;

                document.querySelectorAll('.filter-list')
                    .forEach(l => { if (l !== list) l.style.display = 'none'; });

                list.style.display = list.style.display === 'block' ? 'none' : 'block';
            });
        });

    // ── Toggle sort dropdown ──
    document.querySelector('.sort-toggle')?.addEventListener('click', function (e) {
        e.stopPropagation();

        const list = document.querySelector('.sort-list');
        if (!list) return;

        document.querySelectorAll('.filter-list')
            .forEach(l => l.style.display = 'none');

        list.style.display = list.style.display === 'block' ? 'none' : 'block';
    });

    // ── Áp dụng bộ lọc giá ──
    const applyFilterBtn = document.querySelector(
        '.filter-container > .filter-dropdown:first-child .filter-toggle'
    );
    if (applyFilterBtn) {
        applyFilterBtn.addEventListener('click', function (e) {
            e.stopPropagation();

            const checked = document.querySelectorAll('.price-filter:checked');
            if (checked.length === 0) {
                alert("Vui lòng chọn ít nhất một khoảng giá");
                return;
            }

            const params = [];
            checked.forEach(cb => {
                const min = cb.dataset.min;
                const max = cb.dataset.max;
                params.push(`price=${min}-${max}`);
            });

            window.location.href = `products?${params.join('&')}`;
        });
    }

    // ── FAVORITE (FIX CHUẨN) ──
    document.addEventListener("click", function (e) {
        const button = e.target.closest(".favorite-btn");
        if (!button) return;

        e.preventDefault();

        const form = button.closest("form");

        const productId =
            form.querySelector('input[name="productId"]').value;

        fetch(form.action, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "productId=" + encodeURIComponent(productId)
        })
            .then(res => res.text())
            .then(data => {

                console.log("SERVER TRA VE:", data);

                data = data.trim();

                if (data === "login") {
                    window.location.href = "/LightUp_war/login";
                    return;
                }

                // đổi màu tim
                if (data === "true") {
                    button.classList.add("active");
                } else {
                    button.classList.remove("active");
                }

                // update số trên header
                const count = document.querySelector(".favorite-count");

                if (count) {
                    let current = parseInt(count.innerText.replace(/\D/g, "") || "0");

                    if (data === "true") {
                        count.innerText = current + 1;
                    } else {
                        count.innerText = Math.max(0, current - 1);
                    }
                }

            })
            .catch(err => console.error(err));

    });

});