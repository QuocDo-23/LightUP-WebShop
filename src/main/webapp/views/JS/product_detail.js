document.addEventListener("DOMContentLoaded", function () {

    const accordionTitles = document.querySelectorAll(".wd-accordion-title");

    accordionTitles.forEach(function (title) {
        title.addEventListener("click", function () {
            const targetId = this.getAttribute("data-target");
            const content = document.getElementById("tab-" + targetId);

            this.classList.toggle("wd-active");
            content.classList.toggle("wd-active");
        });
    });

    const stars = document.querySelectorAll('.star-item');

    stars.forEach((star, index) => {
        star.addEventListener('click', function () {

            const input = this.querySelector('input');
            input.checked = true;

            stars.forEach(s => s.classList.remove('active'));

            for (let i = 0; i <= index; i++) {
                stars[i].classList.add('active');
            }
        });
    });

    const submitBtn = document.querySelector('#reviewForm button[type="submit"]');
    if (submitBtn) {
        submitBtn.addEventListener('click', function () {
            const comment = document.querySelector('textarea')?.value;
            console.log(comment);
        });
    }

    const btn = document.querySelector('.btn-reviews-now');
    const overlay = document.querySelector('.overlay');
    const box = document.querySelector('.review-box');

    if (btn && overlay && box) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            overlay.style.display = 'block';
            box.style.display = 'block';
        });
    }

    const closeBtn = document.querySelector('.close-btn');

    if (closeBtn && overlay && box) {
        closeBtn.addEventListener('click', function () {
            overlay.style.display = 'none';
            box.style.display = 'none';
        });
    }

    if (overlay && box) {
        overlay.addEventListener('click', function () {
            overlay.style.display = 'none';
            box.style.display = 'none';
        });
    }

    const fileInput = document.querySelector('input[name="image"]');
    const preview = document.getElementById('preview');

    if (fileInput && preview) {
        fileInput.addEventListener('change', function () {
            preview.innerHTML = "";

            const file = this.files[0];
            if (file) {
                const img = document.createElement("img");
                img.src = URL.createObjectURL(file);
                img.style.width = "80px";
                img.style.borderRadius = "6px";
                preview.appendChild(img);
            }
        });
    }

});