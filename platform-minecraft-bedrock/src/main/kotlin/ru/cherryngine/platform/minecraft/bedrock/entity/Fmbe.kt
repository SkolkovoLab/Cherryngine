package ru.cherryngine.platform.minecraft.bedrock.entity

import it.unimi.dsi.fastutil.longs.LongArrayList
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag
import org.cloudburstmc.protocol.bedrock.packet.AnimateEntityPacket
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.rotation.AxisSequence
import ru.cherryngine.platform.minecraft.bedrock.world.BedrockItemMapping
import kotlin.math.sqrt

/**
 * FMBE (Fox MBE) — vanilla Bedrock display entity hack.
 *
 * Invisible fox holds a block/item in its mouth. Stacked vanilla animations
 * reposition the held item via Molang variables injected through AnimateEntityPacket.
 *
 * Reference: https://wiki.bedrock.dev/commands/display-entities
 *
 * Each animation controls one aspect:
 * 1. animation.player.sleeping          → base controller (stacks animations)
 * 2. animation.creeper.swelling         → scale (v.swelling_scale1, v.swelling_scale2)
 * 3. animation.ender_dragon.neck_head   → base position (v.head_position_x/y/z)
 * 4. animation.warden.move              → pitch + yaw (v.body_x_rot, v.body_z_rot)
 * 5. animation.player.attack.rotations  → roll (v.attack_body_rot_y)
 * 6. animation.parrot.moving            → fine X pos (v.wing_flap)
 * 7. animation.minecart.move.v1.0       → fine Y pos (v.rail_offset.y)
 * 8. animation.parrot.dance             → fine Z pos (v.dance.x, v.dance.y)
 *
 * Position units: 1/16 block (16 = 1 full block).
 */
object Fmbe {

    data class State(
        val scaleXZ: Float,
        val scaleY: Float,
        val xRot: Float,
        val yRot: Float,
        val zRot: Float,
    )

    fun createEntity(): BedrockEntity {
        val entity = BedrockEntity(identifier = "minecraft:fox")
        entity.metadata.getOrCreateFlags().apply {
            put(EntityFlag.HAS_GRAVITY, false)
            put(EntityFlag.SILENT, true)
            put(EntityFlag.NO_AI, true)
            put(EntityFlag.TAMED, true)
            put(EntityFlag.INVISIBLE, true)
        }
        return entity
    }

    fun sendEquipment(entity: BedrockEntity, material: String, itemMapping: BedrockItemMapping) {
        val packet = MobEquipmentPacket()
        packet.runtimeEntityId = entity.runtimeEntityId
        packet.item = itemMapping.createItemData(material)
        packet.inventorySlot = 0
        packet.hotbarSlot = 0
        packet.containerId = 0
        entity.viewers.forEach { it.session.sendPacket(packet) }
    }

    fun stateFromTransform(transform: Transform): State {
        val (xRot, yRot, zRot) = transform.rotation.toAxisAngleSequence(AxisSequence.XYZ).toVec3D()
        return State(
            scaleXZ = transform.scale.x.toFloat(),
            scaleY = transform.scale.y.toFloat(),
            xRot = Math.toDegrees(xRot).toFloat(),
            yRot = Math.toDegrees(yRot).toFloat(),
            zRot = -Math.toDegrees(zRot).toFloat(),
        )
    }

    fun buildPackets(runtimeEntityId: Long, state: State): List<AnimateEntityPacket> {
        val ids = LongArrayList().apply { add(runtimeEntityId) }
        val sqrtXZ = sqrt(state.scaleXZ.toDouble())
        val sqrtY = sqrt(state.scaleY.toDouble())

        // Scale
        val swellingScale1 = 2.1385 * sqrtXZ
        val swellingScale2 = 2.1385 * sqrtY

        // Base position (bone origin offset)
        val headPosX = 0.0
        val headPosY = 10.6925 * sqrtY
        val headPosZ = 17.108 * sqrtXZ

        // Rotation (90 = default upright)
        val bodyXRot = 90.0 + state.xRot
        val bodyZRot = 90.0 + state.yRot
        val attackBodyRotY = -state.zRot.toDouble()

        // Fine position (1/16 block units)
        val ypos = -8.0
        val wingFlap = 16.0 / 0.3
        val railOffsetY = 1.6485 + ypos / 16.0

        return listOf(
            anim(ids, "animation.player.sleeping", "controller.animation.fox.move", ""),
            anim(ids, "animation.creeper.swelling", "wiki:scale",
                "v.swelling_scale1=$swellingScale1;v.swelling_scale2=$swellingScale2;"),
            anim(ids, "animation.ender_dragon.neck_head_movement", "wiki:shift_pos",
                "v.head_rotation_x=0;v.head_rotation_y=0;v.head_rotation_z=0;v.head_position_x=$headPosX;v.head_position_y=$headPosY;v.head_position_z=$headPosZ;"),
            anim(ids, "animation.warden.move", "wiki:xrot",
                "v.body_x_rot=$bodyXRot;v.body_z_rot=$bodyZRot;"),
            anim(ids, "animation.player.attack.rotations", "wiki:zrot",
                "v.attack_body_rot_y=$attackBodyRotY;"),
            anim(ids, "animation.parrot.moving", "wiki:xpos",
                "v.wing_flap=$wingFlap;"),
            anim(ids, "animation.minecart.move.v1.0", "wiki:ypos",
                "v.rail_offset.x=0;v.rail_offset.y=$railOffsetY;v.rail_offset.z=0;"),
            anim(ids, "animation.parrot.dance", "wiki:zpos",
                "v.dance.x=0;v.dance.y=0;"),
        )
    }

    private fun anim(ids: LongArrayList, animation: String, controller: String, molang: String): AnimateEntityPacket {
        val packet = AnimateEntityPacket()
        packet.animation = animation
        packet.nextState = "none"
        packet.blendOutTime = 0f
        packet.stopExpression = molang
        packet.controller = controller
        packet.runtimeEntityIds.addAll(ids)
        return packet
    }
}
