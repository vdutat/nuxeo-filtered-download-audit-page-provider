package org.nuxeo.elasticsearch.audit.pageprovider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.audit.api.document.AdditionalDocumentAuditParams;
import org.nuxeo.ecm.platform.audit.api.document.DocumentAuditHelper;
import org.nuxeo.elasticsearch.audit.pageprovider.ESDocumentHistoryPageProvider;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.services.config.ConfigurationService;

public class ESDocumentHistoryFilteredDownloadPageProvider extends ESDocumentHistoryPageProvider {

    private static final String SINGLEQUERY_PROPNAME = "nuxeo.elasticsearch.audit.pageprovider.DOCUMENT_HISTORY_PROVIDER.singleQuery";

    private static final String COMPLEXQUERY_PROPNAME = "nuxeo.elasticsearch.audit.pageprovider.DOCUMENT_HISTORY_PROVIDER.complexQuery";

    protected Log log = LogFactory.getLog(ESDocumentHistoryFilteredDownloadPageProvider.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected String getFixedPart() {
        String query = "";
        if (getParameters().length == 2) {
            query = Framework.getService(ConfigurationService.class).getProperty(COMPLEXQUERY_PROPNAME, complexQuery);
            if (log.isTraceEnabled()) {
                log.trace("parameters: " + getParameters());
                log.trace(query);
            }
            return query;
        } else {
            query = Framework.getService(ConfigurationService.class).getProperty(SINGLEQUERY_PROPNAME, singleQuery);
            if (log.isTraceEnabled()) {
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
