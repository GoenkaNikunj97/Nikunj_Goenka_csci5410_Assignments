#PROFILE VIEW

from flask import *
from flask_mysqldb import MySQL
import MySQLdb.cursors
import databaseOperations,myConfig

app = Flask(__name__)

app.secret_key = "MyKey1234"
app.config['MYSQL_HOST'] = '108.59.80.112'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'admin'
app.config['MYSQL_DB'] = 'a2'

mysql = MySQL(app)

@app.route('/profile/<email>/<name>')
def viewProfile(email, name):
    session["name"] = name
    session["email"] = email
    dbObject = databaseOperations.DatabaseOp(mysql)
    allOnlineUser = dbObject.fetcOnlineUsers()

    return render_template('profile.html', username=name, allOnlineUser=allOnlineUser)

@app.route('/logout')
def Logout():
    email = session.pop('email', None)
    session.pop('username', None)

    cursor = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
    query = "UPDATE `user-state` SET state=%s WHERE email=%s"
    cursor.execute(query, ("offline", email))
    mysql.connection.commit()
    return redirect(myConfig.Config.REGISTER_URL)


if __name__ == "__main__":
    app.run(debug=True, host = "0.0.0.0", port=5002)