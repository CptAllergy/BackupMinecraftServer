package org.example;

import org.apache.commons.io.FileUtils;
import org.example.mcServerBackup.MinecraftServerBackup;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String serverPath = "./";

        MinecraftServerBackup mc = new MinecraftServerBackup(serverPath);

        System.out.println("Creating Server Backup...");
        mc.createBackup();
        System.out.println("Backup Finished");
    }
}