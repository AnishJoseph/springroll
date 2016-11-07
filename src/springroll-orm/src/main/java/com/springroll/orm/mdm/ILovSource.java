package com.springroll.orm.mdm;

import com.springroll.core.Lov;

import java.util.List;

/**
 * Created by anishjoseph on 07/11/16.
 */
public interface ILovSource {
    String getSource();
    String getName();
    List<Lov> getLovs();
}
