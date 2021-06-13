from flask_mysqldb import MySQL
import MySQLdb.cursors
import myConfig

class DatabaseOp:

    def __init__(self, sql):
        self.sql = sql

    def insert(self, name, password, email, topic):

        cursor = self.sql.connection.cursor(MySQLdb.cursors.DictCursor)

        cursor.execute('SELECT * FROM users WHERE email = % s', (email,))
        account = cursor.fetchone()
        if account:
            return False
        else:
            cursor.execute('INSERT INTO users VALUES (NULL, %s, %s, %s, %s)', (name, email, password, topic))
            self.sql.connection.commit()

            query = "INSERT INTO `user-state`(email,name,state) VALUES ('" + email + "','" + name + "','offline');"
            cursor.execute(query)
            self.sql.connection.commit()
            return True