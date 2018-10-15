package com.littlefisher.LecturePicker.LectureData;

public enum LectureStatus {
    NOSEAT("已无名额"),REGISTERED("已报名"),WAITFORREGISTER("未报名");
    private String description;

    LectureStatus(String s){description=s; }
    String getDescription(){return  description;}

    @Override
    public String toString() {
        return this.getDescription();
    }
}
