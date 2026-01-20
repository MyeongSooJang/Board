DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS board_like;
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS member;

-- Member 테이블
CREATE TABLE member
(
    member_no    BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id    VARCHAR(255) UNIQUE,
    member_pwd   VARCHAR(255) NOT NULL,
    member_name  VARCHAR(255) NOT NULL,
    member_age   INT          NOT NULL,
    member_email VARCHAR(255) NOT NULL,
    member_phone VARCHAR(255) NOT NULL,
    member_role  VARCHAR(255) NOT NULL DEFAULT 'GUEST'
        CHECK (member_role IN ('GUEST', 'ADMIN')),
    create_time  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time  TIMESTAMP    NULL,
    INDEX idx_member_name (member_name),
    INDEX idx_member_delete (delete_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Board 테이블
CREATE TABLE board
(
    board_no         BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_title      VARCHAR(255) NOT NULL,
    board_content    TEXT,
    member_no        BIGINT       NOT NULL,
    board_view_count INT          NOT NULL DEFAULT 0,
    create_time      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time      TIMESTAMP    NULL,
    FOREIGN KEY (member_no) REFERENCES member (member_no),
    INDEX idx_board_member (member_no),
    INDEX idx_board_delete_update (delete_time, update_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Board Like 테이블
CREATE TABLE board_like
(
    like_no   BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_no BIGINT NOT NULL,
    board_no  BIGINT NOT NULL,
    UNIQUE KEY uk_member_board (member_no, board_no),
    FOREIGN KEY (member_no) REFERENCES member (member_no),
    FOREIGN KEY (board_no) REFERENCES board (board_no),
    INDEX idx_like_board (board_no)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Comment 테이블
CREATE TABLE comment
(
    comment_no        BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_parent_no BIGINT,
    comment_content   TEXT      NOT NULL,
    board_no          BIGINT    NOT NULL,
    member_no         BIGINT    NOT NULL,
    create_time       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time       TIMESTAMP NULL,
    FOREIGN KEY (board_no) REFERENCES board (board_no),
    FOREIGN KEY (member_no) REFERENCES member (member_no),
    FOREIGN KEY (comment_parent_no) REFERENCES comment (comment_no),
    INDEX idx_comment_board (board_no),
    INDEX idx_comment_member (member_no),
    INDEX idx_comment_parent (comment_parent_no),
    INDEX idx_comment_delete (delete_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Refresh Token 테이블
CREATE TABLE refresh_token
(
    token_no      BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id     VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(512) NOT NULL,
    expire_date   TIMESTAMP    NOT NULL,
    INDEX idx_token_member_id (member_id),
    INDEX idx_token_expire (expire_date)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;