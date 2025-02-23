import { Request, Response } from 'express';
import { sendCommandToArduino } from '../serial/serialCommand';

// Reset のコマンドを受け取る処理
export const handleResetCommand = (req: Request, res: Response): void => {
    try {
        const { reset_command } = req.body; // ✅ `reset_command` を受け取る
    
        if (!reset_command) {
        res.status(400).json({ message: '❌ Reset コマンドが不足しています' });
        return;
        }
    
        // ✅ 受信したコマンドをコンソールに出力
        console.log(`🔄 受信した Reset コマンド: ${reset_command}`);
    
        sendCommandToArduino(reset_command); // ✅ `reset_command` を Arduino に送信
    
        // ✅ 受け取ったデータをそのままレスポンスとして返す
        res.json({
        message: `✅ Reset コマンド '${reset_command}' を受信しました`,
        });
    } catch (error) {
        console.error('❌ Reset コマンド処理エラー:', error);
        res.status(500).json({ message: 'Reset コマンド処理エラー', error });
    }
    }