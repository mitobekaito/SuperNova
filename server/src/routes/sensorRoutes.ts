import { Router } from "express";
import {
  getLatestSensorData,
  getAllSensorData,
  saveSensorData,
} from "../controllers/sensorController";

const router = Router();

// 最新1件のデータ
router.get("/sensor-latest", getLatestSensorData);

// 全データ
router.get("/sensor-data", getAllSensorData);

// データ保存 (POST)
router.post("/sensor-data", saveSensorData);

export default router;
