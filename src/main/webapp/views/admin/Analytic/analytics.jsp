<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thống Kê</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>

</head>

<body>
<div class="container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">
        <h1>📈 Thống Kê</h1>


        <div class="analytics-stats-grid">

            <div class="analytics-stat-card">
                <div class="stat-value">
                    <fmt:formatNumber value="${totalRevenue}" groupingUsed="true"/>đ
                </div>
                <div class="stat-label">Tổng doanh thu</div>
            </div>

            <div class="analytics-stat-card">
                <div class="stat-value">${totalOrders}</div>
                <div class="stat-label">Tổng đơn hàng</div>
            </div>

            <div class="analytics-stat-card">
                <div class="stat-value">${processingOrders}</div>
                <div class="stat-label">Đơn đang xử lý</div>
            </div>

            <div class="analytics-stat-card">
                <div class="stat-value">${totalCustomers}</div>
                <div class="stat-label">Khách hàng</div>
            </div>



            <div class="analytics-stat-card"
                 onclick="document.getElementById('todayImportSection')
     .scrollIntoView({behavior:'smooth'})">

                <div class="stat-value">${todayImport}</div>

                <div class="stat-label">
                    Nhập hôm nay
                </div>

            </div>

            <div class="analytics-stat-card"
                 onclick="document.getElementById('todaySaleSection')
     .scrollIntoView({behavior:'smooth'})">

                <div class="stat-value">${todaySale}</div>

                <div class="stat-label">
                    Bán hôm nay
                </div>

            </div>
            <div class="analytics-stat-card"
                 onclick="document.getElementById('lowStockSection')
     .scrollIntoView({behavior:'smooth'})">

                <div class="stat-value">${lowStock}</div>

                <div class="stat-label">
                    Sắp hết hàng
                </div>

            </div>

            <div class="analytics-stat-card"
                 onclick="document.getElementById('deadStockSection')
     .scrollIntoView({behavior:'smooth'})">

                <div class="stat-value">${deadStock}</div>

                <div class="stat-label">
                    Hàng ế > 6 tháng
                </div>

            </div>

        </div>



        <div class="analytics-chart-grid">

            <div class="analytics-chart-card">

                <h3>Doanh thu 6 tháng gần nhất</h3>

                <canvas id="revenueChart"></canvas>

            </div>

            <div class="analytics-chart-card">

                <h3>Tỉ lệ trạng thái đơn hàng</h3>

                <canvas id="statusChart"></canvas>

            </div>

        </div>


        <div class="analytics-table-container"
             id="lowStockSection">

            <h2>⚠️ Sản phẩm sắp hết hàng</h2>

            <table>

                <thead>
                <tr>
                    <th>Ảnh</th>
                    <th>Tên sản phẩm</th>
                    <th>Tồn kho</th>
                    <th>Min Stock</th>
                    <th>Thiếu</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${lowStockProducts}" var="p">

                    <tr>

                        <td>

                            <img src="${p.mainImage}"
                                 class="analytics-product-image">

                        </td>

                        <td>${p.name}</td>

                        <td>${p.inventoryQuantity}</td>

                        <td>${p.minStock}</td>
                        <td>

                            <span class="stock-danger">

                                    ${p.minStock - p.inventoryQuantity}

                            </span>

                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>

        </div>
        <div class="analytics-pagination">

            <c:forEach begin="1"
                       end="${totalLowStockPages}"
                       var="i">

                <a href="?lowStockPage=${i}&deadStockPage=${deadStockPage}"
                   class="${i == lowStockPage ? 'active-page' : ''}">

                        ${i}

                </a>

            </c:forEach>

        </div>
        <div class="analytics-table-container"
             id="deadStockSection">

            <h2>📦 Hàng ế trên 6 tháng</h2>

            <table>

                <thead>
                <tr>
                    <th>Ảnh</th>
                    <th>Tên sản phẩm</th>
                    <th>Tồn kho</th>
                    <th>Ngày nhập cuối</th>
                    <th>Ngày bán cuối</th>
                    <th>Số ngày ế</th>
                    <th>Trạng thái</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${deadStockProducts}" var="p">

                    <tr>

                        <td>

                            <img src="${p.mainImage}"
                                         width="60">

                        </td>

                        <td>${p.name}</td>

                        <td>${p.inventoryQuantity}</td>
                        <td>
                            <c:choose>

                                <c:when test="${not empty p.lastImportDate}">
                                    ${p.lastImportDate.toString().replace('T',' ')}
                                </c:when>

                                <c:otherwise>
                                    -
                                </c:otherwise>

                            </c:choose>
                        </td>

                        <td>
                            <c:choose>

                                <c:when test="${not empty p.lastSaleDate}">
                                    ${p.lastSaleDate.toString().replace('T',' ')}
                                </c:when>

                                <c:otherwise>
                                    Chưa từng bán
                                </c:otherwise>

                            </c:choose>
                        </td>
                        <td>

                                ${p.deadDays} ngày

                        </td>
                        <td>
                            <span class="alert-badge alert-danger">
                                HÀNG Ế
                            </span>
                        </td>


                    </tr>

                </c:forEach>

                </tbody>

            </table>

        </div>
        <div class="analytics-pagination">

            <c:forEach begin="1"
                       end="${totalDeadStockPages}"
                       var="i">

                <a href="?deadStockPage=${i}&lowStockPage=${lowStockPage}"
                   class="${i == deadStockPage ? 'active-page' : ''}">

                        ${i}

                </a>

            </c:forEach>

        </div>
        <div class="analytics-detail-grid">
            <div class="analytics-table-container"
                 id="todayImportSection">

            <h2>📥 Chi tiết nhập hôm nay</h2>

            <table>

                <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Lý do</th>
                    <th>Thời gian</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${todayImports}" var="t">

                    <tr>

                        <td>${t.productName}</td>

                        <td>${t.quantity}</td>

                        <td>${t.reason}</td>

                        <td>
                                ${t.createdAt.toString().replace('T',' ')}
                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>

        </div>
            <div class="analytics-table-container"
                 id="todaySaleSection">

                <h2>📤 Chi tiết bán hôm nay</h2>

            <table>

                <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Lý do</th>
                    <th>Thời gian</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${todaySales}" var="t">

                    <tr>

                        <td>${t.productName}</td>

                        <td>${t.quantity}</td>

                        <td>${t.reason}</td>

                        <td>
                                ${t.createdAt.toString().replace('T',' ')}
                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>

        </div>

        </div>
    </div>
