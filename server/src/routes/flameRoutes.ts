import express from "express";
import { handleFlameCommand } from "../controllers/flameController";

const router = express.Router();

// ✅ flame コマンドを受け付けるエンドポイント
router.post("/flame-command", handleFlameCommand);

export default router;
