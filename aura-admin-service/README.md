# AURA - Admin + Report Module (TV5) — bản đơn giản hoá

Backend: Spring Boot 3.3.4 + Java 21 + H2 (mặc định) / PostgreSQL (tuỳ chọn).

## Điểm khác so với bản trước (kiến trúc đơn giản hơn, ít file hơn)

- **Không tách Service interface/Impl** — Controller gọi thẳng Repository, business logic gọn
  trong từng method controller. Phù hợp quy mô đồ án môn học, dễ đọc/dễ sửa.
- **Role/Permission gộp thành enum** (`User.Role`) thay vì 2 bảng riêng — bớt 1 tầng phức tạp
  vì đồ án không cần phân quyền chi tiết tới từng permission.
- **Bỏ NotificationTemplate riêng** — việc gửi thông báo khi duyệt/từ chối Clinic được ghi chú
  là điểm cần nối với module Notification thật sau này (không làm giả CRUD template không cần thiết).
- **4 Controller thay vì 9** — gộp theo nhóm chức năng liên quan:
  - `AdminUserController` → UC1, UC2, UC3, UC4 (User/Doctor/Clinic)
  - `AdminConfigController` → UC6, UC8 (System Config, Service Package)
  - `AdminDashboardController` → UC9, UC10, UC11, UC12 (Billing, Dashboard, Audit Log)
  - `DevAuthController` → sinh JWT test cục bộ

**Tổng: 23 file Java** (so với ~35 file bản trước).

## Cách chạy

```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

Không cần cài Maven hay PostgreSQL — dùng Maven Wrapper + H2 (RAM). Service chạy ở `http://localhost:8083`.

Lấy token test (vì mọi API admin yêu cầu JWT role ADMIN — NFR-12):
```
GET http://localhost:8083/api/dev/token?userId=1&role=ADMIN
```
Dùng token đó làm header `Authorization: Bearer <token>` khi gọi các API `/api/admin/**`.

## Danh sách API

| UC | Endpoint | Method |
|---|---|---|
| UC1,2 | `/api/admin/users?role=USER` | GET |
| UC1,2 | `/api/admin/users/{id}/status?status=DISABLED` | PATCH |
| UC3 | `/api/admin/clinics?status=PENDING` | GET |
| UC4 | `/api/admin/clinics/{id}/review` | POST |
| UC3 | `/api/admin/clinics/{id}/suspend` | POST |
| UC6 | `/api/admin/configs` | GET / PUT |
| UC8 | `/api/admin/packages` | GET / POST |
| UC8 | `/api/admin/packages/{id}/deactivate` | PATCH |
| UC9 | `/api/admin/transactions` | GET |
| UC10,11 | `/api/admin/dashboard/stats` | GET |
| UC12 | `/api/admin/audit-logs?page=0&size=20` | GET |

## Đánh đổi cần lưu ý (nói rõ cho hội đồng nếu được hỏi)

Bản này ưu tiên **gọn/dễ đọc** hơn là **tách lớp chuẩn enterprise** (Controller-Service-Repository).
Nếu module phình to hơn (nhiều business rule phức tạp), nên tách lại Service layer để dễ test
và tái sử dụng logic — đây là trade-off thường gặp giữa tốc độ phát triển và khả năng mở rộng.
