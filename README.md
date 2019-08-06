# nuxeo-filtered-download-audit-page-provider

This plug-in redefines **ES** page provider `DOCUMENT_HISTORY_PROVIDER` in order to easily customize its ES DSL queries.

# Requirements

Building requires the following software:

* git
* maven

Running this plugin requires that the **Nuxeo Platform** is configured to store documents' audit trail in `elasticsearch`.

# Build

```
git clone ...
cd nuxeo-filtered-download-audit-page-provider

mvn clean install
```

# Configuration

By default, page provider `DOCUMENT_HISTORY_PROVIDER` gets either 1 or 2 parameters, depending on the use case. Therefore 2 **ES DSL** queries. Here is the default configuration:
```xml
<component name="ESDocumentHistoryFilteredDownloadPageProvider.config.default" >

<extension point="providers" target="org.nuxeo.ecm.platform.query.api.PageProviderService">
  
  <property name="nuxeo.elasticsearch.audit.pageprovider.DOCUMENT_HISTORY_PROVIDER.singleQuery">{
  "bool" : {
    "must" : {
      "term" : { "docUUID" : "?" }
    }
  }
}
  </property>
  <property name="nuxeo.elasticsearch.audit.pageprovider.DOCUMENT_HISTORY_PROVIDER.complexQuery">{
  "bool": {
    "must": [
      { "term": { "docUUID": "?" } },
      { "range": { "eventDate": { "lte": "?" } } }
    ]
  }
}
  </property>

</extension>

</component>
```

Override the **configration properties** `nuxeo.elasticsearch.audit.pageprovider.DOCUMENT_HISTORY_PROVIDER.complexQuery` and `nuxeo.elasticsearch.audit.pageprovider.DOCUMENT_HISTORY_PROVIDER.singleQuery` in order to customize the queries to your need.

# Installation

```
nuxeoctl mp-install nuxeo-filtered-download-audit-page-provider/nuxeo-filtered-download-audit-page-provider-package/target/nuxeo-filtered-download-audit-page-provider-package*.zip
```

# Support

**These features are not part of the Nuxeo Production platform, they are not supported**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.


# Licensing

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)


# About Nuxeo

Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris.

More information is available at [www.nuxeo.com](http://www.nuxeo.com).
