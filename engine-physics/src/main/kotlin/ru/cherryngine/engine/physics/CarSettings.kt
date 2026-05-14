package ru.cherryngine.engine.physics

import ru.cherryngine.lib.math.Vec3D

/**
 * Полный спек одной машины. То, что передаётся в [PhysicsSpace.addCar] и
 * (с теми же значениями) в render-сторону через `CarComponent`. Все поля
 * обязательные — конкретные значения «тестовой машины» живут в `impl-demo`
 * (см. `DemoCars`).
 */
data class CarSettings(
    /** Размеры box-шасси (X = ширина, Y = высота, Z = длина). */
    val chassisSize: Vec3D,
    val chassisMass: Float,
    /** Расстояние между осями колёс (Z). */
    val wheelbase: Float,
    val wheelRadius: Float,
    val wheelWidth: Float,
    val suspensionMinLength: Float,
    val suspensionMaxLength: Float,
    /** Hz. ~1.5 — sample default; повышенный (≥2.5) держит chassis при spawn-падении
     *  с lift'а и не пробивает `suspensionMin` под тяжёлой машиной при g=17. */
    val suspensionFrequency: Float,
    /** 0..1. 0.5 — sample default; повышенный (~0.7) гасит overshoot после impact. */
    val suspensionDamping: Float,
    val maxSteerAngleDegrees: Float,
    /** Максимальный угол крена/тангажа, после которого constraint срабатывает (60° у sample). */
    val maxPitchRollAngleDegrees: Float,
    val engineMaxTorque: Float,
    val engineMaxRpm: Float,
    /** N·m. 4000 — Jolt/sample default. */
    val rearHandBrakeTorque: Float,
    /** Limited-slip ratio. 1.4 — sample default; передаёт torque на оба задних
     *  колеса равномерно. Понижать (1.0) — больше блокировка → проще удержать дрифт. */
    val limitedSlipRatio: Float,
    /** Жёсткость переднего стабилизатора (sample default ~500). */
    val antiRollBarFrontStiffness: Float,
    val antiRollBarRearStiffness: Float,
    /** Peak μ боковой friction-curve передних колёс. Jolt default ≈ 1.2. */
    val frontLateralFrictionPeak: Float,
    /** Peak μ боковой friction-curve задних колёс. Главное средство для drift'а:
     *  сильно ниже front → задняя ось теряет grip от центробежной силы на скорости. */
    val rearLateralFrictionPeak: Float,
    /** Peak μ продольной friction-curve. Jolt default ~1.2. */
    val longitudinalFrictionPeak: Float,
)
