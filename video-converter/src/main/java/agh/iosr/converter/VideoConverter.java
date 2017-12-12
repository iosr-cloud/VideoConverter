package agh.iosr.converter;

import agh.iosr.model.VideoConversionType;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class VideoConverter {

    public File convert(String url, VideoConversionType videoConversionType) {

        File convertedFile = new File("/tmp/" + System.currentTimeMillis());

        try {
            FileUtils.copyURLToFile(new URL(url), convertedFile);

            //work simulation for now
            Thread.sleep(1000);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}
