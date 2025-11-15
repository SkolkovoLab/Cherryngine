package ru.cherryngine.integration.grim.manager

import ac.grim.grimac.platform.api.manager.PermissionRegistrationManager
import ac.grim.grimac.platform.api.permissions.PermissionDefaultValue
import jakarta.inject.Singleton

@Singleton
class PermissionRegistrationManagerImpl : PermissionRegistrationManager {
    override fun registerPermission(name: String?, defaultValue: PermissionDefaultValue?) = Unit
}