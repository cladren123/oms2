CHANGE MASTER TO
    MASTER_HOST='master',
    MASTER_USER='replica',
    MASTER_PASSWORD='pass',
    MASTER_LOG_FILE='mysql-bin.000003',  -- Master에서 확인한 로그 파일 이름
    MASTER_LOG_POS=547;                    -- Master에서 확인한 로그 위치

START SLAVE;
