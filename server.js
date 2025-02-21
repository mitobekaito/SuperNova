require('dotenv').config();
const mongoose = require('mongoose');
const express = require('express');
const cors = require('cors');
const { SerialPort } = require('serialport');
const { ReadlineParser } = require('@serialport/parser-readline');

const app = express();
const PORT = process.env.PORT || 3000;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://127.0.0.1:27017/sensordata';
const SERIAL_PORT = process.env.SERIAL_PORT || 'COM3';
const BAUD_RATE = parseInt(process.env.BAUD_RATE) || 9600;
const MAX_RECORDS = 50; // Giới hạn số bản ghi trong MongoDB

app.use(cors());
app.use(express.json());

// ✅ Kết nối MongoDB
mongoose.connect(MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    connectTimeoutMS: 30000
}).then(() => console.log('✅ Kết nối MongoDB thành công'))
.catch(err => console.error('❌ Lỗi kết nối MongoDB:', err));

// ✅ Định nghĩa Schema
const SensorSchema = new mongoose.Schema({
  temperature: Number,
  humidity: Number,
  motion: Boolean,
  flame: Boolean,
  timestamp: { type: Date, default: Date.now }
});
const SensorData = mongoose.model('SensorData', SensorSchema);

// ✅ Kết nối SerialPort
const port = new SerialPort({ path: SERIAL_PORT, baudRate: BAUD_RATE });
const parser = port.pipe(new ReadlineParser({ delimiter: '\n' }));

// ✅ Nhận dữ liệu từ Arduino và lưu vào MongoDB
parser.on('data', async (data) => {
  try {
    const cleanData = data.trim();
    if (!cleanData) return;
    
    let jsonData;
    try {
      jsonData = JSON.parse(cleanData);
    } catch (parseError) {
      console.warn("⚠️ Dữ liệu nhận không hợp lệ:", cleanData);
      return;
    }

    console.log("📡 Nhận dữ liệu từ Arduino:", jsonData);
    
    if (!jsonData.temperature || !jsonData.humidity || typeof jsonData.motion !== 'boolean' || typeof jsonData.flame !== 'boolean') {
      console.warn("⚠️ Dữ liệu không đầy đủ hoặc sai định dạng:", jsonData);
      return;
    }

    // Kiểm tra số lượng bản ghi trong MongoDB
    const count = await SensorData.countDocuments();
    if (count >= MAX_RECORDS) {
      console.log("⚠️ Đạt giới hạn 50 bản ghi, xóa dữ liệu cũ...");
      await SensorData.deleteMany({}); // Xóa toàn bộ dữ liệu cũ
    }

    // Lưu bản ghi mới vào MongoDB
    const newData = new SensorData(jsonData);
    await newData.save();
    console.log("✅ Dữ liệu đã lưu vào MongoDB");
  } catch (error) {
    console.error("❌ Lỗi xử lý dữ liệu:", error);
  }
});

// ✅ API để lấy dữ liệu mới nhất từ MongoDB
app.get('/data', async (req, res) => {
  try {
    const data = await SensorData.find().sort({ timestamp: -1 }).limit(10);
    res.json(data);
  } catch (error) {
    res.status(500).json({ message: 'Lỗi lấy dữ liệu từ MongoDB' });
  }
});

// ✅ API để xóa toàn bộ dữ liệu trong MongoDB
app.delete('/data', async (req, res) => {
  try {
    await SensorData.deleteMany({});
    res.json({ message: 'Đã xóa toàn bộ dữ liệu' });
  } catch (error) {
    res.status(500).json({ message: 'Lỗi xóa dữ liệu' });
  }
});

app.listen(PORT, () => console.log(`🚀 Server chạy tại http://localhost:${PORT}`));
