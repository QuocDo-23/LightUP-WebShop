package code.web.lightup.dao;



import code.web.lightup.model.OrderItem;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;
import code.web.lightup.model.Order;


import java.util.*;


public class OrderDAO {

    private final Jdbi jdbi;

    public OrderDAO() {
        this.jdbi = BaseDao.get();
    }


    public int insertOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, recipient_name, recipient_phone, recipient_email, " +
                "shipping_house_number, shipping_commune, shipping_district, shipping_address_detail, " +
                "order_date, total, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind(0, order.getUserId())
                .bind(1, order.getRecipientName())
                .bind(2, order.getRecipientPhone())
                .bind(3, order.getRecipientEmail())
                .bind(4, order.getShippingHouseNumber())
                .bind(5, order.getShippingCommune())
                .bind(6, order.getShippingDistrict())
                .bind(7, order.getShippingAddressDetail())
                .bind(8, order.getOrderDate())
                .bind(9, order.getTotal())
                .bind(10, order.getStatus())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .findOne()
                .orElse(-1));
    }


    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, orderId)
                    .map((rs, ctx) -> {
                        Order order = new Order();
                        order.setId(rs.getInt("id"));

                        Integer userId = (Integer) rs.getObject("user_id");
                        order.setUserId(userId);

                        order.setRecipientName(rs.getString("recipient_name"));
                        order.setRecipientPhone(rs.getString("recipient_phone"));
                        order.setRecipientEmail(rs.getString("recipient_email"));
                        order.setShippingHouseNumber(rs.getString("shipping_house_number"));
                        order.setShippingCommune(rs.getString("shipping_commune"));
                        order.setShippingDistrict(rs.getString("shipping_district"));
                        order.setShippingAddressDetail(rs.getString("shipping_address_detail"));
                        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                        order.setTotal(rs.getDouble("total"));
                        order.setStatus(rs.getString("status"));

                        return order;
                    })
                    .findOne()
                    .orElse(null);
        });
    }


    public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC, id DESC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, userId)
                    .map((rs, ctx) -> {
                        Order order = new Order();
                        order.setId(rs.getInt("id"));

                        Integer uid = (Integer) rs.getObject("user_id");
                        order.setUserId(uid);

                        order.setRecipientName(rs.getString("recipient_name"));
                        order.setRecipientPhone(rs.getString("recipient_phone"));
                        order.setRecipientEmail(rs.getString("recipient_email"));
                        order.setShippingHouseNumber(rs.getString("shipping_house_number"));
                        order.setShippingCommune(rs.getString("shipping_commune"));
                        order.setShippingDistrict(rs.getString("shipping_district"));
                        order.setShippingAddressDetail(rs.getString("shipping_address_detail"));
                        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                        order.setTotal(rs.getDouble("total"));
                        order.setStatus(rs.getString("status"));

                        return order;
                    })
                    .list();
        });
    }


    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, status)
                    .bind(1, orderId)
                    .execute();
            return rows > 0;
        });
    }


    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, orderId)
                    .execute();
            return rows > 0;
        });
    }


    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .map((rs, ctx) -> {
                        Order order = new Order();
                        order.setId(rs.getInt("id"));

                        Integer userId = (Integer) rs.getObject("user_id");
                        order.setUserId(userId);

                        order.setRecipientName(rs.getString("recipient_name"));
                        order.setRecipientPhone(rs.getString("recipient_phone"));
                        order.setRecipientEmail(rs.getString("recipient_email"));
                        order.setShippingHouseNumber(rs.getString("shipping_house_number"));
                        order.setShippingCommune(rs.getString("shipping_commune"));
                        order.setShippingDistrict(rs.getString("shipping_district"));
                        order.setShippingAddressDetail(rs.getString("shipping_address_detail"));
                        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                        order.setTotal(rs.getDouble("total"));
                        order.setStatus(rs.getString("status"));

                        return order;
                    })
                    .list();
        });
    }


    public boolean insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO order_details (order_id, product_id, product_name, img, product_material, " +
                "price, quantity, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, item.getOrderId())
                    .bind(1, item.getProductId())
                    .bind(2, item.getProductName())
                    .bind(3, item.getImg())
                    .bind(4, item.getProductMaterial())
                    .bind(5, item.getPrice())
                    .bind(6, item.getQuantity())
                    .bind(7, item.getSubtotal())
                    .execute();
            return rows > 0;
        });
    }


    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, orderId)
                    .map((rs, ctx) -> {
                        OrderItem item = new OrderItem();
                        item.setId(rs.getInt("id"));
                        item.setOrderId(rs.getInt("order_id"));
                        item.setProductId(rs.getInt("product_id"));
                        item.setProductName(rs.getString("product_name"));
                        item.setImg(rs.getString("img"));
                        item.setProductMaterial(rs.getString("product_material"));
                        item.setPrice(rs.getDouble("price"));
                        item.setQuantity(rs.getInt("quantity"));
                        item.setSubtotal(rs.getDouble("subtotal"));
                        return item;
                    })
                    .list();
        });
    }


    public OrderItem getOrderItemById(int itemId) {
        String sql = "SELECT * FROM order_details WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, itemId)
                    .map((rs, ctx) -> {
                        OrderItem item = new OrderItem();
                        item.setId(rs.getInt("id"));
                        item.setOrderId(rs.getInt("order_id"));
                        item.setProductId(rs.getInt("product_id"));
                        item.setProductName(rs.getString("product_name"));
                        item.setImg(rs.getString("img"));
                        item.setProductMaterial(rs.getString("product_material"));
                        item.setPrice(rs.getDouble("price"));
                        item.setQuantity(rs.getInt("quantity"));
                        item.setSubtotal(rs.getDouble("subtotal"));
                        return item;
                    })
                    .findOne()
                    .orElse(null);
        });
    }


    public boolean deleteOrderItem(int itemId) {
        String sql = "DELETE FROM order_details WHERE id = ?";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, itemId)
                    .execute();
            return rows > 0;
        });
    }


    public boolean deleteOrderItemsByOrderId(int orderId) {
        String sql = "DELETE FROM order_details WHERE order_id = ?";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, orderId)
                    .execute();
            return rows > 0;
        });
    }


    public int countOrdersByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ? AND status != 'delivered' AND status != 'cancelled'";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, userId)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
        });
    }


    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM orders WHERE status != 'cancelled'";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .mapTo(Double.class)
                    .findOne()
                    .orElse(0.0);
        });
    }


    public int getTotalOrderCount() {
        String sql = "SELECT COUNT(*) FROM orders";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
        });
    }


    public int getOrderCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, status)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
        });
    }


    public int getTodayOrderCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(order_date) = CURDATE()";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
        });
    }


    public double getCurrentMonthRevenue() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM orders " +
                "WHERE MONTH(order_date) = MONTH(CURDATE()) " +
                "AND YEAR(order_date) = YEAR(CURDATE()) " +
                "AND status != 'cancelled'";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .mapTo(Double.class)
                    .findOne()
                    .orElse(0.0);
        });
    }


    public int getCurrentMonthOrderCount() {
        String sql = "SELECT COUNT(*) FROM orders " +
                "WHERE MONTH(order_date) = MONTH(CURDATE()) " +
                "AND YEAR(order_date) = YEAR(CURDATE())";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
        });
    }

    public List<Map<String, Object>> getTopSellingProducts(int limit) {

        String sql = """
        SELECT 
            p.name AS product_name,
            COALESCE(i.img, 'no-image.png') AS img,
            c.name AS category_name,
            SUM(od.quantity) AS total_sold,
            SUM(od.price * od.quantity) AS revenue
        FROM order_details od
        JOIN orders o ON od.order_id = o.id
        JOIN product p ON od.product_id = p.id
        JOIN categories c ON p.category_id = c.id
        LEFT JOIN Image i 
            ON i.id = (
                SELECT MIN(id)
                FROM Image
                WHERE ref_id = p.id AND type = 'product'
            )
        WHERE o.status != 'cancelled'
        GROUP BY p.id, p.name, c.name, i.img
        ORDER BY total_sold DESC
        LIMIT :limit
    """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .map((rs, ctx) -> {
                            Map<String, Object> product = new HashMap<>();
                            product.put("productName", rs.getString("product_name"));
                            product.put("img", rs.getString("img"));
                            product.put("category", rs.getString("category_name"));
                            product.put("totalSold", rs.getInt("total_sold"));
                            product.put("revenue", rs.getDouble("revenue"));
                            return product;
                        })
                        .list()
        );
    }



    public List<Order> getRecentOrders(int limit) {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC, id DESC LIMIT ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, limit)
                    .map((rs, ctx) -> {
                        Order order = new Order();
                        order.setId(rs.getInt("id"));
                        order.setUserId((Integer) rs.getObject("user_id"));
                        order.setRecipientName(rs.getString("recipient_name"));
                        order.setRecipientPhone(rs.getString("recipient_phone"));
                        order.setRecipientEmail(rs.getString("recipient_email"));
                        order.setShippingHouseNumber(rs.getString("shipping_house_number"));
                        order.setShippingCommune(rs.getString("shipping_commune"));
                        order.setShippingDistrict(rs.getString("shipping_district"));
                        order.setShippingAddressDetail(rs.getString("shipping_address_detail"));
                        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                        order.setTotal(rs.getDouble("total"));
                        order.setStatus(rs.getString("status"));
                        return order;
                    })
                    .list();
        });
    }


    public List<Map<String, Object>> getMonthlyRevenue(int months) {
        String sql = "SELECT DATE_FORMAT(order_date, '%Y-%m') as month, " +
                "SUM(total) as revenue, COUNT(*) as order_count " +
                "FROM orders " +
                "WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL ? MONTH) " +
                "AND status != 'cancelled' " +
                "GROUP BY DATE_FORMAT(order_date, '%Y-%m') " +
                "ORDER BY month DESC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, months)
                    .map((rs, ctx) -> {
                        Map<String, Object> data = new HashMap<>();
                        data.put("month", rs.getString("month"));
                        data.put("revenue", rs.getDouble("revenue"));
                        data.put("orderCount", rs.getInt("order_count"));
                        return data;
                    })
                    .list();
        });
    }



    /**
     * Lấy danh sách đơn hàng có phân trang và lọc (PHÂN TRANG TẠI DAO)
     *
     * @param page          Trang hiện tại (bắt đầu từ 1)
     * @param pageSize      Số bản ghi trên mỗi trang
     * @param status        Lọc theo trạng thái (null = tất cả)
     * @param searchKeyword Từ khóa tìm kiếm (tên khách, email, mã đơn)
     * @param sortBy        Sắp xếp theo (order_date, total, status)
     * @param sortOrder     Thứ tự (ASC, DESC)
     * @return Danh sách đơn hàng
     */
    public List<Order> getOrdersWithPagination(int page, int pageSize, String status,
                                               String searchKeyword, String sortBy, String sortOrder) {

        int offset = (page - 1) * pageSize;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.*, u.name as user_name, u.email as user_email ");
        sql.append("FROM orders o ");
        sql.append("LEFT JOIN user u ON o.user_id = u.id ");
        sql.append("WHERE 1=1 ");


        List<Object> params = new ArrayList<>();
        if (status != null && !status.isEmpty() && !status.equals("all")) {
            sql.append("AND o.status = ? ");
            params.add(status);
        }

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND (o.recipient_name LIKE ? OR o.recipient_email LIKE ? OR o.id = ?) ");
            String likeKeyword = "%" + searchKeyword.trim() + "%";
            params.add(likeKeyword);
            params.add(likeKeyword);

            try {
                int orderId = Integer.parseInt(searchKeyword.trim());
                params.add(orderId);
            } catch (NumberFormatException e) {
                params.add(-1); // ID không hợp lệ
            }
        }


        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "order_date";
        }
        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "DESC";
        }

        sql.append("ORDER BY o.").append(sortBy).append(" ").append(sortOrder).append(" ");
        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(sql.toString());

            // Bind parameters
            for (int i = 0; i < params.size(); i++) {
                query.bind(i, params.get(i));
            }

            return query.map((rs, ctx) -> {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId((Integer) rs.getObject("user_id"));
                order.setRecipientName(rs.getString("recipient_name"));
                order.setRecipientPhone(rs.getString("recipient_phone"));
                order.setRecipientEmail(rs.getString("recipient_email"));
                order.setShippingHouseNumber(rs.getString("shipping_house_number"));
                order.setShippingCommune(rs.getString("shipping_commune"));
                order.setShippingDistrict(rs.getString("shipping_district"));
                order.setShippingAddressDetail(rs.getString("shipping_address_detail"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotal(rs.getDouble("total"));
                order.setStatus(rs.getString("status"));

                // Thêm thông tin user
                order.setUserName(rs.getString("user_name"));
                order.setUserEmail(rs.getString("user_email"));

                return order;
            }).list();
        });
    }


    public int countOrders(String status, String searchKeyword) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM orders o ");
        sql.append("LEFT JOIN user u ON o.user_id = u.id ");
        sql.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty() && !status.equals("all")) {
            sql.append("AND o.status = ? ");
            params.add(status);
        }

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND (o.recipient_name LIKE ? OR o.recipient_email LIKE ? OR o.id = ?) ");
            String likeKeyword = "%" + searchKeyword.trim() + "%";
            params.add(likeKeyword);
            params.add(likeKeyword);
            try {
                int orderId = Integer.parseInt(searchKeyword.trim());
                params.add(orderId);
            } catch (NumberFormatException e) {
                params.add(-1);
            }
        }

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.bind(i, params.get(i));
            }
            return query.mapTo(Integer.class).findOne().orElse(0);
        });
    }


    public boolean hasOrderReview(int orderId) {
        String sql = "SELECT COUNT(*) FROM review_order WHERE order_id = ?";
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind(0, orderId)
                .mapTo(Integer.class)
                .one() > 0);
    }


    public Map<String, Object> getOrderReview(int orderId) {
        String sql = "SELECT ro.*, u.name as user_name " +
                "FROM review_order ro " +
                "LEFT JOIN user u ON ro.user_id = u.id " +
                "WHERE ro.order_id = ?";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, orderId)
                        .map((rs, ctx) -> {
                            Map<String, Object> review = new HashMap<>();
                            review.put("id", rs.getInt("id"));
                            review.put("orderId", rs.getInt("order_id"));
                            review.put("userId", rs.getInt("user_id"));
                            review.put("userName", rs.getString("user_name"));
                            review.put("rating", rs.getInt("rating"));
                            review.put("content", rs.getString("content"));
                            review.put("date", rs.getTimestamp("date"));
                            return review;
                        })
                        .findOne()
                        .orElse(null)
        );
    }


    public Map<String, Integer> getOrderStatusStatistics() {
        String sql = "SELECT status, COUNT(*) as count FROM orders GROUP BY status";

        Map<String, Integer> stats = new HashMap<>();

        jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> {
                            stats.put(rs.getString("status"), rs.getInt("count"));
                            return null;
                        })
                        .list()
        );


        stats.putIfAbsent("pending", 0);
        stats.putIfAbsent("processing", 0);
        stats.putIfAbsent("shipped", 0);
        stats.putIfAbsent("delivered", 0);
        stats.putIfAbsent("cancelled", 0);

        return stats;
    }


    public int bulkUpdateOrderStatus(List<Integer> orderIds, String newStatus) {
        if (orderIds == null || orderIds.isEmpty()) {
            return 0;
        }

        String placeholders = String.join(",", Collections.nCopies(orderIds.size(), "?"));
        String sql = "UPDATE orders SET status = ? WHERE id IN (" + placeholders + ")";

        return jdbi.withHandle(handle -> {
            var update = handle.createUpdate(sql);
            update.bind(0, newStatus);
            for (int i = 0; i < orderIds.size(); i++) {
                update.bind(i + 1, orderIds.get(i));
            }
            return update.execute();
        });
    }

    public List<Order> exportOrders(String status, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.*, u.name as user_name, u.email as user_email ");
        sql.append("FROM orders o ");
        sql.append("LEFT JOIN user u ON o.user_id = u.id ");
        sql.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty() && !status.equals("all")) {
            sql.append("AND o.status = ? ");
            params.add(status);
        }

        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append("AND DATE(o.order_date) >= ? ");
            params.add(fromDate);
        }

        if (toDate != null && !toDate.isEmpty()) {
            sql.append("AND DATE(o.order_date) <= ? ");
            params.add(toDate);
        }

        sql.append("ORDER BY o.order_date DESC");

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.bind(i, params.get(i));
            }

            return query.map((rs, ctx) -> {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId((Integer) rs.getObject("user_id"));
                order.setRecipientName(rs.getString("recipient_name"));
                order.setRecipientPhone(rs.getString("recipient_phone"));
                order.setRecipientEmail(rs.getString("recipient_email"));
                order.setShippingHouseNumber(rs.getString("shipping_house_number"));
                order.setShippingCommune(rs.getString("shipping_commune"));
                order.setShippingDistrict(rs.getString("shipping_district"));
                order.setShippingAddressDetail(rs.getString("shipping_address_detail"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotal(rs.getDouble("total"));
                order.setStatus(rs.getString("status"));
                order.setUserName(rs.getString("user_name"));
                order.setUserEmail(rs.getString("user_email"));
                return order;
            }).list();
        });
    }

    public boolean addOrderReview(int orderId, int userId, int rating, String reviewText) {
        String sql = "INSERT INTO review_order (order_id, user_id, rating, content, date) " +
                "VALUES (?, ?, ?, ?, NOW())";

        return jdbi.withHandle(handle -> {
            try {
                System.out.println("=== BẮT ĐẦU INSERT REVIEW ===");
                System.out.println("orderId: " + orderId);
                System.out.println("userId: " + userId);
                System.out.println("rating: " + rating);
                System.out.println("content: " + reviewText);

                int rows = handle.createUpdate(sql)
                        .bind(0, orderId)
                        .bind(1, userId)
                        .bind(2, rating)
                        .bind(3, reviewText)
                        .execute();

                System.out.println("Rows inserted: " + rows);
                return rows > 0;
            } catch (Exception e) {
                System.out.println("Lỗi insert review: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }
    public boolean updateShippingAddress(int orderId, String house, String commune, String district, String detail) {
        String sql = "UPDATE orders SET shipping_house_number = ?, shipping_commune = ?, shipping_district = ?, shipping_address_detail = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, house)
                    .bind(1, commune)
                    .bind(2, district)
                    .bind(3, detail)
                    .bind(4, orderId)
                    .execute();
            return rows > 0;
        });
    }
}