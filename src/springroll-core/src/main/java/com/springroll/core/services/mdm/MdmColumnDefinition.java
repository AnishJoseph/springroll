package com.springroll.core.services.mdm;

import com.springroll.core.Lov;

import java.util.List;

/**
 * Created by anishjoseph on 14/12/16.
 */
public interface MdmColumnDefinition {
    String getName();

    boolean isWriteable();

    String getType();

    List<Lov> getLovList();

    Object getDefVal();

    String getLovSource();

    boolean isMultiSelect();

    boolean isNullable();

    String getWidth();
    String getTitle();
}
