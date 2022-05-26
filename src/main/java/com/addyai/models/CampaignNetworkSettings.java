package com.addyai.models;

public class CampaignNetworkSettings {

    private boolean targetGoogleSearch;

    private boolean targetSearchNetwork;

    private boolean targetContentNetwork;

    private boolean targetPartnerSearchNetwork;

    public boolean isTargetGoogleSearch() {
        return targetGoogleSearch;
    }

    public void setTargetGoogleSearch(boolean targetGoogleSearch) {
        this.targetGoogleSearch = targetGoogleSearch;
    }

    public boolean isTargetSearchNetwork() {
        return targetSearchNetwork;
    }

    public void setTargetSearchNetwork(boolean targetSearchNetwork) {
        this.targetSearchNetwork = targetSearchNetwork;
    }

    public boolean isTargetContentNetwork() {
        return targetContentNetwork;
    }

    public void setTargetContentNetwork(boolean targetContentNetwork) {
        this.targetContentNetwork = targetContentNetwork;
    }

    public boolean isTargetPartnerSearchNetwork() {
        return targetPartnerSearchNetwork;
    }

    public void setTargetPartnerSearchNetwork(boolean targetPartnerSearchNetwork) {
        this.targetPartnerSearchNetwork = targetPartnerSearchNetwork;
    }
}
