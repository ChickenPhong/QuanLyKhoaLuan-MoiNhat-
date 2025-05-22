import { createContext } from "react";

// Lưu thông tin người dùng hiện tại
export const MyUserContext = createContext();

// Dùng để login/logout, dispatch reducer
export const MyDispatchContext = createContext();
