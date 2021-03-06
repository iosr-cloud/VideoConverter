package agh.iosr.event.model;

import agh.iosr.model.VideoConversionType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.jms.Message;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class EventMessage implements Serializable {

    private final long id;
    private final String resourceURL;
    private final VideoConversionType conversionType;
}
