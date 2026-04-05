document.querySelectorAll(".nav-links a").forEach((link) => {
    link.addEventListener("click", () => {
        document.querySelector(".nav-links a.active")?.classList.remove("active");
        link.classList.add("active");
    });
});

let currentPage = 0;
const perPage = 4;
const products = document.querySelectorAll(".product-card");
const totalPages = Math.ceil(products.length / perPage);
const prevBtn = document.getElementById("prevBtn");
const nextBtn = document.getElementById("nextBtn");

function updateButtons() {
    prevBtn.disabled = currentPage === 0;
    nextBtn.disabled = currentPage === totalPages - 1;
}

function showPage(page) {
    products.forEach((p, i) => {
        const visible = i >= page * perPage && i < (page + 1) * perPage;
        p.classList.toggle("hidden", !visible);

        if (visible) {
            setTimeout(() => p.classList.add("show"), 20);
        } else {
            p.classList.remove("show");
        }
    });
    updateButtons();
}

function slideProducts(dir) {
    const next = currentPage + dir;
    if (next >= 0 && next < totalPages) {
        currentPage = next;
        showPage(currentPage);
    }
}

showPage(0);

// tìm kiếm khách hàng cho admin
const input = document.getElementById("searchInput");
const form = document.getElementById("searchForm");

let timeout=null;

input.addEventListener("input", function () {
    clearTimeout(timeout);

    const value = input.value.trim();

    if (value === "") {
        window.location.href = form.action;
        return;
    }

    timeout = setTimeout(() => {
        form.submit();
    }, 400);
});