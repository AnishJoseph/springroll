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
public class Countries implements LovProvider {
    @Override
    public List<Lov> getLovs() {
        List<Lov> countries = new ArrayList<>();
        for (String s : Locale.getISOCountries()) {
            countries.add(new Lov(s));
        }
        return countries;
    }
}
