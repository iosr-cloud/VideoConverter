package agh.iosr.converter;

import agh.iosr.model.VideoConversionType;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.net.URL;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, Encoder.class})
public class VideoConverterTest {

    @Mock
    private Encoder encoder;

    @Mock
    private File source;

    @Mock
    private File target;

    @Mock
    private EncodingAttributes attributes;

    @InjectMocks
    private VideoConverter videoConverter;

    @Test
    public void convert() throws Exception {

        //given
        String url = "http://google.pl";
        VideoConversionType videoConversionType = VideoConversionType.TWO_TIMES_FASTER;

        //when
        doNothing().when(encoder).encode(any(), any(), any());
        videoConverter.convert(url, videoConversionType);

        //then
        verify(encoder).encode(any() , any(), any());

    }

}