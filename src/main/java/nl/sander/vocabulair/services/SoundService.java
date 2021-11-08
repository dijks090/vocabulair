package nl.sander.vocabulair.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class SoundService {

    private final ResourceLoader resourceLoader;

    public void cheer() {
        try {
            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(loadResource().getURL());
            // the reference to the clip
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
        } catch (Exception e) {
            log.error("Error opening audio", e);
            throw new RuntimeException();
        }
    }

    public Resource loadResource() {
        return resourceLoader.getResource(String.format("classpath:sounds/cheer/%d.wav", new Random().nextInt(6)));
    }
}
