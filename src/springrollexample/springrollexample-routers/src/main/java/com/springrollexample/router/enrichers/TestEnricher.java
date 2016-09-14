package com.springrollexample.router.enrichers;

import com.springroll.core.DTO;
import com.springroll.core.DTOEnricher;
import com.springroll.core.Principal;
import com.springrollexample.core.IPrincipal;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by anishjoseph on 14/09/16.
 */
@Service public class TestEnricher implements DTOEnricher {
    @Resource(name = "userContext")
    private IPrincipal context;

    @Override
    public void enrich(DTO dto, Principal principal) {
        System.out.println(context.getRole());
    }
}
