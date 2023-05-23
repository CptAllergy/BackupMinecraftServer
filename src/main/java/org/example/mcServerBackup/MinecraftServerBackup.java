package org.example.mcServerBackup;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MinecraftServerBackup {

    private final String serverPath;

    public MinecraftServerBackup(String serverPath) {
        this.serverPath = serverPath;
    }

    public void createBackup() {
        String newFolderName = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());

        zipBackup(newFolderName);
        //copyBackup(newFolderName + "-copy");
    }

    private void copyBackup(String newFolderName) {
        File srcWorld = new File(serverPath + "world");
        File srcMods = new File(serverPath + "mods");

        File destWorld = new File(serverPath + "backups/" + newFolderName + "/world");
        File destMods = new File(serverPath + "backups/" + newFolderName + "/mods");

        File srcBannedIps = new File(serverPath + "banned-ips.json");
        File srcBannedPlayers = new File(serverPath + "banned-players.json");
        File srcOps = new File(serverPath + "ops.json");
        File srcProperties = new File(serverPath + "server.properties");
        File srcUserCache = new File(serverPath + "usercache.json");
        File srcWhitelist = new File(serverPath + "whitelist.json");

        File destBannedIps = new File(serverPath + "backups/" + newFolderName + "/banned-ips.json");
        File destBannedPlayers = new File(serverPath + "backups/" + newFolderName + "/banned-players.json");
        File destOps = new File(serverPath + "backups/" + newFolderName + "/ops.json");
        File destProperties = new File(serverPath + "backups/" + newFolderName + "/server.properties");
        File destUserCache = new File(serverPath + "backups/" + newFolderName + "/usercache.json");
        File destWhitelist = new File(serverPath + "backups/" + newFolderName + "/whitelist.json");

        try {
            // Backup mods and world folder
            FileUtils.copyDirectory(srcWorld, destWorld);
            FileUtils.copyDirectory(srcMods, destMods);

            // Backup config files
            FileUtils.copyFile(srcBannedIps, destBannedIps);
            FileUtils.copyFile(srcBannedPlayers, destBannedPlayers);
            FileUtils.copyFile(srcOps, destOps);
            FileUtils.copyFile(srcProperties, destProperties);
            FileUtils.copyFile(srcUserCache, destUserCache);
            FileUtils.copyFile(srcWhitelist, destWhitelist);

        } catch (IOException e) {
            System.out.println("Error Copying Files");
            e.printStackTrace();
        }
    }

    private void zipBackup(String newFolderName) {
        List<String> fileNames = new LinkedList<>();
        fileNames.add("banned-ips.json");
        fileNames.add("banned-players.json");
        fileNames.add("ops.json");
        fileNames.add("server.properties");
        fileNames.add("usercache.json");
        fileNames.add("whitelist.json");

        List<String> folderNames = new LinkedList<>();
        folderNames.add("world");
        folderNames.add("mods");

        try {
            String zipDestination = "backups/" + newFolderName + ".zip";
            FileOutputStream file = new FileOutputStream(serverPath + zipDestination);
            ZipOutputStream zipOut = new ZipOutputStream(file);

            zipFiles(fileNames, newFolderName, zipOut);

            zipFolders(folderNames, newFolderName, zipOut);

            zipOut.close();
            file.close();
        } catch (IOException e) {
            System.out.println("Error Compressing Files");
            e.printStackTrace();
        }
    }


    /**
     * Zips the files with the names given in <code>fileNames<code/> and saves them to an archive with the name <code>newFolderName.zip<code/>
     *
     * @param fileNames         the names of the files to zip
     * @param newFolderName the name of the newly created archive
     * @param zipOut        the zipOutputStream to write to
     * @throws IOException
     */
    private void zipFiles(List<String> fileNames, String newFolderName, ZipOutputStream zipOut) throws IOException {
        for (String f : fileNames) {
            FileInputStream in = new FileInputStream(serverPath + f);
            ZipEntry e = new ZipEntry(newFolderName + "/" + f);
            zipOut.putNextEntry(e);

            // buffer size
            byte[] b = new byte[1024];
            int count;

            while ((count = in.read(b)) > 0) {
                zipOut.write(b, 0, count);
            }
            in.close();
        }

    }

    private void zipFolders(List<String> folderNames, String newFolderName, ZipOutputStream zipOut) throws IOException {
        for (String f : folderNames) {
            File folder = new File(serverPath + f);
            String destinationFolderName = newFolderName + "/" + folder.getName();
            zipFolder(folder, destinationFolderName, zipOut);
        }
    }


    private void zipFolder(File folder, String folderName, ZipOutputStream zipOut) throws IOException {
        if (folder.isHidden()) {
            return;
        }
        if (folder.isDirectory()) {
            if (folderName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(folderName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(folderName + "/"));
                zipOut.closeEntry();
            }
            File[] children = folder.listFiles();
            for (File childFile : children) {
                zipFolder(childFile, folderName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(folder);
        ZipEntry zipEntry = new ZipEntry(folderName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }


}
