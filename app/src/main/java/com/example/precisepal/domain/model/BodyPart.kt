package com.example.precisepal.domain.model

data class BodyPart(
    val name: String,
    val isActive: Boolean,
    val measuringUnit: String,
    val latestValue: Float? = null,
    val bodyPartId: String? = null
)

enum class MeasuringUnit(
    val code: String,
    val label: String
){
    INCHES("in", "Inches"),
    CENTIMETERS("cm", "Centimeters"),
    FEET("ft", "Feet"),
    PERCENTAGE("%", "Percentage"),
    KILOGRAMS("kg", "Kilograms"),
    POUNDS("lbs", "Pounds"),
    METERS("m", "Meters"),
    MILLIMETERS("mm", "Millimeters")
}

val predefinedBodyPart: List<BodyPart> = listOf(
    BodyPart(
        name = "s",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 40.5f,
    ),
    BodyPart(
        name = "Hips",
        isActive = true,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 95.0f,
    ),
    BodyPart(
        name = "Thigh",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 24.3f,
    ),
    BodyPart(
        name = "Biceps",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 13.5f,
    ),
    BodyPart(
        name = "Neck",
        isActive = true,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 38.0f,
    ),
    BodyPart(
        name = "Forearm",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 10.5f,
    ),
    BodyPart(
        name = "Calf",
        isActive = true,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 38.5f,
    ),
    BodyPart(
        name = "Shoulder",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 45.0f,
    ),
    BodyPart(
        name = "Wrist",
        isActive = true,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 16.0f,
    ),
    BodyPart(
        name = "Ankle",
        isActive = false,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 20.5f,
    ),
    BodyPart(
        name = "Upper Arm",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 12.0f,
    ),
    BodyPart(
        name = "Torso",
        isActive = true,
        measuringUnit = MeasuringUnit.FEET.code,
        latestValue = 5.0f,
    ),
    BodyPart(
        name = "Abdomen",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 34.0f,
    ),
    BodyPart(
        name = "Quads",
        isActive = true,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 54.0f,
    ),
    BodyPart(
        name = "Hamstring",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 18.0f,
    ),
    BodyPart(
        name = "Lower Back",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 32.5f,
    ),
    BodyPart(
        name = "Upper Back",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 38.0f,
    ),
    BodyPart(
        name = "Triceps",
        isActive = true,
        measuringUnit = MeasuringUnit.INCHES.code,
        latestValue = 12.5f,
    ),
    BodyPart(
        name = "Obliques",
        isActive = true,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 88.0f,
    ),
    BodyPart(
        name = "Glutes",
        isActive = true,
        measuringUnit = MeasuringUnit.CENTIMETERS.code,
        latestValue = 100.0f,
    )
)
