import React, { useReducer } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Home from "./components/Home";

import Login from "./components/Login";
import Profile from "./components/Profile";

import AddUser from "./components/Admin/AddUser";
import AdminThongKe from "./components/Admin/AdminThongKe";

import AddTieuChi from "./components/GiaoVu/AddTieuChi";
import XepDeTai from "./components/GiaoVu/XepDeTai";
import DanhSachThucHien from "./components/GiaoVu/DanhSachThucHien";
import TaoHoiDong from "./components/GiaoVu/TaoHoiDong";
import GiaoDeTai from "./components/GiaoVu/GiaoDeTai";
import KhoaHoiDong from "./components/GiaoVu/KhoaHoiDong";
import ThongKeKhoaLuan from "./components/GiaoVu/ThongKeKhoaLuan";

import ChamDiem from "./components/GiangVien/ChamDiem";

import XemDeTai from "./components/SinhVien/XemDeTai";

import { Container } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';

import { MyUserContext, MyDispatchContext } from "./config/Contexts";
import MyUserReducer from "./reducers/MyUserReducer";

function App() {
  // Khởi tạo state user và dispatch
  console.log("MyUserReducer =", MyUserReducer);
  const [user, dispatch] = useReducer(MyUserReducer, null);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <BrowserRouter>
          <Header />
          <Container className="my-4">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/admin/add-user" element={<AddUser />} />
              <Route path="/admin/thongke" element={<AdminThongKe />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/giaovu/tieuchi" element={<AddTieuChi />} />
              <Route path="/giaovu/xepdetai" element={<XepDeTai />} />
              <Route path="/giaovu/danhsachthuchien" element={<DanhSachThucHien />} />
              <Route path="/giaovu/taohoidong" element={<TaoHoiDong />} />
              <Route path="/giaovu/giaodetai" element={<GiaoDeTai />} />
              <Route path="/giaovu/khoahoidong" element={<KhoaHoiDong />} />
              <Route path="/giaovu/thongke" element={<ThongKeKhoaLuan />} />
              <Route path="/giangvien/chamdiem" element={<ChamDiem />} />
              <Route path="/sinhvien/xemdetai" element={<XemDeTai />} />
            </Routes>
          </Container>
          <Footer />
        </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>
  );
}

export default App;
