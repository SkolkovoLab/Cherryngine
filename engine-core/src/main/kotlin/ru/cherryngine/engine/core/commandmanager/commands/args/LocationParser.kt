package ru.cherryngine.engine.core.commandmanager.commands.args

import jakarta.inject.Singleton
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.parser.standard.DoubleParser
import org.incendo.cloud.parser.standard.DoubleParser.DoubleParseException
import org.incendo.cloud.parser.standard.IntegerParser
import org.incendo.cloud.suggestion.BlockingSuggestionProvider
import org.incendo.cloud.type.range.Range
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.core.commandmanager.SArgumentParser
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.protocol.types.ArgumentParserType
import java.util.stream.Collectors

@Singleton
class LocationParser() : SArgumentParser<Vec3D>, BlockingSuggestionProvider.Strings<CommandSender> {
    override val type: Class<Vec3D> = Vec3D::class.java
    override val argumentParserType: ArgumentParserType = ArgumentParserType.Vec3
    override val serverSuggestions: Boolean = false

    override fun parse(
        commandContext: CommandContext<CommandSender>,
        commandInput: CommandInput,
    ): ArgumentParseResult<Vec3D> {
        if (commandInput.remainingTokens() < 3) {
            return ArgumentParseResult.failure<Vec3D>(IllegalArgumentException())
        }

        val coordinates = Array<LocationCoordinate>(3) { _ ->
            if (commandInput.peekString().isEmpty()) {
                return ArgumentParseResult.failure<Vec3D>(IllegalArgumentException())
            }
            val coordinate = parseLocationCoordinate(
                commandContext,
                commandInput
            )
            if (coordinate.failure().isPresent) {
                return ArgumentParseResult.failure<Vec3D>(
                    coordinate.failure().get()
                )
            }
            return@Array coordinate.parsedValue().orElseThrow { NullPointerException() }
        }


        val sender: CommandSender = commandContext.sender()

        var location: Vec3D = when (sender) {
//            is Entity -> sender.position
            else -> Vec3D.ZERO
        }

        // Если все LOCAL, то ебошим LOCAL
        if (coordinates.all { it.type == LocationCoordinate.Type.LOCAL }) {
            val declaredPos: Vec3D = Vec3D(
                coordinates[0].coordinate,
                coordinates[1].coordinate,
                coordinates[2].coordinate
            )
            return ArgumentParseResult.success<Vec3D>(toLocalSpace(location, declaredPos))
        }

        // Если кто-то LOCAL, но не все LOCAL, то так быть не должно
        if (coordinates.any { it.type == LocationCoordinate.Type.LOCAL }) {
            return ArgumentParseResult.failure<Vec3D>(IllegalArgumentException())
        }

        location = if (coordinates[0].type == LocationCoordinate.Type.RELATIVE) {
            location.withX { it + coordinates[0].coordinate }
        } else {
            location.copy(x = coordinates[0].coordinate)
        }

        location = if (coordinates[1].type == LocationCoordinate.Type.RELATIVE) {
            location.withY { it + coordinates[1].coordinate }
        } else {
            location.copy(y = coordinates[1].coordinate)
        }

        location = if (coordinates[2].type == LocationCoordinate.Type.RELATIVE) {
            location.withZ { it + coordinates[2].coordinate }
        } else {
            location.copy(z = coordinates[2].coordinate)
        }

        return ArgumentParseResult.success<Vec3D>(location)
    }

    override fun stringSuggestions(
        commandContext: CommandContext<CommandSender>,
        input: CommandInput,
    ): Iterable<String> {
        return getSuggestions(3, commandContext, input)
    }

