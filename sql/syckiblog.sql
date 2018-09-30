/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50144
Source Host           : localhost:3306
Source Database       : syckiblog

Target Server Type    : MYSQL
Target Server Version : 50144
File Encoding         : 65001

Date: 2018-09-30 15:47:55
*/

SET FOREIGN_KEY_CHECKS=0;
create database if not exists syckiblog;
use syckiblog;

-- ----------------------------
-- Table structure for `blog_articles`
-- ----------------------------
DROP TABLE IF EXISTS `blog_articles`;
CREATE TABLE `blog_articles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `title` text NOT NULL,
  `en_name` varchar(200) NOT NULL,
  `content` longtext NOT NULL,
  `author` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_date` date DEFAULT '0000-00-00',
  `change_date` datetime DEFAULT '0000-00-00 00:00:00',
  `status` varchar(100) NOT NULL DEFAULT 'publish',
  `tags` varchar(100) NOT NULL DEFAULT 'post',
  `like_count` bigint(20) NOT NULL DEFAULT '0',
  `unlike_count` bigint(20) NOT NULL DEFAULT '0',
  `viewer_count` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `type_status_date` (`tags`,`status`,`create_date`,`id`),
  KEY `post_parent` (`parent_id`),
  KEY `post_author` (`author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_articles
-- ----------------------------
INSERT INTO `blog_articles` VALUES ('1', '1', 'auto save', 'auto-save', 'auto save', '1', '2016-12-21', '2017-09-02 14:26:28', 'old', 'post', '0', '0', '0');
INSERT INTO `blog_articles` VALUES ('2', '2', 'Linux - 管道与重定向详解', 'linux-pipe-redirect', '# Linux - 管道与重定向详解\n\n## Unix 哲学\n在 Unix 系统中，任何程序都可以实现三个接口，即：标准输入（stdin）、标准输出（stdout）、标准错误输出（stderr），你应该注意到，这三个东西前面都有标准两个字，是的，正是这种标准，使得 Unix 系统中的所有独立的程序可以相互传递数据而没有任何限制，这种机制给用户带来了极大的方便，这正是 Unix 哲学！\n\n## 什么是管道\n在 Unix/Linux 中，有一个常用的符号，叫做管道符，写做：\"|\"，举个例子：\n```\nls / | grep usr\n```\n`ls` 和 `grep` 是两个独立的程序，但是 `ls` 的输出数据可以传给 `grep` 继续处理，而管道符\"|\"在中间起到了数据传输的作用，正如其名，它就像一根橡胶水管，这根水管的两端可以连接任意程序，因为这些程序都向外提供了一组一模一样的接口，这样我们就可以将任意数量的不同功能的程序随意组合起来使用。\n\n## 什么是重定向\n在上例中，管道符的一端连接到 `ls` 的标准输出，另一端连接到 `grep` 的标准输入，那我们能不能将其中一端连接到一个文件呢？这样我们就可以将结果数据保存下来，或者将一个文件输入到某个程序中去。当然可以，这时就用到重定向了，下面以 Bash Shell 为例，解释常用重定向符号的意义及其用法。\n\n## 重定向符号\n1. `0<` 标准输入重定向，0可省略\n1. `1>` 标准输出重定向，1可省略\n1. `2>` 标准错误输出重定向\n1. 它们的用法都是左边给定一个程序，右边给定一个文件，`>` 表示覆盖，`>>` 表示追加。\n\n## 输出重定向\n如果我想把一个程序的标准输出追加到 `o.txt` 文件中，而错误输出覆盖到 `e.txt` 文件中，可以这样写：\n```\nls >> o.txt 2> e.txt\n```\n\n把标准输出与错误输出都写入到 `o.txt`：\n```\nls &> o.txt\n```\n或者像下面这样写\n```\nls > o.txt 2>&1\n```\n这两种写法是等价的，但明显第一种更为简洁。\n\n但不能写成下面这样：\n```\nls 2>&1 > o.txt\n```\n它不会像你期望的那样执行，这行命令中有两个重定向操作，所有操作符会被从左到右依次解释，首先错误输出中的数据被重定向到标准输出流中，这时标准输出指向的是终端，然后第二个重定向操作将标准输出重定向到了 `o.txt`，但这次操作只影响了标准输出，并没有影响错误输出的指向。\n\n上面的解释有些牵强，因为博主实在不知道该怎样翻译，官方解释如下：\n> directs only the standard output to file dirlist, because the standard error was duplicated from the standard output before the standard output was redirected to dirlist.\n\n## 输入重定向\n将 `i.txt` 输入到 `cat`:\n```\ncat < i.txt\n```\n\n将一段文本重定向到 `cat`，文本中包含的代码被正常执行：\n```\ncat << EOF\nhello\nThe current time:\n	`date`\nEOF\n```\n\n将一段文本重定向到 `cat`，文本中所有内容均被视为普通字符串：\n```\ncat <<\'EOF\'\nhello\nThe current time:\n	`date`\nEOF\n```\n\n将一段文本重定向到 `cat`，文本中所有内容均被视为普通字符串，且忽略所有前导制表符（也就是date前面的空白部分）：\n```\ncat <<-\'EOF\'\nhello\nThe current time:\n	`date`\nEOF\n```\n\n还有一种不常见的输入重定向：\n```\ngrep Sep <<< `date`\n```\n`<<<` 后面的字符中如果包含代码，会被正常执行，然后再重定向给 `grep`。\n\n## 命名管道\n前面讲的管道\"|\"是没有名字的，这将导致它不能在两个独立的进程间传递数据，这时可以创建一个命名管道来解决。\n```\nmkfifo /tmp/fifo\n\nll /tmp/fifo\nprw-r--r-- 1 root root 0 Sep 21 15:59 /tmp/fifo\n```\n\n等待读取数据，如果管道里没有数据，会一直阻塞：\n```\ncat /tmp/fifo\n```\n\n另一个进程写入数据：\n```\necho hello > /tmp/fifo\n```\n\n## 创建文件描述符(File Descriptors)\n我们还可以自定义一个文件描述符9，并将其绑定到一个管道文件：\n```\nmkfifo /tmp/fifo\nexec 9<>/tmp/fifo\n```\n\n然后向管道写入数据：\n```\necho `date` >&9\n```\n\n从管道读取数据：\n```\nread -u 9 var\necho $var\n```\n\n解除绑定：\n```\nexec 9<&-\nexec 9>&-\n```\n\n-End-\n', '1', '2017-09-21', '2017-10-11 13:05:31', 'publish', 'linux', '9', '0', '11');

-- ----------------------------
-- Table structure for `blog_user`
-- ----------------------------
DROP TABLE IF EXISTS `blog_user`;
CREATE TABLE `blog_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_user
-- ----------------------------
INSERT INTO `blog_user` VALUES ('1', 'admin', 'admin');

-- ----------------------------
-- Table structure for `blog_user_action`
-- ----------------------------
DROP TABLE IF EXISTS `blog_user_action`;
CREATE TABLE `blog_user_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(255) NOT NULL,
  `resource` varchar(255) NOT NULL,
  `action` varchar(255) NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_user_action
-- ----------------------------
INSERT INTO `blog_user_action` VALUES ('1', 'e9463773', '1', 'article_like_up', '2018-09-30 15:41:41');
INSERT INTO `blog_user_action` VALUES ('2', 'e9463773', '/', 'page_viewer', '2018-09-30 15:44:16');
INSERT INTO `blog_user_action` VALUES ('3', 'e9463773', '/', 'page_viewer', '2018-09-30 15:46:01');
