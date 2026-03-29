<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<div id="cart-overlay"></div>

<div id="khunggiohang" class="cart-widget-side">
    <div class="wd-heading">
        <span class="title">Giỏ hàng</span>
        <button type="button" id="btn-dong-gio" class="close-cart">× Đóng</button>
    </div>
    <div id="mini-cart-body" class="shopping-cart-widget-body wd-scroll">
    </div>
</div>



<script>
    (function () {
        var panel    = document.getElementById("khunggiohang");
        var overlay  = document.getElementById("cart-overlay");
        var body     = document.getElementById("mini-cart-body");
        var btnDong  = document.getElementById("btn-dong-gio");
        var autoCloseTimer = null;


        function moGio() {
            panel.classList.add("open");
            overlay.classList.add("active");
            document.body.style.overflow = "hidden";
        }
        function dongGio() {
            panel.classList.remove("open");
            overlay.classList.remove("active");
            document.body.style.overflow = "";
            clearTimeout(autoCloseTimer);
        }

        btnDong.addEventListener("click", dongGio);
        overlay.addEventListener("click", dongGio);

        function themVaoGio(pID, quantity) {
            var url = "${pageContext.request.contextPath}/add-cart-ajax"
                + "?pID=" + pID + "&quantity=" + quantity;

            fetch(url)
                .then(function(res) { return res.text(); })
                .then(function(html) {
                    body.innerHTML = html;
                    capNhatCartCount();
                    bindMiniFormEvents();
                    moGio();
                })
                .catch(function() {
                    body.innerHTML = "<p class='mini-cart-empty'>Lỗi kết nối, vui lòng thử lại.</p>";
                    moGio();
                });
        }


        function ajaxForm(form) {
            var params = new URLSearchParams(new FormData(form));
            var url    = form.action;
            fetch(url, {
                method:  "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body:    params.toString()
            })
                .then(function(res) { return res.text(); })
                .then(function(html) {
                    body.innerHTML = html;
                    capNhatCartCount();
                    bindMiniFormEvents();
                })
                .catch(function(err) { console.error("ajaxForm error:", err); });
        }

        function bindMiniFormEvents() {
            body.querySelectorAll(".form-remove-mini").forEach(function(form) {
                form.addEventListener("submit", function(e) {
                    e.preventDefault();
                    ajaxForm(this);
                });
            });

            body.querySelectorAll(".btn-qty-minus, .btn-qty-plus").forEach(function(btn) {
                btn.addEventListener("click", function() {
                    var form     = this.closest(".form-update-mini");
                    var hidden   = form.querySelector(".qty-value");
                    hidden.value = this.dataset.qty;
                    ajaxForm(form);
                });
            });
        }


        function capNhatCartCount() {
            var meta  = document.getElementById("mini-cart-meta");
            var badge = document.getElementById("cartCount");
            if (meta && badge) {
                badge.textContent = meta.dataset.totalItems || "0";
            }
        }


        var btnMoGio = document.getElementById("btnMoGio");
        if (btnMoGio) {
            btnMoGio.addEventListener("click", function(e) {
                e.preventDefault();
                moGio();
            });
        }

        /* ── Hook tất cả nút "Thêm vào giỏ" trên trang ── */
        document.addEventListener("click", function(e) {
            var btn = e.target.closest(".open-cart[data-product-id], a.open-cart[href*='add-cart']");
            if (!btn) return;
            e.preventDefault();

            var pID, qty = 1;

            if (btn.dataset.productId) {
                /* <button data-product-id="..."> */
                pID = btn.dataset.productId;
            } else {
                /* <a href="add-cart?pID=...&quantity=..."> */
                var params = new URLSearchParams(btn.href.split("?")[1]);
                pID = params.get("pID");
                qty = params.get("quantity") || 1;
            }

            if (pID) themVaoGio(pID, qty);
        });

    })();
</script>
