TRUNCATE t_permission;

LOCK TABLES `t_permission` WRITE;
/*!40000 ALTER TABLE `t_permission` DISABLE KEYS */;
INSERT INTO `t_permission` VALUES
(1,1,'movie','movie:list','list'),
(2,1,'comment','comment:list','list'),
(3,1,'comment','comment:insert','insert'),
(4,1,'comment','comment:delete','delete'),
(5,1,'chart','chart:list','list'),
(6,1,'rate','rate:insert','insert'),
(7,1,'rate','rate:update','update'),
(8,1,'tag','tag:list','list'),
(9,1,'user','user:block','update'),
(10,1,'user','user:unblock','update'),

(11,2,'movie','movie:list','list'),
(12,2,'comment','comment:list','list'),
(13,2,'comment','comment:insert','insert'),
(14,2,'chart','chart:list','list'),
(15,2,'rate','rate:insert','insert'),
(16,2,'rate','rate:update','update'),
(17,2,'tag','tag:list','list'),

(18,3,'movie','movie:list','list'),
(19,3,'comment','comment:list','list'),
(20,3,'comment','comment:insert','insert'),
(21,3,'comment','comment:delete','delete'),
(22,3,'chart','chart:list','list'),
(23,3,'rate','rate:insert','insert'),
(24,3,'rate','rate:update','update'),
(25,3,'tag','tag:list','list'),
(26,3,'user','user:block','update'),
(27,3,'user','user:unblock','update'),

(28,4,'movie','movie:list','list'),
(29,4,'comment','comment:list','list'),
(30,4,'chart','chart:list','list'),
(31,4,'tag','tag:list','list')

/*!40000 ALTER TABLE `t_permission` ENABLE KEYS */;
UNLOCK TABLES;