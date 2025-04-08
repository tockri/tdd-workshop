-- Scheduleテーブル
CREATE TABLE schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- スケジュールID (主キー)
    title VARCHAR(255) NOT NULL,                    -- イベント名
    description TEXT,                               -- イベントの説明
    start_time DATETIME NOT NULL,                   -- 開始日時
    end_time DATETIME NOT NULL,                     -- 終了日時
    user_id INT NOT NULL,                           -- ユーザーID (スケジュール作成者または関連ユーザー)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 作成日時
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新日時
    CONSTRAINT chk_end_after_start CHECK (end_time > start_time) -- 開始日時より終了日時が後
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;