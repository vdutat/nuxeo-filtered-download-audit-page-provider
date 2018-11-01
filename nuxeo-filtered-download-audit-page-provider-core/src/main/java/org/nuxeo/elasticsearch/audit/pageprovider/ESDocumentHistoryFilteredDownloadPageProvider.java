package org.nuxeo.elasticsearch.audit.pageprovider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.audit.api.document.AdditionalDocumentAuditParams;
import org.nuxeo.ecm.platform.audit.api.document.DocumentAuditHelper;
import org.nuxeo.elasticsearch.audit.pageprovider.ESDocumentHistoryPageProvider;

public class ESDocumentHistoryFilteredDownloadPageProvider extends ESDocumentHistoryPageProvider {

    protected Log log = LogFactory.getLog(ESDocumentHistoryFilteredDownloadPageProvider.class);

    protected static String singleQuery_nofilter = "            {\n" + "                \"bool\" : {\n"
            + "                  \"must\" : {\n" + "                    \"match\" : {\n"
            + "                      \"docUUID\" : {\n" + "                        \"query\" : \"?\",\n"
            + "                        \"type\" : \"boolean\"\n" + "                      }\n"
            + "                    }\n" + "                  }\n" + "                }\n"
            + "              }          \n" + "";

    protected static String singleQuery = "{\n" + 
            "    \"bool\" : {\n" + 
            "      \"must\" : [\n" + 
            "        {\n" + 
            "          \"constant_score\" : {\n" + 
            "            \"filter\" : {\n" + 
            "              \"term\" : {\n" + 
            "                \"docUUID\" : {\n" + 
            "                  \"value\" : \"?\",\n" + 
            "                  \"boost\" : 1.0\n" + 
            "                }\n" + 
            "              }\n" + 
            "            },\n" + 
            "            \"boost\" : 1.0\n" + 
            "          }\n" + 
            "        },\n" + 
            "        {\n" + 
            "          \"bool\" : {\n" + 
            "            \"should\" : [\n" + 
            "              {\n" + 
            "                \"bool\" : {\n" + 
            "                  \"must\" : [\n" + 
            "                    {\n" + 
            "                      \"constant_score\" : {\n" + 
            "                        \"filter\" : {\n" + 
            "                          \"term\" : {\n" + 
            "                            \"eventId\" : {\n" + 
            "                              \"value\" : \"download\",\n" + 
            "                              \"boost\" : 1.0\n" + 
            "                            }\n" + 
            "                          }\n" + 
            "                        },\n" + 
            "                        \"boost\" : 1.0\n" + 
            "                      }\n" + 
            "                    },\n" + 
            "                    {\n" + 
            "                      \"constant_score\" : {\n" + 
            "                        \"filter\" : {\n" + 
            "                          \"term\" : {\n" + 
            "                            \"extended.blobXPath\" : {\n" + 
            "                              \"value\" : \"file:content\",\n" + 
            "                              \"boost\" : 1.0\n" + 
            "                            }\n" + 
            "                          }\n" + 
            "                        },\n" + 
            "                         \"boost\" : 1.0\n" + 
            "                      }\n" + 
            "                    }\n" + 
            "                  ],\n" + 
            "                  \"disable_coord\" : false,\n" + 
            "                  \"adjust_pure_negative\" : true,\n" + 
            "                  \"boost\" : 1.0\n" + 
            "                }\n" + 
            "              },\n" + 
            "              {\n" + 
            "                \"constant_score\" : {\n" + 
            "                  \"filter\" : {\n" + 
            "                    \"bool\" : {\n" + 
            "                      \"must_not\" : [\n" + 
            "                        {\n" + 
            "                          \"term\" : {\n" + 
            "                            \"eventId\" : {\n" + 
            "                              \"value\" : \"download\",\n" + 
            "                              \"boost\" : 1.0\n" + 
            "                            }\n" + 
            "                          }\n" + 
            "                        }\n" + 
            "                      ],\n" + 
            "                      \"disable_coord\" : false,\n" + 
            "                      \"adjust_pure_negative\" : true,\n" + 
            "                      \"boost\" : 1.0\n" + 
            "                    }\n" + 
            "                  },\n" + 
            "                  \"boost\" : 1.0\n" + 
            "                }\n" + 
            "              }\n" + 
            "            ],\n" + 
            "            \"disable_coord\" : false,\n" + 
            "            \"adjust_pure_negative\" : true,\n" + 
            "            \"boost\" : 1.0\n" + 
            "          }\n" + 
            "        }\n" + 
            "      ],\n" + 
            "      \"disable_coord\" : false,\n" + 
            "      \"adjust_pure_negative\" : true,\n" + 
            "      \"boost\" : 1.0\n" + 
            "    }\n" + 
            "  }\n";

