package com.springroll.router;

import com.springroll.core.DTO;
import com.springroll.core.DTOEnricher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by anishjoseph on 05/11/16.
 */
@Service
public class DummyEnricher implements DTOEnricher {

    @Override
    public void enrich(List<? extends DTO> dtos) {

    }
}
