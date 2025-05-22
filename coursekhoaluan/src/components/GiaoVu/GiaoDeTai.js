
import React, { useState, useEffect } from "react";
import { Container, Row, Col, Form, Button, Table, Alert, Badge } from "react-bootstrap";

const GiaoDeTai = ({
  yearOptions = [],
  initialKhoaHoc = "",
  deTais = [],
  svMap = {},
  hdMap = {},
  message = "",
  onSelectKhoaHoc,
  onSubmitRandomAssign,
  onViewList,
}) => {
  const [khoaHoc, setKhoaHoc] = useState(initialKhoaHoc);
  const [alertMsg, setAlertMsg] = useState(message);

  useEffect(() => {
    setAlertMsg(message);
  }, [message]);

  const handleKhoaHocChange = (e) => {
    setKhoaHoc(e.target.value);
  };

  const handleViewList = (e) => {
    e.preventDefault();
    if (onViewList) onViewList(khoaHoc);
  };

  const handleRandomAssign = (e) => {
    e.preventDefault();
    if (onSubmitRandomAssign) onSubmitRandomAssign(khoaHoc);
  };

  return (
    <Container className="mt-4">
      <h2 className="text-center text-primary mb-4">Giao đề tài cho Hội đồng</h2>

      <Form onSubmit={handleViewList} className="mb-3">
        <Row className="align-items-end">
          <Col md={4}>
            <Form.Group controlId="khoaHoc">
              <Form.Label>Chọn khóa</Form.Label>
              <Form.Select
                value={khoaHoc}
                onChange={handleKhoaHocChange}
                required
              >
                <option value="" disabled>
                  -- Chọn khóa học --
                </option>
                {yearOptions.map((y) => (
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
        <Alert variant="info" onClose={() => setAlertMsg("")} dismissible>
          {alertMsg}
        </Alert>
      )}

      <Form onSubmit={handleRandomAssign} className="d-flex justify-content-end mb-3">
        <input type="hidden" name="khoaHoc" value={khoaHoc} />
        <Button variant="success" type="submit">
          Giao đề tài ngẫu nhiên cho hội đồng
        </Button>
      </Form>

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