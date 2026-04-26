import api from "./index";
import type { LoginRequest, LoginResponse, User } from "@/types/auth";

export const login = (data: LoginRequest) =>
  api.post<LoginResponse>("/auth/login", data);

export const getCurrentUser = () => api.get<User>("/auth/current");
