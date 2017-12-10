package agh.iosr.storage.api;

import java.io.File;
import java.net.URL;

public interface StorageService {
    URL uploadFile(String filename, File uploadFile);
}
