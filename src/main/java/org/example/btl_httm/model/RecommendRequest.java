package org.example.btl_httm.model;

import java.util.List;

public class RecommendRequest {
    public String user_id;
    public int top_k;
    public List<InteractionLog> interaction_history;
    public RecommendRequest() {
    }

    public RecommendRequest(String user_id, int top_k, List<InteractionLog> interaction_history) {
        this.user_id = user_id;
        this.top_k = top_k;
        this.interaction_history = interaction_history;
    }


    public int getTop_k() {
        return top_k;
    }

    public void setTop_k(int top_k) {
        this.top_k = top_k;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<InteractionLog> getInteraction_history() {
        return interaction_history;
    }

    public void setInteraction_history(List<InteractionLog> interaction_history) {
        this.interaction_history = interaction_history;
    }
}
