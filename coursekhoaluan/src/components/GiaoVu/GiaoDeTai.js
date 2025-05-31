import React, { useState, useEffect } from "react";
import { Container, Row, Col, Form, Button, Table, Alert, Badge } from "react-bootstrap";
import { authApis } from "../../config/Apis";
import cookie from 'react-cookies';

const GiaoDeTai = () => {
  const [khoaHoc, setKhoaHoc] = useState("");
  const [khoaHocList, setKhoaHocList] = useState([]);
  const [deTais, setDeTais] = useState([]);
  const [svMap, setSvMap] = useState({});
  const [hdMap, setHdMap] = useState({});
  const [alertMsg, setAlertMsg] = useState("");
  const [alertVariant, setAlertVariant] = useState("info");

  useEffect(() => {
    const loadKhoaHoc = async () => {
      try {
        const res = await authApis().get("/giaovu/khoahoc");
        setKhoaHocList(res.data || []);
      } catch (error) {
        setAlertMsg("Lỗi tải danh sách khóa học: " + error.message);
        setAlertVariant("danger");
      }
    };
    loadKhoaHoc();
  }, []);

  // Hàm gọi API lấy danh sách đề tài + sinh viên + hội đồng theo khóa học
  const fetchDanhSach = async () => {
    const token = cookie.load('token');
    console.log("Token gửi lên API:", token);
    if (!token) {
      setAlertMsg("Token không tồn tại, vui lòng đăng nhập lại");
      setAlertVariant("warning");
      return;
    }
    if (!khoaHoc) {
      setAlertMsg("Vui lòng chọn khóa học");
      setAlertVariant("warning");
      return;
    }
    try {
      // Giả sử backend có API REST /api/giaovu/giaodetai?khoaHoc=xxxx
      // Trả về dạng { deTais: [...], svMap: {...}, hdMap: {...} }
      const res = await authApis().get("giaovu/giaodetai", { params: { khoaHoc } });

      // Nếu dữ liệu trả về đúng cấu trúc
      setDeTais(res.data.deTais || []);
      setSvMap(res.data.svMap || {});
      setHdMap(res.data.hdMap || {});

      setAlertMsg("");
    } catch (error) {
      setAlertMsg("Lỗi khi tải dữ liệu danh sách đề tài: " + (error.response?.data || error.message));
      setAlertVariant("danger");
    }
  };

  // Hàm gọi API giao đề tài ngẫu nhiên cho hội đồng
  const handleRandomAssign = async () => {
    if (!khoaHoc) {
      setAlertMsg("Vui lòng chọn khóa học trước khi giao đề tài!");
      setAlertVariant("warning");
      return;
    }
    try {
      await authApis().post(`giaovu/giaodetai/giao?khoaHoc=${khoaHoc}`);
      setAlertMsg("Giao đề tài thành công");
      setAlertVariant("success");

      // Sau khi giao xong thì tải lại danh sách
      await fetchDanhSach();
    } catch (error) {
      let errMsg = "Lỗi khi giao đề tài: ";
      if (error.response) {
        // Nếu backend trả lỗi dạng object hoặc string
        if (typeof error.response.data === "string") {
          errMsg += error.response.data;
        } else if (typeof error.response.data === "object") {
          errMsg += JSON.stringify(error.response.data);
        } else {
          errMsg += error.message;
        }
      } else {
        errMsg += error.message;
      }
      setAlertMsg(errMsg);
      setAlertVariant("danger");
    }
  };

  return (
    <Container className="mt-4">
      <h2 className="text-center text-primary mb-4">Giao đề tài cho Hội đồng</h2>

      <Form
        onSubmit={(e) => {
          e.preventDefault();
          fetchDanhSach();
        }}
        className="mb-3"
      >
        <Row className="align-items-end">
          <Col md={4}>
            <Form.Group controlId="khoaHoc">
              <Form.Label>Chọn khóa</Form.Label>
              <Form.Select
                value={khoaHoc}
                onChange={(e) => setKhoaHoc(e.target.value)}
                required
              >
                <option value="" disabled>
                  -- Chọn khóa học --
                </option>
                {khoaHocList.map((y) => (
                  <option key={y} value={y}>
                    Khóa {y}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
          </Col>
          <Col md={3}>
            <Button type="submit" variant="primary">
              Xem danh sách
            </Button>
          </Col>
        </Row>
      </Form>

      {alertMsg && (
        <Alert variant={alertVariant} onClose={() => setAlertMsg("")} dismissible>
          {alertMsg}
        </Alert>
      )}

      <div className="d-flex justify-content-end mb-3">
        <Button variant="success" onClick={handleRandomAssign}>
          Giao đề tài ngẫu nhiên cho hội đồng
        </Button>
      </div>

      <Table bordered striped>
        <thead>
          <tr>
            <th>#</th>
            <th>Tên đề tài</th>
            <th>Sinh viên</th>
            <th>Trạng thái</th>
          </tr>
        </thead>
        <tbody>
          {deTais.length > 0 ? (
            deTais.map((dt, index) => (
              <tr key={dt.id || index}>
                <td>{index + 1}</td>
                <td>{dt.title}</td>
                <td>{svMap[dt.id] || "Chưa có sinh viên"}</td>
                <td>
                  {hdMap[dt.id] ? (
                    <Badge bg="success">Đã giao</Badge>
                  ) : (
                    <Badge bg="warning" text="dark">
                      Chưa giao
                    </Badge>
                  )}
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={4} className="text-center">
                Không có đề tài nào
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
};

export default GiaoDeTai;