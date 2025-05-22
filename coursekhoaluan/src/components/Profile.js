import React, { useContext, useState } from "react";
import { Modal, Button, Form, Alert, Spinner } from "react-bootstrap";
import axios from "axios";
import { MyUserContext } from "../config/Contexts";

const Profile = () => {
  const user = useContext(MyUserContext);
  const [showModal, setShowModal] = useState(false);

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");
  const [changingPassword, setChangingPassword] = useState(false);

  // Nếu chưa có user => hiển thị lỗi
  if (!user)
    return (
      <div className="alert alert-danger mt-5">
        Bạn chưa đăng nhập hoặc không có quyền truy cập.
      </div>
    );

  // Xử lý đổi mật khẩu
  const handleChangePassword = async (e) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      setErrorMsg("Mật khẩu mới và xác nhận không khớp");
      return;
    }

    if (newPassword.length < 6) {
      setErrorMsg("Mật khẩu mới phải có ít nhất 6 ký tự");
      return;
    }

    setErrorMsg("");
    setSuccessMsg("");
    setChangingPassword(true);

    try {
      const res = await axios.post(
        "/api/secure/change-password",
        {
          oldPassword,
          newPassword,
          confirmPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

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
          <p>
            <strong>Email:</strong> {user.email}
          </p>
          <p>
            <strong>Vai trò:</strong> {user.role}
          </p>
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
            <Form.Group className="mb-3" controlId="oldPassword">
              <Form.Label>Mật khẩu cũ</Form.Label>
              <Form.Control
                type="password"
                value={oldPassword}
                onChange={(e) => setOldPassword(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="newPassword">
              <Form.Label>Mật khẩu mới</Form.Label>
              <Form.Control
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="confirmPassword">
              <Form.Label>Xác nhận mật khẩu mới</Form.Label>
              <Form.Control
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </Form.Group>

            <Button
              variant="primary"
              type="submit"
              disabled={changingPassword}
            >
              {changingPassword ? "Đang xử lý..." : "Lưu thay đổi"}
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default Profile;
