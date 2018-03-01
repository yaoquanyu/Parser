SELECT * FROM server_illegal_accesses WHERE access_count >= 100 and start_date >= "2017-01-01 13:00:00" and end_date <= "2017-01-01 14:00:00";

SELECT * FROM server_illegal_accesses WHERE IP = '192.168.228.188';
