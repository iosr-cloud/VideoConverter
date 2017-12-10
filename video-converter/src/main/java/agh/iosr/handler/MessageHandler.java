package agh.iosr.handler;

import agh.iosr.converter.VideoConverter;
import agh.iosr.event.model.EventMessage;
import agh.iosr.model.VideoData;
import agh.iosr.repository.VideoDataRepository;
import agh.iosr.storage.impl.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.Serializable;
import java.net.URL;

@Service
public class MessageHandler {

    @Autowired
    private S3StorageService s3StorageService;

    @Autowired
    private VideoConverter videoConverter;

    @Autowired
    private VideoDataRepository videoDataRepository;

    public void handleMessage(Serializable message) {

        EventMessage eventMessage = (EventMessage) message;

        //convert
        File convertedFile = videoConverter.convert(eventMessage.getResourceURL(), eventMessage.getConversionType());

        //upload
        URL fileUrl = s3StorageService.uploadFile(convertedFile.getName(), convertedFile);

        //db
        saveToDatabase(eventMessage.getId(), fileUrl.toString());
    }

    @Transactional
    public void saveToDatabase(long id, String fileUrl) {
        VideoData data = videoDataRepository.findOne(id);
        data.setConvertedFilePath(fileUrl);
        videoDataRepository.save(data);
    }
}
