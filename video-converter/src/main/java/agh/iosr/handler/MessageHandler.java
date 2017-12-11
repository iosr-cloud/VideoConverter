package agh.iosr.handler;

import agh.iosr.converter.VideoConverter;
import agh.iosr.event.model.EventMessage;
import agh.iosr.model.VideoData;
import agh.iosr.repository.VideoDataRepository;
import agh.iosr.storage.impl.S3StorageService;
import lombok.RequiredArgsConstructor;
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

    public void handleMessage(EventMessage message) {

        //convert
        File convertedFile = videoConverter.convert(message.getResourceURL(), message.getConversionType());

        //upload
        URL fileUrl = s3StorageService.uploadFile(convertedFile.getName(), convertedFile);

        //db
        saveToDatabase(message.getId(), fileUrl.toString());
    }

    @Transactional
    private void saveToDatabase(long id, String fileUrl) {
        VideoData data = videoDataRepository.findOne(id);
        data.setConvertedFilePath(fileUrl);
        videoDataRepository.save(data);
    }
}
