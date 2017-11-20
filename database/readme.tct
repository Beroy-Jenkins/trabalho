cria o banco de dados
mysql -u base_user -pbase_user_pass -e "create database trabalho; GRANT ALL PRIVILEGES ON trabalho.* TO root@localhost IDENTIFIED BY 'root'

restaura o banco de dados
mysql -u root -proot trabalho < trabalho.SQL

faz o backup
mysqldump --user=root --password=root trabalho > trabalho-%datetimef%.SQL