package biz.lci.springaidemos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    protected List<Sku> skus = new ArrayList<>();

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty("skus")
    public void setSkus(List<Map<String, Object>> skuList) {
        for(Map<String, Object> skuData: skuList) {
            Sku sku = objectMapper.convertValue(skuData, Sku.class);
            this.skus.add(sku);
        }
    }
}
