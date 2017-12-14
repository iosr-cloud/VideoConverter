package agh.iosr.handler;

import agh.iosr.converter.VideoConverter;
import agh.iosr.event.model.EventMessage;
import agh.iosr.model.VideoConversionType;
import agh.iosr.repository.VideoDataRepository;
import agh.iosr.storage.impl.S3StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(URL.class)
public class MessageHandlerTest {

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private VideoConverter videoConverter;

    @Mock
    private VideoDataRepository videoDataRepository;

    @InjectMocks
    private MessageHandler messageHandler;


    @Test
    public void handleMessage() throws Exception {

        //given
        File file = mock(File.class);
        URL url = mock(URL.class);
        final String filename = "filename";

        EventMessage message = new EventMessage(1, "url", VideoConversionType.TWO_TIMES_FASTER);

        //when
        when(videoConverter.convert(message.getResourceURL(), message.getConversionType())).thenReturn(file);
        when(file.getName()).thenReturn(filename);
        when(s3StorageService.uploadFile(filename, file)).thenReturn(url);
        when(url.toString()).thenReturn(filename);
        messageHandler.handleMessage(message);

        //then
        verify(videoConverter).convert(message.getResourceURL(), message.getConversionType());
        verify(s3StorageService).uploadFile(filename, file);
        verify(videoDataRepository).findOne(message.getId());
    }

}