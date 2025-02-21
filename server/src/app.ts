import express from "express";
import sensorRoutes from "./routes/sensorRoutes";
import ledRoutes from "./routes/ledRoutes";
import motionRoutes from "./routes/motionRoutes";
import flameRoutes from "./routes/flameRoutes";

const app = express();
app.use(express.json());

// ✅ ルートを `/api` プレフィックス付きで登録
app.use("/api", sensorRoutes);
app.use("/api", ledRoutes);
app.use("/api", motionRoutes);
app.use("/api", flameRoutes);

export default app;
