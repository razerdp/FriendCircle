/*
Navicat MySQL Data Transfer

Source Server         : my db
Source Server Version : 50711
Source Host           : 120.26.198.104:3306
Source Database       : db_fwc

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2016-08-04 12:14:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `content`
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
  `web_url` varchar(255) DEFAULT NULL,
  `imgurl` varchar(255) DEFAULT NULL,
  `web_title` varchar(255) DEFAULT NULL,
  `dynamicid` bigint(20) NOT NULL,
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `web_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES (null, 'http://img5.duitang.com/uploads/item/201206/06/20120606175201_WZ2F3.thumb.700_0.jpeg', null, '10004', '1', null);
INSERT INTO `content` VALUES (null, 'http://img4.duitang.com/uploads/item/201411/30/20141130160518_FzGfQ.jpeg', null, '10004', '2', null);
INSERT INTO `content` VALUES (null, 'http://img2.mtime.com/mg/2009/36/9a1d9903-71c6-4654-8b42-673bdad3aaef.jpg', null, '10004', '3', null);
INSERT INTO `content` VALUES (null, 'http://android.tgbus.com/shouji/UploadFiles_6533/201212/2012122110044287.jpg', null, '10002', '4', null);
INSERT INTO `content` VALUES (null, 'http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2eb5cd175766d0167e4c962ca21bf831/95eef01f3a292df5f790e2cfb9315c6035a8739c.jpg', null, '10002', '5', null);
INSERT INTO `content` VALUES (null, 'http://img.zcool.cn/community/019657559f63c632f875370ae5eaa0.jpg', null, '10002', '6', null);
INSERT INTO `content` VALUES (null, 'http://att.bbs.duowan.com/forum/month_0812/20081219_fee313c5204656503222Y6th4aJK58z7.jpg', null, '10002', '7', null);
INSERT INTO `content` VALUES (null, 'http://cdn.duitang.com/uploads/item/201409/27/20140927123303_kdKje.thumb.700_0.jpeg', null, '10003', '8', null);
INSERT INTO `content` VALUES (null, 'http://d.hiphotos.baidu.com/zhidao/pic/item/faedab64034f78f06cadf7e97a310a55b3191c01.jpg', null, '10005', '9', null);
INSERT INTO `content` VALUES ('http://ent.sina.com.cn/s/m/2016-02-26/doc-ifxpvysx1695292.shtml', null, '网信办：演员孙海英等大V微博账号被关闭', '10006', '10', 'http://i0.sinaimg.cn/ent/s/m/2014-12-15/U12244P28T3D4257060F326DT20141215092649.jpg');
INSERT INTO `content` VALUES (null, 'http://img.zcool.cn/community/01b9d2567a0d4e6ac725ad90c390e0.gif', null, '10008', '11', null);
INSERT INTO `content` VALUES (null, 'http://img.zcool.cn/community/018f54567a0d4e6ac725ad9003a6b2.gif', null, '10008', '12', null);
INSERT INTO `content` VALUES (null, 'http://www.ld12.com/upimg358/allimg/c150331/142MS01922030-L4915.png', null, '10008', '13', null);
INSERT INTO `content` VALUES (null, 'http://img01.store.sogou.com/net/a/04/link?appid=100520091&url=http://mmbiz.qpic.cn/mmbiz/IAF0d83aMUtyflKYjFsgf0vYt2lF9NNgP8d1fMoI0ExadYGNGMbAhuBVy9iaqbZP11MAibkQIWaAykTKn0cdTbEQ/0', null, '10008', '14', null);
INSERT INTO `content` VALUES (null, 'http://photo.jokeji.cn/uppic/14-11/11/1030463043483.jpg', null, '10008', '15', null);
INSERT INTO `content` VALUES (null, 'http://f.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=99ea6a0d35a85edffad9f6257964251b/37d3d539b6003af31f5cdf3d342ac65c1138b68c.jpg', null, '10008', '16', null);
INSERT INTO `content` VALUES (null, 'http://www.qqpk.cn/Article/UploadFiles/201201/20120108160105455.jpg', null, '10010', '17', null);
INSERT INTO `content` VALUES (null, 'http://www.padmag.cn/wp-content/uploads/Highly-Commended-Alison-Buttigieg.jpg', null, '10011', '18', null);
INSERT INTO `content` VALUES (null, 'http://oi22.com/wp-content/uploads/2016/03/201603091457542501.jpg', null, '10013', '19', null);
INSERT INTO `content` VALUES (null, 'http://oi22.com/wp-content/uploads/2016/03/201603091457542501-1.jpg', null, '10013', '20', null);
INSERT INTO `content` VALUES (null, 'http://ww4.sinaimg.cn/large/610dc034jw1f47gspphiyj20ia0rf76w.jpg', null, '10014', '21', null);
INSERT INTO `content` VALUES (null, 'http://ww4.sinaimg.cn/large/610dc034jw1f46bsdcls2j20sg0izac0.jpg', null, '10014', '22', null);
INSERT INTO `content` VALUES (null, 'http://ww4.sinaimg.cn/large/610dc034jw1f41lxgc3x3j20jh0tcn14.jpg', null, '10014', '23', null);
INSERT INTO `content` VALUES (null, 'http://ww4.sinaimg.cn/large/610dc034jw1f3zen8idmkj20dw0kun0i.jpg', null, '10014', '24', null);
INSERT INTO `content` VALUES (null, 'http://ww4.sinaimg.cn/large/610dc034jw1f3x32bd1hcj20d90k03zx.jpg', null, '10014', '25', null);
INSERT INTO `content` VALUES (null, 'http://ww2.sinaimg.cn/large/7a8aed7bjw1f3tkjebzzpj20kg0v7q9h.jpg', null, '10014', '26', null);
INSERT INTO `content` VALUES (null, 'http://ww3.sinaimg.cn/large/7a8aed7bjw1f3rdepqtnij21kw2dc1cx.jpg', null, '10014', '27', null);
INSERT INTO `content` VALUES (null, 'http://ww4.sinaimg.cn/large/7a8aed7bjw1f37vhovzlnj20f00evabt.jpg', null, '10014', '28', null);
INSERT INTO `content` VALUES (null, 'http://ww4.sinaimg.cn/large/7a8aed7bjw1f32d0cumhkj20ey0mitbx.jpg', null, '10014', '29', null);

-- ----------------------------
-- Table structure for `dynamic`
-- ----------------------------
DROP TABLE IF EXISTS `dynamic`;
CREATE TABLE `dynamic` (
  `host_id` bigint(20) NOT NULL DEFAULT '0',
  `wallpic` varchar(200) DEFAULT NULL,
  `dynamic_id` bigint(20) NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `dynamic_type` int(11) DEFAULT NULL,
  `praise_state` int(1) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `textField` varchar(300) DEFAULT NULL,
  `candelete` int(11) DEFAULT NULL,
  PRIMARY KEY (`dynamic_id`),
  KEY `dynamic_id` (`dynamic_id`) USING BTREE,
  KEY `user` (`host_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dynamic
-- ----------------------------
INSERT INTO `dynamic` VALUES ('1001', 'http://www.pp3.cn/uploads/allimg/111118/10562Cb5-13.jpg', '10001', '1001', '10', null, '1456296202', '这是第一条朋友圈哦', null);
INSERT INTO `dynamic` VALUES ('1045', 'http://img4.duitang.com/uploads/item/201404/25/20140425100445_MGZRx.jpeg', '10002', '1045', '11', null, '1454656515', '文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字好长TAT文字文字好长TAT文字好长TAT文字好长TAT文字好长TAT好长T文字好长TAT文', null);
INSERT INTO `dynamic` VALUES ('1046', null, '10003', '1010', '11', null, '1454743095', '咳咳。。。。测试一下', null);
INSERT INTO `dynamic` VALUES ('1007', 'http://p.qq181.com/cms/1304/2013040607430286882.jpg', '10004', '1009', '11', null, '1454483715', '我发发图，我不说话。', null);
INSERT INTO `dynamic` VALUES ('1033', null, '10005', '1033', '11', null, '1452485842', '。。。', null);
INSERT INTO `dynamic` VALUES ('1067', null, '10006', '1048', '13', null, '1454299932', null, null);
INSERT INTO `dynamic` VALUES ('1014', null, '10007', '1014', '10', null, '1454299938', '噗。。。', null);
INSERT INTO `dynamic` VALUES ('1018', null, '10008', '1006', '11', null, '1454300330', '发福利了。。。', null);
INSERT INTO `dynamic` VALUES ('1044', null, '10009', '1044', '10', null, '1454309090', '咳咳，有人吗。。。', null);
INSERT INTO `dynamic` VALUES ('1050', null, '10010', '1050', '11', null, '1454309330', '告诉你，什么叫P图P得像僵尸', null);
INSERT INTO `dynamic` VALUES ('1090', null, '10011', '1018', '11', null, '1457579533', '羽翼君在努力的制造假数据。。。', null);
INSERT INTO `dynamic` VALUES ('1020', null, '10012', '1020', '10', null, '1457579653', '测试一下50条评论', null);
INSERT INTO `dynamic` VALUES ('1021', null, '10013', '1021', '11', null, '1457884685', '好可爱的一只喵，噢不，是两只', null);
INSERT INTO `dynamic` VALUES ('1054', null, '10014', '1054', '11', null, '1461150000', '好久没更新了', null);

-- ----------------------------
-- Table structure for `dynamic_comment`
-- ----------------------------
DROP TABLE IF EXISTS `dynamic_comment`;
CREATE TABLE `dynamic_comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dynamic_id` bigint(20) DEFAULT NULL,
  `user_a_id` bigint(20) NOT NULL,
  `user_b_id` bigint(20) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `cnadelete` int(11) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `DId` (`dynamic_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=603 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dynamic_comment
-- ----------------------------
INSERT INTO `dynamic_comment` VALUES ('3', '10001', '1014', null, '新年好', null, '1454397315');
INSERT INTO `dynamic_comment` VALUES ('4', '10004', '1045', null, '路过评论。。。', null, '1454742855');
INSERT INTO `dynamic_comment` VALUES ('5', '10002', '1012', null, '~', null, '1454742975');
INSERT INTO `dynamic_comment` VALUES ('6', '10003', '1010', '1058', 'have a test', null, '1454746698');
INSERT INTO `dynamic_comment` VALUES ('7', '10003', '1058', '1010', '噢~so ga', null, '1454750298');
INSERT INTO `dynamic_comment` VALUES ('8', '10003', '1058', null, '这是啥', null, '1454746695');
INSERT INTO `dynamic_comment` VALUES ('9', '10005', '1078', null, '....', null, '1452555142');
INSERT INTO `dynamic_comment` VALUES ('12', '10005', '1088', null, '楼上你们够了+10086', null, '1452554902');
INSERT INTO `dynamic_comment` VALUES ('16', '10005', '1010', '1045', '路过一下。', null, '1452555322');
INSERT INTO `dynamic_comment` VALUES ('17', '10005', '1045', null, '这么卖萌真的好么。。。', null, '1452555262');
INSERT INTO `dynamic_comment` VALUES ('18', '10007', '1032', null, '噗2。。', null, '1454299998');
INSERT INTO `dynamic_comment` VALUES ('19', '10007', '1033', '1032', '噗3。。', null, '1454300030');
INSERT INTO `dynamic_comment` VALUES ('20', '10007', '1034', '1033', '噗4。。。', null, '1454300040');
INSERT INTO `dynamic_comment` VALUES ('23', '10008', '1037', null, '福利呢', null, '1454300460');
INSERT INTO `dynamic_comment` VALUES ('24', '10008', '1077', null, '福利呢', null, '1454304050');
INSERT INTO `dynamic_comment` VALUES ('25', '10008', '1055', null, '福利呢x10086', null, '1454305250');
INSERT INTO `dynamic_comment` VALUES ('26', '10008', '1097', null, '楼主，福利呢', null, '1454305490');
INSERT INTO `dynamic_comment` VALUES ('29', '10010', '1055', null, '尼玛，这。。。在下无法吐槽', null, '1454316650');
INSERT INTO `dynamic_comment` VALUES ('30', '10010', '1069', null, '宝宝吓尿了TAT', null, '1454403050');
INSERT INTO `dynamic_comment` VALUES ('32', '10012', '1031', null, '我是一楼', null, '1457594053');
INSERT INTO `dynamic_comment` VALUES ('33', '10012', '1032', '1031', '我是二楼', null, '1457594058');
INSERT INTO `dynamic_comment` VALUES ('34', '10012', '1033', '1032', '我是三楼', null, '1457594178');
INSERT INTO `dynamic_comment` VALUES ('35', '10012', '1034', '1033', '我是四楼', null, '1457594298');
INSERT INTO `dynamic_comment` VALUES ('36', '10012', '1035', '1034', 'wo shi wu lou', null, '1457594358');
INSERT INTO `dynamic_comment` VALUES ('37', '10012', '1036', '1035', '楼上傻逼，我是六楼', null, '1457594418');
INSERT INTO `dynamic_comment` VALUES ('38', '10012', '1037', null, '我不回复，我就静静的观看，我是七楼', null, '1457594538');
INSERT INTO `dynamic_comment` VALUES ('42', '10012', '1040', '1001', '惊现开发者，我是十一楼', null, '1457595318');
INSERT INTO `dynamic_comment` VALUES ('43', '10012', '1041', '1001', '参见陛下，小的十二楼', null, '1457595498');
INSERT INTO `dynamic_comment` VALUES ('44', '10012', '1042', '1041', '我是十三楼', null, '1457595618');
INSERT INTO `dynamic_comment` VALUES ('45', '10012', '1043', '1042', '我是十四楼', null, '1457606418');
INSERT INTO `dynamic_comment` VALUES ('46', '10012', '1044', '1043', '我是十五楼，还差35楼', null, '1457610018');
INSERT INTO `dynamic_comment` VALUES ('47', '10012', '1045', null, '我是十六楼，咱们到三十楼停下好么。。。', null, '1457610618');
INSERT INTO `dynamic_comment` VALUES ('48', '10012', '1046', '1045', '这是要测试到50条看卡不卡啊亲，另外，我是十七楼', null, '1457610678');
INSERT INTO `dynamic_comment` VALUES ('49', '10012', '1045', '1046', '我去。。。这羽翼君要精分多久啊，我是十八楼', null, '1457610918');
INSERT INTO `dynamic_comment` VALUES ('50', '10012', '1047', '1046', '别吵，十九楼来也', null, '1457618118');
INSERT INTO `dynamic_comment` VALUES ('52', '10012', '1048', '1001', '摸摸头，不哭，我是二十一楼', null, '1457708118');
INSERT INTO `dynamic_comment` VALUES ('53', '10012', '1049', null, '二十二楼，默默留名', null, '1457751318');
INSERT INTO `dynamic_comment` VALUES ('54', '10012', '1050', '1049', '23楼驾到', null, '1457754918');
INSERT INTO `dynamic_comment` VALUES ('55', '10012', '1051', null, '24.。。。。', null, '1457763018');
INSERT INTO `dynamic_comment` VALUES ('57', '10012', '1055', null, '26楼是我', null, '1457766918');
INSERT INTO `dynamic_comment` VALUES ('58', '10012', '1056', null, '二十七了，加油。。。。', null, '1457770518');
INSERT INTO `dynamic_comment` VALUES ('59', '10012', '1056', null, '二十八还是我！', null, '1457770578');
INSERT INTO `dynamic_comment` VALUES ('60', '10012', '1056', null, '二十九依然是我，哈哈哈哈', null, '1457770638');
INSERT INTO `dynamic_comment` VALUES ('61', '10012', '1057', '1056', '牛逼，但三十楼是我的。', null, '1457770698');
INSERT INTO `dynamic_comment` VALUES ('62', '10012', '1058', '1057', '三十一我的', null, '1457777898');
INSERT INTO `dynamic_comment` VALUES ('63', '10012', '1059', null, '默默路过的三十二', null, '1457778318');
INSERT INTO `dynamic_comment` VALUES ('64', '10012', '1060', null, '33', null, '1457781918');
INSERT INTO `dynamic_comment` VALUES ('65', '10012', '1061', null, '三十四楼，羽翼君精分没', null, '1457783118');
INSERT INTO `dynamic_comment` VALUES ('67', '10012', '1061', '1001', '哈哈哈哈哈哈哈，36', null, '1457784874');
INSERT INTO `dynamic_comment` VALUES ('68', '10012', '1062', null, '37', null, '1457784934');
INSERT INTO `dynamic_comment` VALUES ('69', '10012', '1063', '1062', '38', null, '1457785114');
INSERT INTO `dynamic_comment` VALUES ('70', '10012', '1064', '1063', '我是谁，我只知道我在三十九楼', null, '1457788714');
INSERT INTO `dynamic_comment` VALUES ('71', '10012', '1065', '1064', '尼玛，终于40楼了！', null, '1457789144');
INSERT INTO `dynamic_comment` VALUES ('72', '10012', '1066', null, '41，还差九楼了', null, '1457789204');
INSERT INTO `dynamic_comment` VALUES ('73', '10012', '1067', '1066', '42', null, '1457792804');
INSERT INTO `dynamic_comment` VALUES ('74', '10012', '1068', '1067', '43.。。。', null, '1457796404');
INSERT INTO `dynamic_comment` VALUES ('75', '10012', '1069', null, '四乘以十加上四就是44楼', null, '1457796524');
INSERT INTO `dynamic_comment` VALUES ('76', '10012', '1070', '1069', '45', null, '1457796764');
INSERT INTO `dynamic_comment` VALUES ('77', '10012', '1071', null, '四十六！', null, '1457797064');
INSERT INTO `dynamic_comment` VALUES ('78', '10012', '1072', null, '四十七了。。', null, '1457797505');
INSERT INTO `dynamic_comment` VALUES ('79', '10012', '1073', null, '哈哈哈哈，四十八了，还差最后两楼', null, '1457797685');
INSERT INTO `dynamic_comment` VALUES ('92', '10008', '1001', '1097', '……', null, '1461060409752');
INSERT INTO `dynamic_comment` VALUES ('99', '10013', '1010', null, 'na', null, '1461222314502');
INSERT INTO `dynamic_comment` VALUES ('102', '10007', '1001', '1033', '咩哈哈', null, '1461236338326');
INSERT INTO `dynamic_comment` VALUES ('118', '10010', '1001', '1069', '(●—●)', null, '1461825746705');
INSERT INTO `dynamic_comment` VALUES ('119', '10009', '1001', null, '(づ ●─● )づ', null, '1461825753424');
INSERT INTO `dynamic_comment` VALUES ('126', '10009', '1001', null, '感觉不到你的存在', null, '1461910363141');
INSERT INTO `dynamic_comment` VALUES ('127', '10008', '1001', null, 'AQ', null, '1462006187369');
INSERT INTO `dynamic_comment` VALUES ('128', '10008', '1001', '1037', 'A', null, '1462006187588');
INSERT INTO `dynamic_comment` VALUES ('129', '10008', '1001', null, 'AQ', null, '1462006190259');
INSERT INTO `dynamic_comment` VALUES ('136', '10004', '1001', null, 'OOF', null, '1462198499710');
INSERT INTO `dynamic_comment` VALUES ('155', '10014', '1045', null, '福利！！！！', null, '1461236400');
INSERT INTO `dynamic_comment` VALUES ('189', '10007', '1001', null, 'nmnmn', null, '1464511359014');
INSERT INTO `dynamic_comment` VALUES ('193', '10010', '1001', '1069', '恩看见了', null, '1464587820219');
INSERT INTO `dynamic_comment` VALUES ('254', '10007', '1001', '1034', '电话打不', null, '1465402736474');
INSERT INTO `dynamic_comment` VALUES ('279', '10010', '1001', '1069', '你好', null, '1465723521657');
INSERT INTO `dynamic_comment` VALUES ('280', '10010', '1001', '1055', '你好', null, '1465723529672');
INSERT INTO `dynamic_comment` VALUES ('300', '10007', '1001', '1032', 'mmm', null, '1465810781841');
INSERT INTO `dynamic_comment` VALUES ('314', '10009', '1001', null, '咳咳，没人。', null, '1466058201215');
INSERT INTO `dynamic_comment` VALUES ('357', '10005', '1001', '1045', '哈哈', null, '1466818062614');
INSERT INTO `dynamic_comment` VALUES ('367', '10005', '1001', '1079', '哈哈', null, '1467010598061');
INSERT INTO `dynamic_comment` VALUES ('370', '10011', '12223', null, '火锅', null, '1467099585127');
INSERT INTO `dynamic_comment` VALUES ('374', '10014', '1', null, '11', null, '1467175815548');
INSERT INTO `dynamic_comment` VALUES ('401', '10010', '1001', null, 'ffff', null, '1467445995370');
INSERT INTO `dynamic_comment` VALUES ('403', '10006', '1001', null, '和', null, '1467451141111');
INSERT INTO `dynamic_comment` VALUES ('405', '10008', '1001', null, '哈', null, '1467469044626');
INSERT INTO `dynamic_comment` VALUES ('411', '10005', '1001', '1079', '来咯', null, '1467813653588');
INSERT INTO `dynamic_comment` VALUES ('412', '10005', '1001', '1045', '尽力了', null, '1467813693762');
INSERT INTO `dynamic_comment` VALUES ('428', '10009', '1001', null, 'sgdsh', null, '1467972053502');
INSERT INTO `dynamic_comment` VALUES ('449', '10009', '1001', null, '巴黎', null, '1468342721106');
INSERT INTO `dynamic_comment` VALUES ('450', '10010', '1001', null, 'hehe', null, '1468390692119');
INSERT INTO `dynamic_comment` VALUES ('458', '10006', '1001', null, 'rrrr', null, '1468834980520');
INSERT INTO `dynamic_comment` VALUES ('459', '10006', '1001', null, 'sss', null, '1468834996333');
INSERT INTO `dynamic_comment` VALUES ('473', '10005', '1001', null, '的说法', null, '1468984707543');
INSERT INTO `dynamic_comment` VALUES ('504', '10013', '0', null, 'hhh', null, '1469256866575');
INSERT INTO `dynamic_comment` VALUES ('511', '10005', '1001', null, '急急急', null, '1469356110397');
INSERT INTO `dynamic_comment` VALUES ('522', '10004', '1001', null, '空咯婆婆婆', null, '1469434806804');
INSERT INTO `dynamic_comment` VALUES ('523', '10005', '1001', null, '额咳咳可恶', null, '1469436717886');
INSERT INTO `dynamic_comment` VALUES ('531', '10', '1001', '1234', '哈哈', null, '1469507090469');
INSERT INTO `dynamic_comment` VALUES ('533', '10012', '1001', null, '哈哈', null, '1469517710575');
INSERT INTO `dynamic_comment` VALUES ('535', '729', '1001', null, '哈哈', null, '1469519101234');
INSERT INTO `dynamic_comment` VALUES ('536', '10001', '1001', null, '测试', null, '1469519538249');
INSERT INTO `dynamic_comment` VALUES ('538', '740', '1001', null, '哈哈来咯', null, '1469520221867');
INSERT INTO `dynamic_comment` VALUES ('539', '738', '1001', null, '他咯就我某呼我', null, '1469520241258');
INSERT INTO `dynamic_comment` VALUES ('540', '726', '1001', null, '初级会计学', null, '1469520346512');
INSERT INTO `dynamic_comment` VALUES ('542', '727', '1001', null, '哟西哟西哟西', null, '1469520367919');
INSERT INTO `dynamic_comment` VALUES ('544', '727', '1001', null, '哟西哟西哟西', null, '1469520377669');
INSERT INTO `dynamic_comment` VALUES ('546', '727', '1001', null, '了就酷我能驾控醉咯天我搭理她天天用哦', null, '1469520390263');
INSERT INTO `dynamic_comment` VALUES ('547', '740', '1001', null, '心里看你是休息两天时间我', null, '1469520538987');
INSERT INTO `dynamic_comment` VALUES ('548', '740', '1001', null, '某就可以', null, '1469520551581');
INSERT INTO `dynamic_comment` VALUES ('549', '740', '1001', null, '哦看看就行了', null, '1469520572675');
INSERT INTO `dynamic_comment` VALUES ('550', '711', '1001', null, '嗯哦哦', null, '1469520898234');
INSERT INTO `dynamic_comment` VALUES ('551', '740', '1001', null, '啦咯回家咯', null, '1469522965415');
INSERT INTO `dynamic_comment` VALUES ('552', '808', '1001', null, '丽丽姐', null, '1469537610380');
INSERT INTO `dynamic_comment` VALUES ('553', '809', '1001', null, '爸爸', null, '1469537635397');
INSERT INTO `dynamic_comment` VALUES ('554', '12', '1001', '1234', '看看咯', null, '1469537732400');
INSERT INTO `dynamic_comment` VALUES ('562', '10014', '1001', null, 'eee', null, '1469588471656');
INSERT INTO `dynamic_comment` VALUES ('563', '817', '1001', null, '哈哈', null, '1469588636271');
INSERT INTO `dynamic_comment` VALUES ('565', '10012', '1001', '1031', '哈哈all', null, '1469588780088');
INSERT INTO `dynamic_comment` VALUES ('567', '10001', '1001', '1006', '丽丽姐', null, '1469591681205');
INSERT INTO `dynamic_comment` VALUES ('568', '13', '1001', '1234', '丽丽姐', null, '1469591798037');
INSERT INTO `dynamic_comment` VALUES ('569', '842', '1001', null, '(⊙o⊙)啥？', null, '1469601655554');
INSERT INTO `dynamic_comment` VALUES ('571', '10014', '1001', '1045', 'u', null, '1469614962002');
INSERT INTO `dynamic_comment` VALUES ('579', '10013', '1001', null, '111', null, '1469847975427');
INSERT INTO `dynamic_comment` VALUES ('580', '10012', '1001', '1073', '123', null, '1469848030023');
INSERT INTO `dynamic_comment` VALUES ('582', '10010', '1001', null, '123321', null, '1469849160968');
INSERT INTO `dynamic_comment` VALUES ('583', '10012', '1001', '1040', '地方', null, '1469870638499');
INSERT INTO `dynamic_comment` VALUES ('587', '10014', '1001', '1045', 'hhji', null, '1470040803445');
INSERT INTO `dynamic_comment` VALUES ('588', '10013', '1001', '1010', 'ghk', null, '1470041567284');
INSERT INTO `dynamic_comment` VALUES ('589', '10014', '1001', '1045', '你好', null, '1470045560518');
INSERT INTO `dynamic_comment` VALUES ('590', '10014', '1001', '1045', '居然真的可以', null, '1470045567628');
INSERT INTO `dynamic_comment` VALUES ('591', '10003', '1001', null, '巴黎', null, '1470062560783');
INSERT INTO `dynamic_comment` VALUES ('594', '10013', '1001', null, '，，，', null, '1470105838667');
INSERT INTO `dynamic_comment` VALUES ('595', '10006', '1001', null, '为什么都是', null, '1470106073816');
INSERT INTO `dynamic_comment` VALUES ('596', '10013', '1001', null, '哦', null, '1470107172448');
INSERT INTO `dynamic_comment` VALUES ('598', '10012', '1001', null, '怎么改名啊', null, '1470107252013');
INSERT INTO `dynamic_comment` VALUES ('599', '10014', '1001', null, '牛', null, '1470195206987');
INSERT INTO `dynamic_comment` VALUES ('600', '10011', '1001', null, '不能改名字', null, '1470208828712');
INSERT INTO `dynamic_comment` VALUES ('601', '10011', '1001', null, '搞笑吗', null, '1470209102002');
INSERT INTO `dynamic_comment` VALUES ('602', '10014', '1001', null, '我是谁', null, '1470281825851');

-- ----------------------------
-- Table structure for `dynamic_praise`
-- ----------------------------
DROP TABLE IF EXISTS `dynamic_praise`;
CREATE TABLE `dynamic_praise` (
  `user_id` bigint(20) NOT NULL,
  `dynamic_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`dynamic_id`),
  KEY `uid` (`user_id`) USING BTREE,
  KEY `DPId` (`dynamic_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dynamic_praise
-- ----------------------------
INSERT INTO `dynamic_praise` VALUES ('1', '10005');
INSERT INTO `dynamic_praise` VALUES ('1', '10014');
INSERT INTO `dynamic_praise` VALUES ('1001', '9');
INSERT INTO `dynamic_praise` VALUES ('1001', '10');
INSERT INTO `dynamic_praise` VALUES ('1001', '13');
INSERT INTO `dynamic_praise` VALUES ('1001', '711');
INSERT INTO `dynamic_praise` VALUES ('1001', '712');
INSERT INTO `dynamic_praise` VALUES ('1001', '714');
INSERT INTO `dynamic_praise` VALUES ('1001', '727');
INSERT INTO `dynamic_praise` VALUES ('1001', '738');
INSERT INTO `dynamic_praise` VALUES ('1001', '740');
INSERT INTO `dynamic_praise` VALUES ('1001', '758');
INSERT INTO `dynamic_praise` VALUES ('1001', '791');
INSERT INTO `dynamic_praise` VALUES ('1001', '803');
INSERT INTO `dynamic_praise` VALUES ('1001', '808');
INSERT INTO `dynamic_praise` VALUES ('1001', '870');
INSERT INTO `dynamic_praise` VALUES ('1001', '876');
INSERT INTO `dynamic_praise` VALUES ('1001', '10001');
INSERT INTO `dynamic_praise` VALUES ('1001', '10002');
INSERT INTO `dynamic_praise` VALUES ('1001', '10003');
INSERT INTO `dynamic_praise` VALUES ('1001', '10004');
INSERT INTO `dynamic_praise` VALUES ('1001', '10006');
INSERT INTO `dynamic_praise` VALUES ('1001', '10007');
INSERT INTO `dynamic_praise` VALUES ('1001', '10009');
INSERT INTO `dynamic_praise` VALUES ('1001', '10012');
INSERT INTO `dynamic_praise` VALUES ('1001', '10014');
INSERT INTO `dynamic_praise` VALUES ('1002', '10011');
INSERT INTO `dynamic_praise` VALUES ('1002', '10013');
INSERT INTO `dynamic_praise` VALUES ('1002', '10014');
INSERT INTO `dynamic_praise` VALUES ('1003', '10003');
INSERT INTO `dynamic_praise` VALUES ('1004', '10001');
INSERT INTO `dynamic_praise` VALUES ('1004', '10008');
INSERT INTO `dynamic_praise` VALUES ('1006', '10008');
INSERT INTO `dynamic_praise` VALUES ('1006', '10010');
INSERT INTO `dynamic_praise` VALUES ('1008', '10014');
INSERT INTO `dynamic_praise` VALUES ('1009', '10003');
INSERT INTO `dynamic_praise` VALUES ('1011', '10003');
INSERT INTO `dynamic_praise` VALUES ('1011', '10011');
INSERT INTO `dynamic_praise` VALUES ('1013', '10011');
INSERT INTO `dynamic_praise` VALUES ('1014', '10010');
INSERT INTO `dynamic_praise` VALUES ('1014', '10011');
INSERT INTO `dynamic_praise` VALUES ('1015', '10003');
INSERT INTO `dynamic_praise` VALUES ('1015', '10007');
INSERT INTO `dynamic_praise` VALUES ('1015', '10011');
INSERT INTO `dynamic_praise` VALUES ('1015', '10014');
INSERT INTO `dynamic_praise` VALUES ('1016', '10008');
INSERT INTO `dynamic_praise` VALUES ('1016', '10011');
INSERT INTO `dynamic_praise` VALUES ('1017', '10008');
INSERT INTO `dynamic_praise` VALUES ('1017', '10011');
INSERT INTO `dynamic_praise` VALUES ('1021', '10012');
INSERT INTO `dynamic_praise` VALUES ('1022', '10007');
INSERT INTO `dynamic_praise` VALUES ('1022', '10012');
INSERT INTO `dynamic_praise` VALUES ('1023', '10012');
INSERT INTO `dynamic_praise` VALUES ('1024', '10008');
INSERT INTO `dynamic_praise` VALUES ('1024', '10012');
INSERT INTO `dynamic_praise` VALUES ('1025', '10012');
INSERT INTO `dynamic_praise` VALUES ('1026', '10012');
INSERT INTO `dynamic_praise` VALUES ('1027', '10012');
INSERT INTO `dynamic_praise` VALUES ('1028', '10012');
INSERT INTO `dynamic_praise` VALUES ('1029', '10012');
INSERT INTO `dynamic_praise` VALUES ('1030', '10008');
INSERT INTO `dynamic_praise` VALUES ('1030', '10012');
INSERT INTO `dynamic_praise` VALUES ('1031', '10012');
INSERT INTO `dynamic_praise` VALUES ('1035', '10014');
INSERT INTO `dynamic_praise` VALUES ('1037', '10008');
INSERT INTO `dynamic_praise` VALUES ('1039', '10008');
INSERT INTO `dynamic_praise` VALUES ('1039', '10014');
INSERT INTO `dynamic_praise` VALUES ('1041', '10003');
INSERT INTO `dynamic_praise` VALUES ('1042', '10005');
INSERT INTO `dynamic_praise` VALUES ('1043', '10005');
INSERT INTO `dynamic_praise` VALUES ('1043', '10008');
INSERT INTO `dynamic_praise` VALUES ('1044', '10001');
INSERT INTO `dynamic_praise` VALUES ('1044', '10003');
INSERT INTO `dynamic_praise` VALUES ('1044', '10005');
INSERT INTO `dynamic_praise` VALUES ('1044', '10014');
INSERT INTO `dynamic_praise` VALUES ('1045', '10003');
INSERT INTO `dynamic_praise` VALUES ('1045', '10007');
INSERT INTO `dynamic_praise` VALUES ('1046', '10005');
INSERT INTO `dynamic_praise` VALUES ('1046', '10010');
INSERT INTO `dynamic_praise` VALUES ('1048', '10008');
INSERT INTO `dynamic_praise` VALUES ('1048', '10014');
INSERT INTO `dynamic_praise` VALUES ('1049', '10007');
INSERT INTO `dynamic_praise` VALUES ('1050', '10008');
INSERT INTO `dynamic_praise` VALUES ('1050', '10014');
INSERT INTO `dynamic_praise` VALUES ('1054', '10001');
INSERT INTO `dynamic_praise` VALUES ('1055', '10008');
INSERT INTO `dynamic_praise` VALUES ('1057', '10005');
INSERT INTO `dynamic_praise` VALUES ('1057', '10008');
INSERT INTO `dynamic_praise` VALUES ('1057', '10010');
INSERT INTO `dynamic_praise` VALUES ('1063', '10003');
INSERT INTO `dynamic_praise` VALUES ('1066', '10008');
INSERT INTO `dynamic_praise` VALUES ('1067', '10014');
INSERT INTO `dynamic_praise` VALUES ('1069', '10014');
INSERT INTO `dynamic_praise` VALUES ('1077', '10010');
INSERT INTO `dynamic_praise` VALUES ('1077', '10014');
INSERT INTO `dynamic_praise` VALUES ('1078', '10008');
INSERT INTO `dynamic_praise` VALUES ('1079', '10003');
INSERT INTO `dynamic_praise` VALUES ('1082', '10014');
INSERT INTO `dynamic_praise` VALUES ('1087', '10003');
INSERT INTO `dynamic_praise` VALUES ('1087', '10010');
INSERT INTO `dynamic_praise` VALUES ('1088', '10007');
INSERT INTO `dynamic_praise` VALUES ('1088', '10008');
INSERT INTO `dynamic_praise` VALUES ('1088', '10014');
INSERT INTO `dynamic_praise` VALUES ('1089', '10010');
INSERT INTO `dynamic_praise` VALUES ('1091', '10010');
INSERT INTO `dynamic_praise` VALUES ('1093', '10010');
INSERT INTO `dynamic_praise` VALUES ('1094', '10003');
INSERT INTO `dynamic_praise` VALUES ('1097', '10008');
INSERT INTO `dynamic_praise` VALUES ('1099', '10010');
INSERT INTO `dynamic_praise` VALUES ('1099', '10014');
INSERT INTO `dynamic_praise` VALUES ('1110', '10003');
INSERT INTO `dynamic_praise` VALUES ('1110', '10007');
INSERT INTO `dynamic_praise` VALUES ('1110', '10014');
INSERT INTO `dynamic_praise` VALUES ('1110', '10015');
INSERT INTO `dynamic_praise` VALUES ('1111', '10010');
INSERT INTO `dynamic_praise` VALUES ('1113', '10010');
INSERT INTO `dynamic_praise` VALUES ('1114', '10008');
INSERT INTO `dynamic_praise` VALUES ('122232', '10014');

-- ----------------------------
-- Table structure for `graduation_comment`
-- ----------------------------
DROP TABLE IF EXISTS `graduation_comment`;
CREATE TABLE `graduation_comment` (
  `create_user_id` bigint(20) NOT NULL,
  `comment_id` bigint(20) NOT NULL,
  `url` varchar(200) DEFAULT NULL,
  `comment_time` bigint(20) DEFAULT NULL,
  `can_delete` int(11) DEFAULT NULL,
  `comment_content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of graduation_comment
-- ----------------------------
INSERT INTO `graduation_comment` VALUES ('-1867378635', '1460930913981', 'http://news.163.com/16/0509/18/BML5NHDQ000156PO.html#f=dlist', '1462798292616', null, 'ghhhg');
INSERT INTO `graduation_comment` VALUES ('49', '1463228931871', 'http://tech.qq.com/a/20160514/039206.htm', '1463228931822', null, '1111');
INSERT INTO `graduation_comment` VALUES ('1963611698', '1463788109704', 'http://mp.weixin.qq.com/s?__biz=MzA3MTI1OTkxNQ==&idx=2&mid=2656953701&sn=f859506695c41d55760db2b7ad5b8031', '1461824498006', null, '查查');
INSERT INTO `graduation_comment` VALUES ('1963611698', '1464634455241', 'http://mp.weixin.qq.com/s?__biz=MzA4OTE5MDExMw==&idx=3&mid=2654315082&sn=bd468582db0bb48e540f4edcdb28bd0c', '1462670843543', null, '呵呵');
INSERT INTO `graduation_comment` VALUES ('1963611698', '1464634467382', 'http://mp.weixin.qq.com/s?__biz=MzA4OTE5MDExMw==&idx=3&mid=2654315082&sn=bd468582db0bb48e540f4edcdb28bd0c', '1462670855684', null, '啊啦拉');
INSERT INTO `graduation_comment` VALUES ('1963611698', '1465195891823', 'http://mp.weixin.qq.com/s?__biz=MzA5ODA5NTUwMg==&idx=6&mid=2651602490&sn=00072c8c03fe9c96dbc795b35e82588e', '1463232280125', null, '呵呵');
INSERT INTO `graduation_comment` VALUES ('1963611698', '1465195898635', 'http://mp.weixin.qq.com/s?__biz=MzA5ODA5NTUwMg==&idx=6&mid=2651602490&sn=00072c8c03fe9c96dbc795b35e82588e', '1463232286937', null, '嗯');
INSERT INTO `graduation_comment` VALUES ('1963611698', '1465195905589', 'http://mp.weixin.qq.com/s?__biz=MzA5ODA5NTUwMg==&idx=6&mid=2651602490&sn=00072c8c03fe9c96dbc795b35e82588e', '1463232293891', null, '喵？');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465742147601', 'http://mp.weixin.qq.com/s?__biz=MjM5OTMyODA2MA==&idx=2&mid=2650142071&sn=49b1c3bc5e7c683f5380f4a52cfb7e35', '1464753124205', null, 'jbvg');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465742155585', 'http://mp.weixin.qq.com/s?__biz=MjM5OTMyODA2MA==&idx=2&mid=2650142071&sn=49b1c3bc5e7c683f5380f4a52cfb7e35', '1464753132189', null, 'hbjk');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465742160617', 'http://mp.weixin.qq.com/s?__biz=MjM5OTMyODA2MA==&idx=2&mid=2650142071&sn=49b1c3bc5e7c683f5380f4a52cfb7e35', '1464753137221', null, 'ubhj');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465777074591', 'http://mp.weixin.qq.com/s?__biz=MjM5NjI2NTM2MA==&idx=2&mid=2654541405&sn=5f6599c88aaa1fee9c3540b939b5e98f', '1464788051195', null, 'haha');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465778910388', 'http://mp.weixin.qq.com/s?__biz=MTE3MzE4MTAyMQ==&idx=1&mid=2651133253&sn=e728982c689028cc199753ec01ba2a0e', '1464789886992', null, 'ddd');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465782728677', 'http://mp.weixin.qq.com/s?__biz=MjM5NjI2NTM2MA==&idx=2&mid=2654541405&sn=5f6599c88aaa1fee9c3540b939b5e98f', '1464793705281', null, 'hjj');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465816302275', 'http://mp.weixin.qq.com/s?__biz=MzA4NzE1NzUyMQ==&idx=6&mid=2651097746&sn=af62497b174553b5dff4ab33bef3347e', '1464827278879', null, '啊啦拉');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465816306915', 'http://mp.weixin.qq.com/s?__biz=MzA4NzE1NzUyMQ==&idx=6&mid=2651097746&sn=af62497b174553b5dff4ab33bef3347e', '1464827283519', null, '同事');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465816332869', 'http://mp.weixin.qq.com/s?__biz=MzA4NzE1NzUyMQ==&idx=6&mid=2651097746&sn=af62497b174553b5dff4ab33bef3347e', '1464827309473', null, '同事');
INSERT INTO `graduation_comment` VALUES ('989023396', '1465816347151', 'http://mp.weixin.qq.com/s?__biz=MzA4NzE1NzUyMQ==&idx=6&mid=2651097746&sn=af62497b174553b5dff4ab33bef3347e', '1464827323755', null, '同事');
INSERT INTO `graduation_comment` VALUES ('0', '1467023179686', 'http://mp.weixin.qq.com/s?__biz=MTY0MzI5NDcwMQ==&idx=2&mid=2651188133&sn=94a8a8983bfe7230538d35e018d5fecd', '1467023179686', null, '我想知道图里的风是怎么挂的');

-- ----------------------------
-- Table structure for `graduation_favorite`
-- ----------------------------
DROP TABLE IF EXISTS `graduation_favorite`;
CREATE TABLE `graduation_favorite` (
  `userid` bigint(20) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `ctime` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT '',
  `favoritetime` bigint(20) DEFAULT NULL,
  `picurl` varchar(200) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of graduation_favorite
-- ----------------------------
INSERT INTO `graduation_favorite` VALUES ('1001', '艺龙宣布完成私有化交易私有化价格为每股9美元', '2016-06-01', '互联网头条', 'http://mp.weixin.qq.com/s?__biz=MjM5OTMyODA2MA==&idx=2&mid=2650142071&sn=49b1c3bc5e7c683f5380f4a52cfb7e35', '1464759151273', 'http://t1.qpic.cn/mblogpic/f01a972dbcc1060fd456/2000', '-1743128799');
INSERT INTO `graduation_favorite` VALUES ('1001', '佳片这部电影看似荒诞，却十足的令人瞠目！', '2016-06-01', '电影生活', 'http://mp.weixin.qq.com/s?__biz=MzA5MDE0NTI3MA==&idx=2&mid=2655014188&sn=a69cbe7e981a01847ac4ad03f255699e', '1464759158961', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-5910667.jpg/640', '-1629733697');
INSERT INTO `graduation_favorite` VALUES ('1001', '盘点不思进取而惨遭淘汰的九大过气明星！', '2016-06-02', '娱乐圈那点事', 'http://mp.weixin.qq.com/s?__biz=MzA4NzE1NzUyMQ==&idx=6&mid=2651097746&sn=af62497b174553b5dff4ab33bef3347e', '1464827426524', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-5924391.jpg/640', '-1306815040');
INSERT INTO `graduation_favorite` VALUES ('1001', '进军VR和物联网？不做手机的诺基亚在下一盘大棋', '2016-04-27', '爱范儿', 'http://mp.weixin.qq.com/s?__biz=MjgzMTAwODI0MA==&idx=2&mid=2651842802&sn=51201bcdbe90314934137ca3998d4457', '1461748647072', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-4780950.jpg/640', '-1078350229');
INSERT INTO `graduation_favorite` VALUES ('1001', '不管你在哪里上班，请记住以下黄金定律！', '2016-06-01', '路过心灵的句子', 'http://mp.weixin.qq.com/s?__biz=MjM5NjI2NTM2MA==&idx=2&mid=2654541405&sn=5f6599c88aaa1fee9c3540b939b5e98f', '1464788147432', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-5914362.jpg/640', '-992444086');
INSERT INTO `graduation_favorite` VALUES ('1001', '我是处男不适合哈哈', '2016-06-01', '幽默笑话大全', 'http://mp.weixin.qq.com/s?__biz=MjM5MjUyNzQzOA==&idx=2&mid=2651340182&sn=fcd3e03e8c32cd182bf182a8875c8c4c', '1464759163117', 'http://t1.qpic.cn/mblogpic/48da2c609d0a7f9d083e/2000', '-13673591');
INSERT INTO `graduation_favorite` VALUES ('1001', '这个视频收好，够你笑好几天了', '2016-05-25', '怪噜范', 'http://mp.weixin.qq.com/s?__biz=MjM5Nzk4MDQ3MA==&idx=4&mid=2652691257&sn=d41d33e588e34d4dd51183730123ca5a', '1464166270771', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-5666164.jpg/640', '347344959');
INSERT INTO `graduation_favorite` VALUES ('1001', '《与神同行》5月开机豪华阵容引关注', '2016-04-28', '韩剧', 'http://mp.weixin.qq.com/s?__biz=MzA4OTE5MDExMw==&idx=3&mid=2654315082&sn=bd468582db0bb48e540f4edcdb28bd0c', '1462670872106', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-4806059.jpg/640', '562554048');
INSERT INTO `graduation_favorite` VALUES ('1001', '为报复出轨的老公，我和初恋滚床了。。。', '2016-06-01', '都市言情小说', 'http://mp.weixin.qq.com/s?__biz=MzA3MTA3Nzc4Mg==&idx=1&mid=2649758906&sn=b7d0f588646281a0ea458e097896dd96', '1464759192196', 'http://t1.qpic.cn/mblogpic/f01a972dbcc1060fd456/2000', '781991116');
INSERT INTO `graduation_favorite` VALUES ('1001', '他们研发的这个水杯能让你一键拍出朋友圈大片', '2016-05-25', '极客公园', 'http://mp.weixin.qq.com/s?__biz=MTMwNDMwODQ0MQ==&idx=1&mid=2652840654&sn=cbf6b244d178e7b8bb13b551b874a38f', '1464166266349', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-5695173.jpg/640', '1125842751');
INSERT INTO `graduation_favorite` VALUES ('1001', '【金融】蚂蚁金服完成B轮融资剑指农村金融、绿色金融和国际化', '2016-04-27', '支付圈', 'http://mp.weixin.qq.com/s?__biz=MjM5OTM2NTE4Mw==&idx=2&mid=2652857377&sn=8dfd16c251469446e811982c593bb709', '1464759224494', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-4780134.jpg/640', '1324969477');
INSERT INTO `graduation_favorite` VALUES ('1001', '心里的苦，只有你最清楚', '2016-06-01', '那些路过心上的句子', 'http://mp.weixin.qq.com/s?__biz=MjM5MTg3OTAzNg==&idx=5&mid=2650737301&sn=a6c3374ba96c5ee818290e2390c6e1cc', '1464759187884', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-5911035.jpg/640', '1380893067');
INSERT INTO `graduation_favorite` VALUES ('1001', '北京市第十届“和谐杯”乒乓球比赛在清华大学举行', '2016-05-31 11:00', '凤凰体育', 'http://sports.ifeng.com/a/20160531/48883491_0.shtml', '1464759211759', 'http://d.ifengimg.com/w145_h103/p2.ifengimg.com/a/2016_23/e4186f9a3944834_size498_w621_h408.jpg', '1534741025');
INSERT INTO `graduation_favorite` VALUES ('1001', '话不狠，站不稳，老实人都该看看', '2016-06-01', '那些路过心上的句子', 'http://mp.weixin.qq.com/s?__biz=MjM5MTg3OTAzNg==&idx=4&mid=2650737301&sn=236f5f19a91d3c13ebcc7914523c2d38', '1464759195649', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-4207625.jpg/640', '1576706816');
INSERT INTO `graduation_favorite` VALUES ('1001', '郎平赞男排有士气有进步：打强队要敢赢不怕输', '2016-05-31 14:58', '凤凰体育', 'http://sports.ifeng.com/a/20160531/48884857_0.shtml', '1464759207869', 'http://d.ifengimg.com/w145_h103/p1.ifengimg.com/a/2016_23/43b1c64d7390d6c_size190_w563_h293.png', '1597214734');
INSERT INTO `graduation_favorite` VALUES ('1001', '你的阳台还是用来晾衣服吗？太浪费了！', '2016-06-01', '树洞', 'http://mp.weixin.qq.com/s?__biz=MjM5MDAyOTkyMA==&idx=1&mid=2651281680&sn=1ee40507d665969d2089c25668124a83', '1464759155539', 'http://zxpic.gtimg.com/infonew/0/wechat_pics_-5910644.jpg/640', '1906949911');
INSERT INTO `graduation_favorite` VALUES ('1001', '巴黎奥申委副主席：欧洲杯安全将影响奥运申办', '2016-05-31 23:00', '凤凰体育', 'http://sports.ifeng.com/a/20160531/48886580_0.shtml', '1464759204853', 'http://d.ifengimg.com/w145_h103/p2.ifengimg.com/a/2016_23/8c4ddaca2dab69f_size46_w600_h450.jpg', '2129005791');

-- ----------------------------
-- Table structure for `graduation_login_state`
-- ----------------------------
DROP TABLE IF EXISTS `graduation_login_state`;
CREATE TABLE `graduation_login_state` (
  `userid` bigint(20) NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of graduation_login_state
-- ----------------------------
INSERT INTO `graduation_login_state` VALUES ('-1867378635', '1d54a4068d200dedc3796d23c429c3f8');
INSERT INTO `graduation_login_state` VALUES ('-1247840544', '47437ae52674b389dd82ea0721a3a73c');
INSERT INTO `graduation_login_state` VALUES ('0', '55762f6979c1c7cec78bc674da75a804');
INSERT INTO `graduation_login_state` VALUES ('49', 'a1706490f048279e00f3d22ef6ca193b');
INSERT INTO `graduation_login_state` VALUES ('99', 'daf332e47708cd4ea8288640fd0af700');
INSERT INTO `graduation_login_state` VALUES ('58460', '5d493beebc22d0e10ff877b1856e13e2');
INSERT INTO `graduation_login_state` VALUES ('398685272', '8dd936625cb34cb223626d8e7869e56b');
INSERT INTO `graduation_login_state` VALUES ('989023396', 'd5eed51a13257d7baddcb726bc34ad77');
INSERT INTO `graduation_login_state` VALUES ('1963611698', 'a30e8616852483731236f0f608528b59');

-- ----------------------------
-- Table structure for `graduation_reply`
-- ----------------------------
DROP TABLE IF EXISTS `graduation_reply`;
CREATE TABLE `graduation_reply` (
  `reply_avatar` varchar(255) DEFAULT NULL,
  `reply_nick` varchar(255) DEFAULT NULL,
  `reply_id` bigint(20) NOT NULL,
  `comment_id` bigint(20) NOT NULL,
  `reply_comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reply_content` varchar(255) DEFAULT NULL,
  `reply_time` bigint(20) NOT NULL,
  PRIMARY KEY (`reply_comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of graduation_reply
-- ----------------------------
INSERT INTO `graduation_reply` VALUES ('/Pic/Avatar/1963611698_44b25a40039ef12479c009726a6b5747.jpg', '大灯泡', '1963611698', '1465816332869', '13', '普通人', '1464827309473');
INSERT INTO `graduation_reply` VALUES ('/Pic/Avatar/1963611698_44b25a40039ef12479c009726a6b5747.jpg', '大灯泡', '1963611698', '1465816347151', '14', '户口', '1464827323755');

-- ----------------------------
-- Table structure for `graduation_user`
-- ----------------------------
DROP TABLE IF EXISTS `graduation_user`;
CREATE TABLE `graduation_user` (
  `userId` bigint(20) NOT NULL,
  `nick` char(15) DEFAULT NULL,
  `wall_pic` varchar(200) DEFAULT NULL,
  `avatar` varchar(200) DEFAULT NULL,
  `username` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `register_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of graduation_user
-- ----------------------------
INSERT INTO `graduation_user` VALUES ('-1867378635', '凄凄切切', '/Pic/WallPic/-1867378635_c5af37e95d63a9322223ebfb4ccffa03.jpg', '/Pic/Avatar/-1867378635_8e5048b7ca7659593021238fac6da9e1.jpg', '123456789', '25f9e794323b453885f5181f1b624d0b', null, '1462791937148');
INSERT INTO `graduation_user` VALUES ('-1247840544', 'cvgg', null, null, 'gghhhh', '6a9166ff3fadfa005fc8c97e33e1a476', null, '1463026933584');
INSERT INTO `graduation_user` VALUES ('0', '', null, null, '', 'd41d8cd98f00b204e9800998ecf8427e', null, '1464158135740');
INSERT INTO `graduation_user` VALUES ('49', '1', null, null, '1', 'c4ca4238a0b923820dcc509a6f75849b', null, '1463228863257');
INSERT INTO `graduation_user` VALUES ('99', '1', null, null, 'c', 'd41d8cd98f00b204e9800998ecf8427e', null, '1465234354990');
INSERT INTO `graduation_user` VALUES ('58460', '', null, null, '9u8', 'd41d8cd98f00b204e9800998ecf8427e', null, '1465234530058');
INSERT INTO `graduation_user` VALUES ('398685272', '', null, null, 'a233192133', '3139b704136bbade9579b2206cb1a9da', null, '1462436410549');
INSERT INTO `graduation_user` VALUES ('989023396', 'deng', '/Pic/WallPic/989023396_e8fc1561cc7f375e2ad608948a6320e2.jpg', '/Pic/Avatar/989023396_5687655cd2d62ee0cdb3b4e92d711918.jpg', 'razerdp', 'e10adc3949ba59abbe56e057f20f883e', 'qwrty', '1461816163610');
INSERT INTO `graduation_user` VALUES ('1963611698', '大灯泡', '/Pic/WallPic/1963611698_79146a03906bd6f59165a435e5005ffd.jpg', '/Pic/Avatar/1963611698_35d58d4969fd39c7b7fd101a10f4c568.jpg', '13570489517', 'e10adc3949ba59abbe56e057f20f883e', null, '1461746588751');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userId` bigint(20) NOT NULL,
  `nick` char(15) DEFAULT NULL,
  `avatar` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1001', '羽翼君', 'http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg');
INSERT INTO `user` VALUES ('1002', '拉拉', 'http://img5.duitang.com/uploads/item/201502/13/20150213125742_xyeBc.jpeg');
INSERT INTO `user` VALUES ('1003', '凌之', 'http://img5.imgtn.bdimg.com/it/u=3341777813,2293496692&fm=11&gp=0.jpg');
INSERT INTO `user` VALUES ('1004', '涵菱', 'http://img7.3wmm.cc/pic/c/f/d/cfd2c2291ba75df42efedfe4bc62ee39.jpg');
INSERT INTO `user` VALUES ('1005', '靖灵', 'http://img5.duitang.com/uploads/item/201601/06/20160106202904_NjC8Z.jpeg');
INSERT INTO `user` VALUES ('1006', '诗雁', 'http://img4.duitang.com/uploads/item/201601/11/20160111175420_ZmTzU.jpeg');
INSERT INTO `user` VALUES ('1007', '飞凝念阳', 'http://img1.hq2011.com/uploads/allimg/150130/1145005502-0.jpg');
INSERT INTO `user` VALUES ('1008', '念萱', 'http://img4.duitang.com/uploads/item/201601/21/20160121102221_LjFyB.png');
INSERT INTO `user` VALUES ('1009', '白雪', 'http://img5.duitang.com/uploads/item/201406/26/20140626190424_TCXuP.jpeg');
INSERT INTO `user` VALUES ('1010', '傲露', 'http://img1.hao661.com/uploads/allimg/c141030/141463I01W940-5IH0.jpg');
INSERT INTO `user` VALUES ('1011', '柔胤', 'http://img5.imgtn.bdimg.com/it/u=660454163,590477124&fm=11&gp=0.jpg');
INSERT INTO `user` VALUES ('1012', '皓博', 'http://t2.du114.com/uploads/160105/18-16010511202M47.jpg');
INSERT INTO `user` VALUES ('1013', '枫振', 'http://img5.duitang.com/uploads/item/201505/26/20150526233034_SGwzF.jpeg');
INSERT INTO `user` VALUES ('1014', '  振然', 'http://cdn.duitang.com/uploads/item/201408/30/20140830175648_js4hP.png');
INSERT INTO `user` VALUES ('1015', '琪家', 'http://img5.duitang.com/uploads/item/201502/01/20150201174019_A5LYU.png');
INSERT INTO `user` VALUES ('1016', '涵菲', 'http://img5.duitang.com/uploads/item/201512/21/20151221092455_3QMA2.png');
INSERT INTO `user` VALUES ('1017', '曼彩', 'http://img0.imgtn.bdimg.com/it/u=2518317050,3343661642&fm=11&gp=0.jpg');
INSERT INTO `user` VALUES ('1018', '鑫静', 'http://img.name2012.com/uploads/allimg/121225/25-044048_233.jpg');
INSERT INTO `user` VALUES ('1019', '冰柏', 'http://img5.duitang.com/uploads/item/201512/21/20151221212228_VBNi5.jpeg');
INSERT INTO `user` VALUES ('1020', '萱优', 'http://img4.duitang.com/uploads/item/201503/21/20150321105218_4WZk4.jpeg');
INSERT INTO `user` VALUES ('1021', '呈雪', 'http://cdn.duitang.com/uploads/item/201409/13/20140913105231_4UxHW.png');
INSERT INTO `user` VALUES ('1022', '韵彦', 'http://cdn.duitang.com/uploads/item/201511/07/20151107225910_iyhYc.jpeg');
INSERT INTO `user` VALUES ('1023', '瑶雯', 'http://img.name2012.com/uploads/allimg/2015-02/24-031809_962.jpg');
INSERT INTO `user` VALUES ('1024', '琬露', 'http://cdn.duitang.com/uploads/item/201407/28/20140728104952_wtvEK.jpeg');
INSERT INTO `user` VALUES ('1025', '楠娜', 'http://img.name2012.com/uploads/allimg/120927/144F05C3-1.jpg');
INSERT INTO `user` VALUES ('1026', '梦茜', 'http://www.qqjia.com/z/04/tu6180_28.jpg');
INSERT INTO `user` VALUES ('1027', '桂欣', 'http://d.hiphotos.baidu.com/zhidao/pic/item/b8014a90f603738dfa989413b51bb051f819ec35.jpg');
INSERT INTO `user` VALUES ('1028', '芙彤', 'http://f.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=82bbd66c0b46f21fc9615655c6144758/cefc1e178a82b9011fe15ba4728da9773812efdd.jpg');
INSERT INTO `user` VALUES ('1029', '初静', 'http://img4.duitang.com/uploads/item/201208/15/20120815234815_TF5J3.jpeg');
INSERT INTO `user` VALUES ('1030', '欣', 'http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=741f08f897cad1c8d0eef4234a0e4b3f/d1a20cf431adcbef325866d1aeaf2edda2cc9ffc.jpg');
INSERT INTO `user` VALUES ('1031', '明卿城月、倾城陌', 'http://cdn.duitang.com/uploads/item/201202/07/20120207095217_vWTU8.thumb.200_200_c.jpg');
INSERT INTO `user` VALUES ('1032', '┌;ωǒ缺嗳`灬.°', 'http://a0.att.hudong.com/63/05/01200000024830134395052401105_02_250_250.jpg');
INSERT INTO `user` VALUES ('1033', '静月思怜梦清幽丶', 'http://wenwen.soso.com/p/20101223/20101223204523-2718391.jpg');
INSERT INTO `user` VALUES ('1034', '有卞白贤你特么还想发光', 'http://wenwen.soso.com/p/20100809/20100809101549-2063103943.jpg');
INSERT INTO `user` VALUES ('1035', '暮色伊人。', 'http://b.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=6a5d1183d358ccbf1be9bd3c29e89006/9213b07eca806538d5541c2295dda144ad348241.jpg');
INSERT INTO `user` VALUES ('1036', '璀璨人生', 'http://tx.haiqq.com/uploads/allimg/150323/1513261b8-0.jpg');
INSERT INTO `user` VALUES ('1037', '青衣沽酒醉风尘', 'http://img1.imgtn.bdimg.com/it/u=3591767483,4170926946&fm=21&gp=0.jpg');
INSERT INTO `user` VALUES ('1038', '短发美比我在这i', 'http://img0w.pconline.com.cn/pconline/1310/29/3719457_13667094527.jpg');
INSERT INTO `user` VALUES ('1039', '~花舞う街で~', 'http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=fa3f854c8618367aaddc77d91b43a7e2/bba1cd11728b4710f37cb5a9c3cec3fdfc032307.jpg');
INSERT INTO `user` VALUES ('1040', '听雨潇潇', 'http://v1.qzone.cc/avatar/201402/23/19/17/5309d8db549b1280.jpg!200x200.jpg');
INSERT INTO `user` VALUES ('1041', '贩卖后悔药i', 'http://www.ld12.com/upimg358/allimg/c150416/14291D5442M20-1QN2.jpg');
INSERT INTO `user` VALUES ('1042', '心泉', 'http://www.touxiang.cn/uploads/20131012/12-054248_852.jpg');
INSERT INTO `user` VALUES ('1043', '爱你岂会lay', 'http://img1.touxiang.cn/uploads/20120918/18-011456_951.jpg');
INSERT INTO `user` VALUES ('1044', '花凌若别离', 'http://img1.touxiang.cn/uploads/20131030/30-075658_622.jpg');
INSERT INTO `user` VALUES ('1045', '鹿痴≠路痴i', 'http://img1.touxiang.cn/uploads/20141128/28-021801_65.jpg');
INSERT INTO `user` VALUES ('1046', '゛千城墨白ぅ.', 'http://img1.touxiang.cn/uploads/20141128/28-021802_649.jpg');
INSERT INTO `user` VALUES ('1047', '心里向阳-无惧悲伤°', 'http://img1.touxiang.cn/uploads/20141128/28-021803_437.jpg');
INSERT INTO `user` VALUES ('1048', '丑化小丑不丑。', 'http://img1.touxiang.cn/uploads/20141128/28-021805_451.jpg');
INSERT INTO `user` VALUES ('1049', '彡炫灬月影萧梦灬', 'http://img1.touxiang.cn/uploads/20141128/28-021806_717.jpg');
INSERT INTO `user` VALUES ('1050', '飘扬的青春', 'http://img1.touxiang.cn/uploads/20141128/28-021807_906.jpg');
INSERT INTO `user` VALUES ('1051', '树深时见影', 'http://img1.touxiang.cn/uploads/20141128/28-021805_451.jpg');
INSERT INTO `user` VALUES ('1052', '透过骨z1里的傲 つ', 'http://img1.touxiang.cn/uploads/20141128/28-021810_437.jpg');
INSERT INTO `user` VALUES ('1053', '花开半夏锁琉璃°', 'http://img1.touxiang.cn/uploads/20141128/28-021812_664.jpg');
INSERT INTO `user` VALUES ('1054', '♂ωǒ缺嗳♂', 'http://img1.touxiang.cn/uploads/20141128/28-021813_113.jpg');
INSERT INTO `user` VALUES ('1055', '烟雨寒~伊人醉', 'http://img1.touxiang.cn/uploads/20141128/28-021816_850.jpg');
INSERT INTO `user` VALUES ('1056', '飞翔的企鹅', 'http://img1.touxiang.cn/uploads/20141128/28-021819_895.jpg');
INSERT INTO `user` VALUES ('1057', '妖视觉〃', 'http://img1.touxiang.cn/uploads/20141128/28-021817_497.jpg');
INSERT INTO `user` VALUES ('1058', '天若有情天亦老。', 'http://img1.touxiang.cn/uploads/20141113/13-020214_185.jpg');
INSERT INTO `user` VALUES ('1059', '竹妖妖ヅ鸢尾', 'http://img1.touxiang.cn/uploads/20141104/04-073046_966.jpg');
INSERT INTO `user` VALUES ('1060', '奈何桥旁风满袖', 'http://img1.touxiang.cn/uploads/20141104/04-073052_699.jpg');
INSERT INTO `user` VALUES ('1061', '换一个梦', 'http://img1.touxiang.cn/uploads/20141104/04-073055_975.jpg');
INSERT INTO `user` VALUES ('1062', '青丘。白浅', 'http://img1.touxiang.cn/uploads/20141104/04-073053_703.jpg');
INSERT INTO `user` VALUES ('1063', '我生性薄凉并且坚强', 'http://img1.touxiang.cn/uploads/20141104/04-073103_37.jpg');
INSERT INTO `user` VALUES ('1064', '_斷橋殘影', 'http://img1.touxiang.cn/uploads/20141104/04-073102_708.jpg');
INSERT INTO `user` VALUES ('1065', '耻、 ￣笑', 'http://img1.touxiang.cn/uploads/20141104/04-073114_906.jpg');
INSERT INTO `user` VALUES ('1066', '用心生活i', 'http://img1.touxiang.cn/uploads/20141104/04-073112_180.jpg');
INSERT INTO `user` VALUES ('1067', '黎夕旧梦', 'http://img1.touxiang.cn/uploads/20141104/04-073117_995.jpg');
INSERT INTO `user` VALUES ('1068', '＃〃自刎≈.', 'http://img1.touxiang.cn/uploads/20141104/04-073115_55.jpg');
INSERT INTO `user` VALUES ('1069', '[醉落夕风]', 'http://img1.touxiang.cn/uploads/20141104/04-073106_797.jpg');
INSERT INTO `user` VALUES ('1070', '对你好', 'http://img1.touxiang.cn/uploads/20141104/04-073132_143.jpg');
INSERT INTO `user` VALUES ('1071', '╯轻影映红窗', 'http://img1.touxiang.cn/uploads/20141104/04-073128_640.jpg');
INSERT INTO `user` VALUES ('1072', '初三，拼了。', 'http://img1.touxiang.cn/uploads/20140815/15-072732_222.jpg');
INSERT INTO `user` VALUES ('1073', '墨烟三色倾人城。', 'http://img1.touxiang.cn/uploads/20140815/15-072749_540.jpg');
INSERT INTO `user` VALUES ('1074', '预见你！', 'http://img1.touxiang.cn/uploads/20140815/15-072740_109.jpg');
INSERT INTO `user` VALUES ('1075', '失败的谎言,', 'http://img1.touxiang.cn/uploads/20140815/15-072756_229.jpg');
INSERT INTO `user` VALUES ('1076', '夜清冷、一曲。', 'http://img1.touxiang.cn/uploads/20140812/12-072635_993.jpg');
INSERT INTO `user` VALUES ('1077', '执着于有你的执着i', 'http://img1.touxiang.cn/uploads/20140812/12-072751_803.jpg');
INSERT INTO `user` VALUES ('1078', '静若繁花', 'http://img1.touxiang.cn/uploads/20140812/12-072800_27.jpg');
INSERT INTO `user` VALUES ('1079', '我要当女子汉！', 'http://img1.touxiang.cn/uploads/20140812/12-072642_821.jpg');
INSERT INTO `user` VALUES ('1080', '夏漠、秋雨', 'http://img1.touxiang.cn/uploads/20140812/12-072829_61.jpg');
INSERT INTO `user` VALUES ('1081', '别嘲笑胖女孩！', 'http://img1.touxiang.cn/uploads/20140812/12-072839_61.jpg');
INSERT INTO `user` VALUES ('1082', '倾城一世繁花落', 'http://img1.touxiang.cn/uploads/20140812/12-072851_322.jpg');
INSERT INTO `user` VALUES ('1083', '别停留i', 'http://img1.touxiang.cn/uploads/20140812/12-072906_577.jpg');
INSERT INTO `user` VALUES ('1084', '爱情公寓火不过青果学院', 'http://img1.touxiang.cn/uploads/20140812/12-072909_488.jpg');
INSERT INTO `user` VALUES ('1085', '淡然一身，看透尘世纠纷', 'http://img1.touxiang.cn/uploads/20140812/12-072859_405.jpg');
INSERT INTO `user` VALUES ('1086', '我是手机达人', 'http://img1.touxiang.cn/uploads/20140812/12-072917_799.jpg');
INSERT INTO `user` VALUES ('1087', '爱4太给力', 'http://img1.touxiang.cn/uploads/20140812/12-072847_304.jpg');
INSERT INTO `user` VALUES ('1088', '默 ’＿哀、', 'http://img1.touxiang.cn/uploads/20140812/12-072932_837.jpg');
INSERT INTO `user` VALUES ('1089', '从不低调@', 'http://img1.touxiang.cn/uploads/20140812/12-072935_664.jpg');
INSERT INTO `user` VALUES ('1090', 'mm要吃大饼膜', 'http://img1.touxiang.cn/uploads/20140812/12-072939_501.jpg');
INSERT INTO `user` VALUES ('1091', 'Govern (统治)', 'http://img1.touxiang.cn/uploads/20140721/21-063810_387.jpg');
INSERT INTO `user` VALUES ('1092', '爱情专利', 'http://img1.touxiang.cn/uploads/20140705/05-055159_827.jpg');
INSERT INTO `user` VALUES ('1093', '别太耀眼°', 'http://img1.touxiang.cn/uploads/20140705/05-055217_844.jpg');
INSERT INTO `user` VALUES ('1094', '转身&未来', 'http://img1.touxiang.cn/uploads/20140705/05-055221_591.jpg');
INSERT INTO `user` VALUES ('1095', '不回头的倔强', 'http://img1.touxiang.cn/uploads/20140705/05-055238_933.jpg');
INSERT INTO `user` VALUES ('1096', '让世界为我而改变', 'http://img1.touxiang.cn/uploads/20140705/05-055304_607.jpg');
INSERT INTO `user` VALUES ('1097', '妞,狠天真', 'http://img1.touxiang.cn/uploads/20140701/01-090945_744.jpg');
INSERT INTO `user` VALUES ('1098', '与寂寞抗衡', 'http://img1.touxiang.cn/uploads/20140701/01-090957_36.jpg');
INSERT INTO `user` VALUES ('1099', '@ 友尽,！', 'http://img1.touxiang.cn/uploads/20140701/01-090951_518.jpg');
INSERT INTO `user` VALUES ('1100', '女王驾到i', 'http://img1.touxiang.cn/uploads/20140701/01-091011_835.jpg');
INSERT INTO `user` VALUES ('1101', '′指间de落寞.', 'http://img1.touxiang.cn/uploads/20140701/01-091004_91.jpg');
INSERT INTO `user` VALUES ('1102', '求佛不如拜我', 'http://img1.touxiang.cn/uploads/20140701/01-091014_434.jpg');
INSERT INTO `user` VALUES ('1103', '單身リ天涯', 'http://img1.touxiang.cn/uploads/20140701/01-091027_417.jpg');
INSERT INTO `user` VALUES ('1104', '不败的意志', 'http://img1.touxiang.cn/uploads/20140701/01-091038_463.jpg');
INSERT INTO `user` VALUES ('1105', '秒杀你的自大≈', 'http://img1.touxiang.cn/uploads/20140701/01-091105_4.jpg');
INSERT INTO `user` VALUES ('1106', '放荡不羁就是边爷i', 'http://img1.touxiang.cn/uploads/20140701/01-091132_958.jpg');
INSERT INTO `user` VALUES ('1107', '钻石心i', 'http://img1.touxiang.cn/uploads/20140701/01-091116_380.jpg');
INSERT INTO `user` VALUES ('1108', '枪毙走狗@', 'http://img1.touxiang.cn/uploads/20140701/01-091200_595.jpg');
INSERT INTO `user` VALUES ('1109', '↗奋发向上', 'http://img1.touxiang.cn/uploads/20140701/01-091119_375.jpg');
INSERT INTO `user` VALUES ('1110', '笑对世间狗', 'http://img1.touxiang.cn/uploads/20140701/01-091211_951.jpg');
INSERT INTO `user` VALUES ('1111', '超越梦想、', 'http://img1.touxiang.cn/uploads/20140703/03-084519_708.jpg');
INSERT INTO `user` VALUES ('1112', '分了、我来合。', 'http://img1.touxiang.cn/uploads/20140703/03-084526_369.jpg');
INSERT INTO `user` VALUES ('1113', '╭ァ幸福由╮我独家赞助', 'http://img1.touxiang.cn/uploads/20140703/03-084540_965.jpg');
INSERT INTO `user` VALUES ('1114', '别做逃兵', 'http://img1.touxiang.cn/uploads/20140703/03-084550_937.jpg');
INSERT INTO `user` VALUES ('1115', '超越梦想、', 'http://img1.touxiang.cn/uploads/20140703/03-084543_307.jpg');
