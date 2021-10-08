package nl.sander.vocabulair.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static javax.sound.sampled.AudioSystem.getClip;

@Service
@Slf4j
public class SoundService {

    @Autowired
    ResourceLoader resourceLoader;

    public void cheer() {
        try {
            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(loadResource().getURL());
            // the reference to the clip
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            throw new RuntimeException();
        } catch (Exception e) {
            log.error("Error opening audio", e);
        }
    }

    public Resource loadResource() {
        return resourceLoader.getResource(String.format("classpath:sounds/cheer/%d.wav", new Random().nextInt(6)));
    }
}
