import express from "express";
import { handleMotionCommand } from "../controllers/motionController";

const router = express.Router();

// ✅ motion コマンドを受け付けるエンドポイント
router.post("/motion-command", handleMotionCommand);

export default router;