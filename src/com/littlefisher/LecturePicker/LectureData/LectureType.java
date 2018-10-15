package com.littlefisher.LecturePicker.LectureData;

public enum LectureType {
    JIAXI("嘉锡讲坛"),XINXIZIYUAN("信息资源");
    private String description;

    LectureType(String s){description=s; }
    String getDescription(){return  description;}
}
