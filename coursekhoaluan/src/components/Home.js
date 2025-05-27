import React, { useContext, useEffect, useState } from "react";
import { Alert, Button, Card, Col, Row, Table, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { MyUserContext } from "../config/Contexts";
import MySpinner from "./layout/MySpinner";
import { Image } from "react-bootstrap";
import { authApis, endpoints } from "../config/Apis";

const Home = () => {
  const user = useContext(MyUserContext);
  const [loading, setLoading] = useState(true);
  const [users, setUsers] = useState([]);
  const [deTaiList, setDeTaiList] = useState([]);
  const [khoaLuan, setKhoaLuan] = useState({ id: null, title: "", khoa: "" });
  const [msg, setMsg] = useState("");
  const nav = useNavigate();

  useEffect(() => {
    if (!user) {
      nav("/login");
      return;
    }
    const fetchData = async () => {
      if (user.role === "ROLE_ADMIN") {
        try {
          const res = await authApis().get(endpoints["get-users"]);
          setUsers(res.data);
        } catch (err) {
          setMsg("Lỗi tải danh sách người dùng.");
        }
      }
      setLoading(false);
    };
    fetchData();
  }, [user, nav]);

  useEffect(() => {
    const fetchDeTai = async () => {
      try {
        const res = await authApis().get(endpoints.detai + "/");
        setDeTaiList(res.data);
      } catch (error) {
        setMsg("Lỗi tải danh sách đề tài.");
      }
    };
    if (user && (user.role === "ROLE_GIAOVU" || user.role === "ROLE_ADMIN")) {
      fetchDeTai();
    }
  }, [user]);

  const deleteUser = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa người dùng này?")) return;
    try {
      const formData = new FormData();
      formData.append("userId", id);
      await authApis().post(endpoints["delete-user"], formData);
      const res = await authApis().get(endpoints["get-users"]);
      setUsers(res.data);
    } catch (err) {
      alert("Xóa thất bại, vui lòng thử lại.");
    }
  };

  const handleDeTaiSubmit = async (e) => {
    e.preventDefault();
    if (!khoaLuan.title.trim()) {
      setMsg("Vui lòng nhập tên đề tài");
      return;
    }
    const payload = {
      ...khoaLuan,
      khoa: user.khoa,
      status: "active"
    };
    try {
      if (khoaLuan.id) {
        await authApis().put(`${endpoints.detai}/${khoaLuan.id}`, payload);
      } else {
        await authApis().post(endpoints.detai + "/", payload);
      }
      setKhoaLuan({ id: null, title: "", khoa: "" });
      const res = await authApis().get(endpoints.detai + "/");
      setDeTaiList(res.data);
      setMsg("Thao tác thành công!");
    } catch (error) {
      setMsg("Thao tác thất bại: " + error.message);
    }
  };

  // Toggle status giữa "active" và "disabled"
  const handleToggleDeTaiStatus = async (id, currentStatus) => {
    const action = currentStatus === "active" ? "ẩn" : "hiện";
    if (!window.confirm(`Bạn có chắc chắn muốn ${action} đề tài này?`)) return;
    try {
      const old = deTaiList.find((d) => d.id === id);
      if (!old) {
        setMsg("Không tìm thấy đề tài.");
        return;
      }
      const newStatus = currentStatus === "active" ? "disabled" : "active";
      const payload = { ...old, status: newStatus };
      await authApis().put(`${endpoints.detai}/${id}`, payload);
      const res = await authApis().get(endpoints.detai + "/");
      setDeTaiList(res.data);
      setMsg(`${action.charAt(0).toUpperCase() + action.slice(1)} đề tài thành công.`);
    } catch (error) {
      setMsg(`${action.charAt(0).toUpperCase() + action.slice(1)} đề tài thất bại: ` + (error.response?.data?.message || error.message));
    }
  };

  const handleEditDeTai = (dt) => {
    setKhoaLuan({
      id: dt.id,
      title: dt.title,
      khoa: dt.khoa || user.khoa
    });
  };

  if (!user) return <Alert variant="danger">Bạn chưa đăng nhập!</Alert>;
  if (loading) return <MySpinner />;

  return (
    <>
      <h1 className="text-primary">
        Chào mừng {user.lastName} {user.firstName}
      </h1>

      <Row className="mt-4">
        <Col md={6}>
          <Card border="info" className="mb-3">
            <Card.Body>
              <Card.Title>Thông tin vai trò</Card.Title>
              {user.role === "ROLE_ADMIN" && (
                <Alert variant="info">
                  Bạn là quản trị viên. Hãy vào mục <strong>Quản lý người dùng</strong> để cấp tài khoản.
                </Alert>
              )}
              {user.role === "ROLE_GIAOVU" && (
                <Alert variant="success">
                  Bạn là giáo vụ. Hãy quản lý đề tài ngay bên dưới hoặc truy cập các chức năng khác.
                </Alert>
              )}
              {user.role === "ROLE_GIANGVIEN" && (
                <Alert variant="warning">
                  Bạn là giảng viên. Vui lòng vào mục <strong>Hội đồng</strong> để xem và chấm điểm khóa luận.
                </Alert>
              )}
              {user.role === "ROLE_SINHVIEN" && (
                <Alert variant="secondary">
                  Bạn là sinh viên. Bạn có thể xem điểm khóa luận và lịch bảo vệ của mình.
                </Alert>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {user.role === "ROLE_GIAOVU" && (
        <div>
          <h3>Quản lý đề tài khóa luận</h3>
          <Form onSubmit={handleDeTaiSubmit} className="mb-3">
            <Form.Control
              type="text"
              placeholder="Tên đề tài khóa luận"
              value={khoaLuan.title}
              onChange={(e) => setKhoaLuan({ ...khoaLuan, title: e.target.value })}
              required
            />
            <Button type="submit" className="mt-2">
              {khoaLuan.id ? "Cập nhật đề tài" : "Thêm đề tài"}
            </Button>
          </Form>

          <Table bordered striped>
            <thead>
              <tr>
                <th>#</th>
                <th>Tên đề tài</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {deTaiList.map((dt, idx) => (
                <tr key={dt.id}>
                  <td>{idx + 1}</td>
                  <td>{dt.title}</td>
                  <td>{dt.status}</td>
                  <td>
                    <Button
                      variant={dt.status === "active" ? "secondary" : "success"}
                      size="sm"
                      onClick={() => handleToggleDeTaiStatus(dt.id, dt.status)}
                    >
                      {dt.status === "active" ? "Ẩn đề tài" : "Hiện đề tài"}
                    </Button>
                    <Button
                      variant="warning"
                      size="sm"
                      onClick={() => handleEditDeTai(dt)}
                      className="ms-2"
                    >
                      Sửa
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
          {msg && <Alert variant="info">{msg}</Alert>}
        </div>
      )}

      {user.role === "ROLE_ADMIN" && (
        <div className="mt-5">
          <h4>Danh sách người dùng</h4>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>#</th>
                <th>Họ và tên</th>
                <th>Tên đăng nhập</th>
                <th>Email</th>
                <th>Vai trò</th>
                <th>Mật khẩu (mã hóa)</th>
                <th>Avatar</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {users.length === 0 && (
                <tr>
                  <td colSpan={8} className="text-center">
                    Chưa có người dùng
                  </td>
                </tr>
              )}
              {users.map((u, idx) => (
                <tr key={u.id || u.eid || idx}>
                  <td>{idx + 1}</td>
                  <td>{u.fullname}</td>
                  <td>{u.username}</td>
                  <td>{u.email}</td>
                  <td>{u.role}</td>
                  <td>
                    <input
                      className="form-control form-control-sm"
                      readOnly
                      value={u.password}
                    />
                  </td>
                  <td>
                    {u.avatar ? (
                      <Image src={u.avatar} width={50} height={50} roundedCircle />
                    ) : (
                      <span>No avatar</span>
                    )}
                  </td>
                  <td>
                    <Button
                      size="sm"
                      variant="danger"
                      onClick={() => deleteUser(u.id || u.eid)}
                    >
                      Xóa
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      )}
    </>
  );
};

export default Home;
