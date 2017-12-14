package agh.iosr.converter;

import agh.iosr.model.VideoConversionType;
import it.sauronsoftware.jave.*;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class VideoConverter {

    public File convert(String url, VideoConversionType videoConversionType){

        File inputFile = new File("/tmp/" + System.currentTimeMillis());
        File convertedFile = new File(inputFile.getPath() + ".flv");

        try {
            FileUtils.copyURLToFile(new URL(url), inputFile);
            VideoAttributes video = new VideoAttributes();
            video.setCodec("flv");
            video.setBitRate(500000);
            video.setFrameRate(10);
            video.setSize(new VideoSize(800,800));
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("flv");
            attrs.setVideoAttributes(video);
            attrs.setOffset(2.0f);
            Encoder encoder = new Encoder();
            encoder.encode(inputFile, convertedFile, attrs);

        } catch (IOException | EncoderException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}
