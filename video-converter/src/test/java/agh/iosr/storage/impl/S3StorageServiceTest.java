package agh.iosr.storage.impl;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class S3StorageServiceTest {

    @Mock
    private AmazonS3 client;

    @InjectMocks
    private S3StorageService s3StorageService;


    @Test
    public void uploadFile() throws Exception {

        //given
        final File file = mock(File.class);
        final URL url = new URL("http://google.pl");
        final String fileName = "filename";
        final String bucketName = "bucketname";
        ReflectionTestUtils.setField(s3StorageService, "bucketName", bucketName);

        //when
        when(client.getUrl(bucketName, fileName)).thenReturn(url);
        URL result = s3StorageService.uploadFile(fileName, file);

        //then
        verify(client, times(1)).putObject(bucketName, fileName, file);
        verify(client, times(1)).getUrl(bucketName, fileName);
        assertEquals(url, result);
    }

}