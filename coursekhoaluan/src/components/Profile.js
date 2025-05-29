
import React, { useContext, useState } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";
import { MyUserContext } from "../config/Contexts";
import Chat from "./Chat";
import { authApis } from "../config/Apis";

const Profile = () => {
  const user = useContext(MyUserContext);
  const [showModal, setShowModal] = useState(false);

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");
  const [changingPassword, setChangingPassword] = useState(false);

  if (!user)
    return (
      <div className="alert alert-danger mt-5">
        Bạn chưa đăng nhập hoặc không có quyền truy cập.
      </div>
    );

  const handleChangePassword = async (e) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      setErrorMsg("Mật khẩu mới và xác nhận không khớp");
      return;
    }

    if (newPassword.length < 3) {
      setErrorMsg("Mật khẩu mới phải có ít nhất 3 ký tự");
      return;
    }

    setErrorMsg("");
    setSuccessMsg("");
    setChangingPassword(true);

    try {
      const res = await authApis().post("/secure/change-password", {
        oldPassword,
        newPassword,
        confirmPassword,
      });

      setSuccessMsg(res.data);
      setOldPassword("");
      setNewPassword("");
      setConfirmPassword("");
      setShowModal(false);
    } catch (error) {
      if (error.response) {
        setErrorMsg(error.response.data || "Đổi mật khẩu thất bại");
      } else {
        setErrorMsg("Lỗi hệ thống");
      }
    } finally {
      setChangingPassword(false);
    }
  };

  return (
    <div className="container mt-5">
      <h2>Thông tin cá nhân</h2>
      {errorMsg && <Alert variant="danger">{errorMsg}</Alert>}
      {successMsg && <Alert variant="success">{successMsg}</Alert>}

      <div className="row">
        <div className="col-md-4">
          <img
            src={user.avatar || "/images/default-avatar.png"}
            alt="Avatar"
            className="img-thumbnail"
            style={{ height: 220, objectFit: "cover" }}
          />
          <h4 className="mt-2">{user.username}</h4>
        </div>
        <div className="col-md-8">
          {/* Luôn hiển thị họ và tên, email */}
          <p><strong>Họ và tên:</strong> {user.fullname || user.username}</p>
          <p><strong>Email:</strong> {user.email}</p>

          {/* Hiển thị tùy role */}
          {user.role && user.role.toLowerCase().includes("giangvien") && (
            <p><strong>Khoa:</strong> {user.khoa || "Chưa cập nhật"}</p>
          )}

          {user.role && user.role.toLowerCase().includes("sinhvien") && (
            <>
              <p><strong>Khoa:</strong> {user.khoa || "Chưa cập nhật"}</p>
              <p><strong>Khóa học:</strong> {user.khoaHoc || "Chưa cập nhật"}</p>
              <p><strong>Ngành:</strong> {user.nganh || "Chưa cập nhật"}</p>
            </>
          )}

          <Button variant="warning" onClick={() => setShowModal(true)}>
            Đổi mật khẩu
          </Button>
        </div>
      </div>

      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Đổi mật khẩu</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleChangePassword}>
            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu cũ</Form.Label>
              <Form.Control
                type="password"
                value={oldPassword}
                onChange={(e) => setOldPassword(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu mới</Form.Label>
              <Form.Control
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Xác nhận mật khẩu mới</Form.Label>
              <Form.Control
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </Form.Group>

            <Button type="submit" variant="primary" disabled={changingPassword}>
              {changingPassword ? "Đang xử lý..." : "Lưu thay đổi"}
            </Button>
          </Form>
        </Modal.Body>
      </Modal>

      <h3 className="mt-5">Nhắn tin với người dùng khác</h3>
      <Chat />
    </div>
  );
};

export default Profile;