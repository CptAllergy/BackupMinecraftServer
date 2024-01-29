# BackupMinecraftServer
A java program that creates backups of server files, on server shutdown.

## Description
This program copies all contents from the world folder, mods folder, and configuration files and creates a zip file with all of these contents on a new "Backups" folder.

## Usage
To use this program simply download the jar file, add it to the server source folder and add the following line to the end of your "run.bat" or "start.bat":

```
java -jar BackupMinecraftServer-1-jar-with-dependencies.jar
```

This will create a new backup everytime the the server is shutdown.
