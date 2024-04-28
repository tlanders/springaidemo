package biz.lci.springaidemos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Product {
    protected String productname;
    @JsonProperty("productdesc")
    protected String description;
    @JsonProperty("searchdescription")
    protected String searchDescription;
    protected String productpageurl;
    protected String productimageurl;
    protected String ingredients;
    protected String brand;
    protected String lifestages;
    protected String species;
    protected String form;
    protected String superfamily;
    protected String conditions;
    protected String breedsize;
    protected String feedingguide;
    protected String nutrition;
    protected String howitworks;
    protected String howithelps;
    protected String additionalinformation;
    protected String sku;
    protected String packagedesc;
    protected String packagesize;
    protected String packageunits;
    protected String otherproducts;
    protected String recommendedfordesc;
    protected String notrecommendedfordesc;
    protected Double mevalue;
    protected Double drydensity;

    @JsonProperty("skus")
    protected List<Sku> skus;
}
