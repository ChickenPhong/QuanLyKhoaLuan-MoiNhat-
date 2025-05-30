import { useContext } from "react";
import { Button, Container, Image, Nav, Navbar as RBNavbar } from "react-bootstrap";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { MyDispatchContext, MyUserContext } from "../../config/Contexts";

const Header = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatchContext);

    const isLoginPage = location.pathname === "/login";

    const handleLogout = () => {
        dispatch({ type: "logout" });

        if (location.pathname === "/") {
            navigate("/login");
        }
        // Nếu không ở trang chủ thì không chuyển trang
    };

    return (
        <RBNavbar bg="dark" variant="dark" expand="lg" className="mb-3">
            <Container>
                <RBNavbar.Brand as={Link} to="/">Quản lý Khóa Luận</RBNavbar.Brand>
                {!isLoginPage && (
                    <>
                        <RBNavbar.Toggle aria-controls="basic-navbar-nav" />
                        <RBNavbar.Collapse id="basic-navbar-nav">
                            <Nav className="me-auto">
                                <Nav.Link as={Link} to="/">Trang chủ</Nav.Link>

                                {user?.role === "ROLE_GIAOVU" && (
                                    <>
                                        <Nav.Link as={Link} to="/giaovu/tieuchi">Tiêu chí</Nav.Link>
                                        <Nav.Link as={Link} to="/giaovu/xepdetai">Xếp Đề tài</Nav.Link>
                                        <Nav.Link as={Link} to="/giaovu/danhsachthuchien">Danh sách thực hiện</Nav.Link>
                                        <Nav.Link as={Link} to="/giaovu/taohoidong">Hội đồng</Nav.Link>
                                        <Nav.Link as={Link} to="/giaovu/giaodetai">Giao đề tài</Nav.Link>
                                        <Nav.Link as={Link} to="/giaovu/khoahoidong">Khóa Hội đồng</Nav.Link>
                                        <Nav.Link as={Link} to="/giaovu/thongke">Thống kê</Nav.Link>
                                    </>
                                )}

                                {user?.role === "ROLE_ADMIN" && (
                                    <>
                                        <Nav.Link as={Link} to="/admin/add-user">Thêm người dùng</Nav.Link>
                                        <Nav.Link as={Link} to="/admin/thongke">Thống kê</Nav.Link>
                                    </>
                                )}

                                {user?.role === "ROLE_GIANGVIEN" && (
                                    <Nav.Link
                                        as={Link}
                                        to={`/giangvien/chamdiem?giangVienPhanBienId=${user.id}`}
                                    >
                                        Chấm điểm
                                    </Nav.Link>
                                )}
                                
                                {user?.role === "ROLE_SINHVIEN" && (
                                    <>
                                        <Nav.Link as={Link} to="/sinhvien/xemdetai">Xem Đề Tài</Nav.Link>                                      
                                    </>
                                )}

                                <Nav.Link as={Link} to="/profile">Thông tin cá nhân</Nav.Link>
                            </Nav>

                            <Nav className="d-flex align-items-center gap-3">
                                {user === null ? (
                                    <Nav.Link as={Link} to="/login" className="text-danger">
                                        Đăng nhập
                                    </Nav.Link>
                                ) : (
                                    <>
                                        <div className="d-flex align-items-center gap-2 text-white navbar-text">
                                            <Image
                                                src={user.avatar || "https://via.placeholder.com/40"}
                                                roundedCircle
                                                width={40}
                                                height={40}
                                                alt="Avatar"
                                            />
                                            <span title={user.username}>Xin chào, {user.username}</span>
                                        </div>
                                        <Button
                                            variant="outline-danger"
                                            size="sm"
                                            onClick={handleLogout}
                                        >
                                            Đăng xuất
                                        </Button>
                                    </>
                                )}
                            </Nav>
                        </RBNavbar.Collapse>
                    </>
                )}
            </Container>
        </RBNavbar>
    );
};

export default Header;
