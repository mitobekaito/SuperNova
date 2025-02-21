import { Request, Response } from "express";

// ✅ LED のコマンドを受け取る処理
export const handleMotionCommand = (req: Request, res: Response): void => {
  try {
    const { motion_command } = req.body; // ✅ 変更: `motion_command` を受け取る

    if (!motion_command) {
      res.status(400).json({ message: "❌ Motion コマンドが不足しています" });
      return;
    }

    // ✅ 受信したコマンドをコンソールに出力
    console.log(`⚠️ 受信した Motion コマンド: ${motion_command}`);

    // ✅ 受け取ったデータをそのままレスポンスとして返す
    res.json({ message: `✅ Motion コマンド '${motion_command}' を受信しました` });
  } catch (error) {
    console.error("❌ Motion コマンド処理エラー:", error);
    res.status(500).json({ message: "Motion コマンド処理エラー", error });
  }
};
