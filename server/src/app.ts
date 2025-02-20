import express from "express";
import sensorRoutes from "./routes/sensorRoutes";
import ledRoutes from "./routes/ledRoutes";

const app = express();
app.use(express.json());

// ✅ ルートを `/api` プレフィックス付きで登録
app.use("/api", sensorRoutes);
app.use("/api", ledRoutes);

export default app;
