package nl.sander.vocabulair.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class Taal {
    private List<Word> woorden;
}
