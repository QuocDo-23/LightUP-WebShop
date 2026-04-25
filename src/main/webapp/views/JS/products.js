document.addEventListener("DOMContentLoaded", function () {

    const miniCart = document.getElementById("khunggiohang");
    const overlay = document.getElementById("cart-overlay");


    document.querySelectorAll(".open-cart").forEach(btn => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();

            const productId = this.dataset.productId;
            if (!productId) return;

            fetch(`add-cart?pID=${productId}&quantity=1`)
                .then(res => {
                    if (!res.ok) throw new Error("Add cart failed");


                    location.reload();
                })
                .then(() => {
                    miniCart.classList.add("active");
                    overlay.classList.add("active");
                })
                .catch(err => console.error(err));
        });
    });


    document.querySelectorAll(".close-cart").forEach(btn => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();
            miniCart.classList.remove("active");
            overlay.classList.remove("active");
        });
    });

    if (overlay) {
        overlay.addEventListener("click", function () {
            miniCart.classList.remove("active");
            overlay.classList.remove("active");
        });
    }


    document.addEventListener("click", function (e) {
        const btn = e.target.closest(".remove-mini-cart");
        if (!btn) return;

        e.preventDefault();

        const productId = btn.dataset.id;
        if (!productId) return;

        fetch(`remove?productId=${productId}`)
            .then(res => {
                if (!res.ok) throw new Error("Remove failed");
                location.reload(); // reload để mini cart cập nhật
            })
            .catch(err => console.error(err));
    });
    document.querySelectorAll('.sort-list li').forEach(item => {
        item.addEventListener('click', () => {

            const sortType = item.dataset.sort;
            const products = Array.from(document.querySelectorAll('.product-card'));
            const grid = document.querySelector('.product-grid');

            let sorted;

            if (sortType === 'price-asc') {
                sorted = products.sort((a, b) =>
                    a.dataset.price - b.dataset.price
                );
            }

            if (sortType === 'price-desc') {
                sorted = products.sort((a, b) =>
                    b.dataset.price - a.dataset.price
                );
            }

            if (sortType === 'popular') {
                sorted = products.sort((a, b) =>
                    b.dataset.rating - a.dataset.rating
                );
            }

            grid.innerHTML = '';
            sorted.forEach(p => grid.appendChild(p));
        });
    });

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


    document.querySelector('.sort-toggle')?.addEventListener('click', function (e) {
        e.stopPropagation();

        const list = document.querySelector('.sort-list');
        if (!list) return;

        document.querySelectorAll('.filter-list')
            .forEach(l => l.style.display = 'none');

        list.style.display = list.style.display === 'block' ? 'none' : 'block';
    });



    document.addEventListener('click', () => {
        document.querySelectorAll('.filter-list, .sort-list')
            .forEach(list => list.style.display = 'none');
    });

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

    document.querySelectorAll(".favorite-form").forEach(form => {

        form.addEventListener("submit", function (e) {
            e.preventDefault();

            const button = form.querySelector(".favorite-btn");

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

                    if (data === "true") {
                        button.classList.add("active");
                    } else if (data === "false") {
                        button.classList.remove("active");
                    }

                })
                .catch(err => console.error(err));

        });

    });
});
