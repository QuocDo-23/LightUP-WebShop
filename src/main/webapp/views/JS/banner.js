(function () {
    const carouselSlides = document.querySelectorAll(".carousel-slide");
    const dotsContainer  = document.getElementById("dots");
    const carouselPrev   = document.querySelector(".carousel-arrow.prev");
    const carouselNext   = document.querySelector(".carousel-arrow.next");
    let currentSlide = 0;
    let autoSlide;

    if (carouselSlides.length <= 1) {
        if (carouselPrev) carouselPrev.style.display = "none";
        if (carouselNext) carouselNext.style.display = "none";
    } else {
        carouselSlides.forEach((_, i) => {
            const dot = document.createElement("span");
            dot.className = "dot" + (i === 0 ? " active" : "");
            dot.onclick = () => goToSlide(i);
            dotsContainer.appendChild(dot);
        });
    }

    function showSlide(index) {
        carouselSlides.forEach((s, i) => s.classList.toggle("active", i === index));
        dotsContainer.querySelectorAll(".dot").forEach((d, i) => d.classList.toggle("active", i === index));
    }

    window.nextSlide = function () {
        currentSlide = (currentSlide + 1) % carouselSlides.length;
        showSlide(currentSlide);
    };

    window.prevSlide = function () {
        currentSlide = (currentSlide - 1 + carouselSlides.length) % carouselSlides.length;
        showSlide(currentSlide);
    };

    window.goToSlide = function (index) {
        currentSlide = index;
        showSlide(currentSlide);
    };

    function startAutoSlide() {
        if (carouselSlides.length > 1) {
            autoSlide = setInterval(window.nextSlide, 4000);
        }
    }

    const carouselContainer = document.getElementById("carouselContainer");
    carouselContainer.addEventListener("mouseenter", () => clearInterval(autoSlide));
    carouselContainer.addEventListener("mouseleave", startAutoSlide);

    showSlide(currentSlide);
    startAutoSlide();
})();