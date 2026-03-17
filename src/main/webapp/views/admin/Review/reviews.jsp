<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý đánh giá</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <link rel="stylesheet"
           href="${pageContext.request.contextPath}/views/CSS/adminCSS/reviews-list.css?v=123">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">


</head>

<body>
<div class="container">


    <jsp:include page="/views/layout/siderbar_admin.jsp"/>


    <div class="main-content">

        <div class="header">
            <h1>Quản lý đánh giá</h1>
            <div class="avatar">A</div>
        </div>


        <div class="stats-grid stats-4">
            <div class="stat-card">
                <div class="stat-label">Tổng đánh giá</div>
                <div class="stat-value">${statistics.totalReviews}</div>
            </div>

            <div class="stat-card">
                <div class="stat-label">Điểm TB</div>
                <div class="stat-value" style="color:#f59e0b">
                    <fmt:formatNumber value="${statistics.averageRating}" maxFractionDigits="2"/> ⭐
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-label">⭐ 5 sao</div>
                <div class="stat-value">${statistics.fiveStars}</div>
            </div>

            <div class="stat-card">
                <div class="stat-label">⚠️ Dưới 3 sao</div>
                <div class="stat-value">
                    ${statistics.twoStars + statistics.oneStar}
                </div>
            </div>
        </div>

        <div class="rating-analytics">

            <div class="rating-analytics-header">
                <h3>Phân bố đánh giá</h3>
                <span>Tổng ${statistics.totalReviews} đánh giá</span>
            </div>

            <div class="rating-chart">

                <div class="chart-row">
                    <span class="star">5★</span>
                    <div class="chart-bar">
                        <div class="chart-fill green"
                             style="width:${statistics.getPercentage(5)}%"></div>
                    </div>
                    <span class="percent">
                <fmt:formatNumber value="${statistics.getPercentage(5)}" maxFractionDigits="2"/>%
            </span>
                </div>

                <div class="chart-row">
                    <span class="star">4★</span>
                    <div class="chart-bar">
                        <div class="chart-fill blue"
                             style="width:${statistics.getPercentage(4)}%"></div>
                    </div>
                    <span class="percent">
                <fmt:formatNumber value="${statistics.getPercentage(4)}" maxFractionDigits="2"/>%
            </span>
                </div>

                <div class="chart-row">
                    <span class="star">3★</span>
                    <div class="chart-bar">
                        <div class="chart-fill yellow"
                             style="width:${statistics.getPercentage(3)}%"></div>
                    </div>
                    <span class="percent">
                <fmt:formatNumber value="${statistics.getPercentage(3)}" maxFractionDigits="2"/>%
            </span>
                </div>

                <div class="chart-row">
                    <span class="star">2★</span>
                    <div class="chart-bar">
                        <div class="chart-fill orange"
                             style="width:${statistics.getPercentage(2)}%"></div>
                    </div>
                    <span class="percent">
                <fmt:formatNumber value="${statistics.getPercentage(2)}" maxFractionDigits="2"/>%
            </span>
                </div>

                <div class="chart-row">
                    <span class="star">1★</span>
                    <div class="chart-bar">
                        <div class="chart-fill red"
                             style="width:${statistics.getPercentage(1)}%"></div>
                    </div>
                    <span class="percent">
                <fmt:formatNumber value="${statistics.getPercentage(1)}" maxFractionDigits="2"/>%
            </span>
                </div>

            </div>
        </div>

        <div style="
        margin: 25px 0 10px;
        font-size: 25px;
        font-weight: 600;
        color: #4a5568;">
                📋 Danh sách đánh giá gần đây
            </div>



        <div class="reviews-list-section">
            <c:forEach items="${reviews}" var="r">
                <div class="review-item
                    ${r.status == 0 ? 'pending' : ''}
                    ${r.rating <= 2 ? 'bad' : ''}">

                    <div class="review-header">
                        <div class="review-customer">
                            <div class="customer-avatar">
                                <i class="fas fa-user"></i>
                            </div>
                            <div>
                                <strong>${r.userName}</strong><br>
                                <small>User ID: ${r.userId}</small>
                            </div>
                        </div>

                        <div class="review-meta">
                            <div class="review-rating">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="${i <= r.rating ? 'fas fa-star' : 'far fa-star'}"></i>
                                </c:forEach>
                            </div>
                            <small>${r.date}</small>
                        </div>
                    </div>

                    <div class="review-content">
                        <p>${r.text}</p>
                    </div>

                    <div class="review-actions">
                        <form method="post" action="${pageContext.request.contextPath}/admin/reviews">
                            <input type="hidden" name="action" value="updateStatus">
                            <input type="hidden" name="reviewId" value="${r.id}">
                            <input type="hidden" name="status" value="${r.status == 1 ? 0 : 1}">
                            <button class="btn-action">
                                    ${r.status == 1 ? 'Ẩn' : 'Duyệt'}
                            </button>
                        </form>

                        <form method="post" action="${pageContext.request.contextPath}/admin/reviews"
                              onsubmit="return confirm('Xóa đánh giá này?')">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="reviewId" value="${r.id}">
                            <button class="btn-action">Xóa</button>
                        </form>

                        <button class="btn-action btn-reply"
                                onclick="openReviewModal(
                                        '${r.userName}',
                                        '${r.rating}',
                                        '${r.date}',
                                        '${fn:escapeXml(r.text)}',
                                        '${r.img}'
                                        )">
                            <i class="fas fa-eye"></i> Xem
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>


<div id="reviewModal" class="modal-overlay" onclick="closeReviewModal(event)">
    <div class="modal-content">
        <h3>Chi tiết đánh giá</h3>
        <p><b>Người dùng:</b> <span id="modalUser"></span></p>
        <p><b>Rating:</b> <span id="modalRating"></span> ⭐</p>
        <p><b>Ngày:</b> <span id="modalDate"></span></p>
        <p id="modalText"></p>
        <img id="modalImage" style="max-width:100%;display:none">
    </div>
</div>

<script>
    function openReviewModal(user, rating, date, text, img) {
        modalUser.innerText = user;
        modalRating.innerText = rating;
        modalDate.innerText = date;
        modalText.innerText = text;

        if (img && img !== "null" && img !== "") {
            modalImage.src = img;
            modalImage.style.display = "block";
        } else {
            modalImage.style.display = "none";
        }
        reviewModal.style.display = "flex";
    }

    function closeReviewModal(e) {
        if (!e || e.target.id === "reviewModal") {
            reviewModal.style.display = "none";
        }
    }
</script>

</body>
</html>
