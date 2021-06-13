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

    def selectUser(self, email, password):
        cursor = self.sql.connection.cursor(MySQLdb.cursors.DictCursor)
        query = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "';"
        cursor.execute(query)
        return cursor.fetchone()

    def setState(self, email):
        cursor = self.sql.connection.cursor(MySQLdb.cursors.DictCursor)
        query = "UPDATE `user-state` SET state=%s WHERE email=%s"
        cursor.execute(query, ("online", email))
        self.sql.connection.commit()
