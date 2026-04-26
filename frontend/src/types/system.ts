// frontend/src/types/system.ts

export interface UserDTO {
  id: number;
  username: string;
  name: string;
  departmentId?: number;
  departmentName?: string;
  positionId?: number;
  positionName?: string;
  enabled: boolean;
  roles: string[];
  createdAt?: string;
}

export interface RoleDTO {
  id: number;
  name: string;
  description?: string;
  menuCodes: string[];
  menuNames?: string[];
}

export interface DepartmentDTO {
  id: number;
  name: string;
  parentId?: number;
  parentName?: string;
}

export interface PositionDTO {
  id: number;
  name: string;
  departmentId?: number;
  departmentName?: string;
  dataScope?: string;
}

export interface AccessLogDTO {
  id: number;
  userId?: number;
  username?: string;
  patientId?: number;
  patientName?: string;
  action?: string;
  requestDetail?: string;
  ipAddress?: string;
  accessDatetime?: string;
}
