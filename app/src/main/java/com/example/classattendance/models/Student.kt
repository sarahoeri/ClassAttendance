package com.example.classattendance.models

//data class Student(
//    var name: String?= "",
//    var reg_num: String?= "",
//    var year: String?= ""
//)

class Student{
    var name: String?= null
    var reg_num: String?= null
    var year: String?= null

    constructor()
    constructor(name: String?, reg_num: String?, year: String?) {
        this.name = name
        this.reg_num = reg_num
        this.year = year
    }


}