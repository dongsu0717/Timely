package com.dongsu.timely.presentation.common

enum class EnumRepeat(val order: Int, val repeat: Int) {
    NO(0,0),
    EVERY_DAY(1,-1),
    EVERY_WEEK(2,-2),
    EVERY_MONTH(3,-3)
}

enum class EnumAlarmTime(val order: Int, val time: Int) {
    BEFORE_1_HOUR(0,60),
    BEFORE_1_HALF_HOUR(1,90),
    BEFORE_2_HOUR(2,120),
    BEFORE_2_HALF_HOUR(3,150),
    BEFORE_3_HOUR(4,180)
}

enum class EnumScheduleColor(val order: Int, val color: Int) {
    LAVENDER(0,0),
    SAGE(1,1),
    GRAPE(2,2),
    FLAMINGO(3,3),
    BANANA(4,4)
}

enum class EnumGroupScheduleColor(val order: Int, val color: Int) {
    TANGERINE(0,0),
    PEACOCK(1,1),
    BLUEBERRY(2,2),
    BASIL(3,3),
    TOMATO(4,4)
}

