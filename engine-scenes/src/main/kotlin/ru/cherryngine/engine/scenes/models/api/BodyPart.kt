package ru.cherryngine.engine.scenes.models.api

enum class BodyPart(val value: String) {
    HEAD("head"),
    BODY("body"),
    TORSO("torso"),
    LEFT_ARM("left_arm"),
    RIGHT_ARM("right_arm"),
    LEFT_LEG("left_leg"),
    RIGHT_LEG("right_leg"),
    UNKNOWN("unknown");

    companion object {
        fun from(value: String): BodyPart {
            return when (value) {
                "head" -> HEAD
                "body" -> BODY
                "torso" -> TORSO
                "left_arm" -> LEFT_ARM
                "right_arm" -> RIGHT_ARM
                "left_leg" -> LEFT_LEG
                "right_leg" -> RIGHT_LEG
                else -> UNKNOWN
            }
        }
    }
}