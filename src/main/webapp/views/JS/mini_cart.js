document.addEventListener("DOMContentLoaded", function () {

    var CTX = window.CTX || "";
    var panel = document.getElementById("khunggiohang");
    var overlay = document.getElementById("cart-overlay");
    var body = document.getElementById("mini-cart-body");
    var btnDong = document.getElementById("btn-dong-gio");


    function moGio() {
        panel.classList.add("open");
        overlay.classList.add("active");
        document.body.style.overflow = "hidden";
    }

    function dongGio() {
        panel.classList.remove("open");
        overlay.classList.remove("active");
        document.body.style.overflow = "";
    }

    btnDong.addEventListener("click", dongGio);

    overlay.addEventListener("click", function (e) {
        if (e.target === overlay) {
            dongGio();
        }
    });
    function loadMiniCart(callback) {
        fetch(CTX + "/cart-mini-fragment")
            .then(function (res) {
                return res.text();
            })
            .then(function (html) {
                body.innerHTML = html;
                capNhatCartCount();
                bindMiniFormEvents();
                if (callback) callback();
            })
            .catch(function () {
                body.innerHTML = "<p class='mini-cart-empty'>Lỗi kết nối, vui lòng thử lại.</p>";
                if (callback) callback();
            });
    }

    function moGioVaLoad() {
        loadMiniCart(moGio);
    }

    function themVaoGio(pID, quantity) {
        var url = CTX + "/add-cart-ajax?pID=" + pID + "&quantity=" + quantity;

        fetch(url)
            .then(function (res) {
                if (res.status === 409) {
                    return res.text().then(function (html) {
                        body.innerHTML = html;
                        capNhatCartCount();
                        moGio();
                        return null;
                    });
                }
                return res.text();
            })
            .then(function (html) {
                if (html === null) return;
                body.innerHTML = html;
                capNhatCartCount();
                bindMiniFormEvents();
                moGio();
            })
            .catch(function () {
                body.innerHTML = "<p class='mini-cart-empty'>Lỗi kết nối, vui lòng thử lại.</p>";
                moGio();
            });
    }


    function ajaxForm(form) {
        var params = new URLSearchParams(new FormData(form));
        fetch(form.action, {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: params.toString()
        })
            .then(function (res) {
                return res.text();
            })
            .then(function (html) {
                body.innerHTML = html;
                capNhatCartCount();
                bindMiniFormEvents();
            })
            .catch(function (err) {
                console.error("ajaxForm error:", err);
            });
    }

    function bindMiniFormEvents() {
        body.querySelectorAll(".form-remove-mini").forEach(function (form) {
            form.addEventListener("submit", function (e) {
                e.preventDefault();
                ajaxForm(this);
            });
        });

        body.querySelectorAll(".btn-qty-minus, .btn-qty-plus").forEach(function (btn) {
            btn.addEventListener("click", function () {
                var form = this.closest(".form-update-mini");
                var hidden = form.querySelector(".qty-value");
                hidden.value = this.dataset.qty;
                ajaxForm(form);
            });
        });


        var btnCheckoutMini = body.querySelector("#btnCheckoutMini");
        if (btnCheckoutMini) {
            btnCheckoutMini.addEventListener("click", function () {
                var ids = Array.from(
                    body.querySelectorAll(".mini-cart-item[data-id]")
                ).map(function (el) {
                    return el.dataset.id;
                });

                if (ids.length === 0) return;

                var params = new URLSearchParams();
                ids.forEach(function (id) {
                    params.append("selectedIds", id);
                });

                fetch(CTX + "/checkout-select", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: params.toString()
                }).then(function (res) {
                    if (res.ok) window.location.href = CTX + "/payment";
                    else alert("Có lỗi xảy ra, vui lòng thử lại!");
                });
            });
        }
    }

    function capNhatCartCount() {
        var meta = document.getElementById("mini-cart-meta");
        var badge = document.getElementById("cartCount");
        if (meta && badge) {
            badge.textContent = meta.dataset.totalItems || "0";
        }
    }

    var btnMoGio = document.getElementById("btnMoGio");
    if (btnMoGio) {
        btnMoGio.addEventListener("click", function (e) {
            e.preventDefault();
            moGioVaLoad();
        });
    }


    document.addEventListener("click", function (e) {
        var btn = e.target.closest(".open-cart[data-product-id], a.open-cart[href*='add-cart']");
        if (!btn) return;
        e.preventDefault();
        e.stopPropagation();

        var pID, qty = 1;
        if (btn.dataset.productId) {
            pID = btn.dataset.productId;
            qty = btn.dataset.quantity || 1;
        } else {
            var p = new URLSearchParams(btn.href.split("?")[1]);
            pID = p.get("pID");
            qty = p.get("quantity") || 1;
        }

        if (pID) themVaoGio(pID, qty);
    });
    panel?.addEventListener('click', function (e) {
        e.stopPropagation();
    });
});