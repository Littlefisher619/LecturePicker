package com.littlefisher.LecturePicker.LectureData;

import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

public class Lecture {


    private String Author,Name,Location,SignupTime,StartTime;
    private String Type,Description;
    private HtmlTableCell Operation;
    private int LeftSeat;
    private LectureStatus Status;
    public String genDescription(){
        return String.format("\t%s - %s - %s\n\t%s %s\n\tSignup-deadline - %s\n\t%s - Spared seat(s): %d\n",
                Type,Author,Name,
                Location,StartTime,
                SignupTime,
                Status,LeftSeat
        );
    }
    public String getDescription(){return Description;}
    public LectureStatus getStatus() {
        return Status;
    }

    public void setStatus(LectureStatus status) {
        Status = status;

    }
    public int getLeftSeat() {
        return LeftSeat;
    }

    public void setLeftSeat(int leftSeat) {
        LeftSeat = leftSeat;
    }


    public Lecture(String author, String name, String location, String signupTime, String startTime, String type,int leftSeat, HtmlTableCell operation, LectureStatus status) {
        Author = author;
        Name = name;
        Location = location;
        SignupTime = signupTime;
        StartTime = startTime;
        Type = type;
        Operation = operation;
        LeftSeat = leftSeat;
        Status = status;
    }


    public Lecture(String author, String name, String location, String signupTime, String startTime, String type,int leftSeat, HtmlTableCell operation, String status) {
        Author = author;
        Name = name;
        Location = location;
        SignupTime = signupTime;
        StartTime = startTime;
        Type = type;
        Operation = operation;
        LeftSeat = leftSeat;
        if(status.contains("取消报名")) Status=LectureStatus.REGISTERED;
        else if(status.contains("已无名额")) Status=LectureStatus.NOSEAT;
        else Status=LectureStatus.WAITFORREGISTER;
        this.Description=genDescription();
    }

    public HtmlTableCell getOperation() {
        return Operation;
    }

    public void setOperation(HtmlTableCell operation) {
        Operation = operation;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSignupTime() {
        return SignupTime;
    }

    public void setSignupTime(String signupTime) {
        SignupTime = signupTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }
}