    protected static String complexQuery_old = "{\n" +  //
            "    \"bool\": {\n" +  //
            "      \"should\": [\n" +  //
            "        {\n" +  //
            "          \"term\": {\n" +  //
            "            \"docUUID\": \"?\"\n" +  //
            "          }\n" +  //
            "        },\n" +  //
            "        {\n" +  //
            "          \"bool\": {\n" +  //
            "            \"must\": [\n" +  //
            "              {\n" +  //
            "                \"term\": {\n" +  //
            "                  \"docUUID\": \"?\"\n" +  //
            "                }\n" +  //
            "              },\n" +  //
            "              {\n" +  //
            "                \"range\": {\n" +  //
            "                  \"eventDate\": {\n" +  //
            "                    \"lte\": \"?\"\n" +  //
            "                  }\n" +  //
            "                }\n" +  //
            "              }\n" +  //
            "            ]\n" +  //
            "          }\n" +  //
            "        }\n" +  //
            "      ]\n" +  //
            "    }\n" +  //
            "}\n";

    protected static String complexQuery = "{\n" + 
            "    \"bool\" : {\n" + 
            "      \"must\" : [\n" + 
            "        {\n" + 
            "          \"constant_score\" : {\n" + 
            "            \"filter\" : {\n" + 
            "              \"term\" : {\n" + 
            "                \"docUUID\" : {\n" + 
            "                  \"value\" : \"?\",\n" + 
            "                  \"boost\" : 1.0\n" + 
            "                }\n" + 
            "              }\n" + 
            "            },\n" + 
            "            \"boost\" : 1.0\n" + 
            "          }\n" + 
            "        },\n" + 
            "              {\n" +  //
            "                \"range\": {\n" +  //
            "                  \"eventDate\": {\n" +  //
            "                    \"lte\": \"?\"\n" +  //
            "                  }\n" +  //
            "                }\n" +  //
            "              },\n" +  //
            "        {\n" + 
            "          \"bool\" : {\n" + 
            "            \"should\" : [\n" + 
            "              {\n" + 
            "                \"bool\" : {\n" + 
            "                  \"must\" : [\n" + 
            "                    {\n" + 
            "                      \"constant_score\" : {\n" + 
            "                        \"filter\" : {\n" + 
            "                          \"term\" : {\n" + 
            "                            \"eventId\" : {\n" + 
            "                              \"value\" : \"download\",\n" + 
            "                              \"boost\" : 1.0\n" + 
            "                            }\n" + 
            "                          }\n" + 
            "                        },\n" + 
            "                        \"boost\" : 1.0\n" + 
            "                      }\n" + 
            "                    },\n" + 
            "                    {\n" + 
            "                      \"constant_score\" : {\n" + 
            "                        \"filter\" : {\n" + 
            "                          \"term\" : {\n" + 
            "                            \"extended.blobXPath\" : {\n" + 
            "                              \"value\" : \"file:content\",\n" + 
            "                              \"boost\" : 1.0\n" + 
            "                            }\n" + 
            "                          }\n" + 
            "                        },\n" + 
            "                         \"boost\" : 1.0\n" + 
            "                      }\n" + 
            "                    }\n" + 
            "                  ],\n" + 
            "                  \"disable_coord\" : false,\n" + 
            "                  \"adjust_pure_negative\" : true,\n" + 
            "                  \"boost\" : 1.0\n" + 
            "                }\n" + 
            "              },\n" + 
            "              {\n" + 
            "                \"constant_score\" : {\n" + 
            "                  \"filter\" : {\n" + 
            "                    \"bool\" : {\n" + 
            "                      \"must_not\" : [\n" + 
            "                        {\n" + 
            "                          \"term\" : {\n" + 
            "                            \"eventId\" : {\n" + 
            "                              \"value\" : \"download\",\n" + 
            "                              \"boost\" : 1.0\n" + 
            "                            }\n" + 
            "                          }\n" + 
            "                        }\n" + 
            "                      ],\n" + 
            "                      \"disable_coord\" : false,\n" + 
            "                      \"adjust_pure_negative\" : true,\n" + 
            "                      \"boost\" : 1.0\n" + 
            "                    }\n" + 
            "                  },\n" + 
            "                  \"boost\" : 1.0\n" + 
            "                }\n" + 
            "              }\n" + 
            "            ],\n" + 
            "            \"disable_coord\" : false,\n" + 
            "            \"adjust_pure_negative\" : true,\n" + 
            "            \"boost\" : 1.0\n" + 
            "          }\n" + 
            "        }\n" + 
            "      ],\n" + 
            "      \"disable_coord\" : false,\n" + 
            "      \"adjust_pure_negative\" : true,\n" + 
            "      \"boost\" : 1.0\n" + 
            "    }\n" + 
            "  }\n";

    private static final long serialVersionUID = 1L;


    @Override
    protected String getFixedPart() {
        if (getParameters().length == 2) {
            if (log.isTraceEnabled()) {
                log.trace("parameters: " + getParameters());
                log.trace(complexQuery);
            }
            return complexQuery;
        } else {
            if (log.isTraceEnabled()) {
                log.trace(singleQuery);
            }
            return singleQuery;
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
