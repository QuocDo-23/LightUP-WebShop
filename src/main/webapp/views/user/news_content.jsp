<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${news.title} - LightUP News</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/news_content.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
</head>

<body>
<!--Header-->
<jsp:include page="/views/layout/header.jsp"/>

<div class="link-page" id="up">
    <div class="containt_link">
        <a href="${pageContext.request.contextPath}/"><i class="bi bi-house"></i> Trang chủ </a>
        <span> /</span>
        <a href=${pageContext.request.contextPath}/news>Tin tức</a>
        <span> /</span>
        <b>${news.title}</b>
    </div>
</div>
<main>
    <section class="container_1">
        <div class="title">

            <h1>${news.title}</h1>
            <i>${news.description}
                <div class="article-meta-date">
                    <span>
                        <i class="bi bi-calendar3"></i>
                        <fmt:formatDate value="${news.dateOfPosting}" pattern="dd/MM/yyyy HH:mm"/>
                    </span>

                    <c:if test="${news.feature}">
                        <span style="color: #ffc107;">
                            <i class="bi bi-star-fill"></i> Nổi bật
                        </span>
                    </c:if>
                </div>
            </i>

            <c:if test="${not empty news.mainImg}">
            <div class="img">
                <img src="${news.mainImg}" alt="${news.title}">
            </div>
            </c:if>

            <div class="content_1">
                <c:forEach items="${contents}" var="content">
                <c:choose>
                    <%-- Tiêu đề phụ --%>
                <c:when test="${content.contentType.name() eq 'HEADING'}">
                <h2>${content.content}</h2>
                </c:when>
                    <%-- Văn bản --%>
                <c:when test="${content.contentType.name() eq 'TEXT'}">
                <p>${content.content}</p>
                </c:when>
                    <%-- Hình ảnh --%>
                <c:when test="${content.contentType.name() eq 'IMAGE'}">
                <div class="img">
                    <img src="${content.content}" alt="Hình ảnh">
                </div>
                </c:when>
                    <%-- Trích dẫn --%>
                <c:when test="${content.contentType.name() eq 'QUOTE'}">
                <h3>${content.content}</h3>
                </c:when>

                </c:choose>
                </c:forEach>
                <div class="sidebar-section" style="margin-top: 40px;">
                    <div class="sidebar-title">Chia sẻ bài viết</div>
                    <div class="share-buttons">
                        <a href="https://www.facebook.com/sharer/sharer.php?u=${pageContext.request.requestURL}"
                           target="_blank" class="share-btn facebook" title="Share on Facebook">
                            <i class="bi bi-facebook"></i>
                        </a>
                        <a href="https://twitter.com/intent/tweet?url=${pageContext.request.requestURL}&text=${news.title}"
                           target="_blank" class="share-btn twitter" title="Share on Twitter">
                            <i class="bi bi-twitter"></i>
                        </a>
                        <a href="https://www.linkedin.com/sharing/share-offsite/?url=${pageContext.request.requestURL}"
                           target="_blank" class="share-btn linkedin" title="Share on LinkedIn">
                            <i class="bi bi-linkedin"></i>
                        </a>
                        <button onclick="copyLink()" class="share-btn copy" title="Copy link">
                            <i class="bi bi-link-45deg"></i>
                        </button>
                    </div>
                </div>
    </section>
    <section class="container_2">
        <c:if test="${not empty relatedArticles}">
            <div class="news">
                <h2>Tin liên quan</h2>
                <c:forEach items="${relatedArticles}" var="related">
                    <div class="article-card" data-page="1">
                        <div class="article-image">
                            <img src="${not empty related.mainImg ? related.mainImg : 'https://via.placeholder.com/100x70'}"
                                 alt="${related.title}">
                        </div>
                        <div class="article-content">
                            <h2 class="article-title"><a
                                    href="${pageContext.request.contextPath}//news_content?slug=${related.slug}"
                                    class="related-article-title">
                                    ${related.title}
                            </a></h2>
                            <p class="article-description">${related.description} </p>
                            <div class="article-meta">
                                <strong>${related.categoryName != null ? related.categoryName : 'Tin tức LightUP'}</strong>
                                <span>•</span>
                                <span> <fmt:formatDate value="${related.dateOfPosting}"
                                                       pattern="dd/MM/yyyy"/></span>
                            </div>
                        </div>

                    </div>
                </c:forEach>
            </div>
        </c:if>
    </section>
</main>

<!--Footer-->
<jsp:include page="/views/layout/footer.jsp"/>
<script src="./JS/index.js"></script>
</body>

</html>