CREATE TABLE member
(
    member_no    BIGINT       NOT NULL AUTO_INCREMENT,
    member_id    VARCHAR(255) UNIQUE,
    member_pwd   VARCHAR(255) NOT NULL,
    member_name  VARCHAR(255) NOT NULL,
    member_age   INT          NOT NULL,
    member_email VARCHAR(255) NOT NULL,
    member_phone VARCHAR(255) NOT NULL,
    member_role  VARCHAR(255) NOT NULL DEFAULT 'GUEST'
        CHECK (member_role IN ('GUEST', 'ADMIN')),
    create_time  DATETIME              DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME,
    delete_time  DATETIME,
    PRIMARY KEY (member_no)
);

CREATE TABLE board
(
    board_no         BIGINT       NOT NULL AUTO_INCREMENT,
    board_title      VARCHAR(255) NOT NULL,
    board_content    TEXT,
    member_no        BIGINT       NOT NULL,
    board_view_count INT          NOT NULL DEFAULT 0,
    create_time      DATETIME              DEFAULT CURRENT_TIMESTAMP,
    update_time      DATETIME,
    delete_time      DATETIME,
    PRIMARY KEY (board_no),
    FOREIGN KEY (member_no) REFERENCES member (member_no)
);
CREATE TABLE board_like
(
    like_no   BIGINT       NOT NULL AUTO_INCREMENT,
    member_no BIGINT NOT NULL,
    board_no  BIGINT       NOT NULL,
    PRIMARY KEY (like_no),
    UNIQUE KEY (member_no, board_no)
);

CREATE TABLE comment
(
    comment_no        BIGINT NOT NULL AUTO_INCREMENT,
    comment_parent_no BIGINT,
    comment_content   TEXT   NOT NULL,
    board_no          BIGINT NOT NULL,
    member_no         BIGINT NOT NULL,
    create_time       DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time       DATETIME,
    delete_time       DATETIME,
    PRIMARY KEY (comment_no),
    FOREIGN KEY (board_no) REFERENCES board (board_no),
    FOREIGN KEY (member_no) REFERENCES member (member_no)
);

CREATE TABLE refresh_token
(
    token_no      BIGINT       NOT NULL AUTO_INCREMENT,
    member_id     VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    expire_date   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (token_no)
);
