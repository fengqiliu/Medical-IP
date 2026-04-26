// frontend/src/api/system.ts
import api from "./index";
import type {
  UserDTO,
  RoleDTO,
  DepartmentDTO,
  PositionDTO,
  AccessLogDTO,
} from "@/types/system";

export const getUsers = (params?: {
  keyword?: string;
  departmentId?: number;
  enabled?: boolean;
}) => api.get<UserDTO[]>("/system/users", { params });

export const getUserById = (id: number) =>
  api.get<UserDTO>(`/system/users/${id}`);

export const createUser = (data: {
  username: string;
  name: string;
  departmentId?: number;
  positionId?: number;
  enabled?: boolean;
  roleIds?: number[];
}) => api.post("/system/users", data);

export const updateUser = (
  id: number,
  data: {
    username: string;
    name: string;
    departmentId?: number;
    positionId?: number;
    enabled?: boolean;
    roleIds?: number[];
  },
) => api.put(`/system/users/${id}`, data);

export const updateUserPassword = (id: number, newPassword: string) =>
  api.put(`/system/users/${id}/password`, { newPassword });

export const setUserEnabled = (id: number, enabled: boolean) =>
  api.put(`/system/users/${id}/enabled`, null, { params: { enabled } });

export const deleteUser = (id: number) => api.delete(`/system/users/${id}`);

export const getAllUsers = () => api.get<UserDTO[]>("/system/users/all");

export const getRoles = () => api.get<RoleDTO[]>("/system/roles");

export const getRoleById = (id: number) =>
  api.get<RoleDTO>(`/system/roles/${id}`);

export const createRole = (data: {
  name: string;
  description?: string;
  menuCodes?: string[];
}) => api.post("/system/roles", data);

export const updateRole = (
  id: number,
  data: { name: string; description?: string; menuCodes?: string[] },
) => api.put(`/system/roles/${id}`, data);

export const deleteRole = (id: number) => api.delete(`/system/roles/${id}`);

export const getDepartments = () =>
  api.get<DepartmentDTO[]>("/system/departments");

export const createDepartment = (data: { name: string; parentId?: number }) =>
  api.post("/system/departments", data);

export const updateDepartment = (
  id: number,
  data: { name: string; parentId?: number },
) => api.put(`/system/departments/${id}`, data);

export const deleteDepartment = (id: number) =>
  api.delete(`/system/departments/${id}`);

export const getPositions = () => api.get<PositionDTO[]>("/system/positions");

export const createPosition = (data: {
  name: string;
  departmentId?: number;
  dataScope?: string;
}) => api.post("/system/positions", data);

export const updatePosition = (
  id: number,
  data: { name: string; departmentId?: number; dataScope?: string },
) => api.put(`/system/positions/${id}`, data);

export const deletePosition = (id: number) =>
  api.delete(`/system/positions/${id}`);

export const getAccessLogs = (params?: {
  userId?: number;
  patientId?: number;
  action?: string;
  startDate?: string;
  endDate?: string;
  page?: number;
  size?: number;
}) => api.get<AccessLogDTO[]>("/system/access-logs", { params });
