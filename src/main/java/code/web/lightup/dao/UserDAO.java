package code.web.lightup.dao;

import code.web.lightup.model.User;
import code.web.lightup.util.BaseDao;
import code.web.lightup.util.PasswordUtil;
import org.jdbi.v3.core.Jdbi;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    private final Jdbi jdbi;

    public UserDAO() {
        this.jdbi = BaseDao.get();
    }

    /**
     * Đăng ký user mới
     */
    public boolean register(User user) {
        try {
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            return jdbi.withHandle(handle -> {
                int result = handle.createUpdate(
                                "INSERT INTO User (role_id, name, email, password) " +
                                        "VALUES (:roleId, :name, :email, :password)"
                        )
                        .bind("roleId", 2)
                        .bind("name", user.getName())
                        .bind("email", user.getEmail())
                        .bind("password", hashedPassword)
                        .execute();

                return result > 0;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đăng nhập
     */
    public Optional<User> login(String email, String password) {
        try {
            Optional<User> userOpt = jdbi.withHandle(handle ->
                    handle.createQuery(
                                    "SELECT u.*, r.name as role_name " +
                                            "FROM User u " +
                                            "LEFT JOIN Role r ON u.role_id = r.id " +
                                            "WHERE u.email = :email"
                            )
                            .bind("email", email)
                            .mapToBean(User.class)
                            .findFirst()
            );

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if ("Google".equalsIgnoreCase(user.getAuthProvider())){
                    return Optional.empty();
                }
                if (PasswordUtil.verifyPassword(password, user.getPassword())) {
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
    /**
     * Lấy thông tin khóa tài khoản
     */
    public Optional<User> getUserLoginInfo(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT id, email, password, failed_attempts, lock_until, auth_provider, status " +
                                        "FROM User WHERE email = :email"
                        )
                        .bind("email", email)
                        .map((rs, ctx) -> {
                            User user = new User();
                            user.setId(rs.getInt("id"));
                            user.setEmail(rs.getString("email"));
                            user.setPassword(rs.getString("password"));
                            user.setFailedAttempts(rs.getInt("failed_attempts"));
                            user.setAuthProvider(rs.getString("auth_provider"));
                            user.setStatus(rs.getString("status"));

                            Timestamp lockUntil = rs.getTimestamp("lock_until");
                            if (lockUntil != null) {
                                user.setLockUntil(lockUntil.toInstant()
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDateTime());                            }
                            return user;
                        })
                        .findFirst()
        );
    }

    /**
     * Kiểm tra email đã tồn tại
     */
    public boolean emailExists(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) FROM User WHERE email = :email"
                        )
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }
    /**
     * Tăng số lần đăng nhập sai
     */
    public void recordFailedAttempt(String email) {
        jdbi.withHandle(handle -> {
            handle.createUpdate(
                            "UPDATE User SET " +
                                    "failed_attempts = failed_attempts + 1, " +
                                    "lock_until = CASE " +
                                    "  WHEN failed_attempts + 1 >= 5 " +
                                    "  THEN DATE_ADD(UTC_TIMESTAMP(), INTERVAL 15 MINUTE) " +
                                    "  ELSE lock_until " +
                                    "END " +
                                    "WHERE email = :email"
                    )
                    .bind("email", email)
                    .execute();
            return null;
        });
    }

    /**
     * Reset sau khi đăng nhập thành công
     */
    public void resetFailedAttempts(String email) {
        jdbi.withHandle(handle -> {
            handle.createUpdate(
                            "UPDATE User SET failed_attempts = 0, lock_until = NULL " +
                                    "WHERE email = :email"
                    )
                    .bind("email", email)
                    .execute();
            return null;
        });
    }

    /**
     * Lấy user theo ID
     */
    public Optional<User> getUserById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT u.*, r.name as role_name " +
                                        "FROM User u " +
                                        "LEFT JOIN Role r ON u.role_id = r.id " +
                                        "WHERE u.id = :id"
                        )
                        .bind("id", id)
                        .mapToBean(User.class)
                        .findFirst()
        );
    }


    /**
     * Lấy user theo email
     */
    public Optional<User> getUserByEmail(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT u.*, r.name as role_name " +
                                        "FROM User u " +
                                        "LEFT JOIN Role r ON u.role_id = r.id " +
                                        "WHERE u.email = :email"
                            )
                            .bind("email", email)
                            .mapToBean(User.class)
                            .findFirst()
            ).map(user -> {
                String statusSql = "SELECT status FROM user WHERE email = :email";
                String status = jdbi.withHandle(handle ->
                    handle.createQuery(statusSql)
                        .bind("email", email)
                        .mapTo(String.class)
                        .findOne()
                        .orElse("active")
                );
                user.setStatus(status);
                return user;
            });
    }

    /**
     * Cập nhật mật khẩu
     */
    public boolean updatePassword(String email, String newPassword) {
        try {
            String hashedPassword = PasswordUtil.hashPassword(newPassword);

            return jdbi.withHandle(handle -> {
                int result = handle.createUpdate(
                                "UPDATE User SET password = :password WHERE email = :email"
                        )
                        .bind("email", email)
                        .bind("password", hashedPassword)
                        .execute();

                return result > 0;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin user
     */
    public boolean updateUser(User user) {
        try {
            return jdbi.withHandle(handle -> {
                int result = handle.createUpdate(
                                "UPDATE User SET name = :name, phone = :phone, " +
                                        "gender = :gender, date_of_birth = :dob, avatar_img = :avatar " +
                                        "WHERE id = :id"
                        )
                        .bind("id", user.getId())
                        .bind("name", user.getName())
                        .bind("phone", user.getPhone())
                        .bind("gender", user.getGender())
                        .bind("dob", user.getDateOfBirth())
                        .bind("avatar", user.getAvatarImg())
                        .execute();

                return result > 0;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật ảnh đại diện
     */
    public boolean updateAvatar(int userId, String avatarUrl) {
        String sql = "UPDATE user SET avatar_img = :avatar WHERE id = :id";

        int rows = jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("avatar", avatarUrl)
                        .bind("id", userId)
                        .execute()
        );

        return rows > 0;
    }

    /**
     * Đếm tổng số khách hàng
     */
    public int getTotalCustomerCount() {
        String sql = "SELECT COUNT(*) FROM user WHERE role_id = '2'";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
        });
    }


    public List<User> getAllCustomers() {
        String sql = "SELECT u.*, " +
                     "COUNT(o.id) as order_count, " +
                     "COALESCE(SUM(o.total), 0) as total_spent, " +
                     "u.status, " +
                     "r.name as role_name " +
                     "FROM user u " +
                     "LEFT JOIN orders o ON u.id = o.user_id AND o.status != 'cancelled' " +
                     "LEFT JOIN role r ON u.role_id = r.id " +
                     "WHERE u.role_id = 2 " +
                     "GROUP BY u.id, u.status, r.name " +
                     "ORDER BY u.id DESC";

        return jdbi.withHandle(handle -> 
            handle.createQuery(sql)
                .map((rs, ctx) -> {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setGender(rs.getString("gender"));
                    if (rs.getDate("date_of_birth") != null) {
                        user.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                    }
                    user.setAvatarImg(rs.getString("avatar_img"));

                    user.setOrderCount(rs.getInt("order_count"));
                    user.setTotalSpent(rs.getDouble("total_spent"));

                    user.setStatus(rs.getString("status"));
                    user.setRoleName(rs.getString("role_name"));

                    return user;
                })
                .list()
        );
    }
    
    /**
     * Cập nhật trạng thái tài khoản user (Khóa/Mở khóa)
     */
    public boolean updateUserStatus(int userId, String status) {
        String sql = "UPDATE user SET status = :status WHERE id = :id";
        
        return jdbi.withHandle(handle -> 
            handle.createUpdate(sql)
                .bind("status", status)
                .bind("id", userId)
                .execute() > 0
        );
    }

    /**
     * Cập nhật mật khẩu của user bằng ID
     */
    public boolean updatePasswordById(int userId, String newHashedPassword) {
        String sql = "UPDATE user SET password = :password WHERE id = :id";
        
        return jdbi.withHandle(handle -> 
            handle.createUpdate(sql)
                .bind("password", newHashedPassword)
                .bind("id", userId)
                .execute() > 0
        );
    }

    /**
     * Cập nhật vai trò (role_id) của user
     */
    public boolean updateUserRole(int userId, int newRoleId) {
        String sql = "UPDATE user SET role_id = :newRoleId WHERE id = :userId";
        
        return jdbi.withHandle(handle -> 
            handle.createUpdate(sql)
                .bind("newRoleId", newRoleId)
                .bind("userId", userId)
                .execute() > 0
        );
    }
    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM User WHERE email = :email";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapTo(int.class)
                        .one()
        ) > 0;
    }
    /**
     * Đăng ký user từ Google OAuth (không cần password)
     */
    public boolean registerGoogleUser(User user) {
        try {
            return jdbi.withHandle(handle -> {
                int result = handle.createUpdate(
                                "INSERT INTO User (role_id, name, email, password, avatar_img, auth_provider) " +
                                        "VALUES (:roleId, :name, :email, :password, :avatar, :provider)"
                        )
                        .bind("roleId", 2)
                        .bind("name", user.getName())
                        .bind("email", user.getEmail())
                        .bind("password", "")
                        .bind("avatar", user.getAvatarImg())
                        .bind("provider", "google")
                        .execute();

                return result > 0;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean unlockUser(int userId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "UPDATE User SET failed_attempts = 0, lock_until = NULL " +
                                        "WHERE id = :id"
                        )
                        .bind("id", userId)
                        .execute() > 0
        );
    }

    private Integer tryParseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> searchCustomers(String keyword) {

        Integer id = tryParseInt(keyword);

        String sql;

        if (id != null) {
            sql =
                    "SELECT u.id, u.name, u.email, u.phone, u.status, " +
                            "COUNT(o.id) as order_count, " +
                            "COALESCE(SUM(o.total), 0) as total_spent, " +
                            "r.name as role_name " +
                            "FROM user u " +
                            "LEFT JOIN orders o ON u.id = o.user_id AND o.status != 'cancelled' " +
                            "LEFT JOIN role r ON u.role_id = r.id " +
                            "WHERE u.role_id = 2 AND u.id = :id " +
                            "GROUP BY u.id, u.name, u.email, u.phone, u.status, r.name " +
                            "ORDER BY u.id DESC";

            return jdbi.withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("id", id)
                            .map((rs, ctx) -> {
                                User user = new User();
                                user.setId(rs.getInt("id"));
                                user.setName(rs.getString("name"));
                                user.setEmail(rs.getString("email"));
                                user.setPhone(rs.getString("phone"));
                                user.setOrderCount(rs.getInt("order_count"));
                                user.setTotalSpent(rs.getDouble("total_spent"));
                                user.setStatus(rs.getString("status"));
                                user.setRoleName(rs.getString("role_name"));
                                return user;
                            }).list()
            );

        } else {
            sql =
                    "SELECT u.id, u.name, u.email, u.phone, u.status, " +
                            "COUNT(o.id) as order_count, " +
                            "COALESCE(SUM(o.total), 0) as total_spent, " +
                            "r.name as role_name " +
                            "FROM user u " +
                            "LEFT JOIN orders o ON u.id = o.user_id AND o.status != 'cancelled' " +
                            "LEFT JOIN role r ON u.role_id = r.id " +
                            "WHERE u.role_id = 2 AND (u.name LIKE :kw OR u.email LIKE :kw) " +
                            "GROUP BY u.id, u.name, u.email, u.phone, u.status, r.name " +
                            "ORDER BY u.id DESC";

            return jdbi.withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("kw", "%" + keyword + "%")
                            .map((rs, ctx) -> {
                                User user = new User();
                                user.setId(rs.getInt("id"));
                                user.setName(rs.getString("name"));
                                user.setEmail(rs.getString("email"));
                                user.setPhone(rs.getString("phone"));
                                user.setOrderCount(rs.getInt("order_count"));
                                user.setTotalSpent(rs.getDouble("total_spent"));
                                user.setStatus(rs.getString("status"));
                                user.setRoleName(rs.getString("role_name"));
                                return user;
                            }).list()
            );
        }
    }

}