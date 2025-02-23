import express from 'express';
import { handleSupernovaCommand } from '../controllers/supernovaController';

const router = express.Router();

// ✅ supernova コマンドを受け付けるエンドポイント
router.post('/supernova-command', handleSupernovaCommand);

export default router;