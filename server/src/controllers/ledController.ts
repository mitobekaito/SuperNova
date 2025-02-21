import { Request, Response } from "express";

// ✅ LED のコマンドを受け取る処理
export const handleLedCommand = (req: Request, res: Response): void => {
  try {
    const { led_command } = req.body; // ✅ 変更: `led_command` を受け取る

    if (!led_command) {
      res.status(400).json({ message: "❌ LED コマンドが不足しています" });
      return;
    }

    // ✅ 受信したコマンドをコンソールに出力
    console.log(`💡 受信した LED コマンド: ${led_command}`);

    // ✅ 受け取ったデータをそのままレスポンスとして返す
    res.json({ message: `✅ LED コマンド '${led_command}' を受信しました` });
  } catch (error) {
    console.error("❌ LED コマンド処理エラー:", error);
    res.status(500).json({ message: "LED コマンド処理エラー", error });
  }
};
