package ru.cherryngine.platform.minecraft.bedrock

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.player.MovementSnapshot
import ru.cherryngine.engine.core.player.MovementSource
import ru.cherryngine.engine.core.player.Player

@InstanceSingleton(platform = "bedrock")
class BedrockMovementSource : MovementSource<BedrockPlayer> {
    override fun canHandle(target: Player): Boolean = target is BedrockPlayer

    // Bedrock не шлёт onGround отдельным флагом — оставляем false, потребители это знают.
    override fun pollMovement(player: BedrockPlayer): MovementSnapshot =
        MovementSnapshot(player.clientPosition, player.clientYawPitch, false)
}
