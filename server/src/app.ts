import express from "express";
import sensorRoutes from "./routes/sensorRoutes";
import ledRoutes from "./routes/ledRoutes";
import motionRoutes from "./routes/motionRoutes";
import flameRoutes from "./routes/flameRoutes";
import resetRoutes from "./routes/resetRoutes";
import supernovaRoutes from "./routes/supernovaRoutes";


const app = express();
app.use(express.json());

// ✅ ルートを `/api` プレフィックス付きで登録
app.use("/api", sensorRoutes);
app.use("/api", ledRoutes);
app.use("/api", motionRoutes);
app.use("/api", flameRoutes);
app.use("/api", resetRoutes);
app.use("/api", supernovaRoutes);

export default app;
