import express from 'express';
import { handleResetCommand } from '../controllers/resetController';

const router = express.Router();

// ✅ motion コマンドを受け付けるエンドポイント
router.post('/reset-command', handleResetCommand);

export default router;
