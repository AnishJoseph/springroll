package com.springroll.core.services.mdm;

import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 14/12/16.
 */
public interface MdmService {
    String SEARCH_ID_PREFIX = "MDM:";

    IMdmData getData(String master);

    Map<String, List<String>> getMdmMasterNames();
}
