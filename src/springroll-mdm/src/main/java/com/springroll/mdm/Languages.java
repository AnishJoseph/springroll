package com.springroll.mdm;

import com.springroll.core.LovProvider;
import com.springroll.core.Lov;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by anishjoseph on 08/11/16.
 */
@Service
public class Languages implements LovProvider {
    @Override
    public List<Lov> getLovs() {
        List<Lov> languages = new ArrayList<>();
        for (String s : Locale.getISOLanguages()) {
            languages.add(new Lov(s));
        }
        return languages;
    }
}
