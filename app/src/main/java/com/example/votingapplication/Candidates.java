package com.example.votingapplication;

public class Candidates {
    public String candidate1, candidate2, candidate3;

    public Candidates() {
        // Default constructor required for Firebase
    }

    public Candidates(String c1, String c2, String c3) {
        this.candidate1 = c1;
        this.candidate2 = c2;
        this.candidate3 = c3;
    }
}
