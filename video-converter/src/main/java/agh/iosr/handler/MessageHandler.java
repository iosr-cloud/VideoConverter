package agh.iosr.handler;

import agh.iosr.converter.VideoConverter;
import agh.iosr.event.model.EventMessage;
import agh.iosr.model.VideoData;
import agh.iosr.repository.VideoDataRepository;
import agh.iosr.storage.impl.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class MessageHandler {

    private final S3StorageService s3StorageService;
    private final VideoConverter videoConverter;
    private final VideoDataRepository videoDataRepository;
    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    @Transactional
    public void handleMessage(EventMessage message) {

        logger.info("Downloading file from url: " + message.getResourceURL());

        //convert
        File convertedFile = videoConverter.convert(message.getResourceURL(), message.getConversionType());

        //upload
        URL fileUrl = s3StorageService.uploadFile(convertedFile.getName(), convertedFile);

        //db
        saveToDatabase(message.getId(), fileUrl.toString());

        logger.info("Saved converted file to storage: " + fileUrl.getPath());
    }

    private void saveToDatabase(long id, String fileUrl) {
        VideoData data = videoDataRepository.findOne(id);
        if(data != null){
            data.setConvertedFilePath(fileUrl);
            data.setStatus(true);
            logger.info("Updated path and status in database");
        }else{
            logger.info("Couldn't find records in database");
        }
    }
}
