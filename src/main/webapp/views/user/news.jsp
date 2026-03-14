<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/news.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link href='https://fonts.googleapis.com/css?family=Monsieur La Doulaise' rel='stylesheet'>
    <link href='https://fonts.googleapis.com/css?family=Literata' rel='stylesheet'>
    <title>Tin Tức - LightUp</title>
</head>
<body>

<main>
    <jsp:include page="/views/layout/header.jsp"/>

    <div class="link-page" id="up">
        <div class="containt_link">
            <a href="./index.jsp"><i class="bi bi-house"></i> Trang chủ </a>
            <span> /</span>
            <a href="">Tin tức</a>
        </div>
    </div>

    <div class="search-overlay" id="searchOverlay" onclick="closeSearchPanel()"></div>

    <!-- Section 1: Header -->
    <section class="section_1_news" id="home">
        <div class="cont">
            <div class="section_1_content">
                <h1>Lightup</h1>
                <span class="highlight">TIN TỨC</span>
            </div>
        </div>
        <div class="ux-shape-divider ux-shape-divider--bottom">
            <svg viewBox="0 0 1000 100" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="none">
                <path class="ux-shape-fill"
                      d="M1000 100H0V80H479.686C481.808 80 483.843 80.8429 485.343 82.3432L497.879 94.8787C499.05 96.0503 500.95 96.0503 502.121 94.8787L514.657 82.3431C516.157 80.8428 518.192 80 520.314 80H1000V100Z">
                </path>
            </svg>
        </div>
    </section>

    <!-- Section 2: Bài viết nổi bậc -->
    <div class="section_2_news">
        <div class="section-header">
            <h2>Bài viết nổi bật</h2>
            <div class="divider"></div>
        </div>

        <c:if test="${not empty featuredArticles}">
            <!-- bài viết nổi bậc chính -->
            <c:set var="mainArticle" value="${featuredArticles[0]}"/>
            <div class="new_1">
                <div class="cont">
                    <img src="${mainArticle.mainImg}" alt="${mainArticle.title}">
                    <div class="title">
                        <div class="title_r">
                            <div class="title_last">
                                <span>${mainArticle.categoryName != null ? mainArticle.categoryName : 'Bản tin LightUP'}</span>
                                <li><fmt:formatDate value="${mainArticle.dateOfPosting}" pattern="dd/MM/yyyy"/></li>
                            </div>
                            <div class="name">
                                <a href="news-detail?slug=${mainArticle.slug}">${mainArticle.title}</a>
                            </div>
                        </div>
                        <div class="title_l">
                            <a href="news-detail?slug=${mainArticle.slug}"><i class="bi bi-arrow-up-right"></i></a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- các bài viết nổi bậc khác -->
            <div class="new_2">
                <div class="container">
                    <c:forEach var="article" items="${featuredArticles}" begin="1" end="3">
                        <div class="card">
                            <div class="card_img">
                                <img src="${article.mainImg}" alt="${article.title}">
                            </div>
                            <div class="title">
                                <div class="title_r">
                                    <div class="title_last">
                                        <span>${article.categoryName != null ? article.categoryName : 'Bản tin LightUP'}</span>
                                        <li><fmt:formatDate value="${article.dateOfPosting}" pattern="dd/MM/yyyy"/></li>
                                    </div>
                                    <div class="name">
                                        <a href="news-detail?slug=${article.slug}">${article.title}</a>
                                    </div>
                                </div>
                                <div class="title_l">
                                    <a href="news-detail?slug=${article.slug}"><i class="bi bi-arrow-up-right"></i></a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>

    <!-- Section 3: tất cả bài viết -->
    <section class="section_3_news">
        <div class="container">
            <div class="header">
                <h1>Bài viết</h1>
                <div class="sort-dropdown">
                    <label>Sắp xếp theo:</label>
                    <select onchange="window.location.href='news?page=1&sort=' + this.value + '#articles'">
                    <option value="newest" ${sortBy eq 'newest' ? 'selected' : ''}>Mới nhất</option>
                    <option value="oldest" ${sortBy eq 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                    </select>
                </div>
            </div>

            <div class="articles-list" id="articles">
                <c:forEach var="article" items="${articles}">
                    <div class="article-card">
                        <div class="article-image">
                            <img src="${article.mainImg}" alt="${article.title}">
                        </div>
                        <div class="article-content">
                            <h2 class="article-title">
                                <a href="news-detail?slug=${article.slug}">${article.title}</a>
                            </h2>
                            <p class="article-description">${article.description}</p>
                            <div class="article-meta">
                                <strong>${article.categoryName != null ? article.categoryName : 'Tin tức LightUP'}</strong>
                                <span>•</span>
                                <span><fmt:formatDate value="${article.dateOfPosting}" pattern="dd/MM/yyyy"/></span>
                            </div>
                        </div>

                        <div class="title_l">
                            <a href="news-detail?slug=${article.slug}"><i class="bi bi-arrow-up-right"></i></a>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- phân trang tin tức -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="news?page=${currentPage - 1}&sort=${sortBy}#articles " class="prev-btn">← Trước</a>
                    </c:if>
                    <c:if test="${currentPage == 1}">
                        <a href="#" class="prev-btn disabled">← Trước</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="news?page=${i}&sort=${sortBy}#articles"
                           class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="news?page=${currentPage + 1}&sort=${sortBy}#articles" class="next-btn">Sau →</a>
                    </c:if>
                    <c:if test="${currentPage == totalPages}">
                        <a href="#" class="next-btn disabled">Sau →</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </section>

    <!-- Footer -->
    <jsp:include page="/views/layout/footer.jsp"/>

    <a href="#up">
        <button id="scrollToTopBtn">
            <i class="bi bi-chevron-up"></i>
        </button>
    </a>
</main>

<script src="./JS/index.js"></script>
</body>
</html>
