export interface LoginRequest {
  username: string;
  password: string;
}

export interface User {
  id: number;
  username: string;
  name: string;
  departmentId: number;
  positionId: number;
}

export interface LoginResponse {
  token: string;
}
