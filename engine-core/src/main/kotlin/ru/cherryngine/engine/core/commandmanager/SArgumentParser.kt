package ru.cherryngine.engine.core.commandmanager

import org.incendo.cloud.parser.ArgumentParser

interface SArgumentParser<T> : ArgumentParser<CommandSender, T> {
    val type: Class<T>
}