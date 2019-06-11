# Post A Form

This is an application to store all requests from any url on this server in a json file.

Request access to an URL.


## Access DB

To access the db of the day, use the URL /db

To have a try on forms, use the URL /try

## Post a Form

To post a form use this kind of form :

```html
<form action="/test.html" method="post" enctype="multipart/form-data">
  <input type="text" name="description" value="du texte">
  <input type="file" name="monFichier">
  <button type="submit">Envoyer</button>
</form>
```

## Compiling the application

You can compile the server with /gradlew

## Installing the server as a linux service

* Create an user postaform

```console
$ sudo adduser --system --home /home/postaform postaform
```

* Move the jar file in build/libs and in /home/postaform/post-form.jar

* Copy the file postaform.service in /etc/systemd/system/

```
[Unit]
Description=Post A Form - Server to log request on server
After=network.target

[Service]
ExecStart=/usr/bin/java -jar post-form.jar
WorkingDirectory=/home/postaform/server
StandardOutput=inherit
StandardError=inherit
Restart=always
User=postaform

[Install]
WantedBy=multi-user.target
```

* Start the service
  * sudo systemctl start myscript.service

* Enable the service for the next reboot
  * sudo systemctl enable myscript.service

