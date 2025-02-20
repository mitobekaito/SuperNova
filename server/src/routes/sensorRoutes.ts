import express from "express";
import { getLatestSensorData, saveSensorData } from "../controllers/sensorController";

const router = express.Router();

router.get("/sensor-data", getLatestSensorData);
router.post("/sensor-data", saveSensorData);

export default router;
