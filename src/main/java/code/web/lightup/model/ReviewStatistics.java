package code.web.lightup.model;

public class ReviewStatistics {
    private int totalReviews;
    private double averageRating;
    private int fiveStars;
    private int fourStars;
    private int threeStars;
    private int twoStars;
    private int oneStar;

    public double getPercentage(int stars) {
        if (totalReviews == 0) return 0;
        int count = switch (stars) {
            case 5 -> fiveStars;
            case 4 -> fourStars;
            case 3 -> threeStars;
            case 2 -> twoStars;
            case 1 -> oneStar;
            default -> 0;
        };
        return Math.round((count * 100.0 / totalReviews) * 100.0) / 100.0;

    }


    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getFiveStars() {
        return fiveStars;
    }

    public void setFiveStars(int fiveStars) {
        this.fiveStars = fiveStars;
    }

    public int getFourStars() {
        return fourStars;
    }

    public void setFourStars(int fourStars) {
        this.fourStars = fourStars;
    }

    public int getThreeStars() {
        return threeStars;
    }

    public void setThreeStars(int threeStars) {
        this.threeStars = threeStars;
    }

    public int getTwoStars() {
        return twoStars;
    }

    public void setTwoStars(int twoStars) {
        this.twoStars = twoStars;
    }

    public int getOneStar() {
        return oneStar;
    }

    public void setOneStar(int oneStar) {
        this.oneStar = oneStar;
    }
}