</div>

<script>

    const revenueLabels = [
        <c:forEach var="item" items="${monthlyRevenue}">
        "${item.month}",
        </c:forEach>
    ];

    const revenueData = [
        <c:forEach var="item" items="${monthlyRevenue}">
        ${item.revenue},
        </c:forEach>
    ];

    new Chart(document.getElementById('revenueChart'), {
        type: 'bar',
        data: {
            labels: revenueLabels,
            datasets: [{
                label: 'Doanh thu',
                data: revenueData,
                backgroundColor: '#4f46e5',
                borderRadius: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                },
                datalabels: {
                    color: '#ffffff',
                    anchor: 'center',
                    align: 'center',
                    font: {
                        weight: 'bold',
                        size: 12
                    },
                    formatter: function (value) {
                        return (value / 1_000_000).toFixed(1) + ' tr';
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function (value) {
                            return value.toLocaleString('vi-VN') + ' đ';
                        }
                    }
                }
            }
        },
        plugins: [ChartDataLabels]
    });




    if (window.statusChart instanceof Chart) {
        window.statusChart.destroy();
    }

    window.statusChart = new Chart(document.getElementById('statusChart'), {
        type: 'doughnut',
        data: {
            labels: ['Đang xử lý', 'Đang giao', 'Đã giao', 'Đã huỷ'],
            datasets: [{
                data: [
                    ${pending},
                    ${delivering},
                    ${delivered},
                    ${cancelled}
                ],
                backgroundColor: [
                    '#facc15',
                    '#38bdf8',
                    '#22c55e',
                    '#ef4444'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
</script>

</body>
</html>
