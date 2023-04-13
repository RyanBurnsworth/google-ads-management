package com.addyai.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class DemoDataGenerator {
    private int type;
    private Date date;
    private String customerId;
    private String resourceId;
    private String parentId;
    private String resourceName;
    private long clicks;
    private long impressions;
    private double ctr;
    private int qualityScore;
    private double averageCpc;
    private double cost;
    private double conversions;
    private double costPerConversion;
    private double conversionValue;
    private double invalidClickRate;
    private long invalidClicks;
    private long phoneCalls;
    private long phoneImpressions;
    private double phoneThroughRate;
    private String lastUpdated;

    public static DemoDataGenerator getRandomCampaignObject(String resourceId, String resourceName) {
        DemoDataGenerator object = new DemoDataGenerator();
        Random random = new Random();

        object.type = 1;
        object.date = generateDate();
        object.customerId = "9059845250";
        object.resourceId = resourceId;
        object.parentId = "";
        object.resourceName = resourceName;
        object.clicks = random.nextInt(1000);
        object.impressions = random.nextInt(10000);
        object.ctr = random.nextDouble(5);
        object.qualityScore = random.nextInt(10);
        object.averageCpc = random.nextDouble(25);
        object.cost = random.nextDouble(5000);
        object.conversions = random.nextDouble(50);
        object.costPerConversion = random.nextDouble(36);
        object.conversionValue = random.nextDouble(112);
        object.invalidClickRate = random.nextDouble(5);
        object.invalidClicks = random.nextInt(100);
        object.phoneCalls = random.nextInt(45);
        object.phoneImpressions = random.nextInt(1000);
        object.phoneThroughRate = random.nextDouble(5);
        object.lastUpdated = new Date().toString();

        return object;
    }

    public static DemoDataGenerator getRandomAdGroupObject() {
        DemoDataGenerator object = new DemoDataGenerator();
        Random random = new Random();

        object.type = random.nextInt(5);
        object.date = generateDate();
        object.customerId = "9059845250";
        object.resourceId = "Test AdGroup Refactor 2022-1004";
        object.parentId = "Test Campaign Refactor 2022-1004";
        object.resourceName = "CampaignResource" + random.nextInt(100);
        object.clicks = random.nextInt(1000);
        object.impressions = random.nextInt(10000);
        object.ctr = random.nextDouble(5);
        object.qualityScore = random.nextInt(10);
        object.averageCpc = random.nextDouble(25);
        object.cost = random.nextDouble(5000);
        object.conversions = random.nextDouble(50);
        object.costPerConversion = random.nextDouble(36);
        object.conversionValue = random.nextDouble(112);
        object.invalidClickRate = random.nextDouble(5);
        object.invalidClicks = random.nextInt(100);
        object.phoneCalls = random.nextInt(45);
        object.phoneImpressions = random.nextInt(1000);
        object.phoneThroughRate = random.nextDouble(5);
        object.lastUpdated = new Date().toString();

        return object;
    }

    public static DemoDataGenerator getRandomAdObject() {
        DemoDataGenerator object = new DemoDataGenerator();
        Random random = new Random();

        object.type = random.nextInt(5);
        object.date = generateDate();
        object.customerId = "9059845250";
        object.resourceId = "Test Ad 2022-1004";
        object.parentId = "Test Campaign Refactor 2022-1004";
        object.resourceName = "CampaignResource" + random.nextInt(100);
        object.clicks = random.nextInt(1000);
        object.impressions = random.nextInt(10000);
        object.ctr = random.nextDouble(5);
        object.qualityScore = random.nextInt(10);
        object.averageCpc = random.nextDouble(25);
        object.cost = random.nextDouble(5000);
        object.conversions = random.nextDouble(50);
        object.costPerConversion = random.nextDouble(36);
        object.conversionValue = random.nextDouble(112);
        object.invalidClickRate = random.nextDouble(5);
        object.invalidClicks = random.nextInt(100);
        object.phoneCalls = random.nextInt(45);
        object.phoneImpressions = random.nextInt(1000);
        object.phoneThroughRate = random.nextDouble(5);
        object.lastUpdated = new Date().toString();

        return object;
    }

    public static Date generateDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -6);
        calendar.set(Calendar.DATE, (int) (Math.random() * calendar.getActualMaximum(Calendar.DATE)));
        return calendar.getTime();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public double getCtr() {
        return ctr;
    }

    public void setCtr(double ctr) {
        this.ctr = ctr;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public double getAverageCpc() {
        return averageCpc;
    }

    public void setAverageCpc(double averageCpc) {
        this.averageCpc = averageCpc;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getConversions() {
        return conversions;
    }

    public void setConversions(double conversions) {
        this.conversions = conversions;
    }

    public double getCostPerConversion() {
        return costPerConversion;
    }

    public void setCostPerConversion(double costPerConversion) {
        this.costPerConversion = costPerConversion;
    }

    public double getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(double conversionValue) {
        this.conversionValue = conversionValue;
    }

    public double getInvalidClickRate() {
        return invalidClickRate;
    }

    public void setInvalidClickRate(double invalidClickRate) {
        this.invalidClickRate = invalidClickRate;
    }

    public long getInvalidClicks() {
        return invalidClicks;
    }

    public void setInvalidClicks(long invalidClicks) {
        this.invalidClicks = invalidClicks;
    }

    public long getPhoneCalls() {
        return phoneCalls;
    }

    public void setPhoneCalls(long phoneCalls) {
        this.phoneCalls = phoneCalls;
    }

    public long getPhoneImpressions() {
        return phoneImpressions;
    }

    public void setPhoneImpressions(long phoneImpressions) {
        this.phoneImpressions = phoneImpressions;
    }

    public double getPhoneThroughRate() {
        return phoneThroughRate;
    }

    public void setPhoneThroughRate(double phoneThroughRate) {
        this.phoneThroughRate = phoneThroughRate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
