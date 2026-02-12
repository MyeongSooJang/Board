SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS board_file; -- board 참조
DROP TABLE IF EXISTS report; -- member, board 참조
DROP TABLE IF EXISTS comment; -- board, member, comment 참조
DROP TABLE IF EXISTS board_like; -- member, board 참조
DROP TABLE IF EXISTS board; -- member 참조
DROP TABLE IF EXISTS refresh_token; -- 독립적
DROP TABLE IF EXISTS member; -- 독립적

SET FOREIGN_KEY_CHECKS = 1;

-- Member 테이블
CREATE TABLE member
(
    member_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    age         INT          NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    phone       VARCHAR(255) NOT NULL,
    role        VARCHAR(255) NOT NULL DEFAULT 'GUEST'
        CHECK (role IN ('GUEST', 'ADMIN')),
    create_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time TIMESTAMP    NULL,
    INDEX idx_member_name (name),
    INDEX idx_member_delete (delete_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Board 테이블
CREATE TABLE board
(
    board_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    content       TEXT,
    member_id     BIGINT       NOT NULL,
    view_count    BIGINT       NOT NULL DEFAULT 0,
    comment_count BIGINT       NOT NULL DEFAULT 0,
    like_count    BIGINT       NOT NULL DEFAULT 0,
    create_time   TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time   TIMESTAMP    NULL,
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    INDEX idx_board_member (member_id),
    INDEX idx_board_delete_update (delete_time, update_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Board Like 테이블
CREATE TABLE board_like
(
    like_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    board_id  BIGINT NOT NULL,
    UNIQUE KEY uk_member_board (member_id, board_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (board_id) REFERENCES board (board_id),
    INDEX idx_like_board (board_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Comment 테이블
CREATE TABLE comment
(
    comment_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_parent_id BIGINT,
    comment_content   TEXT      NOT NULL,
    board_id          BIGINT    NOT NULL,
    member_id         BIGINT    NOT NULL,
    create_time       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time       TIMESTAMP NULL,
    FOREIGN KEY (board_id) REFERENCES board (board_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (comment_parent_id) REFERENCES comment (comment_id),
    INDEX idx_comment_board (board_id),
    INDEX idx_comment_member (member_id),
    INDEX idx_comment_parent (comment_parent_id),
    INDEX idx_comment_delete (delete_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Refresh Token 테이블
CREATE TABLE refresh_token
(
    token_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(512) NOT NULL,
    expire_date   TIMESTAMP    NOT NULL,
    INDEX idx_token_username (username
        ),
    INDEX idx_token_expire (expire_date)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Report 테이블
CREATE TABLE report
(
    report_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT       NOT NULL,
    board_id    BIGINT       NOT NULL,
    type        VARCHAR(255) NOT NULL DEFAULT 'OTHER'
        CHECK (type in ('SPAM', 'ABUSIVE', 'ILLEGAL', 'OTHER')),
    content     TEXT         NULL,
    status      VARCHAR(20)           DEFAULT 'SUBMITTED'
        CHECK (status IN ('SUBMITTED', 'APPROVED', 'REJECTED')),
    create_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delete_time TIMESTAMP    NULL,
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (board_id) REFERENCES board (board_id),
    UNIQUE KEY uk_board_member (member_id, board_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- BoardFile

# CREATE TABLE board_file
# (
#     file_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
#     board_id   BIGINT       NOT NULL,
#     original_name VARCHAR(255) NOT NULL,
#     saved_name  VARCHAR(255) NOT NULL,
#     size  INT          NOT NULL,
#     url   VARCHAR(500), -- S3
#     upload_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
#
#     FOREIGN KEY (board_id) REFERENCES board (board_id) ON DELETE CASCADE
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;