#-*-coding:utf-8-*-
#Python 2.7
#Author Scott.fan
import ConfigParser
from hashlib import md5
import os,time
import fnmatch
import MySQLdb
import logging
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
gTime = int(time.time()) #当前时间


class ConfigINI:
    parse = ""
    conffile = "config.ini"

    def __init__(self, configFile=None):
        self.parse = ConfigParser.ConfigParser()
        #通过取传进来的不同配置文件来加载配置
        if len(sys.argv[1:]) >= 1:
            self.conffile = sys.argv[1]
        if configFile != None:
            self.conffile = configFile
        self.parse.read(self.conffile)
        sections = self.parse.sections()
        for i in sections:
            options = self.parse.options(i)
            values = self.parse.items(i)
            print "Section is %s " % i
            #print "Options are %s " % options
            print "Values are %s " % values

    def get_log_level(self):
        return self.parse.get("log", "log_level")

    def get_log_file(self):
        return self.parse.get("log", "log_file")

    def get_db_host(self):
        return self.parse.get("db", "db_host")

    def get_db_port(self):
        return self.parse.get("db", "db_port")

    def get_db_name(self):
        return self.parse.get("db", "db_name")

    def get_db_user(self):
        return self.parse.get("db", "db_user")

    def get_db_passwd(self):
        return self.parse.get("db", "db_passwd")

    def get_list_tpl(self):
    	return self.parse.get("template", "list_tpl")

    def get_listitem_tpl(self):
    	return self.parse.get("template", "listitem_tpl")

    def get_article_tpl(self):
    	return self.parse.get("template", "article_tpl")



def init_logging(mconf):
    log2level = [logging.NOTSET, logging.DEBUG, logging.INFO, logging.WARNING, logging.ERROR, logging.CRITICAL]
    logging.basicConfig(
        level=log2level[int(mconf.get_log_level())],
        format='LINE %(lineno)-4d  %(levelname)-8s %(message)s',
        datefmt='%m-%d %H:%M',
        filename=mconf.get_log_file(),
        filemode='w')
    # define a Handler which writes INFO messages or higher to the sys.stderr
    console = logging.StreamHandler()
    console.setLevel(logging.DEBUG)
    # set a format which is simpler for console use
    formatter = logging.Formatter('LINE %(lineno)-4d : %(levelname)-8s %(message)s')
    # tell the handler to use this format
    console.setFormatter(formatter)
    logging.getLogger('').addHandler(console)
    logging.debug("init_logging sucess !")


def get_file_md5(filename):
    if not os.path.isfile(filename):
        return ""
    m = md5()
    f = open(filename, 'rb')
    m.update(f.read())
    f.close()
    return m.hexdigest()

def get_string_md5(str):
	m = md5()
	m.update(str)
	return m.hexdigest()

