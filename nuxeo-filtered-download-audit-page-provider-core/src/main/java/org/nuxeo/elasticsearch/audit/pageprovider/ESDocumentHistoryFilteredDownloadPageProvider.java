package org.nuxeo.elasticsearch.audit.pageprovider;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.services.config.ConfigurationService;

public class ESDocumentHistoryFilteredDownloadPageProvider extends ESDocumentHistoryPageProvider {

    protected static final String SINGLEQUERY_PROPNAME_PATTERN = "nuxeo.elasticsearch.audit.pageprovider.%s.singleQuery";

    protected static final String COMPLEXQUERY_PROPNAME_PATTERN = "nuxeo.elasticsearch.audit.pageprovider.%s.complexQuery";

    protected Log log = LogFactory.getLog(ESDocumentHistoryFilteredDownloadPageProvider.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected String getFixedPart() {
        String query = "";
        if (getParameters().length == 2) {
            query = Framework.getService(ConfigurationService.class).getProperty(String.format(COMPLEXQUERY_PROPNAME_PATTERN, getDefinition().getName()), complexQuery);
            if (log.isTraceEnabled()) {
                log.trace("parameters: " + Arrays.deepToString(Arrays.stream(getParameters()).map(Object::toString).toArray(String[]::new)));
                log.trace(query);
            }
            return query;
        } else {
            query = Framework.getService(ConfigurationService.class).getProperty(String.format(SINGLEQUERY_PROPNAME_PATTERN, getDefinition().getName()), singleQuery);
            if (log.isTraceEnabled()) {
                log.trace("parameters: " + Arrays.deepToString(Arrays.stream(getParameters()).map(Object::toString).toArray(String[]::new)));
                log.trace(query);
            }
            return query;
        }
    }

    @Override
    public Object[] getParameters() {
        super.getParameters();
        if (newParams.length == 3) {
            Object[] params = new Object[] { newParams[0], newParams[2] };
            return params;
        }
        return newParams;
    }
}
