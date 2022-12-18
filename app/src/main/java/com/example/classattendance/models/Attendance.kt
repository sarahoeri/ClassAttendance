package com.example.classattendance.models

class Attendance {

    var year: String=""
    var level: String=""
    var field: String=""
    var name: String=""
    var reg_num: String=""
    var isAttended: Boolean?=false

    constructor()
    constructor(
        year: String,
        level: String,
        field: String,
        name: String,
        reg_name: String,
        isAttended: Boolean?
    ) {
        this.year = year
        this.level = level
        this.field = field
        this.name = name
        this.reg_num = reg_name
        this.isAttended = isAttended
    }


}