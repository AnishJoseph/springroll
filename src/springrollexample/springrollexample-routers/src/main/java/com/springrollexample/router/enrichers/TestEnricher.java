package com.springrollexample.router.enrichers;

import com.springroll.core.DTO;
import com.springroll.core.DTOEnricher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by anishjoseph on 14/09/16.
 */
@Service public class TestEnricher implements DTOEnricher {

    @Override
    public void enrich(List<? extends DTO> dtos) {

    }
}
