package agh.iosr.handler;

import agh.iosr.converter.VideoConverter;
import agh.iosr.event.model.EventMessage;
import agh.iosr.model.VideoData;
import agh.iosr.repository.VideoDataRepository;
import agh.iosr.storage.impl.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
public class MessageHandler {

    @Autowired
    private S3StorageService s3StorageService;

    @Autowired
    private VideoConverter videoConverter;

    @Autowired
    private VideoDataRepository videoDataRepository;

    public void handleMessage(EventMessage eventMessage) {
        //convert
        File convertedFile = videoConverter.convert(eventMessage.getResourceURL().toString(), eventMessage.getConversionType());

        //upload
        URL fileUrl = s3StorageService.uploadFile(convertedFile.getName(), convertedFile);

        //db
        VideoData data = videoDataRepository.findOne(eventMessage.getId());
        data.setConvertedFilePath(fileUrl.toString());
        videoDataRepository.save(data);
    }
}