    private fun parseLocationCoordinate(
        context: CommandContext<CommandSender>,
        commandInput: CommandInput,
    ): ArgumentParseResult<LocationCoordinate> {
        val input = commandInput.skipWhitespace().peekString()

        /* Determine the type */
        val locationCoordinateType: LocationCoordinate.Type
        if (commandInput.peek() == '^') {
            locationCoordinateType = LocationCoordinate.Type.LOCAL
            commandInput.moveCursor(1)
        } else if (commandInput.peek() == '~') {
            locationCoordinateType = LocationCoordinate.Type.RELATIVE
            commandInput.moveCursor(1)
        } else {
            locationCoordinateType = LocationCoordinate.Type.ABSOLUTE
        }

        val coordinate: Double
        try {
            val empty = commandInput.peekString().isEmpty() || commandInput.peek() == ' '
            coordinate = if (empty) 0.0 else commandInput.readDouble()
            if (commandInput.hasRemainingInput()) {
                commandInput.skipWhitespace()
            }
        } catch (_: Exception) {
            return ArgumentParseResult.failure<LocationCoordinate>(
                DoubleParseException(
                    input,
                    DoubleParser<Any>(
                        DoubleParser.DEFAULT_MINIMUM,
                        DoubleParser.DEFAULT_MAXIMUM
                    ),
                    context
                )
            )
        }

        return ArgumentParseResult.success<LocationCoordinate>(
            LocationCoordinate(locationCoordinateType, coordinate)
        )
    }

    private val SUGGESTION_RANGE: Range<Int> = Range.intRange(Int.MIN_VALUE, Int.MAX_VALUE)

    private fun Vec3D.crossProduct(other: Vec3D) = Vec3D(
        y * other.z - other.y * z,
        z * other.x - other.z * x,
        x * other.y - other.x * y
    )

    private fun toLocalSpace(originalLocation: Vec3D, declaredPos: Vec3D): Vec3D {
//        val cosYaw = cos(toRadians(originalLocation.yaw + 90.0f).toDouble())
//        val sinYaw = sin(toRadians(originalLocation.yaw + 90.0f).toDouble())
//        val cosPitch = cos(toRadians(-originalLocation.pitch).toDouble())
//        val sinPitch = sin(toRadians(-originalLocation.pitch).toDouble())
//        val cosNegYaw = cos(toRadians(-originalLocation.pitch + 90.0f).toDouble())
//        val sinNegYaw = sin(toRadians(-originalLocation.pitch + 90.0f).toDouble())
//        val zModifier: Vec = Vec(cosYaw * cosPitch, sinPitch, sinYaw * cosPitch)
//        val yModifier: Vec = Vec(cosYaw * cosNegYaw, sinNegYaw, sinYaw * cosNegYaw)
//        val xModifier: Vec = zModifier.crossProduct(yModifier).mul(-1.0)
//        val xOffset = dotProduct(declaredPos, xModifier.x, yModifier.x, zModifier.x)
//        val yOffset = dotProduct(declaredPos, xModifier.y, yModifier.y, zModifier.y)
//        val zOffset = dotProduct(declaredPos, xModifier.z, yModifier.z, zModifier.z)
//        return originalLocation.add(xOffset, yOffset, zOffset)
        return originalLocation
    }

    private fun dotProduct(location: Vec3D, x: Double, y: Double, z: Double): Double {
        return location.x * x + location.y * y + location.z * z
    }

    private fun toRadians(degrees: Float): Float {
        return degrees * Math.PI.toFloat() / 180f
    }

    private fun getSuggestions(
        components: Int,
        commandContext: CommandContext<CommandSender>,
        input: CommandInput,
    ): MutableList<String> {
        val inputCopy = input.copy()

        var idx = input.cursor()
        for (i in 0 until components) {
            idx = input.cursor()
            if (!input.hasRemainingInput(true)) break
            val coordinateResult = parseLocationCoordinate(commandContext, input)
            if (coordinateResult.failure().isPresent) break
        }
        input.cursor(idx)

        if (input.hasRemainingInput() && (input.peek() == '~' || input.peek() == '^')) {
            input.read()
        }

        val prefix = inputCopy.difference(input, true)

        return IntegerParser.getSuggestions(
            SUGGESTION_RANGE,
            input
        ).stream().map<String?> { string: String? -> prefix + string }.collect(Collectors.toList())
    }
}
