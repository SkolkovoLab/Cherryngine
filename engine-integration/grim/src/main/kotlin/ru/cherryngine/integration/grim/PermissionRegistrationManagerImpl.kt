package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.manager.PermissionRegistrationManager
import ac.grim.grimac.platform.api.permissions.PermissionDefaultValue

class PermissionRegistrationManagerImpl : PermissionRegistrationManager {
    override fun registerPermission(name: String?, defaultValue: PermissionDefaultValue?) = Unit
}