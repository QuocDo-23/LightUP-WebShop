document.addEventListener("DOMContentLoaded", function () {
    const accordionTitles = document.querySelectorAll(".wd-accordion-title");

    accordionTitles.forEach(function (title) {
        title.addEventListener("click", function () {
            const targetId = this.getAttribute("data-target");
            const content = document.getElementById("tab-" + targetId);

            // Toggle active state
            this.classList.toggle("wd-active");
            content.classList.toggle("wd-active");
        });
    });
});

const stars = document.querySelectorAll('.stars a');
let selectedRating = 5;
stars.forEach(star => {
    star.addEventListener('click', function (e) {
        e.preventDefault();
        selectedRating = parseInt(this.getAttribute('data-rating'));
        stars.forEach(s => s.classList.remove('active'));
        for (let i = 0; i < selectedRating; i++) {
            stars[i].classList.add('active');
        }
    });
});
document.querySelector('.submit-btn').addEventListener('click', function () {
    const comment = document.querySelector('textarea').value;
    console.log(`Đánh giá: ${selectedRating} sao`);
    console.log(`Bình luận: ${comment}`);
    alert(`Cảm ơn bạn đã đánh giá!`);
});
document.querySelector('.btn-reviews-now').addEventListener('click', function (e) {
    e.preventDefault();
    document.querySelector('.overlay').style.display = 'block';
    document.querySelector('.review-box').style.display = 'block';
});
document.querySelector('.close-btn').addEventListener('click', function () {
    document.querySelector('.overlay').style.display = 'none';
    document.querySelector('.review-box').style.display = 'none';
});
document.querySelector('.overlay').addEventListener('click', function () {
    document.querySelector('.overlay').style.display = 'none';
    document.querySelector('.review-box').style.display = 'none';
});