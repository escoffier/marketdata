USE employees;

drop table if exists stockinfo;
CREATE TABLE stockinfo (
                         stock_no            INT             NOT NULL,
                         name                VARCHAR(32)     NOT NULL,
                         listing_date        DATE            NOT NULL,
                         total_share         FLOAT  ,
                         circulation_share   FLOAT,
                         PRIMARY KEY (stock_no)
);

insert into stockinfo values (600000,   '浦发银行',    '1999-11-10', '2935208.04', 2810376.39),
                             (600004,	  '白云机场'	,	  '2003-04-28',	206932.05,	206932.05),
                             (600006,	  '东风汽车',		  '1999-07-27',	200000,	    200000),
                             (600007,	  '中国国贸',		  '1999-03-12',	100728.25,	100728.25),
                             (600008,	  '首创股份',		  '2000-04-27',	568544.82,	482061.41),
                             (600009,   '上海机场',		  '1998-02-18',	192695.84,	109347.64),
                             (600010,	  '包钢股份',		  '2001-03-09',	4558503.26,	3167721.16),
                             (600011,	  '华能国际',		  '2001-12-06',	1569809.34,	1520038.34)
