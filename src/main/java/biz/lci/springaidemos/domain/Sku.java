package biz.lci.springaidemos.domain;

import lombok.Data;

@Data
public class Sku {
    protected String sku;
    protected String packagedesc;
    protected String packagesize;
    protected String packageunits;
}
