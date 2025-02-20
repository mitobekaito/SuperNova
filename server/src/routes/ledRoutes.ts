import express from "express";
import { handleLedCommand } from "../controllers/ledController";

const router = express.Router();

// ✅ LED コマンドを受け付けるエンドポイント
router.post("/led-command", handleLedCommand);

export default router;