class DBUtils:
    conn = None
    confutils = None

    def __init__(self, cnf):
        self.confutils = cnf
        self.get_conn()
        logging.debug("DBUtils init sucess ! create conn !")

    def get_conn(self):
        cnf = self.confutils
        self.conn = MySQLdb.connect(host=cnf.get_db_host(), port=int(cnf.get_db_port()),
                                    user=cnf.get_db_user(), passwd=cnf.get_db_passwd(), db=cnf.get_db_name(), charset="utf8")
        return self.conn

    def __del__(self):
        if not self.conn is None:
            self.conn.close()
            print("DBUtils conn close sucess !")

    def executeSql(self, sql, param):
		logging.debug(" execute sql: " + sql)
		if self.conn is None:
			self.get_conn()
		cursor = self.conn.cursor()
		ret = 0
		try:
			if param is None:
				ret = cursor.execute(sql)
			else:
				ret = cursor.execute(sql, param)
			self.conn.commit()
		except Exception, e:
			logging.error(e)
		finally:
			cursor.close()
		return ret

    def fetchOne(self, sql, param):
		logging.debug(" execute sql: " + sql)
		if self.conn is None:
			self.get_conn()
		cursor = self.conn.cursor()
		ret = 0
		res = None
		try:
			if param is None:
				ret = cursor.execute(sql)
			else:
				ret = cursor.execute(sql, param)
			logging.debug("ret={}".format(ret))
			if ret != 0:
				res = cursor.fetchone()
			self.conn.commit()
		except Exception, e:
			logging.error(e)
		finally:
			cursor.close()
		return res

    def fetchAll(self, sql, param):
		logging.debug(" execute sql: " + sql)
		if self.conn is None:
			self.get_conn()
		cursor = self.conn.cursor()
		ret = 0
		res = None
		try:
			if param is None:
				ret = cursor.execute(sql)
			else:
				ret = cursor.execute(sql, param)
			logging.debug("ret={}".format(ret))
			if ret != 0:
				res = cursor.fetchall()
			self.conn.commit()
		except Exception, e:
			logging.error(e)
		finally:
			cursor.close()
		return res

    def update(self, file_md5, file_path, file_abs_path, tablename):
        logging.debug("path: %s , md5:%s, abs_path:%s, table:%s" % (file_path, file_md5, file_abs_path, tablename))
        #更新数据进db
        if self.conn is None:
            self.get_conn()
        cursor = self.conn.cursor()
        try:
            sql = "replace into "+tablename+"(file_path,file_md5,file_abs_path,insert_time) values(%s, %s,%s, %s)"
            ret = cursor.execute(sql, (file_path, file_md5, file_abs_path, gTime))
            self.conn.commit()
        except Exception, e:
            logging.error(e)
        finally:
            logging.debug("path: %s , md5:%s, ret:%d" % (file_path, file_md5, ret))
            cursor.close()
        pass
    
    def updateColumn(self, column, value, whereArgs, tablename):
        logging.debug("column: %s , value:%s, whereArgs:%s, tablename:%s" % (column, value, whereArgs, tablename))
        #更新数据库表中的某列
        if self.conn is None:
            self.get_conn()
        cursor = self.conn.cursor()
        ret = 0
        try:
            if whereArgs is None:
                sql = "update " + tablename + " set " + column + "='" + value + "'"
            else:
                sql = "update " + tablename + " set " + column + "='" + value + "' where " + whereArgs
            print sql
            ret = cursor.execute(sql)
            self.conn.commit()
        except Exception, e:
            logging.error(e)
        finally:
            logging.debug("ret:%d" % (ret))
            cursor.close()
        pass

    def select(self, selections, whereArgs, tablename):
        logging.debug("selections: %s , whereArgs:%s, tablename:%s" % (selections, whereArgs, tablename))
        if self.conn is None:
            self.get_conn()
        cursor = self.conn.cursor()
        ret = 0
        res = None
        try:
            if whereArgs is None:
                sql = "select " + selections + " from " + tablename
            else:
                sql = "select " + selections +  " from " + tablename + " where " + whereArgs
            print sql
            ret = cursor.execute(sql)

            if ret != 0:
            	res = cursor.fetchall()
            self.conn.commit()
        except Exception, e:
            logging.error(e)
        finally:
            logging.debug("ret:%d" % (ret))
            cursor.close()
        return res

def generateHtml(cnf, db):
	logging.debug("start generate static html page ...")
	tablename = "dpq_posts"
	article_tpl = cnf.get_article_tpl()
	list_tpl = cnf.get_list_tpl()
	listitem_tpl = cnf.get_listitem_tpl()

	res = db.select('*', 'id <5', tablename)
	if res != None:
		for row in res:
			id = row[0]
			author = 'admin'
			keywords = row[2]
			source = row[3]
			post_date = row[4].strftime('%Y-%m-%d')
			content = row[5]
			title = row[6]
			excerpt = row[7]
			href = '/static/{}.html'.format(id)

			logging.debug("id:%s title:%s" % (id, title))

			article_tpl_text = readFile(article_tpl)
			article_tpl_text = article_tpl_text.replace('[--title--]', title)
			article_tpl_text = article_tpl_text.replace('[--href--]', href)
			article_tpl_text = article_tpl_text.replace('[--excerpt--]', excerpt)
			article_tpl_text = article_tpl_text.replace('[--data_key--]', get_string_md5(content))
			article_tpl_text = article_tpl_text.replace('[--post_source--]', source)
			article_tpl_text = article_tpl_text.replace('[--article_content--]', content)
			article_tpl_text = article_tpl_text.replace('[--post_date--]', post_date)
			article_tpl_text = article_tpl_text.replace('[--post_author--]', author)
			filepath = 'static/{}.html'.format(id)
			writeFile(article_tpl_text, filepath)
	pass


def readFile(filename):
	if not os.path.isfile(filename):
		return ""
	fp = open(filename, 'r')
	try:
		content = fp.read()
	finally:
		fp.close()
	return content

def writeFile(content, filename):
	fp = open(filename, 'w')
	try:
		content = fp.write(content)
	finally:
		fp.close()



def db_test(cnf, db):
	sql = "select title,classid,version from aguo_ecms_download where id>%s"
	param = (140379)
	res = db.fetchAll(sql, param)
	if res != None:
		for row in res:
			print row[0]
			print row[1]
			print row[2]
	param = ("abc","123","456")
	title = param[0]
	print title



if __name__ == "__main__":
    conf = ConfigINI()
    #初始化log
    init_logging(conf)
    #初始化db
    dbutils = DBUtils(conf)

    generateHtml(conf, dbutils)

    logging.debug("__main__ is run end !")
    sys.exit(1)
