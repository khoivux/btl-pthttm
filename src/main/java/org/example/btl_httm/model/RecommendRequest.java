package org.example.btl_httm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecommendRequest {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("top_k")
    private int topK;

    @JsonProperty("interaction_history")
    private List<InteractionLog> interactionHistory;
    public RecommendRequest() {
    }
    public RecommendRequest(String userId, int topK, List<InteractionLog> interactionHistory) {
        this.userId = userId;
        this.topK = topK;
        this.interactionHistory = interactionHistory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    public List<InteractionLog> getInteractionHistory() {
        return interactionHistory;
    }

    public void setInteractionHistory(List<InteractionLog> interactionHistory) {
        this.interactionHistory = interactionHistory;
    }
}
