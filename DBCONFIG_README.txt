mkdir /etc/transmart
copy the file DbTypeConfig.groovy under transmartApp/etc/ to /etc/transmart
Set the variable in /etc/transmart/DbTypeConfig.groovy to either "oracle" or "postgres"
Start transmart
go the the URL:
localhost:<your port>/transmart/dbExample/index

It will show either the string 'oracle' or 'postgres' depending on how you configured your setting.
