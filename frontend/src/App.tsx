import { Routes, Route, Navigate } from "react-router-dom";
import AppLayout from "@/components/Layout/AppLayout";
import Login from "@/pages/Login";
import Dashboard from "@/pages/Dashboard";
import Patient360 from "@/pages/Patient360";
import Timeline from "@/pages/Timeline";
import LabDetail from "@/pages/LabDetail";
import ImagingDetail from "@/pages/ImagingDetail";
import { useAuthStore } from "@/stores/authStore";

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { token } = useAuthStore();
  return token ? <>{children}</> : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/"
        element={
          <PrivateRoute>
            <AppLayout />
          </PrivateRoute>
        }
      >
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="patient360/:patientId" element={<Patient360 />} />
        <Route path="timeline/:patientId" element={<Timeline />} />
        <Route path="lab/:orderId" element={<LabDetail />} />
        <Route path="imaging/:orderId" element={<ImagingDetail />} />
      </Route>
    </Routes>
  );
}
