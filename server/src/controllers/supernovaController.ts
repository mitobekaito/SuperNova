import { Request, Response } from 'express';
import { sendCommandToArduino } from '../serial/serialCommand';

// Supernova のコマンドを受け取る処理
export const handleSupernovaCommand = (req: Request, res: Response): void => {
    try {
        const { supernova_command } = req.body; // ✅ `supernova_command` を受け取る
    
        if (!supernova_command) {
        res.status(400).json({ message: '❌ Supernova コマンドが不足しています' });
        return;
        }
    
        // ✅ 受信したコマンドをコンソールに出力
        console.log(`🌠 受信した Supernova コマンド: ${supernova_command}`);
    
        sendCommandToArduino(supernova_command); // ✅ `supernova_command` を Arduino に送信
    
        // ✅ 受け取ったデータをそのままレスポンスとして返す
        res.json({
        message: `✅ Supernova コマンド '${supernova_command}' を受信しました`,
        });
    } catch (error) {
        console.error('❌ Supernova コマンド処理エラー:', error);
        res.status(500).json({ message: 'Supernova コマンド処理エラー', error });
    }
    }