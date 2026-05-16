package model;

import java.sql.Date;

public class Rating {
    private int evaluationId;
    private int rate;
    private String comments;
    private Date dateOfEvaluation;
    private int customerId;

    public Rating() {
    }

    public Rating(int evaluationId, int rate, String comments, Date dateOfEvaluation, int customerId) {
        this.evaluationId = evaluationId;
        this.rate = rate;
        this.comments = comments;
        this.dateOfEvaluation = dateOfEvaluation;
        this.customerId = customerId;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getDateOfEvaluation() {
        return dateOfEvaluation;
    }

    public void setDateOfEvaluation(Date dateOfEvaluation) {
        this.dateOfEvaluation = dateOfEvaluation;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "evaluationId=" + evaluationId +
                ", rate=" + rate +
                ", comments='" + comments + '\'' +
                ", dateOfEvaluation=" + dateOfEvaluation +
                ", customerId=" + customerId +
                '}';
    }
}