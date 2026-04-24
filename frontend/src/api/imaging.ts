import api from "./index";
import type { ImagingOrderDTO } from "@/types/imaging";

export const getImagingOrder = (orderId: number) =>
  api.get<ImagingOrderDTO>(`/imaging/${orderId}`);
